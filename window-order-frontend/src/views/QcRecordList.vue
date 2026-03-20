<template>
  <div class="qc-record-container app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-row :gutter="20" class="w-full">
            <el-col :span="6">
              <el-form-item label="结果" class="w-full">
                <el-select v-model="searchForm.result" placeholder="请选择结果" clearable class="w-full">
                  <el-option label="合格" value="PASS" />
                  <el-option label="不合格" value="FAIL" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="18" class="text-right">
              <el-form-item class="search-actions">
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
                <el-button type="success" @click="handleAdd">新增质检记录</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" v-loading="loading" style="width: 100%" stripe border>
          <el-table-column prop="planNo" label="排产单号" min-width="220" />
          <el-table-column prop="processName" label="工序名称" min-width="120" />
          <el-table-column prop="inspectorName" label="检验员" width="120" />
          <el-table-column prop="checkTime" label="检验时间" width="160" />
          <el-table-column prop="result" label="结果" width="100" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.result === 'PASS' ? 'success' : 'danger'" effect="light">
                {{ scope.row.result === 'PASS' ? '合格' : '不合格' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="defectReason" label="不合格原因" min-width="150" show-overflow-tooltip />
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
      :title="dialogType === 'add' ? '新增质检记录' : '编辑质检记录'"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="排产计划ID" prop="planId">
          <el-input v-model.number="form.planId" placeholder="请输入排产计划ID" />
        </el-form-item>
        <el-form-item label="工序ID" prop="processId">
          <el-input v-model.number="form.processId" placeholder="可选：工序ID" />
        </el-form-item>
        <el-form-item label="检验员ID" prop="inspectorId">
          <el-input v-model.number="form.inspectorId" placeholder="请输入检验员ID" />
        </el-form-item>
        <el-form-item label="检验时间" prop="checkTime">
          <el-date-picker v-model="form.checkTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="结果" prop="result">
          <el-radio-group v-model="form.result">
            <el-radio label="PASS">合格</el-radio>
            <el-radio label="FAIL">不合格</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="不合格原因" prop="defectReason" v-if="form.result === 'FAIL'">
          <el-input v-model="form.defectReason" type="textarea" />
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
import { getQcRecordList, createQcRecord, updateQcRecord, deleteQcRecord } from '../api/production'

const searchForm = reactive({
  result: '',
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
  planId: null,
  processId: null,
  inspectorId: null,
  checkTime: '',
  result: 'PASS',
  defectReason: '',
  remark: ''
})

const rules = {
  planId: [{ required: true, message: '请输入计划ID', trigger: 'blur' }],
  inspectorId: [{ required: true, message: '请输入检验员ID', trigger: 'blur' }],
  checkTime: [{ required: true, message: '请选择检验时间', trigger: 'blur' }],
  result: [{ required: true, message: '请选择结果', trigger: 'change' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getQcRecordList(searchForm)
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
  searchForm.result = ''
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
    planId: null,
    processId: null,
    inspectorId: null,
    checkTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
    result: 'PASS',
    defectReason: '',
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
    const res = await deleteQcRecord(row.id)
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
        const api = dialogType.value === 'add' ? createQcRecord : updateQcRecord
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