package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
/**
 * PurchaseOrder 实体/请求/响应类
 */
public class PurchaseOrder implements Serializable {
    private Long id;
    private String orderNo;
    private Long supplierId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String status; // DRAFT, PENDING, COMPLETED, CANCELLED
    private Date purchaseDate;
    private String remark;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer isDeleted;
    
    // Transient fields
    private String supplierName;
    private List<PurchaseOrderItem> items;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
