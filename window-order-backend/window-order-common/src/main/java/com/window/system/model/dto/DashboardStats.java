package com.window.system.model.dto;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
/**
 * DashboardStats 实体/请求/响应类
 */
public class DashboardStats {
    private Long pendingOrders; // Waiting Install or Producing
    private Long finishedOrders;
    private Long totalOrders;
    private String monthlySales;
    private String monthlyPaidAmount;
    private String customPeriodSales; // New field for filtered sales amount
    private Long customPeriodOrderCount; // New field for filtered order count
    
    // Financial and Pending
    private String totalReceivable; // 总应收
    private String totalCollected; // 总已收
    private String totalOutstanding; // 总欠款
    private Long unpaidOrdersCount; // 待回款订单数
    
    // Charts
    private List<OrderTrendDto> orderTrend; // date, count
    private List<NameValueDto> brandDistribution; // name, value
    private List<NameValueDto> statusDistribution; // name, value
    private List<NameValueDto> windowTypeDistribution; // name, value
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
