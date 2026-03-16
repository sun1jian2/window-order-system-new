package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.dto.LoginResp;
import com.window.system.model.entity.SysUser;
import com.window.system.model.req.user.UserListReq;
import com.window.system.model.req.user.UserSaveReq;
import com.window.system.mapper.SysUserMapper;
import com.window.system.security.JwtUtils;
import com.window.system.security.AuthUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class SysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Result<LoginResp> login(String username, String password) {
        if (sysUserMapper == null) {
            return Result.error("Internal Error: SysUserMapper not injected");
        }
        if (passwordEncoder == null) {
            return Result.error("Internal Error: PasswordEncoder not injected");
        }
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("Invalid username or password");
        }
        // Issue JWT
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        String token = JwtUtils.generateToken(user.getUsername(), claims);
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUser(user);
        return Result.success(resp);
    }

    public Result<String> migratePasswords() {
        List<SysUser> users = sysUserMapper.selectAll();
        int count = 0;
        for (SysUser user : users) {
            String pwd = user.getPassword();
            // Check if password looks like BCrypt (starts with $2a$, $2b$, $2y$ and length is 60)
            boolean isBCrypt = pwd != null && pwd.length() == 60 && (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$"));
            
            if (!isBCrypt && pwd != null && !pwd.isEmpty()) {
                String encodedPwd = passwordEncoder.encode(pwd);
                sysUserMapper.updatePassword(user.getId(), encodedPwd);
                count++;
            }
        }
        return Result.success("Successfully migrated " + count + " users to BCrypt.");
    }

    public Result<PageResponse<SysUser>> list(UserListReq req) {
        long count = sysUserMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<SysUser> list = sysUserMapper.selectList(req);
        return Result.success(PageResponse.of(list, count));
    }
    
    @Cacheable(value = "user_role_list", key = "#role")
    public Result<List<SysUser>> listByRole(String role) {
        return Result.success(sysUserMapper.selectByRole(role));
    }

    @CacheEvict(value = "user_role_list", allEntries = true)
    public Result<String> save(UserSaveReq req) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(req, user);
        
        // Check username uniqueness
        if (req.getUsername() != null && !req.getUsername().isEmpty()) {
            SysUser existing = sysUserMapper.findByUsername(req.getUsername());
            // If exists and it's not the current user being updated (for update case)
            if (existing != null) {
                if (req.getId() == null) {
                     return Result.error("Username already exists");
                } else if (!existing.getId().equals(req.getId())) {
                     return Result.error("Username already exists");
                }
            }
        }
        
        if (req.getId() == null) {
            if (req.getPassword() == null || req.getPassword().isEmpty()) {
                user.setPassword("123456"); // Default password
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // audit from security context
            Object principal = SecurityContextHolder.getContext().getAuthentication() != null 
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
            if (principal instanceof AuthUser au) {
                user.setCreateBy(au.getId());
            }
            user.setCreateBy(req.getCurrentUserId());
            sysUserMapper.insert(user);
        } else {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            Object principal = SecurityContextHolder.getContext().getAuthentication() != null 
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
            if (principal instanceof AuthUser au) {
                user.setUpdateBy(au.getId());
            }
            user.setUpdateBy(req.getCurrentUserId());
            sysUserMapper.update(user);
        }
        return Result.success("Saved successfully");
    }

    public Result<Map<Long, SysUser>> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.success(Collections.emptyMap());
        }
        List<SysUser> users = sysUserMapper.selectByIds(ids);
        Map<Long, SysUser> map = new java.util.HashMap<>();
        for (SysUser u : users) {
            map.put(u.getId(), u);
        }
        return Result.success(map);
    }

    @CacheEvict(value = "user_role_list", allEntries = true)
    public Result<String> delete(Long id) {
        sysUserMapper.delete(id);
        return Result.success("Deleted successfully");
    }
}
