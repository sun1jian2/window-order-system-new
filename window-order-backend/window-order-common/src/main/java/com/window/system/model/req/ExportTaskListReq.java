package com.window.system.model.req;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
/**
 * ExportTaskListReq 实体/请求/响应类
 */
public class ExportTaskListReq {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    
    private String operatorName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    // For internal use
    private Long currentUserId;
    private String currentUserRole;
    
    public int getStartIndex() {
        return (pageNo - 1) * pageSize;
    }

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
