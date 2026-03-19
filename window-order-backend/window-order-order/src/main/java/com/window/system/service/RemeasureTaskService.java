package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.RemeasureTaskMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.RemeasureTask;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.RemeasureTaskAssignReq;
import com.window.system.model.req.RemeasureTaskListReq;
import com.window.system.model.req.RemeasureTaskSubmitReq;
import com.window.system.security.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
/**
 * RemeasureTaskService 服务类/接口
 */
public class RemeasureTaskService {

    @Autowired
    private RemeasureTaskMapper remeasureTaskMapper;

    @Autowired
    private WindowOrderMapper windowOrderMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * list 方法
     */
    public Result<PageResponse<RemeasureTask>> list(RemeasureTaskListReq req) {
        long count = remeasureTaskMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<RemeasureTask> list = remeasureTaskMapper.selectList(req);
        return Result.success(PageResponse.of(list, count));
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * assign 方法
     */
    public Result<String> assign(RemeasureTaskAssignReq req, AuthUser user) {
        WindowOrder order = windowOrderMapper.getById(req.getOrderId());
        if (order == null) {
            return Result.error("Order not found");
        }
        if ("SALES".equals(user.getRole()) && !user.getId().equals(order.getSalespersonId())) {
            return Result.error("无权限");
        }

        // Check if there is already a pending task
        RemeasureTask existing = remeasureTaskMapper.getByOrderId(req.getOrderId());
        if (existing != null && "PENDING".equals(existing.getStatus())) {
            return Result.error("There is already a pending re-measurement task for this order");
        }

        RemeasureTask task = new RemeasureTask();
        task.setOrderId(req.getOrderId());
        task.setAssigneeId(req.getAssigneeId());
        task.setStatus("PENDING");
        task.setRemark(req.getRemark());
        
        remeasureTaskMapper.insert(task);
        
        log.info("Re-measurement task assigned for order: {}", order.getOrderNo());
        
        return Result.success("Task assigned successfully");
    }

    @Transactional(rollbackFor = Exception.class)
    /**
     * submit 方法
     */
    public Result<String> submit(RemeasureTaskSubmitReq req, AuthUser user) {
        RemeasureTask task = remeasureTaskMapper.getById(req.getId());
        if (task == null) {
            return Result.error("Task not found");
        }
        if ("INSTALLER".equals(user.getRole()) && !user.getId().equals(task.getAssigneeId())) {
            return Result.error("无权限");
        }
        
        if (!"PENDING".equals(task.getStatus())) {
            return Result.error("Task is not in PENDING status");
        }

        task.setPreciseWidth(req.getPreciseWidth());
        task.setPreciseHeight(req.getPreciseHeight());
        task.setSketchUrl(req.getSketchUrl());
        task.setSitePhotos(req.getSitePhotos());
        task.setRemark(req.getRemark());
        task.setStatus("COMPLETED");
        
        remeasureTaskMapper.update(task);
        
        // Update Order
        WindowOrder order = windowOrderMapper.getById(task.getOrderId());
        if (order != null) {
            order.setIsRemeasured(true);
            // Update dimensions to precise ones
            order.setWidth(req.getPreciseWidth());
            order.setHeight(req.getPreciseHeight());
            windowOrderMapper.update(order);
            
            notificationService.notifyAdmins("复尺完成", "订单 " + order.getOrderNo() + " 已完成复尺", "success");
        }
        
        log.info("Re-measurement task completed for order: {}", task.getOrderId());
        
        return Result.success("Re-measurement submitted successfully");
    }

    /**
     * get 方法
     */
    public Result<RemeasureTask> get(Long id, AuthUser user) {
        RemeasureTask task = remeasureTaskMapper.getById(id);
        if (task == null) {
            return Result.error("Task not found");
        }
        Result<String> permission = validatePermission(task, user);
        if (permission != null) {
            return Result.error(permission.getMessage());
        }
        return Result.success(task);
    }
    
    /**
     * getByOrderId 方法
     */
    public Result<RemeasureTask> getByOrderId(Long orderId, AuthUser user) {
        RemeasureTask task = remeasureTaskMapper.getByOrderId(orderId);
        if (task == null) {
            return Result.success(null);
        }
        Result<String> permission = validatePermission(task, user);
        if (permission != null) {
            return Result.error(permission.getMessage());
        }
        return Result.success(task);
    }

    /**
     * validatePermission 方法
     */
    private Result<String> validatePermission(RemeasureTask task, AuthUser user) {
        if ("ADMIN".equals(user.getRole())) {
            return null;
        }
        if ("INSTALLER".equals(user.getRole())) {
            if (task.getAssigneeId() == null || !user.getId().equals(task.getAssigneeId())) {
                return Result.error("无权限");
            }
            return null;
        }
        if ("SALES".equals(user.getRole())) {
            WindowOrder order = windowOrderMapper.getById(task.getOrderId());
            if (order == null || !user.getId().equals(order.getSalespersonId())) {
                return Result.error("无权限");
            }
            return null;
        }
        return Result.error("无权限");
    }
}
