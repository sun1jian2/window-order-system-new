package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.PurchaseOrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * PurchaseOrderItemMapper Mapper接口
 */
@Mapper
public interface PurchaseOrderItemMapper {

    /**
     * 新增采购订单项方法
     */
    @Insert("INSERT INTO purchase_order_item (purchase_order_id, material_id, quantity, unit_price, total_price, remark) " +
            "VALUES (#{purchaseOrderId}, #{materialId}, #{quantity}, #{unitPrice}, #{totalPrice}, #{remark})")
    int insert(PurchaseOrderItem item);

    /**
     * 根据订单ID查询采购订单项列表方法
     */
    @Select("SELECT i.*, m.name as material_name, m.code as material_code, m.spec as material_spec, m.unit as material_unit " +
            "FROM purchase_order_item i " +
            "LEFT JOIN material m ON i.material_id = m.id " +
            "WHERE i.purchase_order_id = #{purchaseOrderId}")
    List<PurchaseOrderItem> listByOrderId(Long purchaseOrderId);

    /**
     * 根据订单ID删除采购订单项方法
     */
    @Delete("DELETE FROM purchase_order_item WHERE purchase_order_id = #{purchaseOrderId}")
    int deleteByOrderId(Long purchaseOrderId);
}
