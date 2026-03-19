package com.window.system.model.req.inventory;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
/**
 * OrderCostSaveReq 实体/请求/响应类
 */
public class OrderCostSaveReq {
    private Long id;
    
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal otherCost;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
