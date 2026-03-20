package com.window.system.mapper;

import com.window.system.model.entity.ProductionProcess;
import com.window.system.model.req.ProductionProcessListReq;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * ProductionProcessMapper Mapper接口
 */
@Mapper
public interface ProductionProcessMapper {

    /**
     * 新增生产工序方法
     */
    @Insert("INSERT INTO production_process (plan_id, plan_no, process_name, operator_id, status, start_time, end_time, remark, create_time, create_by, update_by, is_deleted) " +
            "VALUES (#{planId}, #{planNo}, #{processName}, #{operatorId}, #{status}, #{startTime}, #{endTime}, #{remark}, NOW(), #{createBy}, #{updateBy}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProductionProcess process);

    /**
     * 更新生产工序方法
     */
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
    int update(ProductionProcess process);

    /**
     * 根据主键删除生产工序方法
     */
    @Update("UPDATE production_process SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    int delete(Long id);

    /**
     * 根据主键查询生产工序方法
     */
    @Select("SELECT p.*, u.real_name as operator_name " +
            "FROM production_process p " +
            "LEFT JOIN sys_user u ON p.operator_id = u.id " +
            "WHERE p.id = #{id} AND p.is_deleted = 0")
    ProductionProcess getById(Long id);

    /**
     * 查询生产工序列表总数方法
     */
    @Select("<script>" +
            "SELECT count(1) FROM production_process p " +
            "WHERE p.is_deleted = 0 " +
            "<if test='planId != null'> AND p.plan_id = #{planId}</if> " +
            "<if test='planNo != null and planNo != \"\"'> AND p.plan_no LIKE CONCAT('%', #{planNo}, '%')</if> " +
            "<if test='processName != null and processName != \"\"'> AND p.process_name LIKE CONCAT('%', #{processName}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status}</if> " +
            "<if test='operatorId != null'> AND p.operator_id = #{operatorId}</if> " +
            "</script>")
    long countList(ProductionProcessListReq req);

    /**
     * 条件查询生产工序列表方法
     */
    @Select("<script>" +
            "SELECT p.*, u.real_name as operator_name " +
            "FROM production_process p " +
            "LEFT JOIN sys_user u ON p.operator_id = u.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='planId != null'> AND p.plan_id = #{planId}</if> " +
            "<if test='planNo != null and planNo != \"\"'> AND p.plan_no LIKE CONCAT('%', #{planNo}, '%')</if> " +
            "<if test='processName != null and processName != \"\"'> AND p.process_name LIKE CONCAT('%', #{processName}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status}</if> " +
            "<if test='operatorId != null'> AND p.operator_id = #{operatorId}</if> " +
            "ORDER BY p.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<ProductionProcess> list(ProductionProcessListReq req);

    /**
     * 校验排产计划是否存在的方法
     */
    @Select("SELECT id FROM production_plan WHERE plan_no = #{planNo} AND is_deleted = 0 LIMIT 1")
    Long checkPlanExists(@Param("planNo") String planNo);
}
