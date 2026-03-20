package com.window.system.controller;

import com.window.system.annotation.Log;
import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.ProductionPlan;
import com.window.system.model.req.ProductionPlanListReq;
import com.window.system.model.req.ProductionPlanSaveReq;
import com.window.system.service.ProductionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/plan")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
/**
 * ProductionPlanController 控制器类
 */
public class ProductionPlanController {

    @Autowired
    private ProductionPlanService productionPlanService;

    /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<ProductionPlan>> list(@RequestBody ProductionPlanListReq req) {
        log.info("Query production plan list: {}", req);
        return productionPlanService.list(req);
    }

    /**
     * create 方法
     */
    @PostMapping("/create")
    @Log(module = "生产管理", operation = "创建排产计划")
    public Result<String> create(@RequestBody ProductionPlanSaveReq req) {
        log.info("Create production plan: {}", req);
        return productionPlanService.create(req);
    }

    /**
     * update 方法
     */
    @PostMapping("/update")
    @Log(module = "生产管理", operation = "更新排产计划")
    public Result<String> update(@RequestBody ProductionPlanSaveReq req) {
        log.info("Update production plan: {}", req);
        return productionPlanService.update(req);
    }

    /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    @Log(module = "生产管理", operation = "删除排产计划")
    public Result<String> delete(@PathVariable("id") Long id) {
        log.info("Delete production plan id: {}", id);
        return productionPlanService.delete(id);
    }
    
    /**
     * get 方法
     */
    @GetMapping("/detail/{id}")
    public Result<ProductionPlan> get(@PathVariable("id") Long id) {
        log.info("Get production plan detail id: {}", id);
        return productionPlanService.get(id);
    }
}
