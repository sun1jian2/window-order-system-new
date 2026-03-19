package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
/**
 * LogListReq 实体/请求/响应类
 */
public class LogListReq extends BasePageReq {
    private String username;
    private String module;
    private String operation;
    private String keyword;
    private String startTime; // yyyy-MM-dd HH:mm:ss
    private String endTime;   // yyyy-MM-dd HH:mm:ss

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
