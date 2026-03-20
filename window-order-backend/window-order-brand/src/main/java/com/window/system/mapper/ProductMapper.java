package com.window.system.mapper;

import com.window.system.model.entity.Product;
import com.window.system.model.req.ProductListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * ProductMapper Mapper接口
 */
@Mapper
public interface ProductMapper {

    /**
     * 条件查询产品列表方法
     */
    @Select("<script>" +
            "SELECT p.*, c.name as category_name, b.name as brand_name " +
            "FROM product p " +
            "LEFT JOIN product_category c ON p.category_id = c.id " +
            "LEFT JOIN brand b ON p.brand_id = b.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='categoryId != null'> AND p.category_id = #{categoryId} </if> " +
            "<if test='brandId != null'> AND p.brand_id = #{brandId} </if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (p.name LIKE CONCAT('%', #{keyword}, '%') OR p.code LIKE CONCAT('%', #{keyword}, '%')) </if> " +
            "ORDER BY p.id DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<Product> selectList(ProductListReq req);

    /**
     * 查询产品列表总数方法
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM product p WHERE p.is_deleted = 0 " +
            "<if test='categoryId != null'> AND p.category_id = #{categoryId} </if> " +
            "<if test='brandId != null'> AND p.brand_id = #{brandId} </if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (p.name LIKE CONCAT('%', #{keyword}, '%') OR p.code LIKE CONCAT('%', #{keyword}, '%')) </if> " +
            "</script>")
    long countList(ProductListReq req);

    /**
     * 根据主键查询产品方法
     */
    @Select("SELECT p.*, c.name as category_name, b.name as brand_name FROM product p " +
            "LEFT JOIN product_category c ON p.category_id = c.id " +
            "LEFT JOIN brand b ON p.brand_id = b.id " +
            "WHERE p.id = #{id} AND p.is_deleted = 0")
    Product getById(@Param("id") Long id);

    /**
     * 查询所有激活产品方法
     */
    @Select("SELECT * FROM product WHERE is_deleted = 0 AND status = 'ACTIVE' ORDER BY id DESC")
    List<Product> listAllActive();

    /**
     * 新增产品方法
     */
    @Insert("INSERT INTO product (category_id, brand_id, name, code, base_price, color_options, glass_options, description, status, create_by) " +
            "VALUES (#{categoryId}, #{brandId}, #{name}, #{code}, #{basePrice}, #{colorOptions}, #{glassOptions}, #{description}, #{status}, #{createBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    /**
     * 更新产品方法
     */
    @Update("UPDATE product SET category_id = #{categoryId}, brand_id = #{brandId}, name = #{name}, code = #{code}, " +
            "base_price = #{basePrice}, color_options = #{colorOptions}, glass_options = #{glassOptions}, " +
            "description = #{description}, status = #{status}, update_by = #{updateBy} " +
            "WHERE id = #{id}")
    int update(Product product);

    /**
     * 删除产品方法
     */
    @Update("UPDATE product SET is_deleted = 1, update_by = #{updateBy} WHERE id = #{id}")
    int delete(@Param("id") Long id, @Param("updateBy") Long updateBy);
}
