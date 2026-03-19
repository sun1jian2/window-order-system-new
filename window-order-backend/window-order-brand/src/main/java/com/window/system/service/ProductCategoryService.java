package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.entity.ProductCategory;
import com.window.system.model.req.ProductCategorySaveReq;

import java.util.List;

/**
 * ProductCategoryService 服务类/接口
 */
public interface ProductCategoryService {
    /**
     * listAll 方法
     */
    Result<List<ProductCategory>> listAll();
    /**
     * save 方法
     */
    Result<String> save(ProductCategorySaveReq req);
    /**
     * delete 方法
     */
    Result<String> delete(Long id);
}
