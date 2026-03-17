package com.window.system.model.dto;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardStats {
    private Long pendingOrders; // Waiting Install or Producing
    private Long finishedOrders;
    private Long totalOrders;
    private String monthlySales;
    private String monthlyPaidAmount;
    private String customPeriodSales; // New field for filtered sales amount
    private Long customPeriodOrderCount; // New field for filtered order count
    
    // Charts
    private List<OrderTrendDto> orderTrend; // date, count
    private List<NameValueDto> brandDistribution; // name, value
    private List<NameValueDto> statusDistribution; // name, value
    private List<RecentActivityDto> recentActivities; // log or order info
    
    // Sales Performance
    private List<SalesPerformanceDto> salesPerformance; // name, amount, orderCount
    
    // Admin specific
    private Long totalCustomers;
    private Long totalUsers;
    private String todaySales;
    
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
