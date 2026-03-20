package com.window.system.model.req;

import lombok.Data;
import com.window.system.util.JsonUtils;
import jakarta.validation.constraints.NotBlank;

/**
 * 合同签署回调请求参数
 */
@Data
public class ContractSignCallbackReq {

    @NotBlank(message = "合同编号不能为空")
    private String contractNo;

    @NotBlank(message = "签署状态不能为空")
    private String status;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
