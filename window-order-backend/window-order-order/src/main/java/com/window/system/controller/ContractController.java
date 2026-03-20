package com.window.system.controller;

import com.window.system.model.entity.OrderContract;
import com.window.system.model.req.ContractSignCallbackReq;
import com.window.system.model.req.OrderContractCreateReq;
import com.window.system.common.Result;
import com.window.system.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单合同控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/order/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * 生成订单合同
     */
    @PostMapping("/generate")
    public Result<OrderContract> generateContract(@RequestBody OrderContractCreateReq req, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        log.info("生成订单合同: {}", req);
        return contractService.generateContract(req, userId);
    }

    /**
     * 获取订单的合同列表
     */
    @GetMapping("/list/{orderId}")
    public Result<List<OrderContract>> getContractsByOrderId(@PathVariable("orderId") Long orderId) {
        return contractService.getContractsByOrderId(orderId);
    }

    /**
     * 接收第三方电子签章回调
     */
    @PostMapping("/sign-callback")
    public Result<String> signCallback(@RequestBody ContractSignCallbackReq req) {
        log.info("接收到电子签章回调: {}", req);
        return contractService.handleSignCallback(req);
    }
}
