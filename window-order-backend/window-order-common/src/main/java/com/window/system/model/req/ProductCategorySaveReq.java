package com.window.system.model.req;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
/**
 * ProductCategorySaveReq 实体/请求/响应类
 */
public class ProductCategorySaveReq {
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private Integer sort;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
