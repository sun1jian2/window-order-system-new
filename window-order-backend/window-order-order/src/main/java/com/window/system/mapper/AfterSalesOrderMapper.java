package com.window.system.mapper;

import com.window.system.model.entity.AfterSalesOrder;
import com.window.system.model.req.AfterSalesListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * AfterSalesOrderMapper Mapper接口
 */
@Mapper
public interface AfterSalesOrderMapper {

    /**
     * 新增售后工单方法
     */
    @Insert("INSERT INTO after_sales_order (ticket_no, order_id, customer_name, customer_phone, address, issue_description, status, handler_id, appointment_time, create_by, create_time, is_deleted) " +
            "VALUES (#{ticketNo}, #{orderId}, #{customerName}, #{customerPhone}, #{address}, #{issueDescription}, #{status}, #{handlerId}, #{appointmentTime}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AfterSalesOrder order);

    /**
     * 更新售后工单方法
     */
    @Update("<script>" +
            "UPDATE after_sales_order SET update_time = NOW() " +
            "<if test='status != null'>, status = #{status}</if> " +
            "<if test='handlerId != null'>, handler_id = #{handlerId}</if> " +
            "<if test='appointmentTime != null'>, appointment_time = #{appointmentTime}</if> " +
            "<if test='completionTime != null'>, completion_time = #{completionTime}</if> " +
            "<if test='solution != null'>, solution = #{solution}</if> " +
            "<if test='fee != null'>, fee = #{fee}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(AfterSalesOrder order);

    /**
     * 根据主键查询售后工单方法
     */
    @Select("SELECT aso.*, u.real_name as handler_name, wo.order_no " +
            "FROM after_sales_order aso " +
            "LEFT JOIN sys_user u ON aso.handler_id = u.id " +
            "LEFT JOIN window_order wo ON aso.order_id = wo.id " +
            "WHERE aso.id = #{id} AND aso.is_deleted = 0")
    AfterSalesOrder getById(Long id);
    
    /**
     * 查询售后工单列表总数方法
     */
    @Select("<script>" +
            "SELECT count(1) FROM after_sales_order aso " +
            "LEFT JOIN window_order wo ON aso.order_id = wo.id " +
            "WHERE aso.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND wo.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='ticketNo != null and ticketNo != \"\"'> AND aso.ticket_no LIKE CONCAT('%', #{ticketNo}, '%')</if> " +
            "<if test='customerName != null and customerName != \"\"'> AND aso.customer_name LIKE CONCAT('%', #{customerName}, '%')</if> " +
            "<if test='customerPhone != null and customerPhone != \"\"'> AND aso.customer_phone LIKE CONCAT('%', #{customerPhone}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND aso.status = #{status}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND wo.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND aso.handler_id = #{currentUserId}</if> " +
            "</script>")
    long countList(AfterSalesListReq req);

    /**
     * 条件查询售后工单列表方法
     */
    @Select("<script>" +
            "SELECT aso.*, u.real_name as handler_name, wo.order_no " +
            "FROM after_sales_order aso " +
            "LEFT JOIN sys_user u ON aso.handler_id = u.id " +
            "LEFT JOIN window_order wo ON aso.order_id = wo.id " +
            "WHERE aso.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND wo.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='ticketNo != null and ticketNo != \"\"'> AND aso.ticket_no LIKE CONCAT('%', #{ticketNo}, '%')</if> " +
            "<if test='customerName != null and customerName != \"\"'> AND aso.customer_name LIKE CONCAT('%', #{customerName}, '%')</if> " +
            "<if test='customerPhone != null and customerPhone != \"\"'> AND aso.customer_phone LIKE CONCAT('%', #{customerPhone}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND aso.status = #{status}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND wo.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND aso.handler_id = #{currentUserId}</if> " +
            "ORDER BY aso.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<AfterSalesOrder> selectList(AfterSalesListReq req);
    
    /**
     * 导出售后工单列表方法
     */
    @Select("<script>" +
            "SELECT aso.*, u.real_name as handler_name, wo.order_no " +
            "FROM after_sales_order aso " +
            "LEFT JOIN sys_user u ON aso.handler_id = u.id " +
            "LEFT JOIN window_order wo ON aso.order_id = wo.id " +
            "WHERE aso.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND wo.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='ticketNo != null and ticketNo != \"\"'> AND aso.ticket_no LIKE CONCAT('%', #{ticketNo}, '%')</if> " +
            "<if test='customerName != null and customerName != \"\"'> AND aso.customer_name LIKE CONCAT('%', #{customerName}, '%')</if> " +
            "<if test='customerPhone != null and customerPhone != \"\"'> AND aso.customer_phone LIKE CONCAT('%', #{customerPhone}, '%')</if> " +
            "<if test='status != null and status != \"\"'> AND aso.status = #{status}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND wo.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND aso.handler_id = #{currentUserId}</if> " +
            "ORDER BY aso.create_time DESC " +
            "</script>")
    List<AfterSalesOrder> exportList(AfterSalesListReq req);
    
    /**
     * 删除售后工单方法
     */
    @Update("UPDATE after_sales_order SET is_deleted = 1 WHERE id = #{id}")
    int delete(Long id);
}
