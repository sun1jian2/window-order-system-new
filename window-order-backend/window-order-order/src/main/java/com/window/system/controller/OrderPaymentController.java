package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.entity.OrderPayment;
import com.window.system.model.req.PaymentCreateReq;
import com.window.system.security.AuthUser;
import com.window.system.service.OrderPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment Management")
/**
 * OrderPaymentController 控制器类
 */
public class OrderPaymentController {

    @Autowired
    private OrderPaymentService orderPaymentService;

        /**
     * create 方法
     */
    @PostMapping
    @Operation(summary = "Create payment record")
    public Result<String> create(@RequestBody PaymentCreateReq req, @AuthenticationPrincipal AuthUser user) {
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        return orderPaymentService.create(req);
    }

        /**
     * listByOrderId 方法
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "List payments by order id")
    public Result<List<OrderPayment>> listByOrderId(@PathVariable("orderId") Long orderId) {
        return orderPaymentService.listByOrderId(orderId);
    }
}
