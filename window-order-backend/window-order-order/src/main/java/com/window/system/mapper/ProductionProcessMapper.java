package com.window.system.mapper;

import com.window.system.model.entity.ProductionProcess;
import com.window.system.model.req.ProductionProcessListReq;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
/**
 * ProductionProcessMapper Mapper接口
 */
public interface ProductionProcessMapper {

    @Insert("INSERT INTO production_process (plan_id, process_name, operator_id, status, start_time, end_time, remark, create_time, create_by, update_by, is_deleted) " +
            "VALUES (#{planId}, #{processName}, #{operatorId}, #{status}, #{startTime}, #{endTime}, #{remark}, NOW(), #{createBy}, #{updateBy}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(ProductionProcess process);

    @Update("<script>" +
            "UPDATE production_process SET update_time = NOW() " +
            "<if test='processName != null'>, process_name = #{processName}</if> " +
            "<if test='operatorId != null'>, operator_id = #{operatorId}</if> " +
            "<if test='status != null'>, status = #{status}</if> " +
            "<if test='startTime != null'>, start_time = #{startTime}</if> " +
            "<if test='endTime != null'>, end_time = #{endTime}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "<if test='updateBy != null'>, update_by = #{updateBy}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    /**
     * update 方法
     */
    int update(ProductionProcess process);

    @Update("UPDATE production_process SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    /**
     * delete 方法
     */
    int delete(Long id);

    @Select("SELECT p.*, pl.plan_no as plan_no, u.real_name as operator_name " +
            "FROM production_process p " +
            "LEFT JOIN production_plan pl ON p.plan_id = pl.id " +
            "LEFT JOIN sys_user u ON p.operator_id = u.id " +
            "WHERE p.id = #{id} AND p.is_deleted = 0")
    /**
     * getById 方法
     */
    ProductionProcess getById(Long id);

    @Select("<script>" +
            "SELECT count(1) FROM production_process p " +
            "WHERE p.is_deleted = 0 " +
            "<if test='planId != null'> AND p.plan_id = #{planId}</if> " +
            "<if test='processName != null and processName != \"\"'> AND p.process_name LIKE CONCAT('%', #{processName}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status}</if> " +
            "<if test='operatorId != null'> AND p.operator_id = #{operatorId}</if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(ProductionProcessListReq req);

    @Select("<script>" +
            "SELECT p.*, pl.plan_no as plan_no, u.real_name as operator_name " +
            "FROM production_process p " +
            "LEFT JOIN production_plan pl ON p.plan_id = pl.id " +
            "LEFT JOIN sys_user u ON p.operator_id = u.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='planId != null'> AND p.plan_id = #{planId}</if> " +
            "<if test='processName != null and processName != \"\"'> AND p.process_name LIKE CONCAT('%', #{processName}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status}</if> " +
            "<if test='operatorId != null'> AND p.operator_id = #{operatorId}</if> " +
            "ORDER BY p.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * list 方法
     */
    List<ProductionProcess> list(ProductionProcessListReq req);
}
