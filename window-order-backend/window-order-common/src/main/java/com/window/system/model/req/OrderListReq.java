package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
/**
 * OrderListReq 实体/请求/响应类
 */
public class OrderListReq extends BasePageReq {
    private String orderNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String brand;
    private String installProgress;
    private String productionProgress;
    private String logisticsStatus;
    
    private Long searchSalespersonId;
    private Long searchInstallerId;
    
    // Auth fields
    private Long currentUserId;
    private String currentUserRole;


    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
    
}
