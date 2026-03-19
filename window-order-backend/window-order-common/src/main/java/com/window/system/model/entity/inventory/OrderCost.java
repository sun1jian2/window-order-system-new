package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
/**
 * OrderCost 实体/请求/响应类
 */
public class OrderCost implements Serializable {
    private Long id;
    private Long orderId;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal otherCost;
    private BigDecimal totalCost;
    private BigDecimal orderAmount;
    private BigDecimal grossProfit;
    private BigDecimal grossProfitRate;
    private String remark;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
