package com.window.system.controller;

import com.window.system.annotation.Log;
import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.ProductionProcess;
import com.window.system.model.req.ProductionProcessListReq;
import com.window.system.model.req.ProductionProcessSaveReq;
import com.window.system.service.ProductionProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/process")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
/**
 * ProductionProcessController 控制器类
 */
public class ProductionProcessController {

    @Autowired
    private ProductionProcessService productionProcessService;

    /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<ProductionProcess>> list(@RequestBody ProductionProcessListReq req) {
        log.info("Query production process list: {}", req);
        return productionProcessService.list(req);
    }

    /**
     * create 方法
     */
    @PostMapping("/create")
    @Log(module = "生产管理", operation = "创建生产工序")
    public Result<String> create(@RequestBody ProductionProcessSaveReq req) {
        log.info("Create production process: {}", req);
        return productionProcessService.create(req);
    }

    /**
     * update 方法
     */
    @PostMapping("/update")
    @Log(module = "生产管理", operation = "更新生产工序")
    public Result<String> update(@RequestBody ProductionProcessSaveReq req) {
        log.info("Update production process: {}", req);
        return productionProcessService.update(req);
    }

    /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    @Log(module = "生产管理", operation = "删除生产工序")
    public Result<String> delete(@PathVariable("id") Long id) {
        log.info("Delete production process id: {}", id);
        return productionProcessService.delete(id);
    }
    
    /**
     * get 方法
     */
    @GetMapping("/detail/{id}")
    public Result<ProductionProcess> get(@PathVariable("id") Long id) {
        log.info("Get production process detail id: {}", id);
        return productionProcessService.get(id);
    }
}
