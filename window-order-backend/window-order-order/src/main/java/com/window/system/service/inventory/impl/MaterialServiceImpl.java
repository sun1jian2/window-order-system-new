package com.window.system.service.inventory.impl;

import com.window.system.common.Result;
import com.window.system.mapper.inventory.MaterialCategoryMapper;
import com.window.system.mapper.inventory.MaterialMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.Material;
import com.window.system.model.entity.inventory.MaterialCategory;
import com.window.system.model.req.inventory.MaterialListReq;
import com.window.system.model.req.inventory.MaterialSaveReq;
import com.window.system.service.inventory.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
/**
 * MaterialServiceImpl 服务类/接口
 */
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;
    
    @Autowired
    private MaterialCategoryMapper materialCategoryMapper;

    @Override
    /**
     * list 方法
     */
    public Result<PageResponse<Material>> list(MaterialListReq req) {
        long total = materialMapper.countList(req);
        if (total == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<Material> list = materialMapper.selectList(req);
        return Result.success(PageResponse.of(list, total));
    }

    @Override
    /**
     * save 方法
     */
    public Result<String> save(MaterialSaveReq req, Long currentUserId) {
        Material material = new Material();
        material.setId(req.getId());
        material.setCategoryId(req.getCategoryId());
        material.setName(req.getName());
        material.setCode(req.getCode());
        material.setSpec(req.getSpec());
        material.setUnit(req.getUnit());
        material.setUnitPrice(req.getUnitPrice());
        material.setWarningQuantity(req.getWarningQuantity());
        material.setRemark(req.getRemark());

        if (req.getId() == null) {
            material.setStockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : BigDecimal.ZERO);
            material.setCreateBy(currentUserId);
            materialMapper.insert(material);
        } else {
            material.setStockQuantity(req.getStockQuantity());
            material.setUpdateBy(currentUserId);
            materialMapper.update(material);
        }
        return Result.success("保存成功");
    }

    @Override
    /**
     * delete 方法
     */
    public Result<String> delete(Long id, Long currentUserId) {
        materialMapper.delete(id, currentUserId);
        return Result.success("删除成功");
    }

    @Override
    /**
     * listCategories 方法
     */
    public Result<List<MaterialCategory>> listCategories() {
        return Result.success(materialCategoryMapper.listAll());
    }
}
