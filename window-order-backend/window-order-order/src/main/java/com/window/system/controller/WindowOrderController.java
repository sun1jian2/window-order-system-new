package com.window.system.controller;

import com.window.system.annotation.Log;
import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.OrderCreateReq;
import com.window.system.model.req.OrderListReq;
import com.window.system.model.req.OrderUpdateReq;
import com.window.system.service.WindowOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
public class WindowOrderController {

    @Autowired
    private WindowOrderService windowOrderService;

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<PageResponse<WindowOrder>> list(@RequestBody OrderListReq req) {
        log.info("Query order list: {}", req);
        return windowOrderService.list(req);
    }

    @PostMapping("/create")
    @Log(module = "订单", operation = "创建订单")
    @PreAuthorize("hasAnyRole('SALES','ADMIN')")
    public Result<String> create(@RequestBody @Validated OrderCreateReq req) {
        log.info("Create order: {}", req);
        return windowOrderService.create(req);
    }

    @PostMapping("/update")
    @Log(module = "订单", operation = "更新订单")
    @PreAuthorize("hasAnyRole('SALES','ADMIN')")
    public Result<String> update(@RequestBody OrderUpdateReq req) {
        log.info("Update order: {}", req);
        return windowOrderService.update(req);
    }

    @DeleteMapping("/{id}")
    @Log(module = "订单", operation = "删除订单")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<String> delete(@PathVariable("id") Long id, 
                               @RequestParam(name = "currentUserId", required = false) Long currentUserId,
                               @RequestParam(name = "currentUserRole", required = false) String currentUserRole) {
        log.info("Delete order id: {}, user: {}, role: {}", id, currentUserId, currentUserRole);
        return windowOrderService.delete(id, currentUserId, currentUserRole);
    }
    
    @GetMapping("/detail/{id}")
    public Result<WindowOrder> get(@PathVariable("id") Long id) {
        log.info("Get order detail id: {}", id);
        return windowOrderService.get(id);
    }
}
