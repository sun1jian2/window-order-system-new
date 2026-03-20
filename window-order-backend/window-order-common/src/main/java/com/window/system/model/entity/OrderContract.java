package com.window.system.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.window.system.util.JsonUtils;
import java.time.LocalDateTime;

/**
 * 订单合同实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderContract {
    private Long id;
    private String contractNo;
    private Long orderId;
    private Long customerId;
    private String pdfUrl;
    private String signStatus; // PENDING, SIGNING, COMPLETED, EXPIRED
    private String signUrl;
    private String thirdPartyId;
    private String remark;
    private Long createBy;
    private Long updateBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isDeleted;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
