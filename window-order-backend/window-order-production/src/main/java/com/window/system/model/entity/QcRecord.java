package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * QcRecord 实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QcRecord {
    private Long id;
    private Long planId;
    private Long processId;
    private Long inspectorId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkTime;
    
    private String result;
    private String defectReason;
    private String remark;
    private Long createBy;
    private Long updateBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private Boolean isDeleted;
    
    // Vo fields
    private String inspectorName;
    private String planNo;
    private String processName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
