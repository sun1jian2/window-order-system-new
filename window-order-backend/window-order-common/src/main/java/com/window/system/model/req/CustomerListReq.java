package com.window.system.model.req;

import lombok.Data;

@Data
/**
 * CustomerListReq 实体/请求/响应类
 */
public class CustomerListReq {
    private String name;
    private String phone;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
