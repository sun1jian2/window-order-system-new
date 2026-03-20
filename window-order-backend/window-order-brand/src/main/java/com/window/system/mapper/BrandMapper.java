package com.window.system.mapper;

import com.window.system.model.entity.Brand;
import com.window.system.model.req.brand.BrandListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * BrandMapper Mapper接口
 */
@Mapper
public interface BrandMapper {

    /**
     * 新增品牌方法
     */
    @Insert("INSERT INTO brand (name, description, create_by, create_time, is_deleted) VALUES (#{name}, #{description}, #{createBy}, NOW(), 0)")
    int insert(Brand brand);

    /**
     * 更新品牌方法
     */
    @Update("UPDATE brand SET name = #{name}, description = #{description}, update_by = #{updateBy} WHERE id = #{id}")
    int update(Brand brand);

    /**
     * 删除品牌方法
     */
    @Update("UPDATE brand SET is_deleted = 1, name = CONCAT(name, '_del_', #{id}) WHERE id = #{id}")
    int delete(Long id);

    /**
     * 查询品牌列表总数方法
     */
    @Select("<script>" +
            "SELECT count(1) FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "</script>")
    long countList(BrandListReq req);

    /**
     * 条件查询品牌列表方法
     */
    @Select("<script>" +
            "SELECT * FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "ORDER BY create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<Brand> selectList(BrandListReq req);
    
    /**
     * 查询所有品牌方法
     */
    @Select("SELECT * FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) ORDER BY create_time DESC")
    List<Brand> selectAll();

    /**
     * 导出品牌列表方法
     */
    @Select("<script>" +
            "SELECT * FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "ORDER BY create_time DESC " +
            "</script>")
    List<Brand> exportList(BrandListReq req);
}
