package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.AfterSalesOrderMapper;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.model.entity.AfterSalesOrder;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.AfterSalesListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;

@Component
/**
 * AfterSalesExportStrategy 服务类/接口
 */
public class AfterSalesExportStrategy implements ExportStrategy {

    @Autowired
    private AfterSalesOrderMapper afterSalesOrderMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    /**
     * getType 方法
     */
    public String getType() {
        return "AFTER_SALES";
    }

    @Override
    /**
     * export 方法
     */
    public File export(String params, Long taskId) throws Exception {
        AfterSalesListReq req = JSONUtil.toBean(params, AfterSalesListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_aftersales_", ".xlsx");
        List<AfterSalesOrder> list = afterSalesOrderMapper.exportList(req);
        EasyExcel.write(temp, AfterSalesOrder.class).sheet("售后工单").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
