package com.window.system.mapper;

import com.window.system.model.entity.OrderPayment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * OrderPaymentMapper Mapper接口
 */
@Mapper
public interface OrderPaymentMapper {

    /**
     * 新增订单支付记录方法
     */
    @Insert("INSERT INTO order_payment (order_id, amount, pay_time, pay_method, remark, create_by, create_time) " +
            "VALUES (#{orderId}, #{amount}, #{payTime}, #{payMethod}, #{remark}, #{createBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderPayment payment);

    /**
     * 根据订单ID查询支付记录列表方法
     */
    @Select("SELECT p.*, u.real_name as create_by_name " +
            "FROM order_payment p " +
            "LEFT JOIN sys_user u ON p.create_by = u.id " +
            "WHERE p.order_id = #{orderId} " +
            "ORDER BY p.pay_time DESC")
    List<OrderPayment> getByOrderId(Long orderId);
}
