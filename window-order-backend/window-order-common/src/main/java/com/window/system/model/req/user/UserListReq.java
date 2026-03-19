package com.window.system.model.req.user;

import cn.hutool.json.JSONUtil;
import com.window.system.model.req.BasePageReq;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
/**
 * UserListReq 实体/请求/响应类
 */
public class UserListReq extends BasePageReq {
    private String username;
    private String realName;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
