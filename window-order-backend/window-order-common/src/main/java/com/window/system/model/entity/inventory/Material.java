package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
/**
 * Material 实体/请求/响应类
 */
public class Material implements Serializable {
    private Long id;
    private Long categoryId;
    private String name;
    private String code;
    private String spec;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal stockQuantity;
    private BigDecimal warningQuantity;
    private String remark;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer isDeleted;
    
    // Transient fields
    private String categoryName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
