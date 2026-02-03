package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.AfterSalesOrder;
import com.window.system.model.req.AfterSalesCreateReq;
import com.window.system.model.req.AfterSalesListReq;
import com.window.system.model.req.AfterSalesUpdateReq;
import com.window.system.security.AuthUser;
import com.window.system.service.AfterSalesOrderService;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/after-sales")
@Tag(name = "After Sales Management")
public class AfterSalesOrderController {

    @Autowired
    private AfterSalesOrderService afterSalesOrderService;
    
    @Autowired
    private SysExportTaskService sysExportTaskService;

    @PostMapping("/list")
    @Operation(summary = "List after sales orders")
    public Result<PageResponse<AfterSalesOrder>> list(@RequestBody AfterSalesListReq req, @AuthenticationPrincipal AuthUser user) {
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        return afterSalesOrderService.list(req);
    }
    
    @PostMapping("/export")
    @Operation(summary = "Export after sales orders")
    public Result<String> export(@RequestBody AfterSalesListReq req, @AuthenticationPrincipal AuthUser user) {
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出售后工单_" + timeStr + ".xlsx", "AFTER_SALES", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }

    @PostMapping("/create")
    @Operation(summary = "Create after sales order")
    public Result<String> create(@RequestBody AfterSalesCreateReq req, @AuthenticationPrincipal AuthUser user) {
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        return afterSalesOrderService.create(req);
    }

    @PostMapping("/update")
    @Operation(summary = "Update after sales order")
    public Result<String> update(@RequestBody AfterSalesUpdateReq req, @AuthenticationPrincipal AuthUser user) {
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        return afterSalesOrderService.update(req);
    }
    
    @GetMapping("/detail/{id}")
    @Operation(summary = "Get detail")
    public Result<AfterSalesOrder> detail(@PathVariable("id") Long id, @AuthenticationPrincipal AuthUser user) {
        return afterSalesOrderService.getDetail(id, user);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    public Result<String> delete(@PathVariable("id") Long id, @AuthenticationPrincipal AuthUser user) {
        return afterSalesOrderService.delete(id, user);
    }
}
