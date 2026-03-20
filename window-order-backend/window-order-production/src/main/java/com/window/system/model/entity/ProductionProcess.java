package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * ProductionProcess 实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionProcess {
    private Long id;
    private Long planId;
    private String processName;
    private Long operatorId;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    private String remark;
    private Long createBy;
    private Long updateBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private Boolean isDeleted;
    
    // Vo fields
    private String operatorName;
    private String planNo;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
