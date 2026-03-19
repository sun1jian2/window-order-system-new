package com.window.system.model.entity.inventory;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
/**
 * MaterialCategory 实体/请求/响应类
 */
public class MaterialCategory implements Serializable {
    private Long id;
    private String name;
    private Integer sort;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer isDeleted;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
