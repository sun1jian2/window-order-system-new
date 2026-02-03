<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-form-item label="品牌名称">
            <el-input v-model="queryForm.name" placeholder="输入品牌名查询" clearable :prefix-icon="Goods" />
          </el-form-item>
          <el-form-item class="search-actions">
            <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
            <el-button @click="handleReset" :icon="Refresh">重置</el-button>
            <el-button type="success" @click="handleCreate" :icon="Plus">新建品牌</el-button>
            <el-button type="warning" @click="handleExport" :icon="Download">导出</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="name" label="品牌名称" />
          <el-table-column prop="createTime" label="创建时间">
            <template #default="scope">
              {{ scope.row.createTime ? scope.row.createTime.replace('T', ' ') : '' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="220" align="center">
            <template #default="scope">
              <el-button link type="primary" :icon="View" @click="handleDetail(scope.row)">详情</el-button>
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

    <el-dialog v-model="dialogVisible" :title="getDialogTitle()" width="400px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="品牌名称">
          <el-input v-model="form.name" :disabled="dialogType === 'detail'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" v-if="dialogType === 'detail'">关 闭</el-button>
          <template v-else>
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submitForm">确 定</el-button>
          </template>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, Goods, View, Download, Refresh } from '@element-plus/icons-vue'

const router = useRouter()
const currentUser = ref({})
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref('create')

const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  name: ''
})

const form = reactive({
  id: null,
  name: ''
})

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (!userStr) {
    router.push('/login')
    return
  }
  currentUser.value = JSON.parse(userStr)
  if (currentUser.value.role !== 'ADMIN') {
      ElMessage.error('无权访问')
      router.push('/orders')
      return
  }
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.post('/brand/list', queryForm)
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

const handleExport = async () => {
  try {
    const res = await request.post('/brand/export', queryForm)
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

const handleCreate = () => {
  dialogType.value = 'create'
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

const handleDetail = (row) => {
  dialogType.value = 'detail'
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

const getDialogTitle = () => {
    if (dialogType.value === 'create') return '新建品牌'
    if (dialogType.value === 'edit') return '编辑品牌'
    return '品牌详情'
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该品牌吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request.post(`/brand/delete/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('已删除')
      fetchData()
    }
  })
}

const submitForm = async () => {
  try {
    const payload = {
      ...form,
      currentUserId: currentUser.value.id
    }
    const res = await request.post('/brand/save', payload)
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
