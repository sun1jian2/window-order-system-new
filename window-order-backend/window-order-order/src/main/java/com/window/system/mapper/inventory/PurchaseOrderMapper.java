package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.PurchaseOrder;
import com.window.system.model.req.inventory.PurchaseOrderListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * PurchaseOrderMapper Mapper接口
 */
public interface PurchaseOrderMapper {

    @Select("<script>" +
            "SELECT p.*, s.name as supplier_name " +
            "FROM purchase_order p " +
            "LEFT JOIN supplier s ON p.supplier_id = s.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND p.order_no LIKE CONCAT('%', #{orderNo}, '%') </if> " +
            "<if test='supplierId != null'> AND p.supplier_id = #{supplierId} </if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status} </if> " +
            "ORDER BY p.id DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * selectList 方法
     */
    List<PurchaseOrder> selectList(PurchaseOrderListReq req);

    @Select("<script>" +
            "SELECT COUNT(1) FROM purchase_order p WHERE p.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND p.order_no LIKE CONCAT('%', #{orderNo}, '%') </if> " +
            "<if test='supplierId != null'> AND p.supplier_id = #{supplierId} </if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status} </if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(PurchaseOrderListReq req);

    @Insert("INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, purchase_date, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{orderNo}, #{supplierId}, #{totalAmount}, #{status}, #{purchaseDate}, #{remark}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(PurchaseOrder purchaseOrder);

    @Update("UPDATE purchase_order SET supplier_id = #{supplierId}, total_amount = #{totalAmount}, " +
            "purchase_date = #{purchaseDate}, remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    /**
     * update 方法
     */
    int update(PurchaseOrder purchaseOrder);

    @Update("UPDATE purchase_order SET status = #{status}, update_by = #{updateBy} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("updateBy") Long updateBy);

    @Select("SELECT p.*, s.name as supplier_name FROM purchase_order p LEFT JOIN supplier s ON p.supplier_id = s.id WHERE p.id = #{id} AND p.is_deleted = 0")
    /**
     * getById 方法
     */
    PurchaseOrder getById(Long id);
    
    @Update("UPDATE purchase_order SET paid_amount = paid_amount + #{amount} WHERE id = #{id}")
    int addPaidAmount(@Param("id") Long id, @Param("amount") java.math.BigDecimal amount);
}
