package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.OrderCost;
import org.apache.ibatis.annotations.*;

/**
 * OrderCostMapper Mapper接口
 */
@Mapper
public interface OrderCostMapper {

    /**
     * 新增订单成本方法
     */
    @Insert("INSERT INTO order_cost (order_id, material_cost, labor_cost, other_cost, total_cost, order_amount, gross_profit, gross_profit_rate, remark, create_by, create_time) " +
            "VALUES (#{orderId}, #{materialCost}, #{laborCost}, #{otherCost}, #{totalCost}, #{orderAmount}, #{grossProfit}, #{grossProfitRate}, #{remark}, #{createBy}, NOW())")
    int insert(OrderCost orderCost);

    /**
     * 更新订单成本方法
     */
    @Update("UPDATE order_cost SET material_cost = #{materialCost}, labor_cost = #{laborCost}, other_cost = #{otherCost}, " +
            "total_cost = #{totalCost}, order_amount = #{orderAmount}, gross_profit = #{grossProfit}, gross_profit_rate = #{grossProfitRate}, " +
            "remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    int update(OrderCost orderCost);

    /**
     * 根据订单ID查询订单成本方法
     */
    @Select("SELECT * FROM order_cost WHERE order_id = #{orderId}")
    OrderCost getByOrderId(Long orderId);
}
