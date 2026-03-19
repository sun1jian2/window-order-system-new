package com.window.system.model.req.inventory;

import com.window.system.model.req.BasePageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
/**
 * PurchaseOrderListReq 实体/请求/响应类
 */
public class PurchaseOrderListReq extends BasePageReq {
    private String orderNo;
    private Long supplierId;
    private String status;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
