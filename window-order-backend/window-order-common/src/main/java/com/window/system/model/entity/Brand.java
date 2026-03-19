package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty("ID")
    private Long id;
    
    @ExcelProperty("品牌名称")
    private String name;
    
    @ExcelProperty("品牌描述")
    private String description;
    
    @ExcelProperty("创建时间")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long createBy;
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long updateBy;
    @com.alibaba.excel.annotation.ExcelIgnore
    private Boolean isDeleted;
}
