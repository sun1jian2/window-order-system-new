package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.mapper.CustomerMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import com.window.system.model.req.CustomerListReq;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Customer Management")
public class CustomerController {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SysExportTaskService sysExportTaskService;

    @GetMapping("/list")
    @Operation(summary = "List customers")
    public Result<PageResponse<Customer>> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        
        long count = customerMapper.countList(name, phone);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        
        int startIndex = (pageNo - 1) * pageSize;
        List<Customer> list = customerMapper.selectList(name, phone, startIndex, pageSize);
        return Result.success(PageResponse.of(list, count));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Customer customer) {
        if (customer.getId() == null) {
            customerMapper.insert(customer);
        } else {
            customerMapper.update(customer);
        }
        return Result.success("Saved");
    }
    
    @GetMapping("/phone")
    public Result<Customer> getByPhone(@RequestParam("phone") String phone) {
        return Result.success(customerMapper.getByPhone(phone));
    }

    @PostMapping("/export")
    public Result<String> export(@RequestBody CustomerListReq req) {
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出客户_" + timeStr + ".xlsx", "CUSTOMER", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
    
    @GetMapping("/detail/{id}")
    @Operation(summary = "Get customer detail")
    public Result<Customer> getDetail(@PathVariable("id") Long id) {
        return Result.success(customerMapper.getById(id));
    }
}
