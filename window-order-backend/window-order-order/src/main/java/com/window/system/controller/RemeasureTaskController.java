package com.window.system.controller;

import com.window.system.annotation.Log;
import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.RemeasureTask;
import com.window.system.model.req.RemeasureTaskAssignReq;
import com.window.system.model.req.RemeasureTaskListReq;
import com.window.system.model.req.RemeasureTaskSubmitReq;
import com.window.system.security.AuthUser;
import com.window.system.service.RemeasureTaskService;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remeasure")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
/**
 * RemeasureTaskController 控制器类
 */
public class RemeasureTaskController {

    @Autowired
    private RemeasureTaskService remeasureTaskService;
    
    @Autowired
    private SysExportTaskService sysExportTaskService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<PageResponse<RemeasureTask>> list(@RequestBody RemeasureTaskListReq req, @AuthenticationPrincipal AuthUser user) {
        log.info("Query remeasure task list: {}", req);
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        return remeasureTaskService.list(req);
    }
    
        /**
     * export 方法
     */
    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<String> export(@RequestBody RemeasureTaskListReq req, @AuthenticationPrincipal AuthUser user) {
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出复尺任务_" + timeStr + ".xlsx", "REMEASURE", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }

        /**
     * assign 方法
     */
    @PostMapping("/assign")
    @Log(module = "复尺任务", operation = "指派复尺")
    @PreAuthorize("hasAnyRole('SALES','ADMIN')")
    public Result<String> assign(@RequestBody @Validated RemeasureTaskAssignReq req, @AuthenticationPrincipal AuthUser user) {
        log.info("Assign remeasure task: {}", req);
        return remeasureTaskService.assign(req, user);
    }

        /**
     * submit 方法
     */
    @PostMapping("/submit")
    @Log(module = "复尺任务", operation = "提交复尺结果")
    @PreAuthorize("hasAnyRole('INSTALLER','ADMIN')")
    public Result<String> submit(@RequestBody @Validated RemeasureTaskSubmitReq req, @AuthenticationPrincipal AuthUser user) {
        log.info("Submit remeasure task: {}", req);
        return remeasureTaskService.submit(req, user);
    }
    
        /**
     * get 方法
     */
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<RemeasureTask> get(@PathVariable("id") Long id, @AuthenticationPrincipal AuthUser user) {
        log.info("Get remeasure task detail id: {}", id);
        return remeasureTaskService.get(id, user);
    }

        /**
     * getByOrderId 方法
     */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<RemeasureTask> getByOrderId(@PathVariable("orderId") Long orderId, @AuthenticationPrincipal AuthUser user) {
        log.info("Get remeasure task by order id: {}", orderId);
        return remeasureTaskService.getByOrderId(orderId, user);
    }
}
