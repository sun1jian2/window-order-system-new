<template>
  <div class="mock-sign-container">
    <div class="sign-header">
      <div class="logo">
        <el-icon :size="24" color="#409EFF"><EditPen /></el-icon>
        <span>模拟电子签章平台 (法大大/上机签)</span>
      </div>
      <div class="actions">
        <el-button @click="handleCancel">取消签署</el-button>
        <el-button type="primary" :loading="signing" @click="handleSign">
          确认并签署
        </el-button>
      </div>
    </div>

    <div class="sign-content">
      <div class="contract-info">
        <el-alert
          title="您正在进行在线合同签署"
          type="info"
          show-icon
          :closable="false"
          description="请仔细阅读下方的合同内容。确认无误后，点击右上角的【确认并签署】按钮完成操作。"
          style="margin-bottom: 20px;"
        />
        <div class="info-card">
          <p><strong>合同编号：</strong>{{ contractNo }}</p>
          <p><strong>签署方：</strong>客户 (模拟)</p>
          <p><strong>签署时间：</strong>{{ currentTime }}</p>
        </div>
      </div>

      <div class="pdf-viewer-container">
        <div v-if="pdfUrl" class="pdf-iframe-wrapper">
          <iframe :src="pdfUrl" frameborder="0" width="100%" height="100%"></iframe>
        </div>
        <div v-else class="empty-pdf">
          <el-empty description="无法加载合同文件" />
        </div>
      </div>
    </div>

    <!-- 签署成功结果弹窗 -->
    <el-dialog
      v-model="successDialogVisible"
      title="签署完成"
      width="400px"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      center
    >
      <div class="success-content">
        <el-icon :size="60" color="#67C23A"><SuccessFilled /></el-icon>
        <h3>合同签署成功！</h3>
        <p>页面将在 {{ countdown }} 秒后自动关闭，您也可以手动关闭本页面。</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="closeWindow">直接关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { EditPen, SuccessFilled } from '@element-plus/icons-vue'
import request from '@/utils/request'
import dayjs from 'dayjs'

const route = useRoute()
const contractNo = ref('')
const pdfUrl = ref('')
const currentTime = ref(dayjs().format('YYYY-MM-DD HH:mm:ss'))
const signing = ref(false)

const successDialogVisible = ref(false)
const countdown = ref(3)
let timer = null

onMounted(() => {
  contractNo.value = route.query.contractNo || '未知合同编号'
  pdfUrl.value = route.query.pdfUrl || ''
  
  // 更新当前时间
  setInterval(() => {
    currentTime.value = dayjs().format('YYYY-MM-DD HH:mm:ss')
  }, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const handleSign = async () => {
  if (!contractNo.value || contractNo.value === '未知合同编号') {
    ElMessage.error('无效的合同编号')
    return
  }

  signing.value = true
  try {
    // 模拟向第三方平台提交签名，然后第三方平台回调我们的系统
    // 这里我们直接调用后端的 sign-callback 接口来模拟这个回调过程
    const res = await request.post('/order/contract/sign-callback', {
      contractNo: contractNo.value,
      status: 'COMPLETED'
    })
    
    if (res.code === 200) {
      successDialogVisible.value = true
      startCountdown()
    } else {
      ElMessage.error(res.message || '签署失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('签署过程中发生错误')
  } finally {
    signing.value = false
  }
}

const handleCancel = () => {
  window.close()
  // 如果浏览器拦截了 window.close()
  ElMessage.info('您可以直接关闭当前浏览器标签页')
}

const startCountdown = () => {
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
      closeWindow()
    }
  }, 1000)
}

const closeWindow = () => {
  window.close()
  // 提示用户手动关闭
  document.body.innerHTML = '<div style="display:flex;justify-content:center;align-items:center;height:100vh;font-size:20px;color:#606266;">签署完成，请关闭本页面并返回系统刷新状态。</div>'
}
</script>

<style scoped>
.mock-sign-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.sign-header {
  height: 60px;
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30px;
  z-index: 10;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.sign-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  overflow: hidden;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.info-card {
  background-color: #ffffff;
  padding: 15px 20px;
  border-radius: 8px;
  display: flex;
  gap: 40px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}

.info-card p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.pdf-viewer-container {
  flex: 1;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.pdf-iframe-wrapper {
  flex: 1;
  width: 100%;
  height: 100%;
}

.empty-pdf {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.success-content {
  text-align: center;
  padding: 20px 0;
}

.success-content h3 {
  margin: 20px 0 10px;
  color: #303133;
}

.success-content p {
  color: #909399;
  font-size: 14px;
}
</style>
