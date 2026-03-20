package com.window.system.mapper;

import com.window.system.model.entity.SysUser;
import com.window.system.model.req.user.UserListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * SysUserMapper Mapper接口
 */
@Mapper
public interface SysUserMapper {
    /**
     * 根据用户名查询用户方法
     */
    @Select("SELECT u.*, r.role_name as roleName FROM sys_user u LEFT JOIN sys_role r ON u.role = r.role_code WHERE u.username = #{username} AND (u.is_deleted = 0 OR u.is_deleted IS NULL)")
    SysUser findByUsername(String username);
    
    /**
     * 根据主键查询用户方法
     */
    @Select("SELECT u.*, r.role_name as roleName FROM sys_user u LEFT JOIN sys_role r ON u.role = r.role_code WHERE u.id = #{id} AND (u.is_deleted = 0 OR u.is_deleted IS NULL)")
    SysUser getById(Long id);

    /**
     * 新增用户方法
     */
    @Insert("INSERT INTO sys_user (username, password, real_name, role, create_by, create_time, is_deleted) VALUES (#{username}, #{password}, #{realName}, #{role}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser user);

    /**
     * 更新用户方法
     */
    @Update("<script>" +
            "UPDATE sys_user SET role = #{role}, real_name = #{realName}, username = #{username}, update_by = #{updateBy} " +
            "<if test='password != null and password != \"\"'>, password = #{password}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    int update(SysUser user);
    
    /**
     * 删除用户方法
     */
    @Update("UPDATE sys_user SET is_deleted = 1, username = CONCAT(username, '_del_', #{id}) WHERE id = #{id}")
    int delete(Long id);

    /**
     * 更新用户密码方法
     */
    @Update("UPDATE sys_user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 查询用户列表总数方法
     */
    @Select("<script>" +
            "SELECT count(1) FROM sys_user WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='realName != null and realName != \"\"'> AND real_name LIKE CONCAT('%', #{realName}, '%')</if> " +
            "</script>")
    long countList(UserListReq req);

    /**
     * 条件查询用户列表方法
     */
    @Select("<script>" +
            "SELECT u.*, r.role_name as roleName FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role = r.role_code " +
            "WHERE (u.is_deleted = 0 OR u.is_deleted IS NULL) " +
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='realName != null and realName != \"\"'> AND u.real_name LIKE CONCAT('%', #{realName}, '%')</if> " +
            "ORDER BY u.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    List<SysUser> selectList(UserListReq req);
    
    /**
     * 根据角色查询用户列表方法
     */
    @Select("SELECT * FROM sys_user WHERE role = #{role} AND (is_deleted = 0 OR is_deleted IS NULL)")
    List<SysUser> selectByRole(String role);
    
    /**
     * 查询所有用户方法
     */
    @Select("SELECT * FROM sys_user WHERE (is_deleted = 0 OR is_deleted IS NULL)")
    List<SysUser> selectAll();

    /**
     * 导出用户列表方法
     */
    @Select("<script>" +
            "SELECT u.*, r.role_name as roleName FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role = r.role_code " +
            "WHERE (u.is_deleted = 0 OR u.is_deleted IS NULL) " +
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='realName != null and realName != \"\"'> AND u.real_name LIKE CONCAT('%', #{realName}, '%')</if> " +
            "ORDER BY u.create_time DESC " +
            "</script>")
    List<SysUser> exportList(UserListReq req);

    /**
     * 根据ID列表查询用户方法
     */
    @Select("<script>" +
            "SELECT * FROM sys_user WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    List<SysUser> selectByIds(@Param("ids") List<Long> ids);
}
