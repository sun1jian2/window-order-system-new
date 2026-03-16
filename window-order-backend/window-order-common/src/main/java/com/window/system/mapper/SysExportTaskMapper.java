package com.window.system.mapper;

import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.ExportTaskListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysExportTaskMapper {

    @Insert("INSERT INTO sys_export_task(task_name, status, file_url, file_name, error_msg, export_type, export_params, create_by, create_time) " +
            "VALUES(#{taskName}, #{status}, #{fileUrl}, #{fileName}, #{errorMsg}, #{exportType}, #{exportParams}, #{createBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysExportTask task);

    @Update("<script>" +
            "UPDATE sys_export_task SET status = #{status} " +
            "<if test='fileUrl != null'>, file_url = #{fileUrl}</if> " +
            "<if test='fileName != null'>, file_name = #{fileName}</if> " +
            "<if test='errorMsg != null'>, error_msg = #{errorMsg}</if> " +
            "<if test='finishTime != null'>, finish_time = #{finishTime}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(SysExportTask task);

    @Select("SELECT * FROM sys_export_task WHERE id = #{id}")
    SysExportTask getById(Long id);

    @Select("<script>" +
            "SELECT count(1) FROM sys_export_task t " +
            "LEFT JOIN sys_user u ON t.create_by = u.id " +
            "WHERE 1=1 " +
            "<if test='userId != null'> AND t.create_by = #{userId} </if> " +
            "<if test='req.operatorName != null and req.operatorName != \"\"'> AND u.real_name LIKE CONCAT('%', #{req.operatorName}, '%')</if> " +
            "<if test='req.startTime != null'> AND t.create_time &gt;= #{req.startTime}</if> " +
            "<if test='req.endTime != null'> AND t.create_time &lt;= #{req.endTime}</if> " +
            "</script>")
    long count(@Param("req") ExportTaskListReq req, @Param("userId") Long userId);

    @Select("<script>" +
            "SELECT t.*, u.real_name as createByName " +
            "FROM sys_export_task t " +
            "LEFT JOIN sys_user u ON t.create_by = u.id " +
            "WHERE 1=1 " +
            "<if test='userId != null'> AND t.create_by = #{userId} </if> " +
            "<if test='req.operatorName != null and req.operatorName != \"\"'> AND u.real_name LIKE CONCAT('%', #{req.operatorName}, '%')</if> " +
            "<if test='req.startTime != null'> AND t.create_time &gt;= #{req.startTime}</if> " +
            "<if test='req.endTime != null'> AND t.create_time &lt;= #{req.endTime}</if> " +
            "ORDER BY t.create_time DESC " +
            "LIMIT #{req.startIndex}, #{req.pageSize}" +
            "</script>")
    List<SysExportTask> list(@Param("req") ExportTaskListReq req, @Param("userId") Long userId);
}
