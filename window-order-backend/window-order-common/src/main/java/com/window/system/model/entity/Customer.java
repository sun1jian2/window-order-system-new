package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.window.system.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * Customer 实体/请求/响应类
 */
public class Customer {
    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("客户姓名")
    private String name;

    @ExcelProperty("电话")
    private String phone;

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("备注")
    private String remark;

    @ExcelIgnore
    private Long createBy;

    @ExcelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ExcelIgnore
    private Boolean isDeleted;

    // VO Fields
    @ExcelProperty("订单数")
    private Long orderCount;
    @ExcelProperty("总消费金额")
    private BigDecimal totalSpent;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
