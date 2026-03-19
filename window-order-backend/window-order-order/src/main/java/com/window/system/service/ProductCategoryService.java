package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.entity.ProductCategory;
import com.window.system.model.req.ProductCategorySaveReq;

import java.util.List;

public interface ProductCategoryService {
    Result<List<ProductCategory>> listAll();
    Result<String> save(ProductCategorySaveReq req);
    Result<String> delete(Long id);
}
