<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" class="search-form">
          <el-form-item label="月份">
            <el-date-picker
              v-model="queryMonth"
              type="month"
              placeholder="选择月份"
              format="YYYY-MM"
              value-format="YYYY-MM"
              @change="fetchData"
              clearable
            />
          </el-form-item>
          <el-form-item label="销售员">
            <el-select v-model="querySalespersonId" placeholder="全部销售" clearable @change="fetchData">
              <el-option v-for="item in salesList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchData" :icon="Search">查询</el-button>
            <el-button @click="handleReset" :icon="Refresh">重置</el-button>
            <el-button type="warning" @click="handleExport" :icon="Download">导出</el-button>
            <el-button type="success" @click="handleSetTarget" :icon="Plus" v-if="userStore.currentUser.role === 'ADMIN'">设置目标</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe v-loading="loading">
          <el-table-column prop="salespersonName" label="销售员" width="120" />
          <el-table-column prop="targetMonth" label="月份" width="120" />
          <el-table-column prop="targetAmount" label="目标金额" width="150">
            <template #default="scope">
              <span class="font-bold">¥ {{ scope.row.targetAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="actualAmount" label="实际完成" width="150">
            <template #default="scope">
              <span :class="{'text-success': scope.row.actualAmount >= scope.row.targetAmount, 'text-danger': scope.row.actualAmount < scope.row.targetAmount}">
                ¥ {{ scope.row.actualAmount }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="完成率" min-width="200">
            <template #default="scope">
              <el-progress 
                :percentage="Math.min(scope.row.completionRate || 0, 100)" 
                :format="() => (scope.row.completionRate || 0) + '%'"
                :status="getProcessStatus(scope.row.completionRate)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center" v-if="userStore.currentUser.role === 'ADMIN'">
            <template #default="scope">
              <el-button type="primary" link @click="handleEdit(scope.row)">调整</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" title="设置销售目标" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="销售员">
          <el-select v-model="form.salespersonId" placeholder="选择销售员" style="width: 100%" :disabled="!!form.id">
            <el-option v-for="item in salesList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标月份">
          <el-date-picker
            v-model="form.targetMonth"
            type="month"
            placeholder="选择月份"
            format="YYYY-MM"
            value-format="YYYY-MM"
            style="width: 100%"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item label="目标金额">
          <el-input-number v-model="form.targetAmount" :min="0" :precision="2" :step="1000" style="width: 100%">
             <template #prefix>￥</template>
          </el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Plus, Download, Refresh } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { listTargets, setTarget, exportTargets } from '@/api/salesTarget'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'

const userStore = useUserStore()
const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const salesList = ref([])
const queryMonth = ref(dayjs().format('YYYY-MM'))
const querySalespersonId = ref(null)
const dialogVisible = ref(false)

const form = reactive({
  id: null,
  salespersonId: null,
  targetMonth: '',
  targetAmount: 0
})

onMounted(() => {
  fetchSalesList()
  fetchData()
})

const fetchSalesList = async () => {
  try {
    const res = await request.get('/auth/role/SALES')
    if (res.code === 200) {
      salesList.value = res.data
    }
  } catch (e) {}
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      month: queryMonth.value,
      salespersonId: querySalespersonId.value
    }
    // If SALES, force own ID
    if (userStore.currentUser.role === 'SALES') {
        params.salespersonId = userStore.currentUser.id
    }
    const res = await listTargets(params)
    if (res.code === 200) {
      tableData.value = res.data
    }
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryMonth.value = dayjs().format('YYYY-MM')
  querySalespersonId.value = null
  fetchData()
}

const handleExport = async () => {
  try {
    const params = {
      month: queryMonth.value,
      salespersonId: querySalespersonId.value
    }
    if (userStore.currentUser.role === 'SALES') {
        params.salespersonId = userStore.currentUser.id
    }
    const res = await exportTargets(params)
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

const handleSetTarget = () => {
  form.id = null
  form.salespersonId = null
  form.targetMonth = dayjs().format('YYYY-MM')
  form.targetAmount = 0
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.id = row.id
  form.salespersonId = row.salespersonId
  form.targetMonth = row.targetMonth
  form.targetAmount = row.targetAmount
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!form.salespersonId || !form.targetMonth || !form.targetAmount) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  try {
    const res = await setTarget({
      ...form,
      currentUserId: userStore.currentUser.id
    })
    if (res.code === 200) {
      ElMessage.success('设置成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.message)
    }
  } catch(e) {}
}

const getProcessStatus = (rate) => {
  if (rate >= 100) return 'success'
  if (rate >= 80) return 'warning'
  return '' // default blue
}
</script>

<style scoped>
.app-container {
  min-height: 100%;
  padding: 0;
}
.main-content {
  max-width: 100%;
}
.search-card {
  margin-bottom: 16px;
  border-radius: 8px;
  border: none;
}
.table-card {
  border-radius: 8px;
  border: none;
  min-height: 500px;
}
.font-bold {
  font-weight: bold;
}
.text-success {
  color: #67c23a;
  font-weight: bold;
}
.text-danger {
  color: #f56c6c;
  font-weight: bold;
}
</style>
