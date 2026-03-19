package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
/**
 * RemeasureTaskSubmitReq 实体/请求/响应类
 */
public class RemeasureTaskSubmitReq {
    @NotNull(message = "Task ID cannot be null")
    private Long id;

    @NotNull(message = "Precise width cannot be null")
    private Double preciseWidth;

    @NotNull(message = "Precise height cannot be null")
    private Double preciseHeight;

    private String sketchUrl;

    private String sitePhotos; // JSON array string
    
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
