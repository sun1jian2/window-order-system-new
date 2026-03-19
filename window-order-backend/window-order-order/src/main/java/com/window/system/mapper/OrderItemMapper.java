package com.window.system.mapper;

import com.window.system.model.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * OrderItemMapper Mapper接口
 */
public interface OrderItemMapper {

    @Select("SELECT i.*, p.name as product_name, p.code as product_code " +
            "FROM order_item i " +
            "LEFT JOIN product p ON i.product_id = p.id " +
            "WHERE i.order_id = #{orderId} AND i.is_deleted = 0")
    List<OrderItem> listByOrderId(@Param("orderId") Long orderId);

    @Insert("INSERT INTO order_item (order_id, product_id, width, height, area, quantity, unit_price, total_price, color, glass_spec, remark) " +
            "VALUES (#{orderId}, #{productId}, #{width}, #{height}, #{area}, #{quantity}, #{unitPrice}, #{totalPrice}, #{color}, #{glassSpec}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(OrderItem item);

    @Update("UPDATE order_item SET is_deleted = 1 WHERE order_id = #{orderId}")
    int deleteByOrderId(@Param("orderId") Long orderId);
}
