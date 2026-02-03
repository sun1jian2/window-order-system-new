package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.LoginResp;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.SysUser;
import com.window.system.model.req.user.UserListReq;
import com.window.system.model.req.user.UserSaveReq;
import com.window.system.service.SysUserService;
import com.window.system.annotation.Log;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<LoginResp> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return sysUserService.login(username, password);
    }

    @GetMapping("/migrate-passwords")
    public Result<String> migratePasswords() {
        return sysUserService.migratePasswords();
    }

    @GetMapping("/me")
    public Result<Map<String, Object>> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> info = new HashMap<>();
        if (auth != null) {
            info.put("principal", auth.getPrincipal());
            info.put("authorities", auth.getAuthorities());
        }
        return Result.success(info);
    }

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<PageResponse<SysUser>> list(@RequestBody UserListReq req) {
        return sysUserService.list(req);
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<List<SysUser>> listByRole(@PathVariable("role") String role) {
        return sysUserService.listByRole(role);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Log(module = "账号", operation = "保存账号")
    public Result<String> save(@RequestBody @Validated UserSaveReq req) {
        return sysUserService.save(req);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Log(module = "账号", operation = "删除账号")
    public Result<String> delete(@PathVariable("id") Long id) {
        return sysUserService.delete(id);
    }

    @GetMapping("/ids")
    public Result<Map<Long, SysUser>> getUsersByIds(@RequestParam("ids") List<Long> ids) {
        return sysUserService.getUsersByIds(ids);
    }

    @Autowired
    private SysExportTaskService sysExportTaskService;

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<String> export(@RequestBody UserListReq req) {
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出用户_" + timeStr + ".xlsx", "USER", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
