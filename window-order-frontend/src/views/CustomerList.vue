<template>
  <div class="app-container">
    <div class="main-content">
      <!-- Search -->
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-form-item label="客户名">
            <el-input v-model="queryForm.name" placeholder="客户名" clearable />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="queryForm.phone" placeholder="电话" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
            <el-button @click="handleReset" :icon="Refresh">重置</el-button>
            <el-button type="warning" @click="handleExport" :icon="Download">导出</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- Table -->
      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="name" label="客户名" min-width="120" align="center" />
          <el-table-column prop="phone" label="电话" min-width="150" align="center" />
          <el-table-column prop="address" label="默认地址" min-width="200" show-overflow-tooltip align="center" />
          <el-table-column prop="orderCount" label="订单数" min-width="100" align="center" sortable />
          <el-table-column prop="totalSpent" label="总消费" min-width="120" align="right" sortable>
              <template #default="scope">¥ {{ scope.row.totalSpent }}</template>
          </el-table-column>
          <el-table-column prop="createTime" label="首次录入" min-width="160" align="center">
              <template #default="scope">{{ scope.row.createTime ? scope.row.createTime.replace('T', ' ') : '' }}</template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="100" align="center">
            <template #default="scope">
              <el-button link type="primary" @click="handleDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="queryForm.pageNo"
            v-model:page-size="queryForm.pageSize"
            :total="total"
            layout="total, prev, pager, next"
            @change="fetchData"
            background
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Refresh, Download } from '@element-plus/icons-vue'
import { listCustomers, exportCustomers } from '@/api/customer'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryForm = reactive({
    pageNo: 1,
    pageSize: 10,
    name: '',
    phone: ''
})

onMounted(() => {
    fetchData()
})

const fetchData = async () => {
    loading.value = true
    try {
        const res = await listCustomers(queryForm)
        if (res.code === 200) {
            tableData.value = res.data.list
            total.value = res.data.total
        }
    } finally {
        loading.value = false
    }
}

const handleSearch = () => {
    queryForm.pageNo = 1
    fetchData()
}

const handleReset = () => {
    queryForm.name = ''
    queryForm.phone = ''
    fetchData()
}

const handleExport = async () => {
  try {
    const res = await exportCustomers(queryForm)
    if (res.code === 200) {
      ElMessageBox.confirm(
        '导出任务已创建，是否前往导出中心查看进度？',
        '提示',
        {
          confirmButtonText: '前往导出中心',
          cancelButtonText: '留在本页',
          type: 'success',
        }
      )
      .then(() => {
        router.push('/export-center')
      })
      .catch(() => {})
    }
  } catch (error) {
    console.error(error)
  }
}

const handleDetail = (row) => {
    // Navigate to customer detail (to be implemented, reusing order list with filter?)
    // For now, let's just go to order list filtered by phone
    router.push({ path: '/orders', query: { customerId: row.id } })
}
</script>

<style scoped>
.app-container { padding: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
