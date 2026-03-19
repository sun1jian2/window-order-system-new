package com.window.system.model.req.inventory;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
/**
 * PurchaseOrderSaveReq 实体/请求/响应类
 */
public class PurchaseOrderSaveReq {
    private Long id;
    
    @NotNull(message = "供应商不能为空")
    private Long supplierId;
    
    private BigDecimal totalAmount;
    private Date purchaseDate;
    private String remark;
    
    @Valid
    private List<PurchaseOrderItemSaveReq> items;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
