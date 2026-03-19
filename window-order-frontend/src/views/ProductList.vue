<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-row :gutter="20" style="width: 100%">
            <el-col :span="6">
              <el-form-item label="产品名称" class="w-full">
                <el-input v-model="queryForm.keyword" placeholder="名称或编码" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="产品分类" class="w-full">
                <el-select v-model="queryForm.categoryId" placeholder="全部分类" clearable class="w-full">
                  <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属品牌" class="w-full">
                <el-select v-model="queryForm.brandId" placeholder="全部品牌" clearable filterable class="w-full">
                  <el-option v-for="item in brandOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6" class="text-right">
              <el-form-item class="search-actions">
                <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
                <el-button @click="handleReset" :icon="Refresh">重置</el-button>
                <el-button type="success" @click="handleCreate" :icon="Plus">新建产品</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="categoryName" label="分类" min-width="200" align="center" show-overflow-tooltip />
          <el-table-column prop="name" label="产品名称" min-width="100" align="center" show-overflow-tooltip />
          <el-table-column prop="code" label="产品编码" min-width="180" align="center" />
          <el-table-column prop="brandName" label="品牌" min-width="160" align="center" />
          <el-table-column prop="basePrice" label="基础单价(元/㎡)" min-width="180" align="center">
             <template #default="scope">¥ {{ scope.row.basePrice }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" min-width="100" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
                {{ scope.row.status === 'ACTIVE' ? '上架' : '下架' }}
              </el-tag>
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

    <el-dialog v-model="dialogVisible" :title="dialogType === 'create' ? '新建产品' : '编辑产品'" width="600px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="产品分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品品牌" prop="brandId">
          <el-select v-model="form.brandId" placeholder="请选择品牌" style="width: 100%" clearable>
            <el-option v-for="item in brandOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="form.name" placeholder="如：凤铝70系列内开内倒" />
        </el-form-item>
        <el-form-item label="产品编码" prop="code">
          <el-input v-model="form.code" placeholder="选填，内部编码" />
        </el-form-item>
        <el-form-item label="基础单价" prop="basePrice">
          <el-input-number v-model="form.basePrice" :min="0" :precision="2" :step="100" style="width: 200px" />
          <span style="margin-left: 10px; color: #909399">元 / 平方米</span>
        </el-form-item>
        <el-form-item label="可选颜色" prop="colorOptions">
          <el-input v-model="form.colorOptions" placeholder="多个颜色用逗号分隔，如：白色,灰色,木纹" />
        </el-form-item>
        <el-form-item label="可选玻璃" prop="glassOptions">
          <el-input v-model="form.glassOptions" placeholder="如：5+12A+5,5+27A+5" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="ACTIVE">上架</el-radio>
            <el-radio label="INACTIVE">下架</el-radio>
          </el-radio-group>
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
const brandOptions = ref([])

const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  keyword: '',
  categoryId: null,
  brandId: null
})

const form = reactive({
  id: null,
  categoryId: null,
  brandId: null,
  name: '',
  code: '',
  basePrice: 0,
  colorOptions: '',
  glassOptions: '',
  status: 'ACTIVE'
})

const rules = {
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  basePrice: [{ required: true, message: '请输入基础单价', trigger: 'blur' }]
}

onMounted(() => {
  fetchCategories()
  fetchBrands()
  fetchData()
})

const fetchCategories = async () => {
  try {
    const res = await request.get('/product/category/listAll')
    if (res.code === 200) categoryOptions.value = res.data
  } catch (error) {}
}

const fetchBrands = async () => {
  try {
    const res = await request.post('/brand/list', { pageNo: 1, pageSize: 100 })
    if (res.code === 200) brandOptions.value = res.data.list
  } catch (error) {}
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.post('/product/list', queryForm)
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
  queryForm.brandId = null
  queryForm.pageNo = 1
  fetchData()
}

const handleCreate = () => {
  dialogType.value = 'create'
  Object.assign(form, {
    id: null,
    categoryId: null,
    brandId: null,
    name: '',
    code: '',
    basePrice: 0,
    colorOptions: '',
    glassOptions: '',
    status: 'ACTIVE'
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
  ElMessageBox.confirm('确认删除该产品吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
    const res = await request.delete(`/product/${row.id}?currentUserId=${currentUser.id}`)
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
        const res = await request.post(`/product/save?currentUserId=${currentUser.id}`, form)
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
