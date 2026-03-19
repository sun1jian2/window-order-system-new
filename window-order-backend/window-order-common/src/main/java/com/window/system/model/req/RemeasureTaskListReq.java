package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * RemeasureTaskListReq 实体/请求/响应类
 */
public class RemeasureTaskListReq extends BasePageReq {
    private String orderNo;
    private Long orderId;
    private Long assigneeId;
    private String status;
    
    private Long currentUserId;
    private String currentUserRole;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
