package com.window.system.model.dto;

import cn.hutool.json.JSONUtil;
import com.window.system.model.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * LoginResp 实体/请求/响应类
 */
public class LoginResp {
    private String token;
    private SysUser user;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
