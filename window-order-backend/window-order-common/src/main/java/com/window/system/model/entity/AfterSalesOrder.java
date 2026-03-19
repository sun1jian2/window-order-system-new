package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * AfterSalesOrder 实体/请求/响应类
 */
public class AfterSalesOrder {
    @ExcelProperty("ID")
    private Long id;
    
    @ExcelProperty("工单号")
    private String ticketNo;
    
    @ExcelIgnore
    private Long orderId;
    
    @ExcelProperty("客户姓名")
    private String customerName;
    
    @ExcelProperty("电话")
    private String customerPhone;
    
    @ExcelProperty("地址")
    private String address;
    
    @ExcelProperty("问题描述")
    private String issueDescription;
    
    @ExcelProperty("状态")
    private String status; // PENDING, ASSIGNED, PROCESSING, COMPLETED, CANCELLED
    
    @ExcelIgnore
    private Long handlerId;
    
    @ExcelProperty("预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    
    @ExcelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionTime;
    
    @ExcelProperty("解决方案")
    private String solution;
    
    @ExcelProperty("费用")
    private BigDecimal fee;
    
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
    @ExcelProperty("处理人")
    private String handlerName;
    @ExcelProperty("关联订单号")
    private String orderNo; // from WindowOrder

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
