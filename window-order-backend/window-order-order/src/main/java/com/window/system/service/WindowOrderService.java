package com.window.system.service;

import cn.hutool.core.util.IdUtil;
import com.window.system.api.CustomerClient;
import com.window.system.common.Result;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.mapper.OrderItemMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.entity.OrderItem;
import com.window.system.model.entity.Customer;
import com.window.system.model.req.OrderCreateReq;
import com.window.system.model.req.OrderListReq;
import com.window.system.model.req.OrderUpdateReq;
import com.window.system.model.req.OrderItemSaveReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
public class WindowOrderService {

    @Autowired
    private WindowOrderMapper windowOrderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private CustomerClient customerClient;

    public Result<PageResponse<WindowOrder>> list(OrderListReq req) {
        long count = windowOrderMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<WindowOrder> list = windowOrderMapper.selectList(req);
        return Result.success(PageResponse.of(list, count));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> create(OrderCreateReq req) {
        WindowOrder order = new WindowOrder();
        BeanUtils.copyProperties(req, order);
        
        // Handle Customer Logic
        handleCustomerAssociation(req, order);
        
        initializeOrder(order);
        
        windowOrderMapper.insert(order);
        
        // Handle order items
        if (req.getItems() != null && !req.getItems().isEmpty()) {
            for (OrderItemSaveReq itemReq : req.getItems()) {
                OrderItem item = new OrderItem();
                BeanUtils.copyProperties(itemReq, item);
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        }
        
        log.info("Order created: {}", order.getOrderNo());
        
        notifyNewOrder(req, order);
        
        return Result.success("Order created successfully");
    }

    private void handleCustomerAssociation(OrderCreateReq req, WindowOrder order) {
        if (req.getCustomerPhone() == null) return;
        
        Result<Customer> res = customerClient.getByPhone(req.getCustomerPhone());
        Customer customer = res.getData();
        
        if (customer == null) {
            customer = new Customer();
            customer.setName(req.getCustomerName());
            customer.setPhone(req.getCustomerPhone());
            customer.setAddress(req.getAddress()); // Use full address as default
            customer.setCreateBy(req.getCurrentUserId());
            customerClient.save(customer);
            // Fetch again to get ID? Or save returns ID?
            // save API returns String "Saved". It doesn't return ID.
            // But if I create it, I need the ID.
            // CustomerClient.save needs to return ID or Customer object.
            // I should update CustomerController to return Customer or ID.
            // For now, I'll fetch again by phone.
            Result<Customer> newRes = customerClient.getByPhone(req.getCustomerPhone());
            if (newRes.getData() != null) {
                order.setCustomerId(newRes.getData().getId());
            }
        } else {
            // Optional: Update address if changed? Let's just update if empty
            if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
                customer.setAddress(req.getAddress());
                customerClient.save(customer);
            }
            order.setCustomerId(customer.getId());
        }
    }

    private void initializeOrder(WindowOrder order) {
        order.setOrderNo(IdUtil.getSnowflake(1, 1).nextIdStr());
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("SUBMITTED");
        }
        // Always default to WAITING on create
        order.setInstallProgress("WAITING");
        order.setProductionProgress("WAITING");
        
        // Initialize payment
        if (order.getPaidAmount() == null) {
            order.setPaidAmount(BigDecimal.ZERO);
        }
        order.setPaymentStatus("UNPAID");
    }

    private void notifyNewOrder(OrderCreateReq req, WindowOrder order) {
        if ("SALES".equals(req.getCurrentUserRole())) {
             notificationService.notifyAdmins("新订单提醒", "销售人员创建了新订单：" + order.getOrderNo(), "info");
        }
    }

