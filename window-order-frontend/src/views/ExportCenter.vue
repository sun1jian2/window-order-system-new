<template>
  <div class="page-container">
    <div class="header">
      <h2>导出中心</h2>
    </div>

    <el-card shadow="never" class="search-card">
        <el-form :inline="true" :model="queryForm" class="demo-form-inline">
            <el-form-item label="操作人">
                <el-input v-model="queryForm.operatorName" placeholder="输入操作人姓名" clearable />
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker
                    v-model="dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="YYYY-MM-DD"
                    @change="handleDateChange"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleSearch">查询</el-button>
                <el-button @click="handleReset">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>

    <el-table :data="tasks" v-loading="loading" border style="width: 100%; margin-top: 20px;">
      <el-table-column prop="taskName" label="任务名称" min-width="200" align="center" />
      <el-table-column prop="status" label="状态" min-width="120" align="center">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createByName" label="创建人" min-width="120" align="center" />
      <el-table-column prop="createTime" label="创建时间" min-width="180" align="center" />
      <el-table-column prop="finishTime" label="完成时间" min-width="180" align="center" />
      <el-table-column label="操作" min-width="150" align="center">
        <template #default="scope">
          <el-button 
            v-if="scope.row.status === 'COMPLETED' && scope.row.fileUrl" 
            type="primary" 
            link 
            @click="downloadFile(scope.row.fileUrl)"
          >
            下载
          </el-button>
          <span v-else-if="scope.row.status === 'FAILED'" class="text-danger">{{ scope.row.errorMsg }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
        <el-pagination
            v-model:current-page="queryForm.pageNo"
            v-model:page-size="queryForm.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { listTasks } from '@/api/exportTask'
import { ElMessage } from 'element-plus'

const tasks = ref([])
const total = ref(0)
const loading = ref(false)
const dateRange = ref([])
let timer = null

const queryForm = reactive({
    pageNo: 1,
    pageSize: 10,
    operatorName: '',
    startTime: '',
    endTime: ''
})

const fetchTasks = async () => {
  loading.value = true
  try {
    const res = await listTasks(queryForm)
    if (res && res.code === 200) {
        tasks.value = res.data.list
        total.value = res.data.total
        
        // Check if there are any active tasks
        const hasActiveTasks = tasks.value.some(t => ['PENDING', 'PROCESSING'].includes(t.status))
        
        if (hasActiveTasks) {
            // Start polling if not already started
            if (!timer) {
                timer = setInterval(fetchTasks, 3000)
            }
        } else {
            // Stop polling if all tasks are done
            if (timer) {
                clearInterval(timer)
                timer = null
            }
        }
    }
  } catch (error) {
    console.error(error)
  } finally {
      loading.value = false
  }
}

const handleDateChange = (val) => {
    if (val) {
        queryForm.startTime = val[0] + ' 00:00:00'
        queryForm.endTime = val[1] + ' 23:59:59'
    } else {
        queryForm.startTime = ''
        queryForm.endTime = ''
    }
}

const handleSearch = () => {
    queryForm.pageNo = 1
    fetchTasks()
}

const handleReset = () => {
    queryForm.operatorName = ''
    queryForm.startTime = ''
    queryForm.endTime = ''
    dateRange.value = []
    handleSearch()
}

const handleSizeChange = (val) => {
    queryForm.pageSize = val
    fetchTasks()
}

const handleCurrentChange = (val) => {
    queryForm.pageNo = val
    fetchTasks()
}

const downloadFile = (url) => {
  window.open(url, '_blank')
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'info',
    'PROCESSING': 'warning',
    'COMPLETED': 'success',
    'FAILED': 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '等待中',
    'PROCESSING': '处理中',
    'COMPLETED': '完成',
    'FAILED': '失败'
  }
  return map[status] || status
}

onMounted(() => {
  fetchTasks()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.text-danger {
  color: #f56c6c;
}
.search-card {
    margin-bottom: 20px;
}
.pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}
</style>
