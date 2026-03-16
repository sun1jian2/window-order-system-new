<template>
  <div class="dashboard-page">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="hero-content">
        <div class="hero-greeting">
          <h1 class="greeting-title">
            Hi, {{ displayName }} 
            <span class="greeting-emoji">👋</span>
          </h1>
          <p class="greeting-subtitle">
            今天是 {{ todayText }}，祝你工作愉快！
          </p>
        </div>
        <div class="hero-weather">
          <!-- 装饰性元素 -->
          <div class="weather-badge">
            <el-icon><Sunny /></el-icon>
            <span>晴朗</span>
          </div>
        </div>
      </div>
      <div class="hero-actions">
        <el-button type="primary" size="large" class="action-btn" @click="go('/orders')" round>
          <el-icon><List /></el-icon>
          管理订单
        </el-button>
        <el-button v-if="isAdmin" size="large" class="action-btn glass-btn" @click="go('/users')" round>
          <el-icon><User /></el-icon>
          账号管理
        </el-button>
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton animated :rows="10" />
    </div>

    <div v-else class="dashboard-content">
      <!-- KPI Cards -->
      <div class="kpi-grid">
        <!-- 待处理订单 -->
        <div class="kpi-card orange-theme">
          <div class="card-icon-wrapper">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="card-info">
            <div class="card-label">待处理订单</div>
            <div class="card-value">{{ formatNumber(stats.pendingOrders) }}</div>
            <div class="card-trend">
              <span>需要跟进</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon><Timer /></el-icon>
          </div>
        </div>

        <!-- 已完成订单 -->
        <div class="kpi-card green-theme">
          <div class="card-icon-wrapper">
            <el-icon><CircleCheckFilled /></el-icon>
          </div>
          <div class="card-info">
            <div class="card-label">已完成订单</div>
            <div class="card-value">{{ formatNumber(stats.finishedOrders) }}</div>
            <div class="card-trend">
              <span>交付成功</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon><CircleCheckFilled /></el-icon>
          </div>
        </div>

        <!-- 本月销售额 -->
        <div class="kpi-card purple-theme">
          <div class="card-icon-wrapper">
            <el-icon><Money /></el-icon>
          </div>
          <div class="card-info">
            <div class="card-label">本月销售额</div>
            <div class="card-value money">{{ formatMoney(stats.monthlySales) }}</div>
            <div class="card-trend">
              <span>本月业绩</span>
              <el-icon><TrendCharts /></el-icon>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon><Money /></el-icon>
          </div>
        </div>

        <!-- 总订单数 -->
        <div class="kpi-card blue-theme">
          <div class="card-icon-wrapper">
            <el-icon><DataLine /></el-icon>
          </div>
          <div class="card-info">
            <div class="card-label">总订单数</div>
            <div class="card-value">{{ formatNumber(stats.totalOrders) }}</div>
            <div class="card-trend">
              <span>历史累计</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon><DataLine /></el-icon>
          </div>
        </div>
      </div>

      <!-- Admin Stats Section -->
      <div v-if="isAdmin" class="admin-stats-grid">
        <div class="admin-stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ formatNumber(stats.totalCustomers) }}</div>
            <div class="stat-label">总客户数</div>
          </div>
          <div class="stat-icon blue">
            <el-icon><UserFilled /></el-icon>
          </div>
        </div>
        <div class="admin-stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ formatNumber(stats.totalUsers) }}</div>
            <div class="stat-label">系统账号</div>
          </div>
          <div class="stat-icon purple">
            <el-icon><Avatar /></el-icon>
          </div>
        </div>
        <div class="admin-stat-card">
          <div class="stat-content">
            <div class="stat-value money">{{ formatMoney(stats.todaySales) }}</div>
            <div class="stat-label">今日销售额</div>
          </div>
          <div class="stat-icon orange">
            <el-icon><TrendCharts /></el-icon>
          </div>
        </div>
      </div>

      <!-- Charts Section -->
      <div class="charts-grid">
        <!-- Line Chart -->
        <div class="chart-card main-chart">
          <div class="card-header">
            <div class="header-title">
              <el-icon><DataAnalysis /></el-icon>
              <span>近7日订单趋势</span>
            </div>
            <el-tag size="small" effect="plain" round>最近一周</el-tag>
          </div>
          <div class="chart-body">
            <v-chart class="echart-instance" :option="lineOption" autoresize />
          </div>
        </div>

        <!-- Pie Chart -->
        <div class="chart-card sub-chart">
          <div class="card-header">
            <div class="header-title">
              <el-icon><PieChartIcon /></el-icon>
              <span>品牌分布</span>
            </div>
          </div>
          <div class="chart-body">
            <v-chart class="echart-instance" :option="pieOption" autoresize />
          </div>
        </div>

        <!-- Status Distribution Chart -->
        <div class="chart-card sub-chart">
          <div class="card-header">
            <div class="header-title">
              <el-icon><PieChartIcon /></el-icon>
              <span>订单状态</span>
            </div>
          </div>
          <div class="chart-body">
            <v-chart class="echart-instance" :option="statusPieOption" autoresize />
          </div>
        </div>
      </div>

      <!-- Bottom Section -->
      <div class="bottom-grid">
        <!-- Ranking Table -->
        <div class="chart-card rank-card">
          <div class="card-header">
            <div class="header-title">
              <el-icon><Trophy /></el-icon>
              <span>本月销售龙虎榜</span>
            </div>
          </div>
          <div class="rank-list">
             <el-table :data="stats.salesPerformance" :show-header="true" style="width: 100%">
                <el-table-column type="index" label="排名" width="80">
                  <template #default="scope">
                    <div class="rank-badge" :class="'rank-' + (scope.$index + 1)">
                      {{ scope.$index + 1 }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="销售员" />
                <el-table-column prop="orderCount" label="订单数" align="center" />
                <el-table-column prop="amount" label="销售额" align="right">
                  <template #default="scope">
                    <span class="money-font">{{ formatMoney(scope.row.amount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="占比" width="180">
                  <template #default="scope">
                     <el-progress 
                        :percentage="salesPercent(scope.row.amount)" 
                        :stroke-width="8" 
                        :color="getProgressColor(scope.$index)"
                        :show-text="false"
                      />
                  </template>
                </el-table-column>
             </el-table>
          </div>
        </div>

        <!-- Recent Activities & Quick Actions -->
        <div class="right-column">
          <!-- 日期筛选器 -->
          <div class="chart-card filter-card">
            <div class="card-header" style="margin-bottom: 12px;">
              <div class="header-title" style="font-size: 16px;">
                <el-icon><Calendar /></el-icon>
                <span>统计周期</span>
              </div>
            </div>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              size="default"
              value-format="YYYY-MM-DD"
              @change="handleDateChange"
              style="width: 100%"
              :clearable="false"
            />
          </div>

          <!-- 筛选销售额 (仅在选择日期时显示) -->
          <div v-if="dateRange && dateRange.length === 2" class="kpi-card pink-theme" style="margin-bottom: 0;">
            <div class="card-icon-wrapper">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-label">筛选期销售额</div>
              <div class="card-value money">{{ formatMoney(stats.customPeriodSales) }}</div>
              <div class="card-trend">
                <span>{{ dateRange[0] }} 至 {{ dateRange[1] }}</span>
              </div>
            </div>
            <div class="card-bg-icon">
              <el-icon><Calendar /></el-icon>
            </div>
          </div>

          <!-- Recent Activities -->
          <div class="chart-card activity-card">
            <div class="card-header">
              <div class="header-title">
                <el-icon><Timer /></el-icon>
                <span>最新动态</span>
              </div>
            </div>
            <div class="activity-list">
              <div v-for="(item, index) in stats.recentActivities" :key="index" class="activity-item">
                <div class="activity-icon">
                  <el-icon><User /></el-icon>
                </div>
                <div class="activity-content">
                  <div class="activity-text">
                    <span class="user-name">{{ item.username }}</span>
                    <span class="action-text">{{ item.operation }}</span>
                  </div>
                  <div class="activity-time">{{ formatTime(item.createTime) }}</div>
                </div>
              </div>
              <div v-if="!stats.recentActivities || stats.recentActivities.length === 0" class="empty-text">
                暂无动态
              </div>
            </div>
          </div>

          <!-- Quick Actions -->
          <div class="chart-card quick-card">
            <div class="card-header">
              <div class="header-title">
                <el-icon><Lightning /></el-icon>
                <span>快捷入口</span>
              </div>
            </div>
            <div class="quick-actions-grid">
               <div class="quick-btn" @click="go('/orders')">
                  <div class="quick-icon-box blue">
                     <el-icon><DocumentAdd /></el-icon>
                  </div>
                  <span>新增订单</span>
               </div>
               <div class="quick-btn" @click="go('/customers')">
                  <div class="quick-icon-box green">
                     <el-icon><UserFilled /></el-icon>
                  </div>
                  <span>客户档案</span>
               </div>
               <div class="quick-btn" @click="go('/sales-targets')" v-if="isAdmin">
                  <div class="quick-icon-box purple">
                     <el-icon><Aim /></el-icon>
                  </div>
                  <span>设定目标</span>
               </div>
               <div class="quick-btn" @click="go('/export-center')">
                  <div class="quick-icon-box orange">
                     <el-icon><Download /></el-icon>
                  </div>
                  <span>导出中心</span>
               </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getDashboardStats } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart, LineChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
} from 'echarts/components'
import {
  Sunny, List, User, Timer, CircleCheckFilled, Money, DataLine,
  ArrowRight, TrendCharts, DataAnalysis, PieChart as PieChartIcon,
  Trophy, Lightning, DocumentAdd, UserFilled, Aim, Download, Avatar, Calendar
} from '@element-plus/icons-vue'

use([
  CanvasRenderer,
  BarChart,
  PieChart,
  LineChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
])

const userStore = useUserStore()
const router = useRouter()
const loading = ref(true)
const dateRange = ref([])

const displayName = computed(() => {
  const user = userStore.currentUser || {}
  return user.realName || user.username || 'User'
})

const todayText = computed(() => {
  const date = new Date()
  const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }
  return date.toLocaleDateString('zh-CN', options)
})

const isAdmin = computed(() => userStore.currentUser?.role === 'ADMIN')

const go = (path) => {
  router.push(path)
}

const formatNumber = (val) => {
  if (!val) return '0'
  return Number(val).toLocaleString()
}

const formatMoney = (val) => {
  if (!val) return '¥ 0.00'
  return '¥ ' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const stats = ref({
    pendingOrders: 0,
    finishedOrders: 0,
    totalOrders: 0,
    monthlySales: 0,
    monthlyPaidAmount: 0,
    customPeriodSales: 0,
    orderTrend: [],
    brandDistribution: [],
    statusDistribution: [],
    recentActivities: [],
    salesPerformance: [],
    totalCustomers: 0,
    totalUsers: 0,
    todaySales: 0
})

const fetchStats = async () => {
  try {
    loading.value = true
    const params = {}
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    const res = await getDashboardStats(params)
    if (res.code === 200) {
      stats.value = res.data
      initCharts()
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleDateChange = () => {
  fetchStats()
}

const lineOption = ref({})
const pieOption = ref({})
const statusPieOption = ref({})

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now - date
  
  // Less than 1 minute
  if (diff < 60000) return '刚刚'
  // Less than 1 hour
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  // Less than 24 hours
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  // Yesterday
  if (diff < 172800000) return '昨天'
  
  return date.toLocaleDateString()
}

const salesMaxAmount = computed(() => {
  const amounts = (stats.value.salesPerformance || []).map(item => Number(item.amount || 0))
  return Math.max(1, ...amounts) // 避免除以0
})

const salesPercent = (amount) => {
  const val = Number(amount || 0)
  return Math.min(100, Math.round((val / salesMaxAmount.value) * 100))
}

const getProgressColor = (index) => {
  const colors = ['#f59e0b', '#8b5cf6', '#3b82f6', '#10b981']
  return colors[index] || '#9ca3af'
}

const initCharts = () => {
  // 折线图配置
  const dates = stats.value.orderTrend.map(i => i.date)
  const values = stats.value.orderTrend.map(i => i.count)
  
  lineOption.value = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#e5e7eb',
      textStyle: { color: '#374151' },
      axisPointer: {
        lineStyle: {
          color: '#6366f1',
          width: 2,
          type: 'dashed'
        }
      }
    },
    grid: { left: '2%', right: '2%', top: '10%', bottom: '5%', containLabel: true },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#9ca3af', margin: 15 }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { type: 'dashed', color: '#f3f4f6' } },
      axisLabel: { show: false }
    },
    series: [{
      data: values,
      type: 'line',
      smooth: true,
      symbolSize: 8,
      itemStyle: { color: '#6366f1', borderWidth: 2, borderColor: '#fff' },
      lineStyle: { width: 4, shadowColor: 'rgba(99, 102, 241, 0.3)', shadowBlur: 10 },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(99, 102, 241, 0.2)' },
            { offset: 1, color: 'rgba(99, 102, 241, 0)' }
          ]
        }
      }
    }]
  }

  // 饼图配置
  pieOption.value = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '0%', icon: 'circle', itemWidth: 8, itemHeight: 8 },
    series: [{
      name: '品牌分布',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '45%'],
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      data: stats.value.brandDistribution,
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.2)' }
      }
    }]
  }

  // 状态分布图配置
  statusPieOption.value = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '0%', icon: 'circle', itemWidth: 8, itemHeight: 8 },
    series: [{
      name: '订单状态',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '45%'],
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      data: stats.value.statusDistribution,
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.2)' }
      }
    }]
  }
}

