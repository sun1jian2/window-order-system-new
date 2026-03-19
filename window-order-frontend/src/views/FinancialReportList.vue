<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" class="search-form">
          <el-form-item label="报表类型">
            <el-radio-group v-model="reportType" @change="fetchData">
              <el-radio-button label="MONTH">月报</el-radio-button>
              <el-radio-button label="YEAR">年报</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item class="search-actions">
            <el-button type="primary" @click="fetchData" :icon="Refresh">刷新</el-button>
            <el-button type="success" @click="exportData" :icon="Download">导出报表</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card revenue">
            <div class="stat-title">期间总收入</div>
            <div class="stat-value">¥ {{ summary.revenue.toFixed(2) }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card cost">
            <div class="stat-title">期间总成本</div>
            <div class="stat-value">¥ {{ summary.cost.toFixed(2) }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card profit">
            <div class="stat-title">期间总毛利</div>
            <div class="stat-value">¥ {{ summary.profit.toFixed(2) }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card rate">
            <div class="stat-title">平均毛利率</div>
            <div class="stat-value">{{ summary.rate.toFixed(2) }}%</div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="table-card" shadow="hover">
        <div class="form-section-title" style="margin-bottom: 15px; font-weight: bold; border-left: 4px solid #409EFF; padding-left: 10px;">财务报表明细</div>
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading" :header-cell-style="{background:'#f7f9fc', color:'#606266'}">
          <el-table-column prop="period" label="统计期间" width="120" align="center" />
          <el-table-column prop="totalRevenue" label="营业收入(元)" align="right">
             <template #default="scope"><span style="color: #409EFF; font-weight: bold;">¥ {{ scope.row.totalRevenue }}</span></template>
          </el-table-column>
          <el-table-column prop="totalMaterialCost" label="材料成本(元)" align="right" />
          <el-table-column prop="totalLaborCost" label="人工成本(元)" align="right" />
          <el-table-column prop="totalOtherCost" label="其他成本(元)" align="right" />
          <el-table-column prop="totalCost" label="总成本(元)" align="right">
             <template #default="scope"><span style="color: #E6A23C; font-weight: bold;">¥ {{ scope.row.totalCost }}</span></template>
          </el-table-column>
          <el-table-column prop="grossProfit" label="毛利润(元)" align="right">
             <template #default="scope">
               <span :style="{ color: scope.row.grossProfit >= 0 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                 ¥ {{ scope.row.grossProfit }}
               </span>
             </template>
          </el-table-column>
          <el-table-column prop="grossProfitRate" label="毛利率" width="100" align="center">
            <template #default="scope">
              <span :style="{ color: scope.row.grossProfitRate >= 30 ? '#67C23A' : '#E6A23C', fontWeight: 'bold' }">
                 {{ scope.row.grossProfitRate }}%
               </span>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { Refresh, Download } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'

const loading = ref(false)
const tableData = ref([])
const reportType = ref('MONTH')

const summary = reactive({
  revenue: 0,
  cost: 0,
  profit: 0,
  rate: 0
})

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get(`/inventory/report/list?type=${reportType.value}`)
    if (res.code === 200) {
      tableData.value = res.data || []
      calculateSummary()
    }
  } catch (error) {
    ElMessage.error('获取报表数据失败')
  } finally {
    loading.value = false
  }
}

const calculateSummary = () => {
  let rev = 0, cost = 0, prof = 0
  tableData.value.forEach(item => {
    rev += Number(item.totalRevenue || 0)
    cost += Number(item.totalCost || 0)
    prof += Number(item.grossProfit || 0)
  })
  summary.revenue = rev
  summary.cost = cost
  summary.profit = prof
  summary.rate = rev > 0 ? (prof / rev * 100) : 0
}

const exportData = () => {
  if (tableData.value.length === 0) {
    ElMessage.warning('暂无数据可导出')
    return
  }
  
  const exportData = tableData.value.map(item => ({
    '统计期间': item.period,
    '营业收入(元)': item.totalRevenue,
    '材料成本(元)': item.totalMaterialCost,
    '人工成本(元)': item.totalLaborCost,
    '其他成本(元)': item.totalOtherCost,
    '总成本(元)': item.totalCost,
    '毛利润(元)': item.grossProfit,
    '毛利率(%)': item.grossProfitRate
  }))
  
  const ws = XLSX.utils.json_to_sheet(exportData)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '财务报表')
  XLSX.writeFile(wb, `财务${reportType.value === 'MONTH' ? '月报' : '年报'}_${new Date().getTime()}.xlsx`)
}
</script>

<style scoped>
.app-container {
  min-height: 100%;
  background-color: transparent;
  display: flex;
  flex-direction: column;
}
.main-content {
  padding: 0;
  max-width: 100%;
  margin: 0;
  width: 100%;
}
.search-card {
  margin-bottom: 16px;
}
.stat-card {
  text-align: center;
  padding: 10px 0;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
}
.revenue .stat-value { color: #409EFF; }
.cost .stat-value { color: #E6A23C; }
.profit .stat-value { color: #67C23A; }
.rate .stat-value { color: #F56C6C; }
</style>
