package com.window.system.model.req.inventory;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
/**
 * MaterialSaveReq 实体/请求/响应类
 */
public class MaterialSaveReq {
    private Long id;
    
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    @NotBlank(message = "材料名称不能为空")
    private String name;
    
    private String code;
    private String spec;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal stockQuantity;
    private BigDecimal warningQuantity;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
