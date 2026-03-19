package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.OrderCost;
import org.apache.ibatis.annotations.*;

@Mapper
/**
 * OrderCostMapper Mapper接口
 */
public interface OrderCostMapper {

    @Insert("INSERT INTO order_cost (order_id, material_cost, labor_cost, other_cost, total_cost, order_amount, gross_profit, gross_profit_rate, remark, create_by, create_time) " +
            "VALUES (#{orderId}, #{materialCost}, #{laborCost}, #{otherCost}, #{totalCost}, #{orderAmount}, #{grossProfit}, #{grossProfitRate}, #{remark}, #{createBy}, NOW())")
    /**
     * insert 方法
     */
    int insert(OrderCost orderCost);

    @Update("UPDATE order_cost SET material_cost = #{materialCost}, labor_cost = #{laborCost}, other_cost = #{otherCost}, " +
            "total_cost = #{totalCost}, order_amount = #{orderAmount}, gross_profit = #{grossProfit}, gross_profit_rate = #{grossProfitRate}, " +
            "remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    /**
     * update 方法
     */
    int update(OrderCost orderCost);

    @Select("SELECT * FROM order_cost WHERE order_id = #{orderId}")
    /**
     * getByOrderId 方法
     */
    OrderCost getByOrderId(Long orderId);
}
