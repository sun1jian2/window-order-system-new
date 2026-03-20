package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.Supplier;
import com.window.system.model.req.inventory.SupplierListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * SupplierMapper Mapper接口
 */
@Mapper
public interface SupplierMapper {

    /**
     * 条件查询供应商列表方法
     */
    @Select("<script>" +
            "SELECT * FROM supplier WHERE is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if> " +
            "ORDER BY id DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<Supplier> selectList(SupplierListReq req);

    /**
     * 查询供应商列表总数方法
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM supplier WHERE is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if> " +
            "</script>")
    long countList(SupplierListReq req);

    /**
     * 新增供应商方法
     */
    @Insert("INSERT INTO supplier (name, contact_person, phone, address, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{name}, #{contactPerson}, #{phone}, #{address}, #{remark}, #{createBy}, NOW(), 0)")
    int insert(Supplier supplier);

    /**
     * 更新供应商方法
     */
    @Update("UPDATE supplier SET name = #{name}, contact_person = #{contactPerson}, phone = #{phone}, " +
            "address = #{address}, remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    int update(Supplier supplier);

    /**
     * 删除供应商方法
     */
    @Update("UPDATE supplier SET is_deleted = 1, update_by = #{updateBy} WHERE id = #{id}")
    int delete(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 查询所有供应商方法
     */
    @Select("SELECT * FROM supplier WHERE is_deleted = 0")
    List<Supplier> listAll();
}
