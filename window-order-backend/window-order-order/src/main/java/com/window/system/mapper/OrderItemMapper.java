package com.window.system.mapper;

import com.window.system.model.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * OrderItemMapper Mapper接口
 */
@Mapper
public interface OrderItemMapper {

    /**
     * 根据订单ID查询订单项列表方法
     */
    @Select("SELECT i.*, p.name as product_name, p.code as product_code " +
            "FROM order_item i " +
            "LEFT JOIN product p ON i.product_id = p.id " +
            "WHERE i.order_id = #{orderId} AND i.is_deleted = 0")
    List<OrderItem> listByOrderId(@Param("orderId") Long orderId);

    /**
     * 新增订单项方法
     */
    @Insert("INSERT INTO order_item (order_id, product_id, width, height, area, quantity, unit_price, total_price, color, glass_spec, remark) " +
            "VALUES (#{orderId}, #{productId}, #{width}, #{height}, #{area}, #{quantity}, #{unitPrice}, #{totalPrice}, #{color}, #{glassSpec}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem item);

    /**
     * 根据订单ID删除订单项方法
     */
    @Update("UPDATE order_item SET is_deleted = 1 WHERE order_id = #{orderId}")
    int deleteByOrderId(@Param("orderId") Long orderId);
}
