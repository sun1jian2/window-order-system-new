package com.window.system.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * QcRecordSaveReq 请求类
 */
@Getter
@Setter
public class QcRecordSaveReq {
    private Long id;
    private Long planId;
    private Long processId;
    private Long inspectorId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkTime;
    
    private String result;
    private String defectReason;
    private String remark;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
