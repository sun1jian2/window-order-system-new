package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * OrderPaymentAttachment 实体/请求/响应类
 */
public class OrderPaymentAttachment {
    private Long id;
    private Long paymentId;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
