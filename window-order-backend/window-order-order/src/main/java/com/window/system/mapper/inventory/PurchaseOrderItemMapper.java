package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.PurchaseOrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * PurchaseOrderItemMapper Mapper接口
 */
public interface PurchaseOrderItemMapper {

    @Insert("INSERT INTO purchase_order_item (purchase_order_id, material_id, quantity, unit_price, total_price, remark) " +
            "VALUES (#{purchaseOrderId}, #{materialId}, #{quantity}, #{unitPrice}, #{totalPrice}, #{remark})")
    /**
     * insert 方法
     */
    int insert(PurchaseOrderItem item);

    @Select("SELECT i.*, m.name as material_name, m.code as material_code, m.spec as material_spec, m.unit as material_unit " +
            "FROM purchase_order_item i " +
            "LEFT JOIN material m ON i.material_id = m.id " +
            "WHERE i.purchase_order_id = #{purchaseOrderId}")
    /**
     * listByOrderId 方法
     */
    List<PurchaseOrderItem> listByOrderId(Long purchaseOrderId);

    @Delete("DELETE FROM purchase_order_item WHERE purchase_order_id = #{purchaseOrderId}")
    /**
     * deleteByOrderId 方法
     */
    int deleteByOrderId(Long purchaseOrderId);
}
