package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.MaterialCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * MaterialCategoryMapper Mapper接口
 */
@Mapper
public interface MaterialCategoryMapper {

    /**
     * 查询所有材料分类方法
     */
    @Select("SELECT * FROM material_category WHERE is_deleted = 0 ORDER BY sort ASC, id DESC")
    List<MaterialCategory> listAll();
}
