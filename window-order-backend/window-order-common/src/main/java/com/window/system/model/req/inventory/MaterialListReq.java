package com.window.system.model.req.inventory;

import com.window.system.model.req.BasePageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
/**
 * MaterialListReq 实体/请求/响应类
 */
public class MaterialListReq extends BasePageReq {
    private Long categoryId;
    private String keyword; // name or code
    private Boolean isWarning; // true if stock <= warning

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
