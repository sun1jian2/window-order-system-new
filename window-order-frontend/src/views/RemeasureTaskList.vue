<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true">
          <el-form-item label="订单号">
            <el-input v-model="orderNo" placeholder="订单号" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
            <el-button type="warning" :icon="Download" @click="handleExport">导出</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="table-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>复尺任务列表</span>
            <el-button type="primary" :icon="Refresh" @click="fetchData">刷新</el-button>
          </div>
        </template>
        
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="orderNo" label="订单号" width="180" />
          <el-table-column prop="customerName" label="客户" width="100" />
          <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
          <el-table-column prop="createTime" label="指派时间" width="160" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : 'warning'">
                {{ scope.row.status === 'COMPLETED' ? '已完成' : '待复尺' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="120" align="center">
            <template #default="scope">
              <el-button 
                v-if="scope.row.status === 'PENDING' && (userStore.currentUser.role === 'INSTALLER' || userStore.currentUser.role === 'ADMIN')"
                type="primary" 
                size="small" 
                @click="handleSubmit(scope.row)"
              >
                提交结果
              </el-button>
              <el-button 
                v-else
                type="info" 
                size="small" 
                @click="handleView(scope.row)"
              >
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination">
          <el-pagination
            v-model:current-page="pageNo"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="fetchData"
            @current-change="fetchData"
            background
          />
        </div>
      </el-card>
    </div>

    <!-- Submit Dialog -->
    <el-dialog v-model="submitDialogVisible" title="提交复尺结果" width="600px">
      <el-form :model="submitForm" label-width="100px" :rules="rules" ref="submitFormRef">
        <el-form-item label="精确宽度" prop="preciseWidth">
          <el-input-number v-model="submitForm.preciseWidth" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="精确高度" prop="preciseHeight">
          <el-input-number v-model="submitForm.preciseHeight" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="手绘图URL" prop="sketchUrl">
          <el-input v-model="submitForm.sketchUrl" placeholder="输入图片地址" />
        </el-form-item>
        <el-form-item label="现场照片" prop="sitePhotosRaw">
          <el-input v-model="submitForm.sitePhotosRaw" placeholder="输入图片地址，多个用逗号分隔" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="submitForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="submitDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listRemeasureTasks, submitRemeasureTask, exportRemeasureTasks } from '../api/remeasure'
import { useUserStore } from '../stores/user'
import { Refresh, Search, Download } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUserStore()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNo = ref(1)
const pageSize = ref(10)
const orderNo = ref('')

const submitDialogVisible = ref(false)
const submitFormRef = ref(null)
const submitForm = reactive({
  id: null,
  preciseWidth: 0,
  preciseHeight: 0,
  sketchUrl: '',
  sitePhotosRaw: '',
  sitePhotos: '[]',
  remark: ''
})

const rules = {
  preciseWidth: [{ required: true, message: '请输入精确宽度', trigger: 'blur' }],
  preciseHeight: [{ required: true, message: '请输入精确高度', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listRemeasureTasks({
      pageNo: pageNo.value,
      pageSize: pageSize.value,
      orderNo: orderNo.value,
      assigneeId: userStore.currentUser.role === 'INSTALLER' ? userStore.currentUser.id : null
    })
    if (res.code === 200) {
      tableData.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

const handleSubmit = (row) => {
  submitForm.id = row.id
  submitForm.preciseWidth = row.preciseWidth || 0
  submitForm.preciseHeight = row.preciseHeight || 0
  submitForm.sketchUrl = row.sketchUrl || ''
  submitForm.sitePhotosRaw = ''
  submitForm.remark = row.remark || ''
  submitDialogVisible.value = true
}

const handleView = (row) => {
    ElMessage.info('精确宽:' + row.preciseWidth + ' 高:' + row.preciseHeight)
}

const confirmSubmit = async () => {
  if (!submitFormRef.value) return
  await submitFormRef.value.validate(async (valid) => {
    if (valid) {
      if (submitForm.sitePhotosRaw) {
          submitForm.sitePhotos = JSON.stringify(submitForm.sitePhotosRaw.split(',').map(s => s.trim()))
      }
      try {
        const res = await submitRemeasureTask(submitForm)
        if (res.code === 200) {
          ElMessage.success('提交成功')
          submitDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.message)
        }
      } catch (e) {}
    }
  })
}

onMounted(() => {
  fetchData()
})

const handleSearch = () => {
  pageNo.value = 1
  fetchData()
}

const handleReset = () => {
  orderNo.value = ''
  pageNo.value = 1
  fetchData()
}

const handleExport = async () => {
  try {
    const res = await exportRemeasureTasks({
      pageNo: pageNo.value,
      pageSize: pageSize.value,
      orderNo: orderNo.value,
      assigneeId: userStore.currentUser.role === 'INSTALLER' ? userStore.currentUser.id : null
    })
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
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
