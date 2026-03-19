<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-row :gutter="20" style="width: 100%">
            <el-col :span="6">
              <el-form-item label="材料名称" class="w-full">
                <el-input v-model="queryForm.keyword" placeholder="名称或编码" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="材料分类" class="w-full">
                <el-select v-model="queryForm.categoryId" placeholder="全部分类" clearable class="w-full">
                  <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="库存状态" class="w-full">
                <el-select v-model="queryForm.isWarning" placeholder="全部状态" clearable class="w-full">
                  <el-option label="库存预警" :value="true" />
                  <el-option label="库存正常" :value="false" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6" class="text-right">
              <el-form-item class="search-actions">
                <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
                <el-button @click="handleReset" :icon="Refresh">重置</el-button>
                <el-button type="success" @click="handleCreate" :icon="Plus">新建材料</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="categoryName" label="分类" width="120" align="center" show-overflow-tooltip />
          <el-table-column prop="name" label="材料名称" min-width="150" align="center" show-overflow-tooltip />
          <el-table-column prop="code" label="材料编码" width="120" align="center" />
          <el-table-column prop="spec" label="规格型号" width="120" align="center" />
          <el-table-column prop="unit" label="单位" width="80" align="center" />
          <el-table-column prop="unitPrice" label="参考单价" width="100" align="right">
             <template #default="scope">¥ {{ scope.row.unitPrice }}</template>
          </el-table-column>
          <el-table-column prop="stockQuantity" label="当前库存" width="100" align="center">
            <template #default="scope">
              <span :style="{ color: scope.row.stockQuantity <= scope.row.warningQuantity ? '#f56c6c' : '#67c23a', fontWeight: 'bold' }">
                {{ scope.row.stockQuantity }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="warningQuantity" label="预警阈值" width="100" align="center" />
          <el-table-column label="操作" fixed="right" width="180" align="center">
            <template #default="scope">
              <el-button link type="primary" :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button link type="danger" :icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="queryForm.pageNo"
            v-model:page-size="queryForm.pageSize"
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

    <el-dialog v-model="dialogVisible" :title="dialogType === 'create' ? '新建材料' : '编辑材料'" width="600px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="材料分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="材料名称" prop="name">
          <el-input v-model="form.name" placeholder="如：断桥铝型材" />
        </el-form-item>
        <el-form-item label="材料编码" prop="code">
          <el-input v-model="form.code" placeholder="选填，内部编码" />
        </el-form-item>
        <el-form-item label="规格型号" prop="spec">
          <el-input v-model="form.spec" placeholder="如：108系列" />
        </el-form-item>
        <el-form-item label="计量单位" prop="unit">
          <el-input v-model="form.unit" placeholder="如：米, 支, 平方米" />
        </el-form-item>
        <el-form-item label="参考单价" prop="unitPrice">
          <el-input-number v-model="form.unitPrice" :min="0" :precision="2" :step="10" style="width: 200px" />
          <span style="margin-left: 10px; color: #909399">元 / {{ form.unit || '单位' }}</span>
        </el-form-item>
        <el-form-item label="初始库存" prop="stockQuantity" v-if="dialogType === 'create'">
          <el-input-number v-model="form.stockQuantity" :min="0" :precision="2" :step="10" style="width: 200px" />
        </el-form-item>
        <el-form-item label="预警阈值" prop="warningQuantity">
          <el-input-number v-model="form.warningQuantity" :min="0" :precision="2" :step="10" style="width: 200px" />
          <span style="margin-left: 10px; color: #909399">低于此值将触发库存预警</span>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)

const categoryOptions = ref([])

const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  keyword: '',
  categoryId: null,
  isWarning: null
})

const form = reactive({
  id: null,
  categoryId: null,
  name: '',
  code: '',
  spec: '',
  unit: '',
  unitPrice: 0,
  stockQuantity: 0,
  warningQuantity: 0,
  remark: ''
})

const rules = {
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  name: [{ required: true, message: '请输入材料名称', trigger: 'blur' }]
}

onMounted(() => {
  fetchCategories()
  fetchData()
})

const fetchCategories = async () => {
  try {
    const res = await request.get('/inventory/material/categories')
    if (res.code === 200) categoryOptions.value = res.data
  } catch (error) {}
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.post('/inventory/material/list', queryForm)
    if (res.code === 200) {
      tableData.value = res.data.list
      total.value = res.data.total
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryForm.pageNo = 1
  fetchData()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.categoryId = null
  queryForm.isWarning = null
  queryForm.pageNo = 1
  fetchData()
}

const handleCreate = () => {
  dialogType.value = 'create'
  Object.assign(form, {
    id: null,
    categoryId: null,
    name: '',
    code: '',
    spec: '',
    unit: '',
    unitPrice: 0,
    stockQuantity: 0,
    warningQuantity: 0,
    remark: ''
  })
  if (formRef.value) formRef.value.clearValidate()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  if (formRef.value) formRef.value.clearValidate()
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该材料吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
    const res = await request.delete(`/inventory/material/${row.id}?currentUserId=${currentUser.id}`)
    if (res.code === 200) {
      ElMessage.success('已删除')
      fetchData()
    }
  })
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
        const res = await request.post(`/inventory/material/save?currentUserId=${currentUser.id}`, form)
        if (res.code === 200) {
          ElMessage.success('保存成功')
          dialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.message)
        }
      } catch (error) {
        ElMessage.error('提交失败')
      }
    }
  })
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
.search-form .el-form-item {
  margin-right: 0;
  margin-bottom: 10px;
}
.search-actions {
  margin-left: auto;
}
.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}
</style>
