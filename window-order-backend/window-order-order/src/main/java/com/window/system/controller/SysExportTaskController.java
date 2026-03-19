package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.ExportTaskListReq;
import com.window.system.service.SysExportTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export-task")
@Tag(name = "导出任务管理")
/**
 * SysExportTaskController 控制器类
 */
public class SysExportTaskController {

    @Autowired
    private SysExportTaskService sysExportTaskService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    @Operation(summary = "获取导出任务列表")
    public Result<PageResponse<SysExportTask>> list(@RequestBody ExportTaskListReq req) {
        return Result.success(sysExportTaskService.list(req));
    }
}
