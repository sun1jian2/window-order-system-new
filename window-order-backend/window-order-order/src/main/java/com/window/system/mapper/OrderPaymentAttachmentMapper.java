package com.window.system.mapper;

import com.window.system.model.entity.OrderPaymentAttachment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * OrderPaymentAttachmentMapper Mapper接口
 */
@Mapper
public interface OrderPaymentAttachmentMapper {

    /**
     * 新增订单支付附件方法
     */
    @Insert("INSERT INTO order_payment_attachment (payment_id, url, create_time) VALUES (#{paymentId}, #{url}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderPaymentAttachment attachment);

    /**
     * 根据支付ID查询附件列表方法
     */
    @Select("SELECT * FROM order_payment_attachment WHERE payment_id = #{paymentId} ORDER BY id ASC")
    List<OrderPaymentAttachment> getByPaymentId(Long paymentId);
}
