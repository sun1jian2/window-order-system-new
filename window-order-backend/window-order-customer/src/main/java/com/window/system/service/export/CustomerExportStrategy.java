package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.CustomerMapper;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.model.entity.Customer;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.CustomerListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.List;

@Component
public class CustomerExportStrategy implements ExportStrategy {

    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    public String getType() {
        return "CUSTOMER";
    }

    @Override
    public File export(String params, Long taskId) throws Exception {
        CustomerListReq req = JSONUtil.toBean(params, CustomerListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_customer_", ".xlsx");
        List<Customer> list = customerMapper.exportList(req.getName(), req.getPhone());
        EasyExcel.write(temp, Customer.class).sheet("客户").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
