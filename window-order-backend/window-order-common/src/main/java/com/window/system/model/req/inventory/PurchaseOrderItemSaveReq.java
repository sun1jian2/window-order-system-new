package com.window.system.model.req.inventory;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
/**
 * PurchaseOrderItemSaveReq 实体/请求/响应类
 */
public class PurchaseOrderItemSaveReq {
    private Long id;
    
    @NotNull(message = "材料不能为空")
    private Long materialId;
    
    @NotNull(message = "数量不能为空")
    private BigDecimal quantity;
    
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
