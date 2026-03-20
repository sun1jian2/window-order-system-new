package com.window.system.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * ProductionPlanSaveReq 请求类
 */
@Getter
@Setter
public class ProductionPlanSaveReq {
    private Long id;
    private String planNo;
    private Long orderId;
    private String orderNo;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedStartDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedEndDate;
    
    private Long managerId;
    private String status;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
