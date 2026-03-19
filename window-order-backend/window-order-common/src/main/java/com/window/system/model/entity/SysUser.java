package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * SysUser 实体/请求/响应类
 */
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty("ID")
    private Long id;
    
    @ExcelProperty("用户名")
    private String username;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private String password;
    
    @ExcelProperty("真实姓名")
    private String realName;
    
    @ExcelProperty("角色代码")
    private String role;
    
    @ExcelProperty("创建时间")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long createBy;
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long updateBy;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Boolean isDeleted;
    
    // Joined field
    @ExcelProperty("角色名称")
    private String roleName;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
