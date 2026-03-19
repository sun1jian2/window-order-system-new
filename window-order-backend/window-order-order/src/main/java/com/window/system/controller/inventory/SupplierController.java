package com.window.system.controller.inventory;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.Supplier;
import com.window.system.model.req.inventory.SupplierListReq;
import com.window.system.model.req.inventory.SupplierSaveReq;
import com.window.system.service.inventory.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/supplier")
@CrossOrigin(origins = "*")
/**
 * SupplierController 控制器类
 */
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<Supplier>> list(@RequestBody SupplierListReq req) {
        return supplierService.list(req);
    }

        /**
     * listAll 方法
     */
    @GetMapping("/listAll")
    public Result<List<Supplier>> listAll() {
        return supplierService.listAll();
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated SupplierSaveReq req, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return supplierService.save(req, currentUserId);
    }

        /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return supplierService.delete(id, currentUserId);
    }
}