const setDefaultDateRange = () => {
  const end = new Date()
  const start = new Date()
  start.setFullYear(start.getFullYear() - 1) // Default to last 1 year
  
  const formatDate = (d) => {
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }
  
  dateRange.value = [formatDate(start), formatDate(end)]
}

onMounted(() => {
  setDefaultDateRange()
  fetchStats()
})
</script>

<style scoped>
.dashboard-page {
  padding: 24px;
  min-height: 100vh;
  /* 使用混合模式叠加图片和渐变 */
  background: 
    /* 顶层：微弱的渐变光晕，增加科技感 */
    radial-gradient(circle at 10% 20%, rgba(99, 102, 241, 0.1) 0%, transparent 40%),
    radial-gradient(circle at 90% 80%, rgba(16, 185, 129, 0.1) 0%, transparent 40%),
    /* 中层：白色遮罩，确保内容可读性 */
    linear-gradient(135deg, rgba(248, 250, 252, 0.95) 0%, rgba(241, 245, 249, 0.85) 100%),
    /* 底层：高质量建筑/办公背景图 */
    url('https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&q=80&w=2069');
  
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}

/* Hero Section */
.hero-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.greeting-title {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.greeting-emoji {
  animation: wave 2.5s infinite;
  display: inline-block;
}

@keyframes wave {
  0% { transform: rotate(0deg); }
  10% { transform: rotate(14deg); }
  20% { transform: rotate(-8deg); }
  30% { transform: rotate(14deg); }
  40% { transform: rotate(-4deg); }
  50% { transform: rotate(10deg); }
  60% { transform: rotate(0deg); }
  100% { transform: rotate(0deg); }
}

.greeting-subtitle {
  color: #64748b;
  margin: 8px 0 0;
  font-size: 16px;
}

.hero-actions {
  display: flex;
  gap: 12px;
}

.glass-btn {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(4px);
  border: 1px solid rgba(226, 232, 240, 0.8);
  color: #475569;
}

.glass-btn:hover {
  background: white;
  color: #6366f1;
  border-color: #6366f1;
}

/* KPI Grid */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

/* Admin Stats Grid */
.admin-stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 24px;
}

