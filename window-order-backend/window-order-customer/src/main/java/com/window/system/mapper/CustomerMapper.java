package com.window.system.mapper;

import com.window.system.model.entity.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * CustomerMapper Mapper接口
 */
public interface CustomerMapper {

    @Insert("INSERT INTO customer (name, phone, address, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{name}, #{phone}, #{address}, #{remark}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(Customer customer);

    @Update("<script>" +
            "UPDATE customer SET update_time = NOW() " +
            "<if test='name != null'>, name = #{name}</if> " +
            "<if test='phone != null'>, phone = #{phone}</if> " +
            "<if test='address != null'>, address = #{address}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    /**
     * update 方法
     */
    int update(Customer customer);

    @Select("SELECT * FROM customer WHERE phone = #{phone} AND is_deleted = 0 LIMIT 1")
    /**
     * getByPhone 方法
     */
    Customer getByPhone(String phone);

    @Select("SELECT * FROM customer WHERE id = #{id} AND is_deleted = 0")
    /**
     * getById 方法
     */
    Customer getById(Long id);

    @Select("<script>" +
            "SELECT c.*, " +
            "(SELECT COUNT(1) FROM window_order o WHERE o.customer_id = c.id AND o.is_deleted = 0) as order_count, " +
            "(SELECT IFNULL(SUM(o.price), 0) FROM window_order o WHERE o.customer_id = c.id AND o.is_deleted = 0) as total_spent " +
            "FROM customer c " +
            "WHERE c.is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND c.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='phone != null and phone != \"\"'> AND c.phone LIKE CONCAT('%', #{phone}, '%')</if> " +
            "ORDER BY c.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<Customer> selectList(@Param("name") String name, @Param("phone") String phone, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    @Select("<script>" +
            "SELECT count(1) FROM customer c WHERE c.is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND c.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='phone != null and phone != \"\"'> AND c.phone LIKE CONCAT('%', #{phone}, '%')</if> " +
            "</script>")
    long countList(@Param("name") String name, @Param("phone") String phone);

    @Select("<script>" +
            "SELECT c.*, " +
            "(SELECT COUNT(1) FROM window_order o WHERE o.customer_id = c.id AND o.is_deleted = 0) as order_count, " +
            "(SELECT IFNULL(SUM(o.price), 0) FROM window_order o WHERE o.customer_id = c.id AND o.is_deleted = 0) as total_spent " +
            "FROM customer c " +
            "WHERE c.is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND c.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='phone != null and phone != \"\"'> AND c.phone LIKE CONCAT('%', #{phone}, '%')</if> " +
            "ORDER BY c.create_time DESC " +
            "</script>")
    List<Customer> exportList(@Param("name") String name, @Param("phone") String phone);
}
