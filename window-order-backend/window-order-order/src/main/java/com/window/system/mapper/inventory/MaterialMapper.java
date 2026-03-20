package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.Material;
import com.window.system.model.req.inventory.MaterialListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * MaterialMapper Mapper接口
 */
@Mapper
public interface MaterialMapper {

    /**
     * 条件查询材料列表方法
     */
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
    List<Material> selectList(MaterialListReq req);

    /**
     * 查询材料列表总数方法
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM material m WHERE m.is_deleted = 0 " +
            "<if test='categoryId != null'> AND m.category_id = #{categoryId} </if> " +
            "<if test='keyword != null and keyword != \"\"'> AND (m.name LIKE CONCAT('%', #{keyword}, '%') OR m.code LIKE CONCAT('%', #{keyword}, '%')) </if> " +
            "<if test='isWarning != null and isWarning == true'> AND m.stock_quantity &lt;= m.warning_quantity </if> " +
            "</script>")
    long countList(MaterialListReq req);

    /**
     * 新增材料方法
     */
    @Insert("INSERT INTO material (category_id, name, code, spec, unit, unit_price, stock_quantity, warning_quantity, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{categoryId}, #{name}, #{code}, #{spec}, #{unit}, #{unitPrice}, #{stockQuantity}, #{warningQuantity}, #{remark}, #{createBy}, NOW(), 0)")
    int insert(Material material);

    /**
     * 更新材料方法
     */
    @Update("UPDATE material SET category_id = #{categoryId}, name = #{name}, code = #{code}, spec = #{spec}, " +
            "unit = #{unit}, unit_price = #{unitPrice}, stock_quantity = #{stockQuantity}, warning_quantity = #{warningQuantity}, " +
            "remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    int update(Material material);

    /**
     * 删除材料方法
     */
    @Update("UPDATE material SET is_deleted = 1, update_by = #{updateBy} WHERE id = #{id}")
    int delete(@Param("id") Long id, @Param("updateBy") Long updateBy);
}
