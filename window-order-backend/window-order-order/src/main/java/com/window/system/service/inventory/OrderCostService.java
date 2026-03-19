package com.window.system.service.inventory;

import com.window.system.common.Result;
import com.window.system.model.entity.inventory.OrderCost;
import com.window.system.model.req.inventory.OrderCostSaveReq;

/**
 * OrderCostService 服务类/接口
 */
public interface OrderCostService {
    /**
     * getByOrderId 方法
     */
    Result<OrderCost> getByOrderId(Long orderId);
    /**
     * save 方法
     */
    Result<String> save(OrderCostSaveReq req, Long currentUserId);
}
