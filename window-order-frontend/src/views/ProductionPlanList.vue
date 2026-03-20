<template>
  <div class="production-plan-container">
    <el-card>
      <!-- Search Form -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="排产单号">
          <el-input v-model="searchForm.planNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待生产" value="PENDING" />
            <el-option label="生产中" value="PRODUCING" />
            <el-option label="已完成" value="FINISHED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="handleAdd">新增排产</el-button>
        </el-form-item>
      </el-form>

      <!-- Table -->
      <el-table :data="tableData" v-loading="loading" border style="width: 100%">
        <el-table-column prop="planNo" label="排产单号" width="150" />
        <el-table-column prop="orderNo" label="订单号" width="150" />
        <el-table-column prop="customerName" label="客户" width="120" />
        <el-table-column prop="plannedStartDate" label="预计开始" width="120" />
        <el-table-column prop="plannedEndDate" label="预计结束" width="120" />
        <el-table-column prop="managerName" label="负责人" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="scope">
            <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(scope.row)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-container">
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
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model.number="form.orderId" placeholder="请输入订单ID" />
        </el-form-item>
        <el-form-item label="预计开始" prop="plannedStartDate">
          <el-date-picker v-model="form.plannedStartDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="预计结束" prop="plannedEndDate">
          <el-date-picker v-model="form.plannedEndDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="负责人ID" prop="managerId">
          <el-input v-model.number="form.managerId" placeholder="请输入负责人ID" />
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

const form = reactive({
  id: null,
  planNo: '',
  orderId: null,
  plannedStartDate: '',
  plannedEndDate: '',
  managerId: null,
  status: 'PENDING',
  remark: ''
})

const rules = {
  orderId: [{ required: true, message: '请输入订单ID', trigger: 'blur' }]
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

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.production-plan-container {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>