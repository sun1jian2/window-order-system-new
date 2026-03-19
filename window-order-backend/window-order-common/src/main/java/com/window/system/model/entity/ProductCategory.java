package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * ProductCategory 实体/请求/响应类
 */
public class ProductCategory {
    private Long id;
    private String name;
    private Integer sort;
    private Boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
