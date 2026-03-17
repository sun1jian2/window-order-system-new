package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.dto.DashboardStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    @Autowired
    private WindowOrderMapper windowOrderMapper;

    public Result<DashboardStats> getStats(Long userId, String role, String startDate, String endDate) {
        DashboardStats stats = new DashboardStats();
        
        stats.setPendingOrders(windowOrderMapper.countPendingOrders(userId, role));
        stats.setFinishedOrders(windowOrderMapper.countFinishedOrders(userId, role));
        stats.setTotalOrders(windowOrderMapper.countTotalOrders(userId, role));
        stats.setUnpaidOrdersCount(windowOrderMapper.countUnpaidOrders(userId, role));
        
        BigDecimal monthlySales = windowOrderMapper.sumMonthlySales(userId, role);
        stats.setMonthlySales(monthlySales != null ? monthlySales.toPlainString() : "0");
        
        BigDecimal monthlyPaid = windowOrderMapper.sumMonthlyPaidAmount(userId, role);
        stats.setMonthlyPaidAmount(monthlyPaid != null ? monthlyPaid.toPlainString() : "0");
        
        BigDecimal totalReceivable = windowOrderMapper.sumTotalReceivable(userId, role);
        BigDecimal totalCollected = windowOrderMapper.sumTotalCollected(userId, role);
        stats.setTotalReceivable(totalReceivable != null ? totalReceivable.toPlainString() : "0");
        stats.setTotalCollected(totalCollected != null ? totalCollected.toPlainString() : "0");
        
        BigDecimal totalOutstanding = (totalReceivable != null ? totalReceivable : BigDecimal.ZERO)
                .subtract(totalCollected != null ? totalCollected : BigDecimal.ZERO);
        stats.setTotalOutstanding(totalOutstanding.toPlainString());
        
        // Calculate sales for custom period if provided
        if (startDate != null && endDate != null) {
            BigDecimal customSales = windowOrderMapper.sumSalesByDateRange(userId, role, startDate, endDate);
            stats.setCustomPeriodSales(customSales != null ? customSales.toPlainString() : "0");
            
            long customOrderCount = windowOrderMapper.countOrdersByDateRange(userId, role, startDate, endDate);
            stats.setCustomPeriodOrderCount(customOrderCount);
        }
        
        stats.setOrderTrend(windowOrderMapper.getOrderTrend(userId, role));
        stats.setBrandDistribution(windowOrderMapper.getBrandDistribution(userId, role));
        stats.setStatusDistribution(windowOrderMapper.getStatusDistribution(userId, role));
        stats.setWindowTypeDistribution(windowOrderMapper.getWindowTypeDistribution(userId, role));
        stats.setSalesPerformance(windowOrderMapper.getMonthlySalesPerformance(userId, role));
        stats.setRecentActivities(windowOrderMapper.getRecentActivities());
        
        if ("ADMIN".equals(role)) {
            stats.setTotalCustomers(windowOrderMapper.countTotalCustomers());
            stats.setTotalUsers(windowOrderMapper.countTotalUsers());
            BigDecimal todaySales = windowOrderMapper.sumTodaySales(null, "ADMIN");
            stats.setTodaySales(todaySales != null ? todaySales.toPlainString() : "0");
        }
        
        return Result.success(stats);
    }
}
