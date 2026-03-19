package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
/**
 * OrderPayment 实体/请求/响应类
 */
public class OrderPayment {
    private Long id;
    
    private Long orderId;
    
    private BigDecimal amount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
    
    private String payMethod;
    
    private String remark;
    
    private String attachments;
    
    private Long createBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    // VO fields
    private String createByName;
    
    // VO attachments
    private List<String> attachmentList;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
