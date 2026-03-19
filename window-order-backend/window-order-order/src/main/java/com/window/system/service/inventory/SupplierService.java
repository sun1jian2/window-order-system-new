package com.window.system.service.inventory;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.Supplier;
import com.window.system.model.req.inventory.SupplierListReq;
import com.window.system.model.req.inventory.SupplierSaveReq;

import java.util.List;

/**
 * SupplierService 服务类/接口
 */
public interface SupplierService {
    /**
     * list 方法
     */
    Result<PageResponse<Supplier>> list(SupplierListReq req);
    /**
     * listAll 方法
     */
    Result<List<Supplier>> listAll();
    /**
     * save 方法
     */
    Result<String> save(SupplierSaveReq req, Long currentUserId);
    /**
     * delete 方法
     */
    Result<String> delete(Long id, Long currentUserId);
}
