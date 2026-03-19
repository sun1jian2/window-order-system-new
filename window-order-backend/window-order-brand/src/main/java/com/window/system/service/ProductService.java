package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Product;
import com.window.system.model.req.ProductListReq;
import com.window.system.model.req.ProductSaveReq;

import java.util.List;

public interface ProductService {
    Result<PageResponse<Product>> list(ProductListReq req);
    Result<List<Product>> listAllActive();
    Result<String> save(ProductSaveReq req, Long currentUserId);
    Result<String> delete(Long id, Long currentUserId);
    Result<Product> getById(Long id);
}
