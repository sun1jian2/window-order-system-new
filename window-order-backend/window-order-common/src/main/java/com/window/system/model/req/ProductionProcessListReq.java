package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;

/**
 * ProductionProcessListReq 请求类
 */
@Getter
@Setter
public class ProductionProcessListReq extends BasePageReq {
    private Long planId;
    private String processName;
    private String status;
    private Long operatorId;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
