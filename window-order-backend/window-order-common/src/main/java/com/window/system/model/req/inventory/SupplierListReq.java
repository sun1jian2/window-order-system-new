package com.window.system.model.req.inventory;

import com.window.system.model.req.BasePageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
/**
 * SupplierListReq 实体/请求/响应类
 */
public class SupplierListReq extends BasePageReq {
    private String name;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
