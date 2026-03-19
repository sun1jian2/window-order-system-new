package com.window.system.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
/**
 * CustomerSaveReq 实体/请求/响应类
 */
public class CustomerSaveReq {
    private Long id;
    
    @NotBlank(message = "客户名不能为空")
    private String name;
    
    @NotBlank(message = "手机号不能为空")
    private String phone;
    
    private String address;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
