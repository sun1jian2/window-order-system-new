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
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/listAll")
    public Result<List<ProductCategory>> listAll() {
        return productCategoryService.listAll();
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated ProductCategorySaveReq req) {
        return productCategoryService.save(req);
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        return productCategoryService.delete(id);
    }
}
