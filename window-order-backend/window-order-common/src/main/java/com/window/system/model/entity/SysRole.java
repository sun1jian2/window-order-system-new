package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * SysRole 实体/请求/响应类
 */
public class SysRole {
    private Long id;
    private String roleCode;
    private String roleName;
    private LocalDateTime createTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
