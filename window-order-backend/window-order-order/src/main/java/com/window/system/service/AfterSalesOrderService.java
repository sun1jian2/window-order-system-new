package com.window.system.service;

import cn.hutool.core.util.IdUtil;
import com.window.system.common.Result;
import com.window.system.mapper.AfterSalesOrderMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.AfterSalesOrder;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.AfterSalesCreateReq;
import com.window.system.model.req.AfterSalesListReq;
import com.window.system.model.req.AfterSalesUpdateReq;
import com.window.system.security.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
/**
 * AfterSalesOrderService 服务类/接口
 */
public class AfterSalesOrderService {

    @Autowired
    private AfterSalesOrderMapper afterSalesOrderMapper;

    @Autowired
    private WindowOrderMapper windowOrderMapper;

    /**
     * list 方法
     */
    public Result<PageResponse<AfterSalesOrder>> list(AfterSalesListReq req) {
        long count = afterSalesOrderMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<AfterSalesOrder> list = afterSalesOrderMapper.selectList(req);
        return Result.success(PageResponse.of(list, count));
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * create 方法
     */
    public Result<String> create(AfterSalesCreateReq req) {
        if ("SALES".equals(req.getCurrentUserRole())) {
            WindowOrder sourceOrder = windowOrderMapper.getById(req.getOrderId());
            if (sourceOrder == null) {
                return Result.error("订单不存在");
            }
            if (!req.getCurrentUserId().equals(sourceOrder.getSalespersonId())) {
                return Result.error("无权限");
            }
        }

        AfterSalesOrder afterSalesOrder = new AfterSalesOrder();
        BeanUtils.copyProperties(req, afterSalesOrder);
        
        afterSalesOrder.setTicketNo("AS" + IdUtil.getSnowflake(1, 1).nextIdStr());
        afterSalesOrder.setStatus("PENDING");
        afterSalesOrder.setCreateBy(req.getCurrentUserId());
        
        // Auto assign status if handler provided
        if (req.getHandlerId() != null) {
            afterSalesOrder.setStatus("ASSIGNED");
        }
        
        afterSalesOrderMapper.insert(afterSalesOrder);
        log.info("AfterSales Order created: {}", afterSalesOrder.getTicketNo());
        return Result.success("售后工单创建成功");
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * update 方法
     */
    public Result<String> update(AfterSalesUpdateReq req) {
        AfterSalesOrder oldOrder = afterSalesOrderMapper.getById(req.getId());
        if (oldOrder == null) {
            return Result.error("工单不存在");
        }

        Result<String> permission = validatePermission(oldOrder, req.getCurrentUserId(), req.getCurrentUserRole());
        if (permission != null) {
            return permission;
        }
        
        AfterSalesOrder updateOrder = new AfterSalesOrder();
        BeanUtils.copyProperties(req, updateOrder);
        
        // Status flow logic
        if ("COMPLETED".equals(req.getStatus()) && oldOrder.getCompletionTime() == null) {
            updateOrder.setCompletionTime(LocalDateTime.now());
        }
        
        afterSalesOrderMapper.update(updateOrder);
        return Result.success("更新成功");
    }
    
    /**
     * getDetail 方法
     */
    public Result<AfterSalesOrder> getDetail(Long id, AuthUser user) {
        AfterSalesOrder order = afterSalesOrderMapper.getById(id);
        if (order == null) {
            return Result.error("工单不存在");
        }
        Result<String> permission = validatePermission(order, user.getId(), user.getRole());
        if (permission != null) {
            return Result.error(permission.getMessage());
        }
        return Result.success(order);
    }
    
    /**
     * delete 方法
     */
    public Result<String> delete(Long id, AuthUser user) {
        AfterSalesOrder order = afterSalesOrderMapper.getById(id);
        if (order == null) {
            return Result.error("工单不存在");
        }
        Result<String> permission = validatePermission(order, user.getId(), user.getRole());
        if (permission != null) {
            return permission;
        }
        afterSalesOrderMapper.delete(id);
        return Result.success("删除成功");
    }

    /**
     * validatePermission 方法
     */
    private Result<String> validatePermission(AfterSalesOrder order, Long userId, String role) {
        if ("ADMIN".equals(role)) {
            return null;
        }
        if ("INSTALLER".equals(role)) {
            if (order.getHandlerId() == null || !userId.equals(order.getHandlerId())) {
                return Result.error("无权限");
            }
            return null;
        }
        if ("SALES".equals(role)) {
            WindowOrder wo = windowOrderMapper.getById(order.getOrderId());
            if (wo == null || !userId.equals(wo.getSalespersonId())) {
                return Result.error("无权限");
            }
            return null;
        }
        return Result.error("无权限");
    }
}
