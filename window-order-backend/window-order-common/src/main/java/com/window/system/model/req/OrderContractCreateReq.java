package com.window.system.model.req;

import lombok.Data;
import com.window.system.util.JsonUtils;
import jakarta.validation.constraints.NotNull;

/**
 * 订单合同生成请求参数
 */
@Data
public class OrderContractCreateReq {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    private String remark;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
