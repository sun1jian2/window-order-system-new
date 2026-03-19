package com.window.system.model.req;

import lombok.Data;

@Data
/**
 * SalesTargetListReq 实体/请求/响应类
 */
public class SalesTargetListReq {
    private String month;
    private Long salespersonId;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
