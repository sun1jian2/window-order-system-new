package com.window.system.model.req.inventory;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
/**
 * SupplierSaveReq 实体/请求/响应类
 */
public class SupplierSaveReq {
    private Long id;
    
    @NotBlank(message = "供应商名称不能为空")
    private String name;
    
    private String contactPerson;
    private String phone;
    private String address;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
