package com.window.system.model.req.user;

import cn.hutool.json.JSONUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * UserSaveReq 实体/请求/响应类
 */
public class UserSaveReq {
    private Long id;
    
    @NotBlank(message = "Username cannot be empty")
    private String username;
    
    private String password; // Optional for update
    
    private String realName;
    
    @NotBlank(message = "Role cannot be empty")
    private String role;
    
    // 操作人ID，用于审计字段
    private Long currentUserId;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
