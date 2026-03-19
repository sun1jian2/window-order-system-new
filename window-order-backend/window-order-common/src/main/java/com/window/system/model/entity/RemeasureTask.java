package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * RemeasureTask 实体/请求/响应类
 */
public class RemeasureTask {
    @ExcelProperty("ID")
    private Long id;

    @com.alibaba.excel.annotation.ExcelIgnore
    private Long orderId;

    @com.alibaba.excel.annotation.ExcelIgnore
    private Long assigneeId;

    @ExcelProperty("状态")
    private String status; // PENDING, COMPLETED

    @ExcelProperty("精确宽度")
    private Double preciseWidth;

    @ExcelProperty("精确高度")
    private Double preciseHeight;

    @com.alibaba.excel.annotation.ExcelIgnore
    private String sketchUrl;

    @com.alibaba.excel.annotation.ExcelIgnore
    private String sitePhotos; // JSON array

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @com.alibaba.excel.annotation.ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // VO fields
    @ExcelProperty("订单号")
    private String orderNo;
    @ExcelProperty("客户姓名")
    private String customerName;
    @ExcelProperty("地址")
    private String address;
    @ExcelProperty("复尺人员")
    private String assigneeName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
