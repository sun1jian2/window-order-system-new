package com.window.system.service.inventory.impl;

import com.window.system.common.Result;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.mapper.inventory.OrderCostMapper;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.entity.inventory.OrderCost;
import com.window.system.model.req.inventory.OrderCostSaveReq;
import com.window.system.service.inventory.OrderCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
/**
 * OrderCostServiceImpl 服务类/接口
 */
public class OrderCostServiceImpl implements OrderCostService {

    @Autowired
    private OrderCostMapper orderCostMapper;
    
    @Autowired
    private WindowOrderMapper windowOrderMapper;

    @Override
    /**
     * getByOrderId 方法
     */
    public Result<OrderCost> getByOrderId(Long orderId) {
        return Result.success(orderCostMapper.getByOrderId(orderId));
    }

    @Override
    /**
     * save 方法
     */
    public Result<String> save(OrderCostSaveReq req, Long currentUserId) {
        WindowOrder order = windowOrderMapper.getById(req.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        OrderCost cost = new OrderCost();
        cost.setOrderId(req.getOrderId());
        cost.setMaterialCost(req.getMaterialCost() != null ? req.getMaterialCost() : BigDecimal.ZERO);
        cost.setLaborCost(req.getLaborCost() != null ? req.getLaborCost() : BigDecimal.ZERO);
        cost.setOtherCost(req.getOtherCost() != null ? req.getOtherCost() : BigDecimal.ZERO);
        cost.setRemark(req.getRemark());
        
        // 计算总成本
        BigDecimal totalCost = cost.getMaterialCost().add(cost.getLaborCost()).add(cost.getOtherCost());
        cost.setTotalCost(totalCost);
        
        // 获取订单总金额作为收入
        BigDecimal orderAmount = order.getPrice() != null ? order.getPrice() : BigDecimal.ZERO;
        cost.setOrderAmount(orderAmount);
        
        // 计算毛利
        BigDecimal grossProfit = orderAmount.subtract(totalCost);
        cost.setGrossProfit(grossProfit);
        
        // 计算毛利率
        if (orderAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = grossProfit.divide(orderAmount, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            cost.setGrossProfitRate(rate);
        } else {
            cost.setGrossProfitRate(BigDecimal.ZERO);
        }

        OrderCost exist = orderCostMapper.getByOrderId(req.getOrderId());
        if (exist == null) {
            cost.setCreateBy(currentUserId);
            orderCostMapper.insert(cost);
        } else {
            cost.setId(exist.getId());
            cost.setUpdateBy(currentUserId);
            orderCostMapper.update(cost);
        }
        
        return Result.success("成本核算保存成功");
    }
}
