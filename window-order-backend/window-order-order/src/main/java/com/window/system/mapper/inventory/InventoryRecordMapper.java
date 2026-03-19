package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.InventoryRecord;
import org.apache.ibatis.annotations.*;

@Mapper
/**
 * InventoryRecordMapper Mapper接口
 */
public interface InventoryRecordMapper {

    @Insert("INSERT INTO inventory_record (type, material_id, quantity, before_quantity, after_quantity, relation_type, relation_id, operator_id, remark, create_time) " +
            "VALUES (#{type}, #{materialId}, #{quantity}, #{beforeQuantity}, #{afterQuantity}, #{relationType}, #{relationId}, #{operatorId}, #{remark}, NOW())")
    /**
     * insert 方法
     */
    int insert(InventoryRecord record);
    
    @Update("UPDATE material SET stock_quantity = stock_quantity + #{quantity} WHERE id = #{materialId}")
    int updateMaterialStock(@Param("materialId") Long materialId, @Param("quantity") java.math.BigDecimal quantity);
    
    @Select("SELECT stock_quantity FROM material WHERE id = #{materialId}")
    java.math.BigDecimal getMaterialStock(Long materialId);
}
