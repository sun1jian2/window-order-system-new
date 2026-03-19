package com.window.system.model.req;

import com.window.system.model.req.BasePageReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * AfterSalesListReq 实体/请求/响应类
 */
public class AfterSalesListReq extends BasePageReq {
    private String orderNo;
    private String ticketNo;
    private String customerName;
    private String customerPhone;
    private String status;
    private Long handlerId;
    
    // Auth
    private Long currentUserId;
    private String currentUserRole;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
