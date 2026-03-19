package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.entity.SysRole;
import com.window.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
/**
 * SysRoleController 控制器类
 */
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

        /**
     * listAll 方法
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<List<SysRole>> listAll() {
        return sysRoleService.listAll();
    }
}
