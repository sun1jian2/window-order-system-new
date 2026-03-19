package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
/**
 * RemeasureTaskAssignReq 实体/请求/响应类
 */
public class RemeasureTaskAssignReq {
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "Assignee ID cannot be null")
    private Long assigneeId;
    
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
