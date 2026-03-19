package com.window.system.mapper;

import com.window.system.model.dto.SalesTargetResp;
import com.window.system.model.entity.SalesTarget;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * SalesTargetMapper Mapper接口
 */
public interface SalesTargetMapper {

    @Insert("INSERT INTO sales_target (salesperson_id, target_month, target_amount, create_by, create_time, update_by, update_time) " +
            "VALUES (#{salespersonId}, #{targetMonth}, #{targetAmount}, #{createBy}, NOW(), #{updateBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(SalesTarget salesTarget);

    @Update("UPDATE sales_target SET target_amount = #{targetAmount}, update_by = #{updateBy}, update_time = NOW() WHERE id = #{id}")
    /**
     * update 方法
     */
    int update(SalesTarget salesTarget);

    @Select("SELECT * FROM sales_target WHERE salesperson_id = #{salespersonId} AND target_month = #{targetMonth}")
    SalesTarget getBySalespersonAndMonth(@Param("salespersonId") Long salespersonId, @Param("targetMonth") String targetMonth);

    @Select("<script>" +
            "SELECT t.*, u.real_name as salesperson_name, " +
            "IFNULL((SELECT SUM(o.price) FROM window_order o WHERE o.salesperson_id = t.salesperson_id AND DATE_FORMAT(o.create_time, '%Y-%m') = t.target_month AND o.is_deleted = 0), 0) as actual_amount " +
            "FROM sales_target t " +
            "LEFT JOIN sys_user u ON t.salesperson_id = u.id " +
            "WHERE 1=1 " +
            "<if test='month != null and month != \"\"'> AND t.target_month = #{month} </if> " +
            "<if test='salespersonId != null'> AND t.salesperson_id = #{salespersonId} </if> " +
            "ORDER BY t.target_month DESC, actual_amount DESC" +
            "</script>")
    List<SalesTargetResp> list(@Param("month") String month, @Param("salespersonId") Long salespersonId);

    @Select("<script>" +
            "SELECT t.*, u.real_name as salesperson_name, " +
            "IFNULL((SELECT SUM(o.price) FROM window_order o WHERE o.salesperson_id = t.salesperson_id AND DATE_FORMAT(o.create_time, '%Y-%m') = t.target_month AND o.is_deleted = 0), 0) as actual_amount " +
            "FROM sales_target t " +
            "LEFT JOIN sys_user u ON t.salesperson_id = u.id " +
            "WHERE 1=1 " +
            "<if test='month != null and month != \"\"'> AND t.target_month = #{month} </if> " +
            "<if test='salespersonId != null'> AND t.salesperson_id = #{salespersonId} </if> " +
            "ORDER BY t.target_month DESC, actual_amount DESC" +
            "</script>")
    List<SalesTargetResp> exportList(@Param("month") String month, @Param("salespersonId") Long salespersonId);
}
