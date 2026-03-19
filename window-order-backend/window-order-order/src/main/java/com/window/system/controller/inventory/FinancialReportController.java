package com.window.system.controller.inventory;

import com.window.system.common.Result;
import com.window.system.mapper.inventory.FinancialReportMapper;
import com.window.system.model.dto.inventory.FinancialReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/report")
@CrossOrigin(origins = "*")
/**
 * FinancialReportController 控制器类
 */
public class FinancialReportController {

    @Autowired
    private FinancialReportMapper financialReportMapper;

        /**
     * getReport 方法
     */
    @GetMapping("/list")
    public Result<List<FinancialReportDto>> getReport(@RequestParam(value = "type", defaultValue = "MONTH") String type) {
        List<FinancialReportDto> list = financialReportMapper.getReport(type);
        for (FinancialReportDto dto : list) {
            if (dto.getTotalRevenue() != null && dto.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0 && dto.getGrossProfit() != null) {
                BigDecimal rate = dto.getGrossProfit().divide(dto.getTotalRevenue(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                dto.setGrossProfitRate(rate);
            } else {
                dto.setGrossProfitRate(BigDecimal.ZERO);
            }
        }
        return Result.success(list);
    }
}
