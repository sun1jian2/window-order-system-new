<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>日志中心</span>
        </div>
      </template>
      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="用户名">
          <el-input v-model="query.username" placeholder="用户名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="模块">
          <el-input v-model="query.module" placeholder="模块" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-input v-model="query.operation" placeholder="操作类型" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="方法/参数关键词" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="warning" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="list" stripe>
        <el-table-column prop="createTime" label="时间" min-width="170" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="module" label="模块" min-width="120" />
        <el-table-column prop="operation" label="操作类型" min-width="120" />
        <el-table-column prop="method" label="方法" min-width="220" show-overflow-tooltip />
        <el-table-column prop="params" label="参数" min-width="240" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP" min-width="120" />
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          v-model:page-size="query.pageSize"
          v-model:current-page="query.pageNo"
          @size-change="fetchList"
          @current-change="fetchList"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listLogs, exportLogs } from '../api/logs'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const query = ref({
  pageNo: 1,
  pageSize: 10,
  username: '',
  module: '',
  operation: '',
  keyword: '',
  startTime: '',
  endTime: ''
})
const dateRange = ref([])
const list = ref([])
const total = ref(0)

const buildQuery = () => {
  if (dateRange.value && dateRange.value.length === 2) {
    query.value.startTime = dateRange.value[0]
    query.value.endTime = dateRange.value[1]
  } else {
    query.value.startTime = ''
    query.value.endTime = ''
  }
  return { ...query.value }
}

const fetchList = async () => {
  const res = await listLogs(buildQuery())
  if (res.code === 200) {
    list.value = res.data.list || []
    total.value = res.data.total || 0
  }
}

const handleSearch = () => {
  query.value.pageNo = 1
  fetchList()
}

const handleReset = () => {
  query.value = {
    pageNo: 1,
    pageSize: 10,
    username: '',
    module: '',
    operation: '',
    keyword: '',
    startTime: '',
    endTime: ''
  }
  dateRange.value = []
  fetchList()
}

const handleExport = async () => {
  try {
    const res = await exportLogs(buildQuery())
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

onMounted(fetchList)
</script>

<style scoped>
.filter-form {
  margin-bottom: 12px;
}
.pager {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
