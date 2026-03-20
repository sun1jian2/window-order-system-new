package com.window.system.mapper;

import com.window.system.model.entity.QcRecord;
import com.window.system.model.req.QcRecordListReq;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
/**
 * QcRecordMapper Mapper接口
 */
public interface QcRecordMapper {

    @Insert("INSERT INTO qc_record (plan_id, plan_no, process_id, inspector_id, check_time, result, defect_reason, remark, create_time, create_by, update_by, is_deleted) " +
            "VALUES (#{planId}, #{planNo}, #{processId}, #{inspectorId}, #{checkTime}, #{result}, #{defectReason}, #{remark}, NOW(), #{createBy}, #{updateBy}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(QcRecord record);

    @Update("<script>" +
            "UPDATE qc_record SET update_time = NOW() " +
            "<if test='result != null'>, result = #{result}</if> " +
            "<if test='defectReason != null'>, defect_reason = #{defectReason}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "<if test='updateBy != null'>, update_by = #{updateBy}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    /**
     * update 方法
     */
    int update(QcRecord record);

    @Update("UPDATE qc_record SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    /**
     * delete 方法
     */
    int delete(Long id);

    @Select("SELECT q.*, pr.process_name as process_name, u.real_name as inspector_name " +
            "FROM qc_record q " +
            "LEFT JOIN production_process pr ON q.process_id = pr.id " +
            "LEFT JOIN sys_user u ON q.inspector_id = u.id " +
            "WHERE q.id = #{id} AND q.is_deleted = 0")
    /**
     * getById 方法
     */
    QcRecord getById(Long id);

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
    /**
     * countList 方法
     */
    long countList(QcRecordListReq req);

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
    /**
     * list 方法
     */
    List<QcRecord> list(QcRecordListReq req);

    @Select("SELECT count(1) FROM production_plan WHERE plan_no = #{planNo} AND is_deleted = 0")
    /**
     * checkPlanExists 方法
     */
    long checkPlanExists(@Param("planNo") String planNo);
}
