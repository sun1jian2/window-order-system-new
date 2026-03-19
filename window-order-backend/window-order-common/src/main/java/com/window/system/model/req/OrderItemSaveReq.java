package com.window.system.model.req;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
/**
 * OrderItemSaveReq 实体/请求/响应类
 */
public class OrderItemSaveReq {
    private Long id;
    
    @NotNull(message = "产品不能为空")
    private Long productId;
    
    @NotNull(message = "宽度不能为空")
    private Double width;
    
    @NotNull(message = "高度不能为空")
    private Double height;
    
    private Double area;
    
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    private String color;
    private String glassSpec;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
