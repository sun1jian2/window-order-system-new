package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * SysOperationLog 实体/请求/响应类
 */
public class SysOperationLog {
    @ExcelProperty("ID")
    private Long id;
    
    @ExcelProperty("用户ID")
    private Long userId;
    
    @ExcelProperty("用户名")
    private String username;
    
    @ExcelProperty("模块")
    private String module;
    
    @ExcelProperty("操作类型")
    private String operation;
    
    @ExcelProperty("方法名")
    private String method;
    
    @ExcelProperty("请求参数")
    private String params;
    
    @ExcelProperty("IP地址")
    private String ip;
    
    @ExcelProperty("状态")
    private Integer status; // 1:成功 0:失败
    
    @ExcelProperty("错误信息")
    private String errorMsg;
    
    @ExcelProperty("耗时(ms)")
    private Long costTime;
    
    @ExcelProperty("操作时间")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Override
    public String toString() {
        return com.window.system.util.JsonUtils.toJson(this);
    }
}
