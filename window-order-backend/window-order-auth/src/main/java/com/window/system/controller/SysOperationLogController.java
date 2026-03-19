package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.SysOperationLog;
import com.window.system.model.req.LogListReq;
import com.window.system.mapper.SysOperationLogMapper;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
/**
 * SysOperationLogController 控制器类
 */
public class SysOperationLogController {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

        /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<SysOperationLog>> list(@RequestBody LogListReq req) {
        log.info("Query sys operation logs: {}", req);
        long total = sysOperationLogMapper.countList(req);
        List<SysOperationLog> list = sysOperationLogMapper.selectList(req);
        return Result.success(PageResponse.of(list, total));
    }

    @Autowired
    private SysExportTaskService sysExportTaskService;

        /**
     * export 方法
     */
    @PostMapping("/export")
    public Result<String> export(@RequestBody LogListReq req) {
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出日志_" + timeStr + ".xlsx", "LOG", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
