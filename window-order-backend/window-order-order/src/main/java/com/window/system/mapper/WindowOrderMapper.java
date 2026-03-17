package com.window.system.mapper;

import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.OrderListReq;
import com.window.system.model.dto.NameValueDto;
import com.window.system.model.dto.OrderTrendDto;
import com.window.system.model.dto.RecentActivityDto;
import com.window.system.model.dto.SalesPerformanceDto;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface WindowOrderMapper {

    @Insert("INSERT INTO window_order (order_no, customer_id, customer_name, customer_phone, address, region_codes, province, city, district, detail_address, brand, window_type, color, glass_spec, width, height, price, paid_amount, payment_status, order_time, scheduled_install_date, actual_install_end_date, salesperson_id, installer_id, install_progress, production_progress, logistics_status, status, create_time, create_by, update_by, is_deleted) " +
            "VALUES (#{orderNo}, #{customerId}, #{customerName}, #{customerPhone}, #{address}, #{regionCodes}, #{province}, #{city}, #{district}, #{detailAddress}, #{brand}, #{windowType}, #{color}, #{glassSpec}, #{width}, #{height}, #{price}, #{paidAmount}, #{paymentStatus}, #{orderTime}, #{scheduledInstallDate}, #{actualInstallEndDate}, #{salespersonId}, #{installerId}, #{installProgress}, #{productionProgress}, #{logisticsStatus}, #{status}, NOW(), #{createBy}, #{updateBy}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(WindowOrder order);

    @Update("<script>" +
            "UPDATE window_order SET update_time = NOW() " +
            "<if test='customerName != null'>, customer_name = #{customerName}</if> " +
            "<if test='customerPhone != null'>, customer_phone = #{customerPhone}</if> " +
            "<if test='address != null'>, address = #{address}</if> " +
            "<if test='regionCodes != null'>, region_codes = #{regionCodes}</if> " +
            "<if test='province != null'>, province = #{province}</if> " +
            "<if test='city != null'>, city = #{city}</if> " +
            "<if test='district != null'>, district = #{district}</if> " +
            "<if test='detailAddress != null'>, detail_address = #{detailAddress}</if> " +
            "<if test='brand != null'>, brand = #{brand}</if> " +
            "<if test='windowType != null'>, window_type = #{windowType}</if> " +
            "<if test='color != null'>, color = #{color}</if> " +
            "<if test='glassSpec != null'>, glass_spec = #{glassSpec}</if> " +
            "<if test='width != null'>, width = #{width}</if> " +
            "<if test='height != null'>, height = #{height}</if> " +
            "<if test='isRemeasured != null'>, is_remeasured = #{isRemeasured}</if> " +
            "<if test='price != null'>, price = #{price}</if> " +
            "<if test='paidAmount != null'>, paid_amount = #{paidAmount}</if> " +
            "<if test='paymentStatus != null'>, payment_status = #{paymentStatus}</if> " +
            "<if test='orderTime != null'>, order_time = #{orderTime}</if> " +
            "<if test='installTime != null'>, install_time = #{installTime}</if> " +
            "<if test='scheduledInstallDate != null'>, scheduled_install_date = #{scheduledInstallDate}</if> " +
            "<if test='actualInstallEndDate != null'>, actual_install_end_date = #{actualInstallEndDate}</if> " +
            "<if test='installProgress != null'>, install_progress = #{installProgress}</if> " +
            "<if test='productionProgress != null'>, production_progress = #{productionProgress}</if> " +
            "<if test='status != null'>, status = #{status}</if> " +
            "<if test='salespersonId != null'>, salesperson_id = #{salespersonId}</if> " +
            "<if test='installerId != null'>, installer_id = #{installerId}</if> " +
            "<if test='updateBy != null'>, update_by = #{updateBy}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(WindowOrder order);

    @Update("UPDATE window_order SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT o.*, s.real_name as salesperson_name, i.real_name as installer_name " +
            "FROM window_order o " +
            "LEFT JOIN sys_user s ON o.salesperson_id = s.id " +
            "LEFT JOIN sys_user i ON o.installer_id = i.id " +
            "WHERE o.id = #{id} AND o.is_deleted = 0")
    WindowOrder getById(Long id);

    @Select("<script>" +
            "SELECT count(1) FROM window_order WHERE is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='customerId != null'> AND customer_id = #{customerId}</if> " +
            "<if test='customerName != null and customerName != \"\"'> AND customer_name LIKE CONCAT('%', #{customerName}, '%')</if> " +
            "<if test='customerPhone != null and customerPhone != \"\"'> AND customer_phone LIKE CONCAT('%', #{customerPhone}, '%')</if> " +
            "<if test='brand != null and brand != \"\"'> AND brand = #{brand}</if> " +
            "<if test='installProgress != null and installProgress != \"\"'> AND install_progress = #{installProgress}</if> " +
            "<if test='productionProgress != null and productionProgress != \"\"'> AND production_progress = #{productionProgress}</if> " +
            "<if test='searchSalespersonId != null'> AND salesperson_id = #{searchSalespersonId}</if> " +
            "<if test='searchInstallerId != null'> AND installer_id = #{searchInstallerId}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND installer_id = #{currentUserId}</if> " +
            "</script>")
    long countList(OrderListReq req);

    @Select("<script>" +
            "SELECT o.*, s.real_name as salesperson_name, i.real_name as installer_name " +
            "FROM window_order o " +
            "LEFT JOIN sys_user s ON o.salesperson_id = s.id " +
            "LEFT JOIN sys_user i ON o.installer_id = i.id " +
            "WHERE o.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='customerId != null'> AND o.customer_id = #{customerId}</if> " +
            "<if test='customerName != null and customerName != \"\"'> AND o.customer_name LIKE CONCAT('%', #{customerName}, '%')</if> " +
            "<if test='customerPhone != null and customerPhone != \"\"'> AND o.customer_phone LIKE CONCAT('%', #{customerPhone}, '%')</if> " +
            "<if test='brand != null and brand != \"\"'> AND o.brand = #{brand}</if> " +
            "<if test='installProgress != null and installProgress != \"\"'> AND o.install_progress = #{installProgress}</if> " +
            "<if test='productionProgress != null and productionProgress != \"\"'> AND o.production_progress = #{productionProgress}</if> " +
            "<if test='logisticsStatus != null and logisticsStatus != \"\"'> AND o.logistics_status = #{logisticsStatus}</if> " +
            "<if test='searchSalespersonId != null'> AND o.salesperson_id = #{searchSalespersonId}</if> " +
            "<if test='searchInstallerId != null'> AND o.installer_id = #{searchInstallerId}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND o.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND o.installer_id = #{currentUserId}</if> " +
            "ORDER BY o.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<WindowOrder> selectList(OrderListReq req);

    @Select("<script>" +
            "SELECT o.*, s.real_name as salesperson_name, i.real_name as installer_name " +
            "FROM window_order o " +
            "LEFT JOIN sys_user s ON o.salesperson_id = s.id " +
            "LEFT JOIN sys_user i ON o.installer_id = i.id " +
            "WHERE o.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if> " +
            "<if test='customerName != null and customerName != \"\"'> AND o.customer_name LIKE CONCAT('%', #{customerName}, '%')</if> " +
            "<if test='customerPhone != null and customerPhone != \"\"'> AND o.customer_phone LIKE CONCAT('%', #{customerPhone}, '%')</if> " +
            "<if test='brand != null and brand != \"\"'> AND o.brand = #{brand}</if> " +
            "<if test='installProgress != null and installProgress != \"\"'> AND o.install_progress = #{installProgress}</if> " +
            "<if test='productionProgress != null and productionProgress != \"\"'> AND o.production_progress = #{productionProgress}</if> " +
            "<if test='logisticsStatus != null and logisticsStatus != \"\"'> AND o.logistics_status = #{logisticsStatus}</if> " +
            "<if test='searchSalespersonId != null'> AND o.salesperson_id = #{searchSalespersonId}</if> " +
            "<if test='searchInstallerId != null'> AND o.installer_id = #{searchInstallerId}</if> " +
            "<if test='currentUserRole == \"SALES\"'> AND o.salesperson_id = #{currentUserId}</if> " +
            "<if test='currentUserRole == \"INSTALLER\"'> AND o.installer_id = #{currentUserId}</if> " +
            "ORDER BY o.create_time DESC " +
            "</script>")
    List<WindowOrder> exportList(OrderListReq req);
    
    @Select("<script>" +
            "SELECT count(1) FROM window_order WHERE is_deleted = 0 AND (install_progress = 'WAITING' OR production_progress = 'WAITING') " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    long countPendingOrders(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT count(1) FROM window_order WHERE is_deleted = 0 AND payment_status IN ('UNPAID', 'PARTIAL') " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    long countUnpaidOrders(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT count(1) FROM window_order WHERE is_deleted = 0 AND install_progress = 'FINISHED' " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    long countFinishedOrders(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT count(1) FROM window_order WHERE is_deleted = 0 " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    long countTotalOrders(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT IFNULL(SUM(price), 0) FROM window_order WHERE is_deleted = 0 " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    BigDecimal sumTotalReceivable(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT IFNULL(SUM(paid_amount), 0) FROM window_order WHERE is_deleted = 0 " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    BigDecimal sumTotalCollected(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT IFNULL(SUM(price), 0) FROM window_order WHERE is_deleted = 0 AND DATE_FORMAT(create_time, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    BigDecimal sumMonthlySales(@Param("userId") Long userId, @Param("role") String role);

    @Select("<script>" +
            "SELECT IFNULL(SUM(p.amount), 0) " +
            "FROM order_payment p " +
            "LEFT JOIN window_order o ON p.order_id = o.id " +
            "WHERE o.is_deleted = 0 AND DATE_FORMAT(p.pay_time, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') " +
            "<if test='role == \"SALES\"'> AND o.salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND o.installer_id = #{userId} </if> " +
            "</script>")
    BigDecimal sumMonthlyPaidAmount(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT IFNULL(SUM(price), 0) FROM window_order WHERE is_deleted = 0 AND DATE_FORMAT(create_time, '%Y%m%d') = DATE_FORMAT(CURDATE(), '%Y%m%d') " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    BigDecimal sumTodaySales(@Param("userId") Long userId, @Param("role") String role);

    @Select("<script>" +
            "SELECT IFNULL(SUM(price), 0) FROM window_order WHERE is_deleted = 0 " +
            "<if test='startDate != null'> AND create_time &gt;= #{startDate} </if> " +
            "<if test='endDate != null'> AND create_time &lt;= CONCAT(#{endDate}, ' 23:59:59') </if> " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    BigDecimal sumSalesByDateRange(@Param("userId") Long userId, @Param("role") String role, 
                                            @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("<script>" +
            "SELECT count(1) FROM window_order WHERE is_deleted = 0 " +
            "<if test='startDate != null'> AND create_time &gt;= #{startDate} </if> " +
            "<if test='endDate != null'> AND create_time &lt;= CONCAT(#{endDate}, ' 23:59:59') </if> " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "</script>")
    long countOrdersByDateRange(@Param("userId") Long userId, @Param("role") String role,
                                             @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT count(1) FROM customer WHERE is_deleted = 0")
    long countTotalCustomers();
    
    @Select("SELECT count(1) FROM sys_user WHERE is_deleted = 0")
    long countTotalUsers();

    @Select("SELECT * FROM window_order WHERE is_deleted = 0 ORDER BY create_time DESC")
    List<WindowOrder> selectAll();
    
    @Select("<script>" +
            "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, COUNT(1) as count FROM window_order " +
            "WHERE is_deleted = 0 AND create_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "GROUP BY date ORDER BY date" +
            "</script>")
    List<OrderTrendDto> getOrderTrend(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT brand as name, COUNT(1) as value FROM window_order " +
            "WHERE is_deleted = 0 AND brand IS NOT NULL " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "GROUP BY brand" +
            "</script>")
    List<NameValueDto> getBrandDistribution(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT window_type as name, COUNT(1) as value FROM window_order " +
            "WHERE is_deleted = 0 AND window_type IS NOT NULL AND window_type != '' " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "GROUP BY window_type" +
            "</script>")
    List<NameValueDto> getWindowTypeDistribution(@Param("userId") Long userId, @Param("role") String role);
    
    @Select("<script>" +
            "SELECT IFNULL(s.real_name, '未分配') as name, COUNT(1) as orderCount, IFNULL(SUM(o.price), 0) as amount " +
            "FROM window_order o " +
            "LEFT JOIN sys_user s ON o.salesperson_id = s.id " +
            "WHERE o.is_deleted = 0 AND DATE_FORMAT(o.create_time, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') " +
            "<if test='role == \"SALES\"'> AND o.salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND o.installer_id = #{userId} </if> " +
            "GROUP BY o.salesperson_id, s.real_name " +
            "ORDER BY amount DESC" +
            "</script>")
    List<SalesPerformanceDto> getMonthlySalesPerformance(@Param("userId") Long userId, @Param("role") String role);

    @Select("<script>" +
            "SELECT CASE " +
            "  WHEN status = 'FINISHED' THEN '已完成' " +
            "  WHEN install_progress = 'INSTALLING' THEN '安装中' " +
            "  WHEN install_progress = 'SCHEDULED' THEN '已预约安装' " +
            "  WHEN logistics_status = 'SHIPPING' THEN '送货中' " +
            "  WHEN logistics_status = 'OUTBOUND' THEN '已出库' " +
            "  WHEN production_progress = 'FINISHED' THEN '制作完成' " +
            "  WHEN production_progress = 'PRODUCING' THEN '制作中' " +
            "  WHEN status = 'PENDING_PRODUCTION' THEN '待排产' " +
            "  WHEN status = 'PENDING_CONFIRM' THEN '待确认' " +
            "  WHEN status = 'PENDING_QUOTE' THEN '待报价' " +
            "  WHEN status = 'PENDING_MEASURE' THEN '待测量' " +
            "  WHEN status = 'DRAFT' THEN '草稿' " +
            "  WHEN status = 'SUBMITTED' THEN '已提交' " +
            "  ELSE status " +
            "END as name, COUNT(1) as value " +
            "FROM window_order " +
            "WHERE is_deleted = 0 " +
            "<if test='role == \"SALES\"'> AND salesperson_id = #{userId} </if> " +
            "<if test='role == \"INSTALLER\"'> AND installer_id = #{userId} </if> " +
            "GROUP BY name" +
            "</script>")
    List<NameValueDto> getStatusDistribution(@Param("userId") Long userId, @Param("role") String role);

    @Select("<script>" +
            "SELECT module, operation, username, create_time as createTime " +
            "FROM sys_operation_log " +
            "ORDER BY create_time DESC LIMIT 10" +
            "</script>")
    List<RecentActivityDto> getRecentActivities();
}
