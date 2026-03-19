<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-form-item label="供应商名称">
            <el-input v-model="queryForm.name" placeholder="请输入名称" clearable />
          </el-form-item>
          <el-form-item class="search-actions">
            <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
            <el-button @click="handleReset" :icon="Refresh">重置</el-button>
            <el-button type="success" @click="handleCreate" :icon="Plus">新建供应商</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="name" label="供应商名称" min-width="180" align="center" show-overflow-tooltip />
          <el-table-column prop="contactPerson" label="联系人" min-width="120" align="center" />
          <el-table-column prop="phone" label="联系电话" min-width="130" align="center" />
          <el-table-column prop="address" label="地址" min-width="180" align="center" show-overflow-tooltip />
          <el-table-column prop="accountBalance" label="应付账款(元)" min-width="140" align="right">
             <template #default="scope">
               <span style="color: #f56c6c; font-weight: bold;">¥ {{ scope.row.accountBalance }}</span>
             </template>
          </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="dialogType === 'create' ? '新建供应商' : '编辑供应商'" width="500px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="供应商名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="备注说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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

const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  name: ''
})

const form = reactive({
  id: null,
  name: '',
  contactPerson: '',
  phone: '',
  address: '',
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }]
}

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.post('/inventory/supplier/list', queryForm)
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
  queryForm.name = ''
  queryForm.pageNo = 1
  fetchData()
}

const handleCreate = () => {
  dialogType.value = 'create'
  Object.assign(form, {
    id: null,
    name: '',
    contactPerson: '',
    phone: '',
    address: '',
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
  ElMessageBox.confirm('确认删除该供应商吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
    const res = await request.delete(`/inventory/supplier/${row.id}?currentUserId=${currentUser.id}`)
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
        const res = await request.post(`/inventory/supplier/save?currentUserId=${currentUser.id}`, form)
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
.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}
</style>
