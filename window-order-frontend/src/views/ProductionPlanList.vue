<template>
  <div class="production-plan-container app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-row :gutter="20" class="w-full">
            <el-col :span="6">
              <el-form-item label="排产单号" class="w-full">
                <el-input v-model="searchForm.planNo" placeholder="请输入单号" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="状态" class="w-full">
                <el-select v-model="searchForm.status" placeholder="请选择状态" clearable class="w-full">
                  <el-option label="待生产" value="PENDING" />
                  <el-option label="生产中" value="PRODUCING" />
                  <el-option label="已完成" value="FINISHED" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-form-item class="search-actions">
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
                <el-button type="success" @click="handleAdd">新增排产</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" v-loading="loading" style="width: 100%" stripe border>
          <el-table-column prop="planNo" label="排产单号" min-width="220" />
          <el-table-column prop="orderNo" label="订单号" min-width="220" />
          <el-table-column prop="customerName" label="客户" min-width="100" />
          <el-table-column prop="plannedStartDate" label="预计开始" width="120" />
          <el-table-column prop="plannedEndDate" label="预计结束" width="120" />
          <el-table-column prop="managerName" label="负责人" width="100" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" effect="light">
                {{ getStatusLabel(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" fixed="right" width="150" align="center">
            <template #default="scope">
              <div class="action-cell">
                <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row)">
                  <template #reference>
                    <el-button link type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="searchForm.pageNo"
            v-model:page-size="searchForm.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- Dialog -->
    <el-dialog
      :title="dialogType === 'add' ? '新增排产' : '编辑排产'"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="排产单号" prop="planNo" v-if="dialogType === 'edit'">
          <el-input v-model="form.planNo" disabled placeholder="自动生成" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="预计开始" prop="plannedStartDate">
          <el-date-picker v-model="form.plannedStartDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="预计结束" prop="plannedEndDate">
          <el-date-picker v-model="form.plannedEndDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="负责人" prop="managerId">
          <el-select v-model="form.managerId" placeholder="请选择负责人" filterable style="width: 100%">
            <el-option 
              v-for="user in salesList" 
              :key="user.id" 
              :label="user.realName || user.username" 
              :value="user.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="待生产" value="PENDING" />
            <el-option label="生产中" value="PRODUCING" />
            <el-option label="已完成" value="FINISHED" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { getProductionPlanList, createProductionPlan, updateProductionPlan, deleteProductionPlan } from '../api/production'

const searchForm = reactive({
  planNo: '',
  status: '',
  pageNo: 1,
  pageSize: 10
})

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const dialogVisible = ref(false)
const dialogType = ref('add')
const submitLoading = ref(false)
const formRef = ref(null)
const salesList = ref([])

const form = reactive({
  id: null,
  planNo: '',
  orderId: null,
  orderNo: '',
  plannedStartDate: '',
  plannedEndDate: '',
  managerId: null,
  status: 'PENDING',
  remark: ''
})

const rules = {
  orderNo: [{ required: true, message: '请输入订单号', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'info',
    'PRODUCING': 'primary',
    'FINISHED': 'success'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = {
    'PENDING': '待生产',
    'PRODUCING': '生产中',
    'FINISHED': '已完成'
  }
  return map[status] || status
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getProductionPlanList(searchForm)
    if (res.code === 200) {
      tableData.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  searchForm.pageNo = 1
  fetchList()
}

const resetSearch = () => {
  searchForm.planNo = ''
  searchForm.status = ''
  handleSearch()
}

const handleSizeChange = (val) => {
  searchForm.pageSize = val
  fetchList()
}

const handleCurrentChange = (val) => {
  searchForm.pageNo = val
  fetchList()
}

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(form, {
    id: null,
    planNo: '',
    orderId: null,
    orderNo: '',
    plannedStartDate: '',
    plannedEndDate: '',
    managerId: null,
    status: 'PENDING',
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    const res = await deleteProductionPlan(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchList()
    }
  } catch (error) {
    console.error(error)
  }
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const api = dialogType.value === 'add' ? createProductionPlan : updateProductionPlan
        const res = await api(form)
        if (res.code === 200) {
          ElMessage.success(dialogType.value === 'add' ? '创建成功' : '更新成功')
          dialogVisible.value = false
          fetchList()
        } else {
          ElMessage.error(res.message || '操作失败')
        }
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const fetchSalesList = async () => {
  try {
    const res = await request.get('/auth/role/SALES')
    if (res.code === 200) {
      salesList.value = res.data
    }
  } catch (e) {
    console.error('Failed to fetch sales list', e)
  }
}

onMounted(() => {
  fetchList()
  fetchSalesList()
})
</script>

<style scoped>
.app-container {
  min-height: 100%;
  background-color: transparent;
  display: flex;
  flex-direction: column;
}

/* 产品明细表格行高优化 */
:deep(.el-table .el-table__row) {
  height: 55px;
}
:deep(.el-table .cell) {
  line-height: normal;
}

.main-content {
  padding: 0;
  max-width: 100%;
  margin: 0;
  width: 100%;
  box-sizing: border-box;
}

.search-card {
  margin-bottom: 16px;
  border-radius: 8px;
  border: none;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.w-full {
  width: 100%;
}

.text-right {
  text-align: right;
  display: flex;
  justify-content: flex-end;
}

/* Remove default inline form margin */
.search-form .el-form-item {
  margin-right: 0;
  margin-bottom: 10px;
}

.search-actions {
  margin-left: auto;
}

.table-card {
  border-radius: 8px;
  border: none;
  min-height: 500px;
  display: flex;
  flex-direction: column;
}

:deep(.table-card .el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

:deep(.el-table) {
  flex: 1;
}

.action-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  margin-right: 0;
  border-bottom: 1px solid #EBEEF5;
  padding: 20px;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid #EBEEF5;
  padding: 20px;
}
</style>