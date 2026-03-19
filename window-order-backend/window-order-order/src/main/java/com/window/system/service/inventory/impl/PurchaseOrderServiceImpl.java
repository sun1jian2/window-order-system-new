package com.window.system.service.inventory.impl;

import com.window.system.common.Result;
import com.window.system.mapper.inventory.InventoryRecordMapper;
import com.window.system.mapper.inventory.PurchaseOrderItemMapper;
import com.window.system.mapper.inventory.PurchaseOrderMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.InventoryRecord;
import com.window.system.model.entity.inventory.PurchaseOrder;
import com.window.system.model.entity.inventory.PurchaseOrderItem;
import com.window.system.model.req.inventory.PurchaseOrderItemSaveReq;
import com.window.system.model.req.inventory.PurchaseOrderListReq;
import com.window.system.model.req.inventory.PurchaseOrderSaveReq;
import com.window.system.service.inventory.PurchaseOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
/**
 * PurchaseOrderServiceImpl 服务类/接口
 */
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    
    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;
    
    @Autowired
    private InventoryRecordMapper inventoryRecordMapper;

    @Override
    public Result<PageResponse<PurchaseOrder>> list(PurchaseOrderListReq req) {
        long total = purchaseOrderMapper.countList(req);
        if (total == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<PurchaseOrder> list = purchaseOrderMapper.selectList(req);
        return Result.success(PageResponse.of(list, total));
    }

    @Override
    public Result<PurchaseOrder> getDetail(Long id) {
        PurchaseOrder order = purchaseOrderMapper.getById(id);
        if (order != null) {
            order.setItems(purchaseOrderItemMapper.listByOrderId(id));
        }
        return Result.success(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> save(PurchaseOrderSaveReq req, Long currentUserId) {
        try {
            PurchaseOrder order = new PurchaseOrder();
            BeanUtils.copyProperties(req, order);

            if (req.getId() == null) {
                order.setOrderNo(generateOrderNo());
                order.setStatus("DRAFT");
                order.setCreateBy(currentUserId);
                purchaseOrderMapper.insert(order);
            } else {
                order.setUpdateBy(currentUserId);
                purchaseOrderMapper.update(order);
                purchaseOrderItemMapper.deleteByOrderId(order.getId());
            }

            if (req.getItems() != null && !req.getItems().isEmpty()) {
                for (PurchaseOrderItemSaveReq itemReq : req.getItems()) {
                    PurchaseOrderItem item = new PurchaseOrderItem();
                    BeanUtils.copyProperties(itemReq, item);
                    item.setPurchaseOrderId(order.getId());
                    purchaseOrderItemMapper.insert(item);
                }
            }

            return Result.success("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存采购订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Result<String> submit(Long id, Long currentUserId) {
        purchaseOrderMapper.updateStatus(id, "PENDING", currentUserId);
        return Result.success("提交成功，等待入库");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> inbound(Long id, Long currentUserId) {
        PurchaseOrder order = purchaseOrderMapper.getById(id);
        if (order == null || !"PENDING".equals(order.getStatus())) {
            return Result.error("订单状态不正确");
        }
        
        List<PurchaseOrderItem> items = purchaseOrderItemMapper.listByOrderId(id);
        for (PurchaseOrderItem item : items) {
            // 1. 获取当前库存
            BigDecimal beforeQuantity = inventoryRecordMapper.getMaterialStock(item.getMaterialId());
            if (beforeQuantity == null) beforeQuantity = BigDecimal.ZERO;
            BigDecimal afterQuantity = beforeQuantity.add(item.getQuantity());
            
            // 2. 更新库存
            inventoryRecordMapper.updateMaterialStock(item.getMaterialId(), item.getQuantity());
            
            // 3. 记录流水
            InventoryRecord record = new InventoryRecord();
            record.setType("INBOUND");
            record.setMaterialId(item.getMaterialId());
            record.setQuantity(item.getQuantity());
            record.setBeforeQuantity(beforeQuantity);
            record.setAfterQuantity(afterQuantity);
            record.setRelationType("PURCHASE");
            record.setRelationId(id);
            record.setOperatorId(currentUserId);
            record.setRemark("采购入库");
            inventoryRecordMapper.insert(record);
        }
        
        purchaseOrderMapper.updateStatus(id, "COMPLETED", currentUserId);
        return Result.success("入库成功");
    }
    
    /**
     * generateOrderNo 方法
     */
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "PO" + sdf.format(new Date()) + (1000 + new Random().nextInt(9000));
    }
}
