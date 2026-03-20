package com.window.system.mapper.inventory;

import com.window.system.model.entity.inventory.PurchaseOrder;
import com.window.system.model.req.inventory.PurchaseOrderListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 采购订单Mapper接口
 */
@Mapper
public interface PurchaseOrderMapper {

    /**
     * 条件查询采购订单列表方法
     */
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
    List<PurchaseOrder> selectList(PurchaseOrderListReq req);

    /**
     * 查询采购订单列表总数方法
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM purchase_order p WHERE p.is_deleted = 0 " +
            "<if test='orderNo != null and orderNo != \"\"'> AND p.order_no LIKE CONCAT('%', #{orderNo}, '%') </if> " +
            "<if test='supplierId != null'> AND p.supplier_id = #{supplierId} </if> " +
            "<if test='status != null and status != \"\"'> AND p.status = #{status} </if> " +
            "</script>")
    long countList(PurchaseOrderListReq req);

    /**
     * 新增采购订单方法
     */
    @Insert("INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, purchase_date, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{orderNo}, #{supplierId}, #{totalAmount}, #{status}, #{purchaseDate}, #{remark}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PurchaseOrder purchaseOrder);

    /**
     * 更新采购订单方法
     */
    @Update("UPDATE purchase_order SET supplier_id = #{supplierId}, total_amount = #{totalAmount}, " +
            "purchase_date = #{purchaseDate}, remark = #{remark}, update_by = #{updateBy} WHERE id = #{id}")
    int update(PurchaseOrder purchaseOrder);

    /**
     * 更新采购订单状态方法
     */
    @Update("UPDATE purchase_order SET status = #{status}, update_by = #{updateBy} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("updateBy") Long updateBy);

    /**
     * 根据主键查询采购订单方法
     */
    @Select("SELECT p.*, s.name as supplier_name FROM purchase_order p LEFT JOIN supplier s ON p.supplier_id = s.id WHERE p.id = #{id} AND p.is_deleted = 0")
    PurchaseOrder getById(Long id);
    
    /**
     * 增加已付金额方法
     */
    @Update("UPDATE purchase_order SET paid_amount = paid_amount + #{amount} WHERE id = #{id}")
    int addPaidAmount(@Param("id") Long id, @Param("amount") java.math.BigDecimal amount);
}
