package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.BrandMapper;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.model.entity.Brand;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.brand.BrandListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;

@Component
public class BrandExportStrategy implements ExportStrategy {

    @Autowired
    private BrandMapper brandMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    public String getType() {
        return "BRAND";
    }

    @Override
    public File export(String params, Long taskId) throws Exception {
        BrandListReq req = JSONUtil.toBean(params, BrandListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_brand_", ".xlsx");
        List<Brand> list = brandMapper.exportList(req);
        EasyExcel.write(temp, Brand.class).sheet("品牌").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
