package com.window.system.mapper;

import com.window.system.model.entity.SysUser;
import com.window.system.model.req.user.UserListReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
/**
 * SysUserMapper Mapper接口
 */
public interface SysUserMapper {
    @Select("SELECT u.*, r.role_name as roleName FROM sys_user u LEFT JOIN sys_role r ON u.role = r.role_code WHERE u.username = #{username} AND (u.is_deleted = 0 OR u.is_deleted IS NULL)")
    /**
     * findByUsername 方法
     */
    SysUser findByUsername(String username);
    
    @Select("SELECT u.*, r.role_name as roleName FROM sys_user u LEFT JOIN sys_role r ON u.role = r.role_code WHERE u.id = #{id} AND (u.is_deleted = 0 OR u.is_deleted IS NULL)")
    /**
     * getById 方法
     */
    SysUser getById(Long id);

    @Insert("INSERT INTO sys_user (username, password, real_name, role, create_by, create_time, is_deleted) VALUES (#{username}, #{password}, #{realName}, #{role}, #{createBy}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /**
     * insert 方法
     */
    int insert(SysUser user);

    @Update("<script>" +
            "UPDATE sys_user SET role = #{role}, real_name = #{realName}, username = #{username}, update_by = #{updateBy} " +
            "<if test='password != null and password != \"\"'>, password = #{password}</if> " +
            "WHERE id = #{id}" +
            "</script>")
    /**
     * update 方法
     */
    int update(SysUser user);
    
    @Update("UPDATE sys_user SET is_deleted = 1, username = CONCAT(username, '_del_', #{id}) WHERE id = #{id}")
    /**
     * delete 方法
     */
    int delete(Long id);

    @Update("UPDATE sys_user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Select("<script>" +
            "SELECT count(1) FROM sys_user WHERE (is_deleted = 0 OR is_deleted IS NULL) " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='realName != null and realName != \"\"'> AND real_name LIKE CONCAT('%', #{realName}, '%')</if> " +
            "</script>")
    /**
     * countList 方法
     */
    long countList(UserListReq req);

    @Select("<script>" +
            "SELECT u.*, r.role_name as roleName FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role = r.role_code " +
            "WHERE (u.is_deleted = 0 OR u.is_deleted IS NULL) " +
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='realName != null and realName != \"\"'> AND u.real_name LIKE CONCAT('%', #{realName}, '%')</if> " +
            "ORDER BY u.create_time DESC " +
            "LIMIT #{startIndex}, #{pageSize}" +
            "</script>")
    /**
     * selectList 方法
     */
    List<SysUser> selectList(UserListReq req);
    
    @Select("SELECT * FROM sys_user WHERE role = #{role} AND (is_deleted = 0 OR is_deleted IS NULL)")
    /**
     * selectByRole 方法
     */
    List<SysUser> selectByRole(String role);
    
    @Select("SELECT * FROM sys_user WHERE (is_deleted = 0 OR is_deleted IS NULL)")
    /**
     * selectAll 方法
     */
    List<SysUser> selectAll();

    @Select("<script>" +
            "SELECT u.*, r.role_name as roleName FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role = r.role_code " +
            "WHERE (u.is_deleted = 0 OR u.is_deleted IS NULL) " +
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%')</if> " +
            "<if test='realName != null and realName != \"\"'> AND u.real_name LIKE CONCAT('%', #{realName}, '%')</if> " +
            "ORDER BY u.create_time DESC " +
            "</script>")
    /**
     * exportList 方法
     */
    List<SysUser> exportList(UserListReq req);

    @Select("<script>" +
            "SELECT * FROM sys_user WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    List<SysUser> selectByIds(@Param("ids") List<Long> ids);
}
