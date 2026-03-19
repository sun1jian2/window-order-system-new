package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.config.MinioConfig;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.model.entity.SysExportTask;
import com.window.system.security.AuthUser;
import com.window.system.service.export.ExportStrategy;
import com.window.system.service.export.ExportStrategyFactory;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Service
@Slf4j
/**
 * SysExportTaskService 服务类/接口
 */
public class SysExportTaskService {

    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private ExportStrategyFactory exportStrategyFactory;

    @Autowired
    @Lazy
    private SysExportTaskService self;

    public com.window.system.model.dto.PageResponse<SysExportTask> list(com.window.system.model.req.ExportTaskListReq req) {
        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        // If admin, can see all
        if ("ADMIN".equalsIgnoreCase(user.getRole()) || "admin".equalsIgnoreCase(user.getUsername())) {
            userId = null;
        }
        
        long total = sysExportTaskMapper.count(req, userId);
        List<SysExportTask> list = sysExportTaskMapper.list(req, userId);
        
        return com.window.system.model.dto.PageResponse.of(list, total);
    }


    /**
     * createTask 方法
     */
    public Long createTask(String taskName, String exportType, String exportParams) {
        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysExportTask task = new SysExportTask();
        task.setTaskName(taskName);
        task.setExportType(exportType);
        task.setExportParams(exportParams);
        task.setStatus("PENDING");
        task.setCreateBy(user.getId());
        sysExportTaskMapper.insert(task);

        // Trigger async execution immediately using self-proxy
        if (exportType != null) {
            self.executeTask(task.getId(), exportType, exportParams);
        }

        return task.getId();
    }

    @Async("taskExecutor")
    /**
     * executeTask 方法
     */
    public void executeTask(Long taskId, String exportType, String exportParams) {
        log.info("executeTask start {}，exportType {}", taskId, exportType);
        ExportStrategy strategy = exportStrategyFactory.getStrategy(exportType);
        if (strategy != null) {
            this.executeExport(taskId, () -> {
                try {
                    return strategy.export(exportParams, taskId);
                } catch (Exception e) {
                    throw new RuntimeException("Export failed: " + e.getMessage(), e);
                }
            });
        } else {
            log.error("Unknown export type: {}", exportType);
            // Mark task as failed
            SysExportTask task = new SysExportTask();
            task.setId(taskId);
            task.setStatus("FAILED");
            task.setErrorMsg("Unknown export type: " + exportType);
            task.setFinishTime(LocalDateTime.now());
            sysExportTaskMapper.update(task);
        }
    }

    @Async("taskExecutor")
    /**
     * executeExport 方法
     */
    public void executeExport(Long taskId, Supplier<File> exportLogic) {
        log.info("executeExport start {}", taskId);

        SysExportTask task = new SysExportTask();
        task.setId(taskId);
        task.setStatus("PROCESSING");
        sysExportTaskMapper.update(task);

        try {
            File file = exportLogic.get();
            if (file == null || !file.exists()) {
                /**
                 * RuntimeException 方法
                 */
                throw new RuntimeException("Export file not found or generation failed");
            }

            // Upload to MinIO
            String dateDir = LocalDate.now().toString();
            String originalFilename = file.getName();
            // Use original filename instead of UUID to keep meaningful names in MinIO
            String objectName = dateDir + "/" + originalFilename;

            try (FileInputStream fis = new FileInputStream(file)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .object(objectName)
                                .stream(fis, file.length(), -1)
                                .contentType("application/vnd.ms-excel")
                                .build());
            }

            String url = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;

            task.setStatus("COMPLETED");
            task.setFileUrl(url);
            task.setFileName(originalFilename);
            task.setFinishTime(LocalDateTime.now());
            sysExportTaskMapper.update(task);

            // Cleanup local file
            try {
                file.delete();
            } catch (Exception ignored) {
            }

        } catch (Exception e) {
            log.error("Export failed for task " + taskId, e);
            task.setStatus("FAILED");
            task.setErrorMsg(e.getMessage());
            task.setFinishTime(LocalDateTime.now());
            sysExportTaskMapper.update(task);
        }
    }
}
