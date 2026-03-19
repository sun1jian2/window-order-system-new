package com.window.system.controller.inventory;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.PurchaseOrder;
import com.window.system.model.req.inventory.PurchaseOrderListReq;
import com.window.system.model.req.inventory.PurchaseOrderSaveReq;
import com.window.system.service.inventory.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/purchase")
@CrossOrigin(origins = "*")
/**
 * PurchaseOrderController 控制器类
 */
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<PurchaseOrder>> list(@RequestBody PurchaseOrderListReq req) {
        return purchaseOrderService.list(req);
    }

        /**
     * getDetail 方法
     */
    @GetMapping("/{id}")
    public Result<PurchaseOrder> getDetail(@PathVariable("id") Long id) {
        return purchaseOrderService.getDetail(id);
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated PurchaseOrderSaveReq req, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return purchaseOrderService.save(req, currentUserId);
    }

        /**
     * submit 方法
     */
    @PostMapping("/{id}/submit")
    public Result<String> submit(@PathVariable("id") Long id, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return purchaseOrderService.submit(id, currentUserId);
    }

        /**
     * inbound 方法
     */
    @PostMapping("/{id}/inbound")
    public Result<String> inbound(@PathVariable("id") Long id, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return purchaseOrderService.inbound(id, currentUserId);
    }
}
