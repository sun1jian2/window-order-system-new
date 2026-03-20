package com.window.system.mapper;

import com.window.system.model.entity.SysOperationLog;
import com.window.system.model.req.LogListReq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * SysOperationLogMapper Mapper接口
 */
@Mapper
public interface SysOperationLogMapper {

    /**
     * 新增系统操作日志方法
     */
    @Insert("INSERT INTO sys_operation_log(user_id, username, module, operation, method, params, ip, status, error_msg, cost_time, create_time) " +
            "VALUES(#{userId}, #{username}, #{module}, #{operation}, #{method}, #{params}, #{ip}, #{status}, #{errorMsg}, #{costTime}, NOW())")
    void insert(SysOperationLog log);
    
    /**
     * 查询日志列表总数方法
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM sys_operation_log WHERE 1=1 " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='module != null and module != \"\"'> AND module = #{module}</if> " +
            "<if test='operation != null and operation != \"\"'> AND operation = #{operation}</if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (method LIKE CONCAT('%', #{keyword}, '%') OR params LIKE CONCAT('%', #{keyword}, '%'))</if> " +
            "<if test='startTime != null and startTime != \"\"'> AND create_time &gt;= #{startTime}</if> " +
            "<if test='endTime != null and endTime != \"\"'> AND create_time &lt;= #{endTime}</if> " +
            "</script>")
    long countList(LogListReq req);
    
    /**
     * 条件查询日志列表方法
     */
    @Select("<script>" +
            "SELECT id, user_id, username, module, operation, method, params, ip, status, error_msg as errorMsg, cost_time as costTime, create_time as createTime " +
            "FROM sys_operation_log WHERE 1=1 " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='module != null and module != \"\"'> AND module = #{module}</if> " +
            "<if test='operation != null and operation != \"\"'> AND operation = #{operation}</if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (method LIKE CONCAT('%', #{keyword}, '%') OR params LIKE CONCAT('%', #{keyword}, '%'))</if> " +
            "<if test='startTime != null and startTime != \"\"'> AND create_time &gt;= #{startTime}</if> " +
            "<if test='endTime != null and endTime != \"\"'> AND create_time &lt;= #{endTime}</if> " +
            "ORDER BY create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<SysOperationLog> selectList(LogListReq req);

    /**
     * 导出日志列表方法
     */
    @Select("<script>" +
            "SELECT id, user_id, username, module, operation, method, params, ip, status, error_msg as errorMsg, cost_time as costTime, create_time as createTime " +
            "FROM sys_operation_log WHERE 1=1 " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='module != null and module != \"\"'> AND module = #{module}</if> " +
            "<if test='operation != null and operation != \"\"'> AND operation = #{operation}</if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (method LIKE CONCAT('%', #{keyword}, '%') OR params LIKE CONCAT('%', #{keyword}, '%'))</if> " +
            "<if test='startTime != null and startTime != \"\"'> AND create_time &gt;= #{startTime}</if> " +
            "<if test='endTime != null and endTime != \"\"'> AND create_time &lt;= #{endTime}</if> " +
            "ORDER BY create_time DESC " +
            "</script>")
    List<SysOperationLog> exportList(LogListReq req);
}
