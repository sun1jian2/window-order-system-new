package com.window.system.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductListReq extends BasePageReq {
    private Long categoryId;
    private String keyword;
}
