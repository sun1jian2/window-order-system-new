package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * OrderCreateReq 实体/请求/响应类
 */
public class OrderCreateReq {
    private String customerName;
    private String customerPhone;
    private Long customerId;
    private String address;
    private String brand;
    private String windowType;
    private String color;
    private String glassSpec;
    private Double width;
    private Double height;
    private BigDecimal price;
    private LocalDateTime orderTime;
    private Long salespersonId;
    private Long installerId;
    private String status;
    
    // Auth fields
    private Long currentUserId;
    private String currentUserRole;
    
    // Address structured fields
    private String regionCodes;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    
    // Order items (Smart Pricing)
    @jakarta.validation.Valid
    private java.util.List<OrderItemSaveReq> items;


    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }




}
