package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.entity.SysRole;
import com.window.system.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/**
 * SysRoleService 服务类/接口
 */
public class SysRoleService {
    
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * listAll 方法
     */
    public Result<List<SysRole>> listAll() {
        return Result.success(sysRoleMapper.selectAll());
    }
}
