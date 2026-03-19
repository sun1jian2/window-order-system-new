package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.Supplier;
import com.window.system.model.req.inventory.SupplierListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * SupplierMapper Mapper接口
 */
public interface SupplierMapper {

    @Select("<script>" +
            "SELECT * FROM supplier WHERE is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if> " +
            "ORDER BY id DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * selectList 方法
     */
    List<Supplier> selectList(SupplierListReq req);

    @Select("<script>" +
            "SELECT COUNT(1) FROM supplier WHERE is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(SupplierListReq req);

    @Insert("INSERT INTO supplier (name, contact_person, phone, address, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{name}, #{contactPerson}, #{phone}, #{address}, #{remark}, #{createBy}, NOW(), 0)")
    /**
     * insert 方法
     */
    int insert(Supplier supplier);

    @Update("UPDATE supplier SET name = #{name}, contact_person = #{contactPerson}, phone = #{phone}, " +
            "address = #{address}, remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    /**
     * update 方法
     */
    int update(Supplier supplier);

    @Update("UPDATE supplier SET is_deleted = 1, update_by = #{updateBy} WHERE id = #{id}")
    int delete(@Param("id") Long id, @Param("updateBy") Long updateBy);

    @Select("SELECT * FROM supplier WHERE is_deleted = 0")
    /**
     * listAll 方法
     */
    List<Supplier> listAll();
}
