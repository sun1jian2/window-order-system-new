package com.window.system.controller.inventory;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.Material;
import com.window.system.model.entity.inventory.MaterialCategory;
import com.window.system.model.req.inventory.MaterialListReq;
import com.window.system.model.req.inventory.MaterialSaveReq;
import com.window.system.service.inventory.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/material")
@CrossOrigin(origins = "*")
/**
 * MaterialController 控制器类
 */
public class MaterialController {

    @Autowired
    private MaterialService materialService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<Material>> list(@RequestBody MaterialListReq req) {
        return materialService.list(req);
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated MaterialSaveReq req, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return materialService.save(req, currentUserId);
    }

        /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return materialService.delete(id, currentUserId);
    }

        /**
     * listCategories 方法
     */
    @GetMapping("/categories")
    public Result<List<MaterialCategory>> listCategories() {
        return materialService.listCategories();
    }
}
