package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.SalesTargetResp;
import com.window.system.model.req.SalesTargetReq;
import com.window.system.service.SalesTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.window.system.model.req.SalesTargetListReq;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/sales-target")
/**
 * SalesTargetController 控制器类
 */
public class SalesTargetController {

    @Autowired
    private SalesTargetService salesTargetService;
    
    @Autowired
    private SysExportTaskService sysExportTaskService;

        /**
     * setTarget 方法
     */
    @PostMapping("/set")
    public Result<String> setTarget(@RequestBody SalesTargetReq req) {
        return salesTargetService.setTarget(req);
    }

        /**
     * list 方法
     */
    @GetMapping("/list")
    public Result<List<SalesTargetResp>> list(@RequestParam(name = "month", required = false) String month,
                                              @RequestParam(name = "salespersonId", required = false) Long salespersonId) {
        return salesTargetService.list(month, salespersonId);
    }

        /**
     * export 方法
     */
    @PostMapping("/export")
    public Result<String> export(@RequestBody SalesTargetListReq req) {
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出销售目标_" + timeStr + ".xlsx", "SALES_TARGET", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
