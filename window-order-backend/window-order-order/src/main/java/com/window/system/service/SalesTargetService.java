package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.SalesTargetMapper;
import com.window.system.model.dto.SalesTargetResp;
import com.window.system.model.entity.SalesTarget;
import com.window.system.model.req.SalesTargetReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
/**
 * SalesTargetService 服务类/接口
 */
public class SalesTargetService {

    @Autowired
    private SalesTargetMapper salesTargetMapper;

    @Transactional(rollbackFor = Exception.class)
    /**
     * setTarget 方法
     */
    public Result<String> setTarget(SalesTargetReq req) {
        SalesTarget existing = salesTargetMapper.getBySalespersonAndMonth(req.getSalespersonId(), req.getTargetMonth());
        if (existing != null) {
            existing.setTargetAmount(req.getTargetAmount());
            existing.setUpdateBy(req.getCurrentUserId());
            salesTargetMapper.update(existing);
        } else {
            SalesTarget target = new SalesTarget();
            BeanUtils.copyProperties(req, target);
            target.setCreateBy(req.getCurrentUserId());
            target.setUpdateBy(req.getCurrentUserId());
            salesTargetMapper.insert(target);
        }
        return Result.success("设置成功");
    }

    /**
     * list 方法
     */
    public Result<List<SalesTargetResp>> list(String month, Long salespersonId) {
        List<SalesTargetResp> list = salesTargetMapper.list(month, salespersonId);
        for (SalesTargetResp resp : list) {
            if (resp.getTargetAmount() != null && resp.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rate = resp.getActualAmount()
                        .divide(resp.getTargetAmount(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);
                resp.setCompletionRate(rate);
            } else {
                resp.setCompletionRate(BigDecimal.ZERO);
            }
        }
        return Result.success(list);
    }
}
