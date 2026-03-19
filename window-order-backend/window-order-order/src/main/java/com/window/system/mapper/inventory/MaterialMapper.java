package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.Material;
import com.window.system.model.req.inventory.MaterialListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * MaterialMapper Mapper接口
 */
public interface MaterialMapper {

    @Select("<script>" +
            "SELECT m.*, c.name as category_name " +
            "FROM material m " +
            "LEFT JOIN material_category c ON m.category_id = c.id " +
            "WHERE m.is_deleted = 0 " +
            "<if test='categoryId != null'> AND m.category_id = #{categoryId} </if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (m.name LIKE CONCAT('%', #{keyword}, '%') OR m.code LIKE CONCAT('%', #{keyword}, '%')) </if> " +
            "<if test='isWarning != null and isWarning == true'> AND m.stock_quantity &lt;= m.warning_quantity </if> " +
            "ORDER BY m.id DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * selectList 方法
     */
    List<Material> selectList(MaterialListReq req);

    @Select("<script>" +
            "SELECT COUNT(1) FROM material m WHERE m.is_deleted = 0 " +
            "<if test='categoryId != null'> AND m.category_id = #{categoryId} </if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (m.name LIKE CONCAT('%', #{keyword}, '%') OR m.code LIKE CONCAT('%', #{keyword}, '%')) </if> " +
            "<if test='isWarning != null and isWarning == true'> AND m.stock_quantity &lt;= m.warning_quantity </if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(MaterialListReq req);

    @Insert("INSERT INTO material (category_id, name, code, spec, unit, unit_price, stock_quantity, warning_quantity, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{categoryId}, #{name}, #{code}, #{spec}, #{unit}, #{unitPrice}, #{stockQuantity}, #{warningQuantity}, #{remark}, #{createBy}, NOW(), 0)")
    /**
     * insert 方法
     */
    int insert(Material material);

    @Update("UPDATE material SET category_id = #{categoryId}, name = #{name}, code = #{code}, spec = #{spec}, " +
            "unit = #{unit}, unit_price = #{unitPrice}, stock_quantity = #{stockQuantity}, warning_quantity = #{warningQuantity}, " +
            "remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    /**
     * update 方法
     */
    int update(Material material);

    @Update("UPDATE material SET is_deleted = 1, update_by = #{updateBy} WHERE id = #{id}")
    int delete(@Param("id") Long id, @Param("updateBy") Long updateBy);
}
