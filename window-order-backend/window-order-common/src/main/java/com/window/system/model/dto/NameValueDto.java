package com.window.system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * NameValueDto 实体/请求/响应类
 */
public class NameValueDto {
    private String name;
    private Long value;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
