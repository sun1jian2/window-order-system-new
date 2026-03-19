package com.window.system.service.inventory.impl;

import com.window.system.common.Result;
import com.window.system.mapper.inventory.SupplierMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.Supplier;
import com.window.system.model.req.inventory.SupplierListReq;
import com.window.system.model.req.inventory.SupplierSaveReq;
import com.window.system.service.inventory.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
/**
 * SupplierServiceImpl 服务类/接口
 */
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    /**
     * list 方法
     */
    public Result<PageResponse<Supplier>> list(SupplierListReq req) {
        long total = supplierMapper.countList(req);
        if (total == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<Supplier> list = supplierMapper.selectList(req);
        return Result.success(PageResponse.of(list, total));
    }

    @Override
    /**
     * listAll 方法
     */
    public Result<List<Supplier>> listAll() {
        return Result.success(supplierMapper.listAll());
    }

    @Override
    /**
     * save 方法
     */
    public Result<String> save(SupplierSaveReq req, Long currentUserId) {
        Supplier supplier = new Supplier();
        supplier.setId(req.getId());
        supplier.setName(req.getName());
        supplier.setContactPerson(req.getContactPerson());
        supplier.setPhone(req.getPhone());
        supplier.setAddress(req.getAddress());
        supplier.setRemark(req.getRemark());

        if (req.getId() == null) {
            supplier.setCreateBy(currentUserId);
            supplierMapper.insert(supplier);
        } else {
            supplier.setUpdateBy(currentUserId);
            supplierMapper.update(supplier);
        }
        return Result.success("保存成功");
    }

    @Override
    /**
     * delete 方法
     */
    public Result<String> delete(Long id, Long currentUserId) {
        supplierMapper.delete(id, currentUserId);
        return Result.success("删除成功");
    }
}
