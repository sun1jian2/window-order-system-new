package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.mapper.SysUserMapper;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.entity.SysUser;
import com.window.system.model.req.user.UserListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;

@Component
/**
 * UserExportStrategy 服务类/接口
 */
public class UserExportStrategy implements ExportStrategy {

    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    /**
     * getType 方法
     */
    public String getType() {
        return "USER";
    }

    @Override
    /**
     * export 方法
     */
    public File export(String params, Long taskId) throws Exception {
        UserListReq req = JSONUtil.toBean(params, UserListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_user_", ".xlsx");
        List<SysUser> list = sysUserMapper.exportList(req);
        EasyExcel.write(temp, SysUser.class).sheet("用户").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
