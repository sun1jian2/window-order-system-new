package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.InventoryRecord;
import org.apache.ibatis.annotations.*;

/**
 * InventoryRecordMapper Mapper接口
 */
@Mapper
public interface InventoryRecordMapper {

    /**
     * 新增库存记录方法
     */
    @Insert("INSERT INTO inventory_record (type, material_id, quantity, before_quantity, after_quantity, relation_type, relation_id, operator_id, remark, create_time) " +
            "VALUES (#{type}, #{materialId}, #{quantity}, #{beforeQuantity}, #{afterQuantity}, #{relationType}, #{relationId}, #{operatorId}, #{remark}, NOW())")
    int insert(InventoryRecord record);
    
    /**
     * 更新材料库存数量方法
     */
    @Update("UPDATE material SET stock_quantity = stock_quantity + #{quantity} WHERE id = #{materialId}")
    int updateMaterialStock(@Param("materialId") Long materialId, @Param("quantity") java.math.BigDecimal quantity);
    
    /**
     * 获取材料当前库存方法
     */
    @Select("SELECT stock_quantity FROM material WHERE id = #{materialId}")
    java.math.BigDecimal getMaterialStock(Long materialId);
}
