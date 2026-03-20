package com.window.system.mapper;

import com.window.system.model.entity.OrderContract;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单合同Mapper接口
 */
@Mapper
public interface OrderContractMapper {

    /**
     * 新增订单合同方法
     */
    @Insert("INSERT INTO order_contract (contract_no, order_id, customer_id, pdf_url, sign_status, sign_url, third_party_id, remark, create_by, create_time, is_deleted) " +
            "VALUES (#{contractNo}, #{orderId}, #{customerId}, #{pdfUrl}, #{signStatus}, #{signUrl}, #{thirdPartyId}, #{remark}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderContract contract);

    /**
     * 更新订单合同方法
     */
    @Update("<script>" +
            "UPDATE order_contract SET update_time = NOW() " +
            "<if test='pdfUrl != null'>, pdf_url = #{pdfUrl}</if> " +
            "<if test='signStatus != null'>, sign_status = #{signStatus}</if> " +
            "<if test='signUrl != null'>, sign_url = #{signUrl}</if> " +
            "<if test='thirdPartyId != null'>, third_party_id = #{thirdPartyId}</if> " +
            "<if test='remark != null'>, remark = #{remark}</if> " +
            "<if test='updateBy != null'>, update_by = #{updateBy}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(OrderContract contract);

    /**
     * 根据订单ID查询合同方法
     */
    @Select("SELECT * FROM order_contract WHERE order_id = #{orderId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<OrderContract> getByOrderId(Long orderId);
    
    /**
     * 根据合同编号查询合同方法
     */
    @Select("SELECT * FROM order_contract WHERE contract_no = #{contractNo} AND is_deleted = 0 LIMIT 1")
    OrderContract getByContractNo(String contractNo);

    /**
     * 删除订单合同方法
     */
    @Update("UPDATE order_contract SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    int delete(Long id);
}
