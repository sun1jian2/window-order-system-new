package com.window.system.mapper;

import com.window.system.model.entity.RemeasureTask;
import com.window.system.model.req.RemeasureTaskListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * RemeasureTaskMapper Mapper接口
 */
public interface RemeasureTaskMapper {

    @Insert("INSERT INTO remeasure_task (order_id, assignee_id, status, precise_width, precise_height, sketch_url, site_photos, remark, create_time, update_time) " +
            "VALUES (#{orderId}, #{assigneeId}, #{status}, #{preciseWidth}, #{preciseHeight}, #{sketchUrl}, #{sitePhotos}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(RemeasureTask task);

    @Update("<script>" +
            "UPDATE remeasure_task SET update_time = NOW() " +
            "<if test='assigneeId != null'>, assignee_id = #{assigneeId}</if> " +
            "<if test='status != null'>, status = #{status}</if> " +
            "<if test='preciseWidth != null'>, precise_width = #{preciseWidth}</if> " +
            "<if test='preciseHeight != null'>, precise_height = #{preciseHeight}</if> " +
            "<if test='sketchUrl != null'>, sketch_url = #{sketchUrl}</if> " +
            "<if test='sitePhotos != null'>, site_photos = #{sitePhotos}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    /**
     * update 方法
     */
    int update(RemeasureTask task);

    @Select("SELECT t.*, o.order_no, o.customer_name, o.address, s.real_name as assignee_name " +
            "FROM remeasure_task t " +
            "LEFT JOIN window_order o ON t.order_id = o.id " +
            "LEFT JOIN sys_user s ON t.assignee_id = s.id " +
            "WHERE t.id = #{id}")
    /**
     * getById 方法
     */
    RemeasureTask getById(Long id);
    
    @Select("SELECT t.*, o.order_no, o.customer_name, o.address, s.real_name as assignee_name " +
            "FROM remeasure_task t " +
            "LEFT JOIN window_order o ON t.order_id = o.id " +
            "LEFT JOIN sys_user s ON t.assignee_id = s.id " +
            "WHERE t.order_id = #{orderId} " +
            "ORDER BY t.create_time DESC LIMIT 1")
    /**
     * getByOrderId 方法
     */
    RemeasureTask getByOrderId(Long orderId);

    @Select("<script>" +
            "SELECT count(1) FROM remeasure_task t " +
            "LEFT JOIN window_order o ON t.order_id = o.id " +
            "WHERE 1=1 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='orderId != null'> AND t.order_id = #{orderId}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND o.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND t.assignee_id = #{currentUserId}</if> " +
            "<if test='assigneeId != null'> AND t.assignee_id = #{assigneeId}</if> " +
            "<if test='status != null and status != \"\"'> AND t.status = #{status}</if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(RemeasureTaskListReq req);

    @Select("<script>" +
            "SELECT t.*, o.order_no, o.customer_name, o.address, s.real_name as assignee_name " +
            "FROM remeasure_task t " +
            "LEFT JOIN window_order o ON t.order_id = o.id " +
            "LEFT JOIN sys_user s ON t.assignee_id = s.id " +
            "WHERE 1=1 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='orderId != null'> AND t.order_id = #{orderId}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND o.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND t.assignee_id = #{currentUserId}</if> " +
            "<if test='assigneeId != null'> AND t.assignee_id = #{assigneeId}</if> " +
            "<if test='status != null and status != \"\"'> AND t.status = #{status}</if> " +
            "ORDER BY t.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * selectList 方法
     */
    List<RemeasureTask> selectList(RemeasureTaskListReq req);

    @Select("<script>" +
            "SELECT t.*, o.order_no, o.customer_name, o.address, s.real_name as assignee_name " +
            "FROM remeasure_task t " +
            "LEFT JOIN window_order o ON t.order_id = o.id " +
            "LEFT JOIN sys_user s ON t.assignee_id = s.id " +
            "WHERE 1=1 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='orderId != null'> AND t.order_id = #{orderId}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND o.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND t.assignee_id = #{currentUserId}</if> " +
            "<if test='assigneeId != null'> AND t.assignee_id = #{assigneeId}</if> " +
            "<if test='status != null and status != \"\"'> AND t.status = #{status}</if> " +
            "ORDER BY t.create_time DESC " +
            "</script>")
    /**
     * exportList 方法
     */
    List<RemeasureTask> exportList(RemeasureTaskListReq req);
}
