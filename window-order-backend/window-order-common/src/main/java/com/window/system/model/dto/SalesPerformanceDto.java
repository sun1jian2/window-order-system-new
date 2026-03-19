package com.window.system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * SalesPerformanceDto 实体/请求/响应类
 */
public class SalesPerformanceDto {
    private String name;
    private Long orderCount;
    private BigDecimal amount;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
