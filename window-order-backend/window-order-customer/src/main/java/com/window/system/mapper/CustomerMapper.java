package com.window.system.mapper;

import com.window.system.model.entity.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * CustomerMapper Mapper接口
 */
@Mapper
public interface CustomerMapper {

    /**
     * 新增客户方法
     */
    @Insert("INSERT INTO customer (name, phone, address, remark, source, create_by, create_time, is_deleted) " +
            "VALUES (#{name}, #{phone}, #{address}, #{remark}, #{source}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Customer customer);

    /**
     * 更新客户方法
     */
    @Update("<script>" +
            "UPDATE customer SET update_time = NOW() " +
            "<if test='name != null'>, name = #{name}</if> " +
            "<if test='phone != null'>, phone = #{phone}</if> " +
            "<if test='address != null'>, address = #{address}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(Customer customer);

    /**
     * 根据手机号查询客户方法
     */
    @Select("SELECT * FROM customer WHERE phone = #{phone} AND is_deleted = 0 LIMIT 1")
    Customer getByPhone(String phone);

    /**
     * 根据主键查询客户方法
     */
    @Select("SELECT * FROM customer WHERE id = #{id} AND is_deleted = 0")
    Customer getById(Long id);

    /**
     * 条件查询客户列表方法
     */
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

    /**
     * 查询客户列表总数方法
     */
    @Select("<script>" +
            "SELECT count(1) FROM customer c WHERE c.is_deleted = 0 " +
            "<if test='name != null and name != \"\"'> AND c.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='phone != null and phone != \"\"'> AND c.phone LIKE CONCAT('%', #{phone}, '%')</if> " +
            "</script>")
    long countList(@Param("name") String name, @Param("phone") String phone);

    /**
     * 导出客户列表方法
     */
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
