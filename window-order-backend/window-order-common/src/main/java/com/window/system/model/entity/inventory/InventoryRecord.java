package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
/**
 * InventoryRecord 实体/请求/响应类
 */
public class InventoryRecord implements Serializable {
    private Long id;
    private String type; // INBOUND, OUTBOUND
    private Long materialId;
    private BigDecimal quantity;
    private BigDecimal beforeQuantity;
    private BigDecimal afterQuantity;
    private String relationType; // PURCHASE, ORDER, MANUAL
    private Long relationId;
    private Long operatorId;
    private String remark;
    private Date createTime;
    
    // Transient fields
    private String materialName;
    private String materialCode;
    private String operatorName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
