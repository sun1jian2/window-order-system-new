package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Brand;
import com.window.system.model.req.brand.BrandListReq;
import com.window.system.model.req.brand.BrandSaveReq;
import com.window.system.service.BrandService;
import com.window.system.annotation.Log;
import com.window.system.service.SysExportTaskService;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@CrossOrigin(origins = "*")
/**
 * BrandController 控制器类
 */
public class BrandController {

    @Autowired
    private BrandService brandService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<PageResponse<Brand>> list(@RequestBody BrandListReq req) {
        return brandService.list(req);
    }
    
        /**
     * listAll 方法
     */
    @GetMapping("/all")
    public Result<List<Brand>> listAll() {
        return brandService.listAll();
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Log(module = "品牌", operation = "保存品牌")
    public Result<String> save(@RequestBody @Validated BrandSaveReq req) {
        return brandService.save(req);
    }

        /**
     * delete 方法
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Log(module = "品牌", operation = "删除品牌")
    public Result<String> delete(@PathVariable("id") Long id) {
        return brandService.delete(id);
    }

    @Autowired
    private SysExportTaskService sysExportTaskService;

        /**
     * export 方法
     */
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<String> export(@RequestBody BrandListReq req) {
        String params = JSONUtil.toJsonStr(req);
        String timeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        sysExportTaskService.createTask("导出品牌_" + timeStr + ".xlsx", "BRAND", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
