package com.window.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * RecentActivityDto 实体/请求/响应类
 */
public class RecentActivityDto {
    private String module;
    private String operation;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