    // Helper method to convert status to Chinese
    private String getStatusLabel(String status, String type) {
        if (status == null) return "";
        if ("install".equals(type)) {
            switch (status) {
                case "WAITING": return "等待中";
                case "SCHEDULED": return "已排期";
                case "INSTALLING": return "安装中";
                case "FINISHED": return "已完成";
                default: return status;
            }
        } else {
            switch (status) {
                case "WAITING": return "等待中";
                case "PRODUCING": return "制作中";
                case "FINISHED": return "已完成";
                default: return status;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> update(OrderUpdateReq req) {
        WindowOrder oldOrder = windowOrderMapper.getById(req.getId());
        if (oldOrder == null) {
            log.warn("Update order failed, not found id: {}", req.getId());
            return Result.error("Order not found");
        }

        Result<String> checkResult = checkUpdatePermissionAndStatus(req, oldOrder);
        if (checkResult.getCode() != 200) {
            return checkResult;
        }

        WindowOrder order = new WindowOrder();
        BeanUtils.copyProperties(req, order);
        
        if ("DRAFT".equals(order.getStatus())) {
            order.setInstallProgress("WAITING");
            order.setProductionProgress("WAITING");
        }
        
        sendProgressNotification(req, oldOrder);
        
        windowOrderMapper.update(order);
        
        // Handle order items (simple delete and insert)
        if (req.getItems() != null) {
            orderItemMapper.deleteByOrderId(order.getId());
            for (OrderItemSaveReq itemReq : req.getItems()) {
                OrderItem item = new OrderItem();
                BeanUtils.copyProperties(itemReq, item);
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        }
        
        log.info("Order updated: {}", order.getId());
        return Result.success("Order updated successfully");
    }

    private Result<String> checkUpdatePermissionAndStatus(OrderUpdateReq req, WindowOrder oldOrder) {
        if ("SALES".equals(req.getCurrentUserRole())) {
            if (!oldOrder.getSalespersonId().equals(req.getCurrentUserId())) {
                 log.warn("Update permission denied for user: {}", req.getCurrentUserId());
                 return Result.error("Permission denied: You can only update your own orders.");
            }
        }
        
        if ("FINISHED".equals(oldOrder.getInstallProgress()) && "FINISHED".equals(oldOrder.getProductionProgress())) {
            log.warn("Update failed, order finished: {}", oldOrder.getOrderNo());
            return Result.error("订单已全部完成，无法修改");
        }
        
        String effectiveProduction = req.getProductionProgress() != null ? req.getProductionProgress() : oldOrder.getProductionProgress();
        String effectiveInstall = req.getInstallProgress() != null ? req.getInstallProgress() : oldOrder.getInstallProgress();
        
        if (("INSTALLING".equals(effectiveInstall) || "FINISHED".equals(effectiveInstall)) && 
            !"FINISHED".equals(effectiveProduction)) {
             log.warn("Update failed, invalid progress sequence for order: {}", oldOrder.getOrderNo());
             return Result.error("制作进度必须为已完成，才能进行安装或完成安装");
        }
        return Result.success();
    }

    private void sendProgressNotification(OrderUpdateReq req, WindowOrder oldOrder) {
        boolean installChanged = req.getInstallProgress() != null && !req.getInstallProgress().equals(oldOrder.getInstallProgress());
        boolean productionChanged = req.getProductionProgress() != null && !req.getProductionProgress().equals(oldOrder.getProductionProgress());
        
        if (installChanged && productionChanged) {
             notificationService.notifyAdmins("进度更新提醒", "订单 " + oldOrder.getOrderNo() + " 安装和制作进度均已更新", "success");
        } else if (installChanged) {
             notificationService.notifyAdmins("安装进度更新", "订单 " + oldOrder.getOrderNo() + " 安装进度更新为: " + getStatusLabel(req.getInstallProgress(), "install"), "success");
        } else if (productionChanged) {
             notificationService.notifyAdmins("制作进度更新", "订单 " + oldOrder.getOrderNo() + " 制作进度更新为: " + getStatusLabel(req.getProductionProgress(), "production"), "success");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> delete(Long id, Long currentUserId, String currentUserRole) {
         // Check permission
        if ("SALES".equals(currentUserRole)) {
             WindowOrder existing = windowOrderMapper.getById(id);
             if (existing != null && !existing.getSalespersonId().equals(currentUserId)) {
                 log.warn("Delete permission denied for user: {}", currentUserId);
                 return Result.error("Permission denied: You can only delete your own orders.");
             }
        }
        
        windowOrderMapper.delete(id);
        log.info("Order deleted: {}", id);
        return Result.success("Order deleted successfully");
    }
    
    public Result<WindowOrder> get(Long id) {
        WindowOrder order = windowOrderMapper.getById(id);
        if (order != null) {
            order.setItems(orderItemMapper.listByOrderId(id));
        }
        return Result.success(order);
    }
}
