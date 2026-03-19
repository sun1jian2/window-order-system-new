package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * AfterSalesCreateReq 实体/请求/响应类
 */
public class AfterSalesCreateReq {
    private Long orderId;
    private String customerName;
    private String customerPhone;
    private String address;
    private String issueDescription;
    private Long handlerId;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    
    // Auth
    private Long currentUserId;
    private String currentUserRole;
    
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
