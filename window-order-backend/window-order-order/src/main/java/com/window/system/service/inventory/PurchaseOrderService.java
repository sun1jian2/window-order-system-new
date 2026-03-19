package com.window.system.service.inventory;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.PurchaseOrder;
import com.window.system.model.req.inventory.PurchaseOrderListReq;
import com.window.system.model.req.inventory.PurchaseOrderSaveReq;

/**
 * PurchaseOrderService 服务类/接口
 */
public interface PurchaseOrderService {
    /**
     * list 方法
     */
    Result<PageResponse<PurchaseOrder>> list(PurchaseOrderListReq req);
    /**
     * getDetail 方法
     */
    Result<PurchaseOrder> getDetail(Long id);
    /**
     * save 方法
     */
    Result<String> save(PurchaseOrderSaveReq req, Long currentUserId);
    /**
     * submit 方法
     */
    Result<String> submit(Long id, Long currentUserId);
    /**
     * inbound 方法
     */
    Result<String> inbound(Long id, Long currentUserId);
}
