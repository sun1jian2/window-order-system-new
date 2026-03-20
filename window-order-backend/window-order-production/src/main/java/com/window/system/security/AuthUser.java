package com.window.system.security;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthUser {
    private Long id;
    private String username;
    private String role; // ADMIN/SALES/INSTALLER

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
