package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "导出任务")
/**
 * SysExportTask 实体/请求/响应类
 */
public class SysExportTask {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "状态: PENDING, PROCESSING, COMPLETED, FAILED")
    private String status;

    @Schema(description = "文件下载地址")
    private String fileUrl;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "导出类型")
    private String exportType;

    @Schema(description = "导出参数")
    private String exportParams;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "创建人姓名")
    private String createByName; // 关联查询用

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
