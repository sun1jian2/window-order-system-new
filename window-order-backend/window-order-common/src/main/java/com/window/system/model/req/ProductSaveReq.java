package com.window.system.model.req;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductSaveReq {
    private Long id;
    
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    private Long brandId;
    
    @NotBlank(message = "产品名称不能为空")
    private String name;
    
    private String code;
    
    @NotNull(message = "基础单价不能为空")
    private BigDecimal basePrice;
    
    private String colorOptions;
    
    private String glassOptions;
    
    private String description;
    
    private String status;
}
