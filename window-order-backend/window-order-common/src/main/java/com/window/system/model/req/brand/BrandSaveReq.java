package com.window.system.model.req.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * BrandSaveReq 实体/请求/响应类
 */
public class BrandSaveReq {
    private Long id;
    
    @NotBlank(message = "Brand name cannot be empty")
    private String name;
    
    private String description;
    
    // 操作人ID，用于审计字段
    private Long currentUserId;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
