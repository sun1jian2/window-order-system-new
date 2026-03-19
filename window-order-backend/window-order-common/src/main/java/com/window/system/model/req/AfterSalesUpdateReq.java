package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * AfterSalesUpdateReq 实体/请求/响应类
 */
public class AfterSalesUpdateReq {
    private Long id;
    private String status;
    private Long handlerId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    private String solution;
    private BigDecimal fee;
    
    // Auth
    private Long currentUserId;
    private String currentUserRole;
    
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
