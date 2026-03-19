package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * Product 实体/请求/响应类
 */
public class Product {
    private Long id;
    private Long categoryId;
    private Long brandId;
    private String name;
    private String code;
    private BigDecimal basePrice;
    private String colorOptions;
    private String glassOptions;
    private String description;
    private String status;
    private Boolean isDeleted;
    private Long createBy;
    private Long updateBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // VO fields
    private String categoryName;
    private String brandName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
