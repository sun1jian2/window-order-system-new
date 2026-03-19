package com.window.system.service.export;

import com.window.system.service.export.ExportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
/**
 * ExportStrategyFactory 服务类/接口
 */
public class ExportStrategyFactory {

    private final Map<String, ExportStrategy> strategyMap;

    @Autowired
    /**
     * ExportStrategyFactory 方法
     */
    public ExportStrategyFactory(List<ExportStrategy> strategies) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(ExportStrategy::getType, Function.identity()));
    }

    /**
     * getStrategy 方法
     */
    public ExportStrategy getStrategy(String type) {
        return strategyMap.get(type);
    }
}
