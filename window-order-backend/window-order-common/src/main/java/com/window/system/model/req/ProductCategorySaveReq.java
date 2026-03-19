package com.window.system.model.req;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ProductCategorySaveReq {
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private Integer sort;
}
