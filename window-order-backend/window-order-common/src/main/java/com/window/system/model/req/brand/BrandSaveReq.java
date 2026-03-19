package com.window.system.model.req.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandSaveReq {
    private Long id;
    
    @NotBlank(message = "Brand name cannot be empty")
    private String name;
    
    private String description;
    
    // 操作人ID，用于审计字段
    private Long currentUserId;
}
