package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * SalesTarget 实体/请求/响应类
 */
public class SalesTarget {
    @ExcelProperty("ID")
    private Long id;
    
    @ExcelIgnore
    private Long salespersonId;
    
    @ExcelProperty("月份")
    private String targetMonth; // YYYY-MM
    
    @ExcelProperty("目标金额")
    private BigDecimal targetAmount;
    
    @ExcelIgnore
    private Long createBy;
    
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ExcelIgnore
    private Long updateBy;
    @ExcelIgnore
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
