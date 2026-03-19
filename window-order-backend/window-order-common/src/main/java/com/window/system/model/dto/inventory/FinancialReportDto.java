package com.window.system.model.dto.inventory;

import lombok.Data;
import java.math.BigDecimal;

@Data
/**
 * FinancialReportDto 实体/请求/响应类
 */
public class FinancialReportDto {
    private String period; // e.g. '2023-05' or '2023'
    private BigDecimal totalRevenue; // 总收入 (订单总额)
    private BigDecimal totalMaterialCost; // 材料成本
    private BigDecimal totalLaborCost; // 人工成本
    private BigDecimal totalOtherCost; // 其他成本
    private BigDecimal totalCost; // 总成本
    private BigDecimal grossProfit; // 毛利润
    private BigDecimal grossProfitRate; // 毛利率

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
