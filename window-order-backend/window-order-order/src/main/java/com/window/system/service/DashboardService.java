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

    public Result<DashboardStats> getStats(Long userId, String role) {
        DashboardStats stats = new DashboardStats();
        
        stats.setPendingOrders(windowOrderMapper.countPendingOrders(userId, role));
        stats.setFinishedOrders(windowOrderMapper.countFinishedOrders(userId, role));
        stats.setTotalOrders(windowOrderMapper.countTotalOrders(userId, role));
        
        BigDecimal monthlySales = windowOrderMapper.sumMonthlySales(userId, role);
        stats.setMonthlySales(monthlySales != null ? monthlySales.toPlainString() : "0");
        
        BigDecimal monthlyPaid = windowOrderMapper.sumMonthlyPaidAmount(userId, role);
        stats.setMonthlyPaidAmount(monthlyPaid != null ? monthlyPaid.toPlainString() : "0");
        
        stats.setOrderTrend(windowOrderMapper.getOrderTrend(userId, role));
        stats.setBrandDistribution(windowOrderMapper.getBrandDistribution(userId, role));
        stats.setSalesPerformance(windowOrderMapper.getMonthlySalesPerformance(userId, role));
        
        if ("ADMIN".equals(role)) {
            stats.setTotalCustomers(windowOrderMapper.countTotalCustomers());
            stats.setTotalUsers(windowOrderMapper.countTotalUsers());
            BigDecimal todaySales = windowOrderMapper.sumTodaySales(null, "ADMIN");
            stats.setTodaySales(todaySales != null ? todaySales.toPlainString() : "0");
        }
        
        return Result.success(stats);
    }
}
