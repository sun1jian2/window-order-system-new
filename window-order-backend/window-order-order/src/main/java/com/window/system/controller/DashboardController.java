package com.window.system.controller;

import cn.hutool.json.JSONUtil;
import com.window.system.common.Result;
import com.window.system.model.dto.DashboardStats;
import com.window.system.model.req.OrderListReq;
import com.window.system.security.AuthUser;
import com.window.system.service.DashboardService;
import com.window.system.service.SysExportTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<DashboardStats> getStats(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser user = (AuthUser) authentication.getPrincipal();
        Long userId = user.getId();
        String role = user.getRole();

        return dashboardService.getStats(userId, role, startDate, endDate);
    }

    @Autowired
    private SysExportTaskService sysExportTaskService;

    @PostMapping("/export")
    public Result<String> export(@RequestBody OrderListReq req) {
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出订单_" + timeStr + ".xlsx", "ORDER", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
