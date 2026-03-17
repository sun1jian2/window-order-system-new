<template>
  <div class="app-container">
    <div class="main-content">
      <!-- Search -->
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-form-item label="订单号">
            <el-input v-model="queryForm.orderNo" placeholder="订单号" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="工单号">
            <el-input v-model="queryForm.ticketNo" placeholder="工单号" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="客户名">
            <el-input v-model="queryForm.customerName" placeholder="客户名" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryForm.status" placeholder="全部状态" clearable size="small" class="status-select">
                <el-option label="待处理" value="PENDING" />
                <el-option label="已指派" value="ASSIGNED" />
                <el-option label="处理中" value="PROCESSING" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已取消" value="CANCELLED" />
            </el-select>
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
          <el-table-column prop="ticketNo" label="工单号" width="180" />
          <el-table-column prop="orderNo" label="关联订单" width="180" />
          <el-table-column prop="customerName" label="客户名" width="100" />
          <el-table-column prop="customerPhone" label="电话" width="120" />
          <el-table-column prop="issueDescription" label="问题描述" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
             <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
             </template>
          </el-table-column>
          <el-table-column prop="handlerName" label="处理人" width="100" />
          <el-table-column prop="createTime" label="创建时间" width="160">
              <template #default="scope">{{ scope.row.createTime ? scope.row.createTime.replace('T', ' ') : '' }}</template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="180" align="center">
            <template #default="scope">
              <el-button link type="primary" @click="handleEdit(scope.row)">处理</el-button>
              <el-button link type="danger" @click="handleDelete(scope.row)" v-if="currentUser.role === 'ADMIN'">删除</el-button>
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

    <!-- Edit Dialog -->
    <el-dialog v-model="dialogVisible" title="处理工单" width="600px">
        <el-form :model="form" label-width="100px">
            <el-form-item label="工单号">{{ form.ticketNo }}</el-form-item>
            <el-form-item label="问题描述">{{ form.issueDescription }}</el-form-item>
            <el-form-item label="状态">
                <el-select v-model="form.status" style="width: 100%" size="small">
                    <el-option label="待处理" value="PENDING" />
                    <el-option label="已指派" value="ASSIGNED" />
                    <el-option label="处理中" value="PROCESSING" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已取消" value="CANCELLED" />
                </el-select>
            </el-form-item>
            <el-form-item label="指派师傅">
                 <el-select v-model="form.handlerId" placeholder="选择师傅" style="width: 100%" filterable clearable>
                  <el-option v-for="item in installerList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="预约时间">
                 <el-date-picker v-model="form.appointmentTime" type="datetime" style="width: 100%" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm:ss" />
            </el-form-item>
            <el-form-item label="解决方案" v-if="form.status === 'COMPLETED'">
                <el-input v-model="form.solution" type="textarea" placeholder="填写维修结果" />
            </el-form-item>
            <el-form-item label="维修费用" v-if="form.status === 'COMPLETED'">
                <el-input-number v-model="form.fee" :min="0" :precision="2" />
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitForm">保存</el-button>
        </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Download } from '@element-plus/icons-vue'
import { listAfterSales, updateAfterSales, deleteAfterSales, exportAfterSales } from '@/api/afterSales'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUserStore()
const currentUser = userStore.currentUser
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const installerList = ref([])
const dialogVisible = ref(false)

const queryForm = reactive({
    pageNo: 1,
    pageSize: 10,
    orderNo: '',
    ticketNo: '',
    customerName: '',
    status: ''
})

const form = reactive({
    id: null,
    ticketNo: '',
    issueDescription: '',
    status: '',
    handlerId: null,
    appointmentTime: null,
    solution: '',
    fee: 0
})

onMounted(() => {
    fetchData()
    fetchInstallers()
})

const fetchData = async () => {
    loading.value = true
    try {
        const res = await listAfterSales(queryForm)
        if (res.code === 200) {
            tableData.value = res.data.list
            total.value = res.data.total
        }
    } finally {
        loading.value = false
    }
}

const fetchInstallers = async () => {
    try {
        const res = await request.get('/auth/role/INSTALLER')
        if (res.code === 200) installerList.value = res.data
    } catch(e){}
}

const handleSearch = () => {
    queryForm.pageNo = 1
    fetchData()
}

const handleReset = () => {
    queryForm.orderNo = ''
    queryForm.ticketNo = ''
    queryForm.customerName = ''
    queryForm.status = ''
    fetchData()
}

const handleExport = async () => {
  try {
    const res = await exportAfterSales(queryForm)
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

const handleEdit = (row) => {
    Object.assign(form, row)
    dialogVisible.value = true
}

const submitForm = async () => {
    try {
        const res = await updateAfterSales(form)
        if (res.code === 200) {
            ElMessage.success('保存成功')
            dialogVisible.value = false
            fetchData()
        } else {
            ElMessage.error(res.message)
        }
    } catch(e){}
}

const handleDelete = (row) => {
    ElMessageBox.confirm('确认删除?', '警告', { type: 'warning' })
    .then(async () => {
        const res = await deleteAfterSales(row.id)
        if (res.code === 200) {
            ElMessage.success('已删除')
            fetchData()
        }
    })
}

const getStatusType = (status) => {
    const map = { 'PENDING': 'info', 'ASSIGNED': 'primary', 'PROCESSING': 'warning', 'COMPLETED': 'success', 'CANCELLED': 'danger' }
    return map[status] || 'info'
}

const getStatusLabel = (status) => {
    const map = { 'PENDING': '待处理', 'ASSIGNED': '已指派', 'PROCESSING': '处理中', 'COMPLETED': '已完成', 'CANCELLED': '已取消' }
    return map[status] || status
}
</script>

<style scoped>
.app-container { padding: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
.status-select { width: 140px; }
</style>
