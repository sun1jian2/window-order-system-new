package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Product;
import com.window.system.model.req.ProductListReq;
import com.window.system.model.req.ProductSaveReq;
import com.window.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*")
/**
 * ProductController 控制器类
 */
public class ProductController {

    @Autowired
    private ProductService productService;

        /**
     * list 方法
     */
    @PostMapping("/list")
    public Result<PageResponse<Product>> list(@RequestBody ProductListReq req) {
        return productService.list(req);
    }

        /**
     * listAllActive 方法
     */
    @GetMapping("/listAllActive")
    public Result<List<Product>> listAllActive() {
        return productService.listAllActive();
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated ProductSaveReq req, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return productService.save(req, currentUserId);
    }

        /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id, @RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        return productService.delete(id, currentUserId);
    }

        /**
     * getById 方法
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }
}