@media (max-width: 768px) {
  .admin-stats-grid {
    grid-template-columns: 1fr;
  }
}

.admin-stat-card {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 12px rgba(148, 163, 184, 0.05);
  border: 1px solid rgba(241, 245, 249, 1);
  transition: transform 0.2s;
}

.admin-stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(148, 163, 184, 0.1);
}

.admin-stat-card .stat-value {
  font-size: 28px;
  font-weight: 800;
  color: #1e293b;
  line-height: 1.2;
}

.admin-stat-card .stat-value.money {
  font-family: 'Inter', sans-serif;
  color: #0f172a;
}

.admin-stat-card .stat-label {
  color: #64748b;
  font-size: 14px;
  margin-top: 4px;
}

.admin-stat-card .stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.admin-stat-card .stat-icon.blue { background: #eff6ff; color: #3b82f6; }
.admin-stat-card .stat-icon.purple { background: #f5f3ff; color: #8b5cf6; }
.admin-stat-card .stat-icon.orange { background: #fff7ed; color: #f97316; }

.kpi-card {
  background: white;
  border-radius: 20px;
  padding: 24px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(148, 163, 184, 0.08);
  transition: all 0.3s ease;
  border: 1px solid rgba(241, 245, 249, 1);
  display: flex;
  align-items: center;
  gap: 20px;
}

.kpi-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 30px rgba(148, 163, 184, 0.15);
}

.card-icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  flex-shrink: 0;
}

.card-info {
  flex: 1;
  z-index: 2;
}

.card-label {
  color: #64748b;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
}

.card-value {
  color: #1e293b;
  font-size: 36px;
  font-weight: 800;
  line-height: 1.2;
  letter-spacing: -0.5px;
}

.card-value.money {
  color: #0f172a;
  font-family: 'Inter', sans-serif;
}

.card-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  margin-top: 8px;
  opacity: 0.8;
  font-weight: 500;
}

