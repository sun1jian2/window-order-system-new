package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.MaterialCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * MaterialCategoryMapper Mapper接口
 */
public interface MaterialCategoryMapper {

    @Select("SELECT * FROM material_category WHERE is_deleted = 0 ORDER BY sort ASC, id DESC")
    /**
     * listAll 方法
     */
    List<MaterialCategory> listAll();
}
