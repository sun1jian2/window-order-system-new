package com.window.system.mapper;

import com.window.system.model.entity.OrderPaymentAttachment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
/**
 * OrderPaymentAttachmentMapper Mapper接口
 */
public interface OrderPaymentAttachmentMapper {

    @Insert("INSERT INTO order_payment_attachment (payment_id, url, create_time) VALUES (#{paymentId}, #{url}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(OrderPaymentAttachment attachment);

    @Select("SELECT * FROM order_payment_attachment WHERE payment_id = #{paymentId} ORDER BY id ASC")
    /**
     * getByPaymentId 方法
     */
    List<OrderPaymentAttachment> getByPaymentId(Long paymentId);
}
