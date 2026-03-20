package com.window.system.mapper;

import com.window.system.model.entity.QcRecord;
import com.window.system.model.req.QcRecordListReq;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * QcRecordMapper Mapper接口
 */
@Mapper
public interface QcRecordMapper {

    /**
     * 新增质检记录方法
     */
    @Insert("INSERT INTO qc_record (plan_id, plan_no, process_id, inspector_id, check_time, result, defect_reason, remark, create_time, create_by, update_by, is_deleted) " +
            "VALUES (#{planId}, #{planNo}, #{processId}, #{inspectorId}, #{checkTime}, #{result}, #{defectReason}, #{remark}, NOW(), #{createBy}, #{updateBy}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(QcRecord record);

    /**
     * 更新质检记录方法
     */
    @Update("<script>" +
            "UPDATE qc_record SET update_time = NOW() " +
            "<if test='result != null'>, result = #{result}</if> " +
            "<if test='defectReason != null'>, defect_reason = #{defectReason}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "<if test='updateBy != null'>, update_by = #{updateBy}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(QcRecord record);

    /**
     * 根据主键删除质检记录方法
     */
    @Update("UPDATE qc_record SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    int delete(Long id);

    /**
     * 根据主键查询质检记录方法
     */
    @Select("SELECT q.*, pr.process_name as process_name, u.real_name as inspector_name " +
            "FROM qc_record q " +
            "LEFT JOIN production_process pr ON q.process_id = pr.id " +
            "LEFT JOIN sys_user u ON q.inspector_id = u.id " +
            "WHERE q.id = #{id} AND q.is_deleted = 0")
    QcRecord getById(Long id);

    /**
     * 查询质检记录列表总数方法
     */
    @Select("<script>" +
            "SELECT count(1) FROM qc_record q " +
            "WHERE q.is_deleted = 0 " +
            "<if test='planId != null'> AND q.plan_id = #{planId}</if> " +
            "<if test='planNo != null and planNo != \"\"'> AND q.plan_no LIKE CONCAT('%', #{planNo}, '%')</if> " +
            "<if test='processId != null'> AND q.process_id = #{processId}</if> " +
            "<if test='inspectorId != null'> AND q.inspector_id = #{inspectorId}</if> " +
            "<if test='result != null and result != \"\"'> AND q.result = #{result}</if> " +
            "<if test='startDate != null'> AND q.check_time &gt;= #{startDate}</if> " +
            "<if test='endDate != null'> AND q.check_time &lt;= #{endDate}</if> " +
            "</script>")
    long countList(QcRecordListReq req);

    /**
     * 条件查询质检记录列表方法
     */
    @Select("<script>" +
            "SELECT q.*, pr.process_name as process_name, u.real_name as inspector_name " +
            "FROM qc_record q " +
            "LEFT JOIN production_process pr ON q.process_id = pr.id " +
            "LEFT JOIN sys_user u ON q.inspector_id = u.id " +
            "WHERE q.is_deleted = 0 " +
            "<if test='planId != null'> AND q.plan_id = #{planId}</if> " +
            "<if test='planNo != null and planNo != \"\"'> AND q.plan_no LIKE CONCAT('%', #{planNo}, '%')</if> " +
            "<if test='processId != null'> AND q.process_id = #{processId}</if> " +
            "<if test='inspectorId != null'> AND q.inspector_id = #{inspectorId}</if> " +
            "<if test='result != null and result != \"\"'> AND q.result = #{result}</if> " +
            "<if test='startDate != null'> AND q.check_time &gt;= #{startDate}</if> " +
            "<if test='endDate != null'> AND q.check_time &lt;= #{endDate}</if> " +
            "ORDER BY q.check_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<QcRecord> list(QcRecordListReq req);

    /**
     * 校验排产计划是否存在的方法
     */
    @Select("SELECT id FROM production_plan WHERE plan_no = #{planNo} AND is_deleted = 0 LIMIT 1")
    Long checkPlanExists(@Param("planNo") String planNo);
}
