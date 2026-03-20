package com.window.system.controller;

import com.window.system.annotation.Log;
import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.QcRecord;
import com.window.system.model.req.QcRecordListReq;
import com.window.system.model.req.QcRecordSaveReq;
import com.window.system.service.QcRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/qc")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
/**
 * QcRecordController 控制器类
 */
public class QcRecordController {

    @Autowired
    private QcRecordService qcRecordService;

    /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<QcRecord>> list(@RequestBody QcRecordListReq req) {
        log.info("Query qc record list: {}", req);
        return qcRecordService.list(req);
    }

    /**
     * create 方法
     */
    @PostMapping("/create")
    @Log(module = "生产管理", operation = "创建质检记录")
    public Result<String> create(@RequestBody QcRecordSaveReq req) {
        log.info("Create qc record: {}", req);
        return qcRecordService.create(req);
    }

    /**
     * update 方法
     */
    @PostMapping("/update")
    @Log(module = "生产管理", operation = "更新质检记录")
    public Result<String> update(@RequestBody QcRecordSaveReq req) {
        log.info("Update qc record: {}", req);
        return qcRecordService.update(req);
    }

    /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    @Log(module = "生产管理", operation = "删除质检记录")
    public Result<String> delete(@PathVariable("id") Long id) {
        log.info("Delete qc record id: {}", id);
        return qcRecordService.delete(id);
    }
    
    /**
     * get 方法
     */
    @GetMapping("/detail/{id}")
    public Result<QcRecord> get(@PathVariable("id") Long id) {
        log.info("Get qc record detail id: {}", id);
        return qcRecordService.get(id);
    }
}
