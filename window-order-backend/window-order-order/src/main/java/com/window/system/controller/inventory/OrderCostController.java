package com.window.system.controller.inventory;

import com.window.system.common.Result;
import com.window.system.model.entity.inventory.OrderCost;
import com.window.system.model.req.inventory.OrderCostSaveReq;
import com.window.system.service.inventory.OrderCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/cost")
@CrossOrigin(origins = "*")
/**
 * OrderCostController 控制器类
 */
public class OrderCostController {

    @Autowired
    private OrderCostService orderCostService;

        /**
     * getByOrderId 方法
     */
    @GetMapping("/order/{orderId}")
    public Result<OrderCost> getByOrderId(@PathVariable("orderId") Long orderId) {
        return orderCostService.getByOrderId(orderId);
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated OrderCostSaveReq req, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return orderCostService.save(req, currentUserId);
    }
}
