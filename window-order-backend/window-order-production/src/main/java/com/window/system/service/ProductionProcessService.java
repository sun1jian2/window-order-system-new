package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.ProductionProcessMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.ProductionProcess;
import com.window.system.model.req.ProductionProcessListReq;
import com.window.system.model.req.ProductionProcessSaveReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
/**
 * ProductionProcessService 服务类
 */
public class ProductionProcessService {

    @Autowired
    private ProductionProcessMapper productionProcessMapper;

    /**
     * list 方法
     */
    public Result<PageResponse<ProductionProcess>> list(ProductionProcessListReq req) {
        long count = productionProcessMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<ProductionProcess> list = productionProcessMapper.list(req);
        return Result.success(PageResponse.of(list, count));
    }

    /**
     * create 方法
     */
    public Result<String> create(ProductionProcessSaveReq req) {
        if (req.getPlanNo() != null && !req.getPlanNo().isEmpty()) {
            long planExists = productionProcessMapper.checkPlanExists(req.getPlanNo());
            if (planExists == 0) {
                return Result.error("单号校验失败：系统内不存在该排产单号 (" + req.getPlanNo() + ")");
            }
        }

        ProductionProcess process = new ProductionProcess();
        BeanUtils.copyProperties(req, process);
        // planId 可以为空，现在使用 planNo
        productionProcessMapper.insert(process);
        return Result.success("创建成功");
    }

    /**
     * update 方法
     */
    public Result<String> update(ProductionProcessSaveReq req) {
        if (req.getPlanNo() != null && !req.getPlanNo().isEmpty()) {
            long planExists = productionProcessMapper.checkPlanExists(req.getPlanNo());
            if (planExists == 0) {
                return Result.error("单号校验失败：系统内不存在该排产单号 (" + req.getPlanNo() + ")");
            }
        }

        ProductionProcess process = new ProductionProcess();
        BeanUtils.copyProperties(req, process);
        productionProcessMapper.update(process);
        return Result.success("更新成功");
    }

    /**
     * delete 方法
     */
    public Result<String> delete(Long id) {
        productionProcessMapper.delete(id);
        return Result.success("删除成功");
    }

    /**
     * get 方法
     */
    public Result<ProductionProcess> get(Long id) {
        return Result.success(productionProcessMapper.getById(id));
    }
}
