package com.window.system.mapper;

import com.window.system.model.entity.Brand;
import com.window.system.model.req.brand.BrandListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * BrandMapper Mapper接口
 */
public interface BrandMapper {

    @Insert("INSERT INTO brand (name, description, create_by, create_time, is_deleted) VALUES (#{name}, #{description}, #{createBy}, NOW(), 0)")
    /**
     * insert 方法
     */
    int insert(Brand brand);

    @Update("UPDATE brand SET name = #{name}, description = #{description}, update_by = #{updateBy} WHERE id = #{id}")
    /**
     * update 方法
     */
    int update(Brand brand);

    @Update("UPDATE brand SET is_deleted = 1, name = CONCAT(name, '_del_', #{id}) WHERE id = #{id}")
    /**
     * delete 方法
     */
    int delete(Long id);

    @Select("<script>" +
            "SELECT count(1) FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(BrandListReq req);

    @Select("<script>" +
            "SELECT * FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "ORDER BY create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * selectList 方法
     */
    List<Brand> selectList(BrandListReq req);
    
    @Select("SELECT * FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) ORDER BY create_time DESC")
    /**
     * selectAll 方法
     */
    List<Brand> selectAll();

    @Select("<script>" +
            "SELECT * FROM brand WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "ORDER BY create_time DESC " +
            "</script>")
    /**
     * exportList 方法
     */
    List<Brand> exportList(BrandListReq req);
}
