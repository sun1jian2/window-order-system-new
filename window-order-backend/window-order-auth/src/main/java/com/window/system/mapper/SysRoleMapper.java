package com.window.system.mapper;

import com.window.system.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * SysRoleMapper Mapper接口
 */
@Mapper
public interface SysRoleMapper {
    
    /**
     * 查询所有角色方法
     */
    @Select("SELECT * FROM sys_role ORDER BY create_time ASC")
    List<SysRole> selectAll();
    
    /**
     * 根据角色代码查询角色方法
     */
    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode}")
    SysRole findByCode(String roleCode);
}
