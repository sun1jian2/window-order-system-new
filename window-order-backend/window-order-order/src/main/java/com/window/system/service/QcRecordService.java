package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.QcRecordMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.QcRecord;
import com.window.system.model.req.QcRecordListReq;
import com.window.system.model.req.QcRecordSaveReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
/**
 * QcRecordService 服务类
 */
public class QcRecordService {

    @Autowired
    private QcRecordMapper qcRecordMapper;

    /**
     * list 方法
     */
    public Result<PageResponse<QcRecord>> list(QcRecordListReq req) {
        long count = qcRecordMapper.countList(req);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        List<QcRecord> list = qcRecordMapper.list(req);
        return Result.success(PageResponse.of(list, count));
    }

    /**
     * create 方法
     */
    public Result<String> create(QcRecordSaveReq req) {
        QcRecord record = new QcRecord();
        BeanUtils.copyProperties(req, record);
        qcRecordMapper.insert(record);
        return Result.success("创建成功");
    }

    /**
     * update 方法
     */
    public Result<String> update(QcRecordSaveReq req) {
        QcRecord record = new QcRecord();
        BeanUtils.copyProperties(req, record);
        qcRecordMapper.update(record);
        return Result.success("更新成功");
    }

    /**
     * delete 方法
     */
    public Result<String> delete(Long id) {
        qcRecordMapper.delete(id);
        return Result.success("删除成功");
    }

    /**
     * get 方法
     */
    public Result<QcRecord> get(Long id) {
        return Result.success(qcRecordMapper.getById(id));
    }
}
