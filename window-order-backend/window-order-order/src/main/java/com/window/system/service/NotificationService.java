package com.window.system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.window.system.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
/**
 * NotificationService 服务类/接口
 */
public class NotificationService {
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * notifyAdmins 方法
     */
    public void notifyAdmins(String title, String message, String type) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("title", title);
            payload.put("message", message);
            payload.put("type", type); // info, success, warning, error
            
            String json = objectMapper.writeValueAsString(payload);
            WebSocketServer.sendToAdmins(json);
        } catch (Exception e) {
            log.error("Failed to notify admins", e);
        }
    }
}
