package com.window.system.mapper;

import com.window.system.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
/**
 * SysRoleMapper Mapper接口
 */
public interface SysRoleMapper {
    
    @Select("SELECT * FROM sys_role ORDER BY create_time ASC")
    /**
     * selectAll 方法
     */
    List<SysRole> selectAll();
    
    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode}")
    /**
     * findByCode 方法
     */
    SysRole findByCode(String roleCode);
}
