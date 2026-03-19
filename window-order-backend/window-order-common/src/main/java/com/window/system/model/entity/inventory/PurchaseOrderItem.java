package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
/**
 * PurchaseOrderItem 实体/请求/响应类
 */
public class PurchaseOrderItem implements Serializable {
    private Long id;
    private Long purchaseOrderId;
    private Long materialId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String remark;
    
    // Transient fields
    private String materialName;
    private String materialCode;
    private String materialSpec;
    private String materialUnit;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
