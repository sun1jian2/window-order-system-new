package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private Double width;
    private Double height;
    private Double area;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String color;
    private String glassSpec;
    private String remark;
    private Boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // VO fields
    private String productName;
    private String productCode;
}
