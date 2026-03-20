package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * ProductionPlanListReq 请求类
 */
@Getter
@Setter
public class ProductionPlanListReq extends BasePageReq {
    private String planNo;
    private Long orderId;
    private String orderNo;
    private String status;
    private Long managerId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
