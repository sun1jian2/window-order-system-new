package com.window.system.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
/**
 * ProductListReq 实体/请求/响应类
 */
public class ProductListReq extends BasePageReq {
    private Long categoryId;
    private Long brandId;
    private String keyword;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
