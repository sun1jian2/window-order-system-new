package com.window.system.model.req;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
/**
 * SalesTargetReq 实体/请求/响应类
 */
public class SalesTargetReq {
    private Long id;
    
    @NotNull(message = "销售员不能为空")
    private Long salespersonId;
    
    @NotBlank(message = "目标月份不能为空")
    private String targetMonth; // YYYY-MM
    
    @NotNull(message = "目标金额不能为空")
    private BigDecimal targetAmount;
    
    private Long currentUserId;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
