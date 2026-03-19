package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Product;
import com.window.system.model.req.ProductListReq;
import com.window.system.model.req.ProductSaveReq;

import java.util.List;

/**
 * ProductService 服务类/接口
 */
public interface ProductService {
    /**
     * list 方法
     */
    Result<PageResponse<Product>> list(ProductListReq req);
    /**
     * listAllActive 方法
     */
    Result<List<Product>> listAllActive();
    /**
     * save 方法
     */
    Result<String> save(ProductSaveReq req, Long currentUserId);
    /**
     * delete 方法
     */
    Result<String> delete(Long id, Long currentUserId);
    /**
     * getById 方法
     */
    Result<Product> getById(Long id);
}
