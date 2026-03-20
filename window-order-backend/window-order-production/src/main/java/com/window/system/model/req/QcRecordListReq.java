package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * QcRecordListReq 请求类
 */
@Getter
@Setter
public class QcRecordListReq extends BasePageReq {
    private Long planId;
    private String planNo;
    private Long processId;
    private Long inspectorId;
    private String result;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
