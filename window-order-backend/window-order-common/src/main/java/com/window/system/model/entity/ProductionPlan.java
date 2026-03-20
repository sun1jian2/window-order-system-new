package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ProductionPlan 实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionPlan {
    private Long id;
    private String planNo;
    private Long orderId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedStartDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedEndDate;
    
    private Long managerId;
    private String status;
    private String remark;
    private Long createBy;
    private Long updateBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private Boolean isDeleted;
    
    // Vo fields
    private String orderNo;
    private String customerName;
    private String managerName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
