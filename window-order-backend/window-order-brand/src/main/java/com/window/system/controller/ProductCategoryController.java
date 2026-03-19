package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.entity.ProductCategory;
import com.window.system.model.req.ProductCategorySaveReq;
import com.window.system.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/category")
@CrossOrigin(origins = "*")
/**
 * ProductCategoryController 控制器类
 */
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

        /**
     * listAll 方法
     */
    @GetMapping("/listAll")
    public Result<List<ProductCategory>> listAll() {
        return productCategoryService.listAll();
    }

        /**
     * save 方法
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated ProductCategorySaveReq req) {
        return productCategoryService.save(req);
    }

        /**
     * delete 方法
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        return productCategoryService.delete(id);
    }
}
