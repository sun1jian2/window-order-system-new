package com.window.system.mapper;

import com.window.system.model.entity.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductCategoryMapper {

    @Select("SELECT * FROM product_category WHERE is_deleted = 0 ORDER BY sort ASC")
    List<ProductCategory> listAll();

    @Select("SELECT * FROM product_category WHERE id = #{id} AND is_deleted = 0")
    ProductCategory getById(@Param("id") Long id);

    @Insert("INSERT INTO product_category (name, sort) VALUES (#{name}, #{sort})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProductCategory category);

    @Update("UPDATE product_category SET name = #{name}, sort = #{sort} WHERE id = #{id}")
    int update(ProductCategory category);

    @Update("UPDATE product_category SET is_deleted = 1 WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
