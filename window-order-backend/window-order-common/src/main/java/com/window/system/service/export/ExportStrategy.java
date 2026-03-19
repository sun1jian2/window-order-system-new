package com.window.system.service.export;

import java.io.File;

/**
 * ExportStrategy 服务类/接口
 */
public interface ExportStrategy {
    /**
     * 获取策略类型
     */
    String getType();

    /**
     * 执行导出逻辑
     * @param params JSON格式的查询参数
     * @param taskId 任务ID
     * @return 导出的临时文件
     */
    File export(String params, Long taskId) throws Exception;
}
