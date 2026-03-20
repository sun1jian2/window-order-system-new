package com.window.system.mapper;

import com.window.system.model.entity.ProductionPlan;
import com.window.system.model.req.ProductionPlanListReq;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
/**
 * ProductionPlanMapper Mapper接口
 */
public interface ProductionPlanMapper {

    @Insert("INSERT INTO production_plan (plan_no, order_id, order_no, planned_start_date, planned_end_date, manager_id, status, remark, create_time, create_by, update_by, is_deleted) " +
            "VALUES (#{planNo}, #{orderId}, #{orderNo}, #{plannedStartDate}, #{plannedEndDate}, #{managerId}, #{status}, #{remark}, NOW(), #{createBy}, #{updateBy}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(ProductionPlan plan);

    @Update("<script>" +
            "UPDATE production_plan SET update_time = NOW() " +
            "<if test='planNo != null'>, plan_no = #{planNo}</if> " +
            "<if test='plannedStartDate != null'>, planned_start_date = #{plannedStartDate}</if> " +
            "<if test='plannedEndDate != null'>, planned_end_date = #{plannedEndDate}</if> " +
            "<if test='managerId != null'>, manager_id = #{managerId}</if> " +
            "<if test='status != null'>, status = #{status}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "<if test='updateBy != null'>, update_by = #{updateBy}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    /**
     * update 方法
     */
    int update(ProductionPlan plan);

    @Update("UPDATE production_plan SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    /**
     * delete 方法
     */
    int delete(Long id);

    @Select("SELECT p.*, o.order_no as order_no, o.customer_name as customer_name, u.real_name as manager_name " +
            "FROM production_plan p " +
            "LEFT JOIN window_order o ON p.order_id = o.id " +
            "LEFT JOIN sys_user u ON p.manager_id = u.id " +
            "WHERE p.id = #{id} AND p.is_deleted = 0")
    /**
     * getById 方法
     */
    ProductionPlan getById(Long id);

    @Select("<script>" +
            "SELECT count(1) FROM production_plan p " +
            "LEFT JOIN window_order o ON p.order_id = o.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='planNo != null and planNo != \"\"'> AND p.plan_no LIKE CONCAT('%', #{planNo}, '%')</if> " +
            "<if test='orderId != null'> AND p.order_id = #{orderId}</if> " +
            "<if test='orderNo != null and orderNo != \"\"'> AND p.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status}</if> " +
            "<if test='managerId != null'> AND p.manager_id = #{managerId}</if> " +
            "<if test='startDate != null'> AND p.planned_start_date &gt;= #{startDate}</if> " +
            "<if test='endDate != null'> AND p.planned_end_date &lt;= #{endDate}</if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(ProductionPlanListReq req);

    @Select("<script>" +
            "SELECT p.*, o.customer_name as customer_name, u.real_name as manager_name " +
            "FROM production_plan p " +
            "LEFT JOIN window_order o ON p.order_id = o.id " +
            "LEFT JOIN sys_user u ON p.manager_id = u.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='planNo != null and planNo != \"\"'> AND p.plan_no LIKE CONCAT('%', #{planNo}, '%')</if> " +
            "<if test='orderId != null'> AND p.order_id = #{orderId}</if> " +
            "<if test='orderNo != null and orderNo != \"\"'> AND p.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status}</if> " +
            "<if test='managerId != null'> AND p.manager_id = #{managerId}</if> " +
            "<if test='startDate != null'> AND p.planned_start_date &gt;= #{startDate}</if> " +
            "<if test='endDate != null'> AND p.planned_end_date &lt;= #{endDate}</if> " +
            "ORDER BY p.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * list 方法
     */
    List<ProductionPlan> list(ProductionPlanListReq req);

    @Select("SELECT count(1) FROM window_order WHERE order_no = #{orderNo} AND is_deleted = 0")
    /**
     * checkOrderExists 方法
     */
    long checkOrderExists(@Param("orderNo") String orderNo);
}
