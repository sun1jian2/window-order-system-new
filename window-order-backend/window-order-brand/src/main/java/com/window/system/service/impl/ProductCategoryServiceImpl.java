package com.window.system.service.impl;

import com.window.system.common.Result;
import com.window.system.mapper.ProductCategoryMapper;
import com.window.system.model.entity.ProductCategory;
import com.window.system.model.req.ProductCategorySaveReq;
import com.window.system.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/**
 * ProductCategoryServiceImpl 服务类/接口
 */
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    /**
     * listAll 方法
     */
    public Result<List<ProductCategory>> listAll() {
        return Result.success(productCategoryMapper.listAll());
    }

    @Override
    /**
     * save 方法
     */
    public Result<String> save(ProductCategorySaveReq req) {
        ProductCategory category = new ProductCategory();
        category.setId(req.getId());
        category.setName(req.getName());
        category.setSort(req.getSort() != null ? req.getSort() : 0);

        if (req.getId() == null) {
            productCategoryMapper.insert(category);
        } else {
            productCategoryMapper.update(category);
        }
        return Result.success("保存成功");
    }

    @Override
    /**
     * delete 方法
     */
    public Result<String> delete(Long id) {
        productCategoryMapper.delete(id);
        return Result.success("删除成功");
    }
}
