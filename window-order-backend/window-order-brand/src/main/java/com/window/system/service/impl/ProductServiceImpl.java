package com.window.system.service.impl;

import com.window.system.common.Result;
import com.window.system.mapper.ProductMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Product;
import com.window.system.model.req.ProductListReq;
import com.window.system.model.req.ProductSaveReq;
import com.window.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
/**
 * ProductServiceImpl 服务类/接口
 */
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    /**
     * list 方法
     */
    public Result<PageResponse<Product>> list(ProductListReq req) {
        long total = productMapper.countList(req);
        if (total == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<Product> list = productMapper.selectList(req);
        return Result.success(PageResponse.of(list, total));
    }

    @Override
    /**
     * listAllActive 方法
     */
    public Result<List<Product>> listAllActive() {
        return Result.success(productMapper.listAllActive());
    }

    @Override
    /**
     * save 方法
     */
    public Result<String> save(ProductSaveReq req, Long currentUserId) {
        Product product = new Product();
        product.setId(req.getId());
        product.setCategoryId(req.getCategoryId());
        product.setBrandId(req.getBrandId());
        product.setName(req.getName());
        product.setCode(req.getCode());
        product.setBasePrice(req.getBasePrice());
        product.setColorOptions(req.getColorOptions());
        product.setGlassOptions(req.getGlassOptions());
        product.setDescription(req.getDescription());
        product.setStatus(req.getStatus() != null ? req.getStatus() : "ACTIVE");

        if (req.getId() == null) {
            product.setCreateBy(currentUserId);
            productMapper.insert(product);
        } else {
            product.setUpdateBy(currentUserId);
            productMapper.update(product);
        }
        return Result.success("保存成功");
    }

    @Override
    /**
     * delete 方法
     */
    public Result<String> delete(Long id, Long currentUserId) {
        productMapper.delete(id, currentUserId);
        return Result.success("删除成功");
    }

    @Override
    /**
     * getById 方法
     */
    public Result<Product> getById(Long id) {
        return Result.success(productMapper.getById(id));
    }
}
