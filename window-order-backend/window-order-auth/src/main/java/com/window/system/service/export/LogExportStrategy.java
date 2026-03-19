package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.mapper.SysOperationLogMapper;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.entity.SysOperationLog;
import com.window.system.model.req.LogListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;

@Component
/**
 * LogExportStrategy 服务类/接口
 */
public class LogExportStrategy implements ExportStrategy {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    /**
     * getType 方法
     */
    public String getType() {
        return "LOG";
    }

    @Override
    /**
     * export 方法
     */
    public File export(String params, Long taskId) throws Exception {
        LogListReq req = JSONUtil.toBean(params, LogListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_log_", ".xlsx");
        List<SysOperationLog> list = sysOperationLogMapper.exportList(req);
        EasyExcel.write(temp, SysOperationLog.class).sheet("日志").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
