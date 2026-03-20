package com.window.system.mapper;

import com.window.system.model.entity.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * ProductCategoryMapper Mapper接口
 */
@Mapper
public interface ProductCategoryMapper {

    /**
     * 查询所有产品分类方法
     */
    @Select("SELECT * FROM product_category WHERE is_deleted = 0 ORDER BY sort ASC")
    List<ProductCategory> listAll();

    /**
     * 根据主键查询产品分类方法
     */
    @Select("SELECT * FROM product_category WHERE id = #{id} AND is_deleted = 0")
    ProductCategory getById(@Param("id") Long id);

    /**
     * 新增产品分类方法
     */
    @Insert("INSERT INTO product_category (name, sort) VALUES (#{name}, #{sort})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProductCategory category);

    /**
     * 更新产品分类方法
     */
    @Update("UPDATE product_category SET name = #{name}, sort = #{sort} WHERE id = #{id}")
    int update(ProductCategory category);

    /**
     * 删除产品分类方法
     */
    @Update("UPDATE product_category SET is_deleted = 1 WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
