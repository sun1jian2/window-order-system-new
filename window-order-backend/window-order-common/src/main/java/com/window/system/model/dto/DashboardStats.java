package com.window.system.model.dto;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DashboardStats {
    private Long pendingOrders; // Waiting Install or Producing
    private Long finishedOrders;
    private Long totalOrders;
    private String monthlySales;
    private String monthlyPaidAmount;
    
    // Charts
    private List<Map<String, Object>> orderTrend; // date, count
    private List<Map<String, Object>> brandDistribution; // name, value
    private List<Map<String, Object>> statusDistribution; // name, value
    private List<Map<String, Object>> recentActivities; // log or order info
    
    // Sales Performance
    private List<Map<String, Object>> salesPerformance; // name, amount, orderCount
    
    // Admin specific
    private Long totalCustomers;
    private Long totalUsers;
    private String todaySales;
    
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
