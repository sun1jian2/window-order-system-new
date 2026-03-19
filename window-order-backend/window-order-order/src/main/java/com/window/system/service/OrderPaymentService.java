package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.OrderPaymentAttachmentMapper;
import com.window.system.mapper.OrderPaymentMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.entity.OrderPayment;
import com.window.system.model.entity.OrderPaymentAttachment;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.PaymentCreateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
/**
 * OrderPaymentService 服务类/接口
 */
public class OrderPaymentService {

    @Autowired
    private OrderPaymentMapper orderPaymentMapper;
    
    @Autowired
    private WindowOrderMapper windowOrderMapper;
    
    @Autowired
    private OrderPaymentAttachmentMapper orderPaymentAttachmentMapper;

    @Transactional(rollbackFor = Exception.class)
    /**
     * create 方法
     */
    public Result<String> create(PaymentCreateReq req) {
        WindowOrder order = windowOrderMapper.getById(req.getOrderId());
        if (order == null) {
            return Result.error("Order not found");
        }
        
        if (order.getPrice() != null && order.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            List<OrderPayment> existed = orderPaymentMapper.getByOrderId(order.getId());
            BigDecimal totalPaid = existed.stream()
                    .map(OrderPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal after = totalPaid.add(req.getAmount() == null ? BigDecimal.ZERO : req.getAmount());
            if (after.compareTo(order.getPrice()) > 0) {
                return Result.error("收款金额总和不能大于订单总额");
            }
        }
        
        OrderPayment payment = new OrderPayment();
        BeanUtils.copyProperties(req, payment);
        payment.setCreateBy(req.getCurrentUserId());
        if (payment.getPayTime() == null) {
            payment.setPayTime(LocalDateTime.now());
        }
        // Persist first to get payment id
        
        orderPaymentMapper.insert(payment);
        
        if (req.getAttachments() != null && !req.getAttachments().isEmpty()) {
            for (String url : req.getAttachments()) {
                OrderPaymentAttachment att = new OrderPaymentAttachment();
                att.setPaymentId(payment.getId());
                att.setUrl(url);
                orderPaymentAttachmentMapper.insert(att);
            }
        }
        
        // Update order payment status
        updateOrderPaymentStatus(order);
        
        return Result.success("Payment recorded successfully");
    }
    
    /**
     * updateOrderPaymentStatus 方法
     */
    private void updateOrderPaymentStatus(WindowOrder order) {
        List<OrderPayment> payments = orderPaymentMapper.getByOrderId(order.getId());
        BigDecimal totalPaid = payments.stream()
                .map(OrderPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setPaidAmount(totalPaid);
        
        if (order.getPrice() != null && order.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
                order.setPaymentStatus("UNPAID");
            } else if (totalPaid.compareTo(order.getPrice()) >= 0) {
                order.setPaymentStatus("PAID");
            } else {
                order.setPaymentStatus("PARTIAL");
            }
        } else {
            // Price not set yet
             if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
                 order.setPaymentStatus("PARTIAL");
             } else {
                 order.setPaymentStatus("UNPAID");
             }
        }
        
        windowOrderMapper.update(order);
    }

    /**
     * listByOrderId 方法
     */
    public Result<List<OrderPayment>> listByOrderId(Long orderId) {
        List<OrderPayment> list = orderPaymentMapper.getByOrderId(orderId);
        for (OrderPayment p : list) {
            List<OrderPaymentAttachment> atts = orderPaymentAttachmentMapper.getByPaymentId(p.getId());
            if (atts != null && !atts.isEmpty()) {
                List<String> urls = new ArrayList<>();
                for (OrderPaymentAttachment a : atts) {
                    urls.add(a.getUrl());
                }
                p.setAttachmentList(urls);
            }
        }
        return Result.success(list);
    }
}
