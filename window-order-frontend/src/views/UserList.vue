<template>
  <div class="app-container">
    <div class="main-content">
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form">
          <el-form-item label="用户名">
            <el-input v-model="queryForm.username" placeholder="输入用户名" clearable :prefix-icon="User" />
          </el-form-item>
          <el-form-item label="真实姓名">
             <el-input v-model="queryForm.realName" placeholder="输入真实姓名" clearable :prefix-icon="User" />
          </el-form-item>
          <el-form-item class="search-actions">
            <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
            <el-button @click="handleReset" :icon="Refresh">重置</el-button>
            <el-button type="success" @click="handleCreate" :icon="Plus">新建账号</el-button>
            <el-button type="warning" @click="handleExport" :icon="Download">导出</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="realName" label="真实姓名" />
          <el-table-column prop="role" label="角色">
             <template #default="scope">
                <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'primary'">{{ scope.row.roleName || scope.row.role }}</el-tag>
             </template>
          </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="getDialogTitle()" width="500px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="dialogType === 'detail'" />
        </el-form-item>
        <el-form-item label="密码" v-if="dialogType !== 'detail'">
          <el-input v-model="form.password" type="password" placeholder="留空则不修改(默认123456)" show-password />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" :disabled="dialogType === 'detail'" />
        </el-form-item>
        <el-form-item label="角色">
           <el-select v-model="form.role" style="width: 100%" placeholder="请选择角色" :disabled="dialogType === 'detail'">
              <el-option v-for="item in roleList" :key="item.id" :label="item.roleName + ' (' + item.roleCode + ')'" :value="item.roleCode" />
           </el-select>
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
import { Search, Plus, Edit, Delete, User, View, Download, Refresh } from '@element-plus/icons-vue'

const router = useRouter()
const currentUser = ref({})
const loading = ref(false)
const tableData = ref([])
const roleList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref('create')

const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  username: '',
  realName: ''
})

const form = reactive({
  id: null,
  username: '',
  password: '',
  realName: '',
  role: ''
})

onMounted(async () => {
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
  await fetchRoles()
  fetchData()
})

const fetchRoles = async () => {
    try {
        const res = await request.get('/role/all')
        if (res.code === 200) {
            roleList.value = res.data
        }
    } catch (e) {
        console.error(e)
    }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.post('/auth/list', queryForm)
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
  queryForm.username = ''
  queryForm.realName = ''
  queryForm.pageNo = 1
  fetchData()
}

const handleExport = async () => {
  try {
    const res = await request.post('/auth/export', queryForm)
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
  Object.assign(form, {
      id: null,
      username: '',
      password: '',
      realName: '',
      role: 'SALES' // Default
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  form.password = '' // Don't show password
  dialogVisible.value = true
}

const handleDetail = (row) => {
  dialogType.value = 'detail'
  Object.assign(form, row)
  form.password = '' 
  dialogVisible.value = true
}

const getDialogTitle = () => {
    if (dialogType.value === 'create') return '新建账号'
    if (dialogType.value === 'edit') return '编辑账号'
    return '账号详情'
}

const handleDelete = (row) => {
  if (row.username === 'admin') {
      ElMessage.warning('不能删除超级管理员')
      return
  }
  ElMessageBox.confirm('确认删除该账号吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request.post(`/auth/delete/${row.id}`)
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
    const res = await request.post('/auth/save', payload)
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
