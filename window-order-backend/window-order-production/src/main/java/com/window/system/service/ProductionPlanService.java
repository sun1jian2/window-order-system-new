package com.window.system.service;

import cn.hutool.core.util.IdUtil;
import com.window.system.common.Result;
import com.window.system.mapper.ProductionPlanMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.ProductionPlan;
import com.window.system.model.req.ProductionPlanListReq;
import com.window.system.model.req.ProductionPlanSaveReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
/**
 * ProductionPlanService 服务类
 */
public class ProductionPlanService {

    @Autowired
    private ProductionPlanMapper productionPlanMapper;

    /**
     * list 方法
     */
    public Result<PageResponse<ProductionPlan>> list(ProductionPlanListReq req) {
        long count = productionPlanMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<ProductionPlan> list = productionPlanMapper.list(req);
        return Result.success(PageResponse.of(list, count));
    }

    /**
     * create 方法
     */
    public Result<String> create(ProductionPlanSaveReq req) {
        if (req.getOrderNo() != null && !req.getOrderNo().isEmpty()) {
            long orderExists = productionPlanMapper.checkOrderExists(req.getOrderNo());
            if (orderExists == 0) {
                return Result.error("单号校验失败：系统内不存在该订单号 (" + req.getOrderNo() + ")");
            }
        }

        ProductionPlan plan = new ProductionPlan();
        BeanUtils.copyProperties(req, plan);
        // 自动生成排产单号，前缀 PP
        plan.setPlanNo("PP" + IdUtil.getSnowflake(1, 1).nextIdStr());
        // orderId 可以为空，因为现在使用 orderNo
        productionPlanMapper.insert(plan);
        return Result.success("创建成功");
    }

    /**
     * update 方法
     */
    public Result<String> update(ProductionPlanSaveReq req) {
        if (req.getOrderNo() != null && !req.getOrderNo().isEmpty()) {
            long orderExists = productionPlanMapper.checkOrderExists(req.getOrderNo());
            if (orderExists == 0) {
                return Result.error("单号校验失败：系统内不存在该订单号 (" + req.getOrderNo() + ")");
            }
        }

        ProductionPlan plan = new ProductionPlan();
        BeanUtils.copyProperties(req, plan);
        productionPlanMapper.update(plan);
        return Result.success("更新成功");
    }

    /**
     * delete 方法
     */
    public Result<String> delete(Long id) {
        productionPlanMapper.delete(id);
        return Result.success("删除成功");
    }

    /**
     * get 方法
     */
    public Result<ProductionPlan> get(Long id) {
        return Result.success(productionPlanMapper.getById(id));
    }
}
