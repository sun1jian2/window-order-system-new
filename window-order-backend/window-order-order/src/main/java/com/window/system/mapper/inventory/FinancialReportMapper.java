package com.window.system.mapper.inventory;

import com.window.system.model.dto.inventory.FinancialReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
/**
 * FinancialReportMapper Mapper接口
 */
public interface FinancialReportMapper {

    @Select("<script>" +
            "SELECT " +
            "<if test='type == \"MONTH\"'> DATE_FORMAT(o.create_time, '%Y-%m') as period, </if> " +
            "<if test='type == \"YEAR\"'> DATE_FORMAT(o.create_time, '%Y') as period, </if> " +
            "SUM(IFNULL(o.price, 0)) as totalRevenue, " +
            "SUM(IFNULL(c.material_cost, 0)) as totalMaterialCost, " +
            "SUM(IFNULL(c.labor_cost, 0)) as totalLaborCost, " +
            "SUM(IFNULL(c.other_cost, 0)) as totalOtherCost, " +
            "SUM(IFNULL(c.total_cost, 0)) as totalCost, " +
            "SUM(IFNULL(c.gross_profit, 0)) as grossProfit " +
            "FROM window_order o " +
            "LEFT JOIN order_cost c ON o.id = c.order_id " +
            "WHERE o.is_deleted = 0 AND o.status != 'DRAFT' " +
            "GROUP BY period " +
            "ORDER BY period DESC " +
            "LIMIT 12" +
            "</script>")
    List<FinancialReportDto> getReport(@Param("type") String type);
}