.card-bg-icon {
  position: absolute;
  right: -20px;
  bottom: -20px;
  font-size: 120px;
  opacity: 0.06;
  transform: rotate(-15deg);
  pointer-events: none;
  z-index: 1;
}

/* Themes */
.orange-theme .card-icon-wrapper { background: #fff7ed; color: #f97316; }
.orange-theme .card-trend { color: #f97316; }

.green-theme .card-icon-wrapper { background: #f0fdf4; color: #10b981; }
.green-theme .card-trend { color: #10b981; }

.purple-theme .card-icon-wrapper { background: #f5f3ff; color: #8b5cf6; }
.purple-theme .card-trend { color: #8b5cf6; }

.blue-theme .card-icon-wrapper { background: #eff6ff; color: #3b82f6; }
.blue-theme .card-trend { color: #3b82f6; }

.pink-theme .card-icon-wrapper { background: #fdf2f8; color: #db2777; }
.pink-theme .card-trend { color: #db2777; font-size: 12px; }

/* Filter Card */
.filter-card {
  padding: 20px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(148, 163, 184, 0.08);
  border: 1px solid rgba(241, 245, 249, 1);
}

.filter-card :deep(.el-date-editor) {
  --el-date-editor-width: 100%;
  box-shadow: none;
  background-color: #f8fafc;
  border: 1px solid transparent;
  transition: all 0.3s;
  padding: 18px 12px;
  border-radius: 12px;
}

.filter-card :deep(.el-date-editor:hover) {
  background-color: #fff;
  border-color: #e2e8f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.filter-card :deep(.el-date-editor.is-active) {
  background-color: #fff;
  border-color: #6366f1;
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.1);
}

.filter-card :deep(.el-range-separator) {
  color: #94a3b8;
}

/* Charts Grid */
.charts-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

@media (max-width: 1024px) {
  .charts-grid { grid-template-columns: 1fr; }
}

.chart-card {
  background: white;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(148, 163, 184, 0.08);
  border: 1px solid rgba(241, 245, 249, 1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
}

.chart-body {
  height: 320px;
}

/* Bottom Grid */
.bottom-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

@media (max-width: 1024px) {
  .bottom-grid { grid-template-columns: 1fr; }
}

.right-column {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Activity List */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 280px;
  overflow-y: auto;
  padding-right: 4px;
}

.activity-item {
  display: flex;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f1f5f9;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #f1f5f9;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
}

.activity-text {
  font-size: 14px;
  color: #334155;
  line-height: 1.4;
  margin-bottom: 4px;
}

.user-name {
  font-weight: 600;
  margin-right: 6px;
}

.activity-time {
  font-size: 12px;
  color: #94a3b8;
}

.empty-text {
  text-align: center;
  color: #94a3b8;
  padding: 20px 0;
}

/* Rank Table */
.rank-badge {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #f1f5f9;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
}

.rank-1 { background: #fff7ed; color: #f59e0b; border: 1px solid #fcd34d; }
.rank-2 { background: #f8fafc; color: #94a3b8; border: 1px solid #cbd5e1; }
.rank-3 { background: #fff1f2; color: #f43f5e; border: 1px solid #fda4af; }

.money-font {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  color: #334155;
}

/* Quick Actions */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.quick-btn {
  background: #f8fafc;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
}

.quick-btn:hover {
  background: white;
  border-color: #e2e8f0;
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.quick-icon-box {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
}

.quick-icon-box.blue { background: #eff6ff; color: #3b82f6; }
.quick-icon-box.green { background: #f0fdf4; color: #10b981; }
.quick-icon-box.purple { background: #f5f3ff; color: #8b5cf6; }
.quick-icon-box.orange { background: #fff7ed; color: #f97316; }

.quick-btn span {
  font-size: 16px;
  font-weight: 600;
  color: #475569;
}
</style>
