package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.SalesTargetMapper;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.model.dto.SalesTargetResp;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.SalesTargetListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
/**
 * SalesTargetExportStrategy 服务类/接口
 */
public class SalesTargetExportStrategy implements ExportStrategy {

    @Autowired
    private SalesTargetMapper salesTargetMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    /**
     * getType 方法
     */
    public String getType() {
        return "SALES_TARGET";
    }

    @Override
    /**
     * export 方法
     */
    public File export(String params, Long taskId) throws Exception {
        SalesTargetListReq req = JSONUtil.toBean(params, SalesTargetListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_target_", ".xlsx");
        List<SalesTargetResp> list = salesTargetMapper.exportList(req.getMonth(), req.getSalespersonId());
        
        // Calculate completion rate if needed (though it might be null or handled by frontend, better to calc here)
        for (SalesTargetResp item : list) {
            if (item.getTargetAmount() != null && item.getTargetAmount().compareTo(BigDecimal.ZERO) > 0 && item.getActualAmount() != null) {
                BigDecimal rate = item.getActualAmount().divide(item.getTargetAmount(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                item.setCompletionRate(rate);
            } else {
                item.setCompletionRate(BigDecimal.ZERO);
            }
        }
        
        EasyExcel.write(temp, SalesTargetResp.class).sheet("销售目标").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
