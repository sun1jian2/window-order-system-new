package com.window.system.service.inventory;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.inventory.Material;
import com.window.system.model.entity.inventory.MaterialCategory;
import com.window.system.model.req.inventory.MaterialListReq;
import com.window.system.model.req.inventory.MaterialSaveReq;

import java.util.List;

/**
 * MaterialService 服务类/接口
 */
public interface MaterialService {
    /**
     * list 方法
     */
    Result<PageResponse<Material>> list(MaterialListReq req);
    /**
     * save 方法
     */
    Result<String> save(MaterialSaveReq req, Long currentUserId);
    /**
     * delete 方法
     */
    Result<String> delete(Long id, Long currentUserId);
    /**
     * listCategories 方法
     */
    Result<List<MaterialCategory>> listCategories();
}
