<template>
  <div class="detail-container">
    <div class="header">
      <el-page-header @back="$router.back()">
        <template #content>
          <span class="text-large font-600 mr-3"> 收款记录 </span>
        </template>
        <template #extra>
            <el-button type="primary" @click="handlePayment" v-if="remainingAmount > 0.01">添加收款</el-button>
        </template>
      </el-page-header>
    </div>

    <div class="content" v-loading="loading">
      <el-card class="box-card" shadow="never">
        <template #header>
            <div class="card-header">
                <span>订单财务概览</span>
                <div>
                     <el-tag :type="getPaymentStatusType(order.paymentStatus)" class="mr-2">{{ getPaymentStatusLabel(order.paymentStatus) }}</el-tag>
                </div>
            </div>
        </template>
        <el-descriptions :column="3" border>
             <el-descriptions-item label="订单总额">
                 <span class="price-text">¥ {{ order.price || 0 }}</span>
             </el-descriptions-item>
             <el-descriptions-item label="已付金额">
                 <span class="price-text" style="color: #67c23a">¥ {{ order.paidAmount || 0 }}</span>
             </el-descriptions-item>
             <el-descriptions-item label="待付金额">
                 <span class="price-text" style="color: #e6a23c">¥ {{ remainingAmount.toFixed(2) }}</span>
             </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="box-card mt-4" shadow="hover">
        <template #header>
            <div class="card-header">
                <span>收款记录明细</span>
            </div>
        </template>
        <div class="edge-to-edge">
            <el-table :data="paymentList" stripe size="default" class="receipt-table">
                <el-table-column prop="payTime" label="收款时间" width="200" align="center" />
                <el-table-column prop="amount" label="收款金额" width="180" align="right">
                    <template #default="scope">
                        <span class="amount">¥ {{ scope.row.amount }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="payMethod" label="支付方式" width="160" align="center">
                  <template #default="scope">
                    <el-tag :type="getPayMethodTagType(scope.row.payMethod)" effect="light" round>{{ scope.row.payMethod }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createByName" label="操作人" width="140" align="center">
                    <template #default="scope">
                        <el-avatar :size="24" :icon="UserFilled" class="mr-2" style="vertical-align: middle; background-color: #409EFF;" />
                        <span>{{ scope.row.createByName }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
                <el-table-column label="附件" min-width="200">
                    <template #default="scope">
                        <div v-if="getAttachments(scope.row).length > 0" class="attachment-list">
                            <div v-for="url in getAttachments(scope.row)" :key="url" class="list-attachment-item">
                                <el-image 
                                    v-if="isImage(url)"
                                    :src="url" 
                                    :preview-src-list="[url]"
                                    fit="cover"
                                    class="list-attachment-image"
                                    preview-teleported
                                />
                                <a v-else :href="url" target="_blank" class="list-attachment-link">
                                    <el-icon><Link /></el-icon>
                                    <span>附件</span>
                                </a>
                            </div>
                        </div>
                        <span v-else class="text-gray">-</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
      </el-card>
    </div>

    <!-- Payment Dialog -->
    <el-dialog v-model="paymentDialogVisible" title="添加收款" width="400px">
        <el-form :model="paymentForm" label-width="80px">
            <el-form-item label="收款金额">
                <el-input-number v-model="paymentForm.amount" :min="0" :precision="2" :step="1" style="width: 100%" />
            </el-form-item>
            <el-form-item label="支付方式">
                <el-select v-model="paymentForm.payMethod" style="width: 100%">
                    <el-option label="微信" value="微信" />
                    <el-option label="支付宝" value="支付宝" />
                    <el-option label="现金" value="现金" />
                    <el-option label="银行转账" value="银行转账" />
                </el-select>
            </el-form-item>
            <el-form-item label="付款凭证">
                <el-upload
                  action="/api/file/upload"
                  :headers="uploadHeaders"
                  :file-list="uploadFileList"
                  :on-success="handleUploadSuccess"
                  :on-remove="handleUploadRemove"
                  multiple
                  :limit="10"
                  accept=".png,.jpg,.jpeg,.gif,.pdf,.doc,.docx,.xls,.xlsx"
                  list-type="picture-card"
                >
                  <el-icon><Plus /></el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model="paymentForm.remark" type="textarea" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="paymentDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitPayment">确定</el-button>
            </span>
        </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { createPayment, listPayments } from '@/api/payment'
import { ElMessage } from 'element-plus'
import { Plus, UserFilled, Link } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref({})
const paymentList = ref([])
const paymentDialogVisible = ref(false)
const paymentForm = ref({
    amount: 0,
    payMethod: '微信',
    remark: '',
    attachments: []
})
const uploadHeaders = ref({
  Authorization: `Bearer ${localStorage.getItem('token') || ''}`
})
const uploadFileList = ref([])

const remainingAmount = computed(() => {
    const price = Number(order.value.price || 0)
    const paid = Number(order.value.paidAmount || 0)
    return Math.max(0, price - paid)
})

onMounted(() => {
    const id = route.params.id
    if (id) {
        fetchDetail(id)
        fetchPayments(id)
    }
})

const fetchDetail = async (id) => {
    loading.value = true
    try {
        const res = await request.get(`/order/detail/${id}`)
        if (res.code === 200) {
            order.value = res.data
        } else {
            ElMessage.error(res.message)
        }
    } catch (e) {
        // handled
    } finally {
        loading.value = false
    }
}

const fetchPayments = async (id) => {
    try {
        const res = await listPayments(id)
        if (res.code === 200) {
            paymentList.value = res.data
        }
    } catch(e) {}
}

const handlePayment = () => {
    paymentForm.value = { amount: 0, payMethod: '微信', remark: '', attachments: [] }
    uploadFileList.value = []
    paymentDialogVisible.value = true
}

const submitPayment = async () => {
    if (paymentForm.value.amount <= 0) {
        ElMessage.warning('金额必须大于0')
        return
    }
    const price = Number(order.value.price || 0)
    const paid = Number(order.value.paidAmount || 0)
    const add = Number(paymentForm.value.amount || 0)
    if (price > 0 && paid + add > price) {
        ElMessage.warning('收款总额不能超过订单总额')
        return
    }
    try {
        const res = await createPayment({
            orderId: order.value.id,
            amount: paymentForm.value.amount,
            payMethod: paymentForm.value.payMethod,
            remark: paymentForm.value.remark || '',
            attachments: paymentForm.value.attachments || []
        })
        if (res.code === 200) {
            ElMessage.success('收款成功')
            paymentDialogVisible.value = false
            fetchDetail(order.value.id) // refresh order status
            fetchPayments(order.value.id)
        } else {
            ElMessage.error(res.message)
        }
    } catch(e) {}
}

const openPaymentPage = (row) => {
  if (!row || !order.value?.id) return
  router.push({ name: 'PaymentDetail', params: { id: row.id }, query: { orderId: order.value.id } })
}

const parseAttachments = (value) => {
  if (!value) return []
  return value.split(',').map(s => s.trim()).filter(Boolean)
}

const getAttachments = (row) => {
  if (!row) return []
  if (Array.isArray(row.attachmentList)) return row.attachmentList
  if (typeof row.attachments === 'string') return parseAttachments(row.attachments)
  return []
}

const isImage = (url) => {
  if (!url) return false
  const ext = url.split('.').pop().toLowerCase()
  return ['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)
}

const handleUploadSuccess = (response, file, fileList) => {
  if (response && response.code === 200) {
    const url = response.data
    paymentForm.value.attachments.push(url)
    uploadFileList.value = fileList.map(f => ({
      name: f.name,
      url: f.response?.data || f.url
    }))
  } else {
    ElMessage.error('上传失败')
  }
}

const handleUploadRemove = (file, fileList) => {
  const url = file.url || (file.response && file.response.data)
  if (url) {
    paymentForm.value.attachments = paymentForm.value.attachments.filter(u => u !== url)
  }
  uploadFileList.value = fileList.map(f => ({
    name: f.name,
    url: f.response?.data || f.url
  }))
}

const getPaymentStatusType = (status) => {
  const map = {
    'UNPAID': 'danger',
    'PARTIAL': 'warning',
    'PAID': 'success'
  }
  return map[status] || 'info'
}

const getPaymentStatusLabel = (status) => {
  const map = {
    'UNPAID': '未支付',
    'PARTIAL': '部分支付',
    'PAID': '已付清'
  }
  return map[status] || '未支付'
}

const getPayMethodTagType = (method) => {
  const map = {
    '微信': 'success',
    '支付宝': 'primary',
    '现金': 'warning',
    '银行转账': 'info'
  }
  return map[method] || 'info'
}
</script>

<style scoped>
.detail-container {
    background-color: transparent;
    min-height: 100%;
    display: flex;
    flex-direction: column;
}

.header {
    background-color: #fff;
    padding: 16px 24px;
    margin: 0;
    width: 100%;
    box-sizing: border-box;
    border-radius: 4px;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.content {
    max-width: 100%;
    margin: 24px auto;
    width: 100%;
    box-sizing: border-box;
    padding: 0;
}

.box-card {
    margin-bottom: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
}

.price-text {
    color: #f56c6c;
    font-weight: bold;
    font-size: 16px;
}

.mt-4 {
    margin-top: 16px;
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
}

.edge-to-edge {
  margin-left: -20px;
  margin-right: -20px;
}

:deep(.receipt-table) {
  border-radius: 8px;
  overflow: hidden;
}
:deep(.receipt-table .el-table__header-wrapper th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
  height: 50px;
}
:deep(.receipt-table .el-table__body tr) {
  height: 60px;
}
:deep(.receipt-table .el-table__body tr:hover td) {
  background-color: #f0f9eb !important;
}
.amount {
  color: #67c23a;
  font-weight: bold;
  font-size: 15px;
  font-family: 'DIN Alternate', 'Helvetica Neue', Helvetica, Arial, sans-serif;
}
.mr-2 {
    margin-right: 8px;
}
.attachment-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}
.list-attachment-item {
    display: flex;
    align-items: center;
}
.list-attachment-image {
    width: 40px;
    height: 40px;
    border-radius: 4px;
    border: 1px solid #e4e7ed;
    cursor: pointer;
}
.list-attachment-link {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    color: #409EFF;
    text-decoration: none;
    font-size: 13px;
    background: #ecf5ff;
    padding: 2px 8px;
    border-radius: 4px;
}
.list-attachment-link:hover {
    color: #66b1ff;
}
.text-gray {
    color: #909399;
}
</style>
