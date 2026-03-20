<template>
  <div class="app-container">
    <div class="main-content">
      <!-- Search -->
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true" :model="queryForm" class="search-form" @keyup.enter="handleSearch">
          <el-row :gutter="20" style="width: 100%">
            <el-col :span="6">
              <el-form-item label="订单号" class="w-full">
                <el-input v-model="queryForm.orderNo" placeholder="输入订单号查询" clearable :prefix-icon="Search" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="客户名" class="w-full">
                <el-input v-model="queryForm.customerName" placeholder="输入姓名搜索" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电话" class="w-full">
                <el-input v-model="queryForm.customerPhone" placeholder="输入电话查询" clearable :prefix-icon="Phone" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="品牌" class="w-full">
                <el-select v-model="queryForm.brand" placeholder="选择品牌" clearable class="w-full">
                  <el-option v-for="item in brandList" :key="item.id" :label="item.name" :value="item.name" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <!-- Second row for extra fields and actions -->
           <el-row :gutter="20" style="width: 100%; margin-top: 10px;">
             <el-col :span="6">
              <el-form-item label="销售员" v-if="userStore.currentUser.role === 'ADMIN'" class="w-full">
                <el-select v-model="queryForm.searchSalespersonId" placeholder="选择销售" clearable filterable class="w-full">
                  <el-option v-for="item in salesList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="安装师傅" v-else-if="userStore.currentUser.role === 'ADMIN' || userStore.currentUser.role === 'SALES'" class="w-full">
                <el-select v-model="queryForm.searchInstallerId" placeholder="选择师傅" clearable filterable class="w-full">
                  <el-option v-for="item in installerList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
                </el-select>
              </el-form-item>
             </el-col>
             <el-col :span="6" v-if="userStore.currentUser.role === 'ADMIN'">
                <el-form-item label="安装师傅" class="w-full">
                  <el-select v-model="queryForm.searchInstallerId" placeholder="选择师傅" clearable filterable class="w-full">
                    <el-option v-for="item in installerList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
                  </el-select>
                </el-form-item>
             </el-col>
             <el-col :span="userStore.currentUser.role === 'ADMIN' ? 6 : (userStore.currentUser.role === 'SALES' ? 12 : 18)" class="text-right">
                <el-form-item class="search-actions">
                  <el-button type="primary" @click="handleSearch" :icon="Search">查询</el-button>
                  <el-button @click="handleReset" :icon="Refresh">重置</el-button>
                  <el-button type="warning" @click="handleExport" :icon="Download">导出</el-button>
                  <el-button type="success" @click="handleCreate" :icon="Plus" v-if="userStore.currentUser.role !== 'INSTALLER'">新建订单</el-button>
                </el-form-item>
             </el-col>
           </el-row>
        </el-form>
      </el-card>

      <!-- Table -->
      <el-card class="table-card" shadow="hover">
        <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
          <el-table-column prop="orderNo" label="订单号" min-width="190" show-overflow-tooltip />
          <el-table-column prop="customerName" label="客户名" min-width="100" />
          <el-table-column prop="customerPhone" label="电话" min-width="130" />
          <el-table-column prop="price" label="价格" min-width="110">
             <template #default="scope">
               <span class="price-text">¥ {{ scope.row.price }}</span>
             </template>
          </el-table-column>
          <el-table-column prop="paidAmount" label="已付" min-width="110">
             <template #default="scope">
               <span class="price-text" style="color: #67c23a">¥ {{ scope.row.paidAmount || 0 }}</span>
             </template>
          </el-table-column>
          <el-table-column label="支付状态" min-width="110">
            <template #default="scope">
               <el-tag size="small" :type="getPaymentStatusType(scope.row.paymentStatus)" effect="light">
                  {{ getPaymentStatusLabel(scope.row.paymentStatus) }}
                </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="80">
            <template #default="scope">
               <el-tag v-if="scope.row.status === 'DRAFT'" type="info" effect="plain">草稿</el-tag>
               <el-tag v-else type="success" effect="plain">已提交</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="制作进度" min-width="90">
            <template #default="scope">
               <el-tag size="small" :type="getProgressType(scope.row.productionProgress)" effect="light">
                  {{ getProgressLabel(scope.row.productionProgress) }}
                </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="物流状态" min-width="90">
            <template #default="scope">
               <el-tag v-if="scope.row.logisticsStatus" size="small" :type="getLogisticsStatusType(scope.row.logisticsStatus)" effect="light">
                  {{ getLogisticsStatusLabel(scope.row.logisticsStatus) }}
                </el-tag>
                <span v-else class="text-placeholder">-</span>
            </template>
          </el-table-column>
          <el-table-column label="安装进度" min-width="90">
            <template #default="scope">
               <el-tag size="small" :type="getProgressType(scope.row.installProgress)" effect="light">
                  {{ getProgressLabel(scope.row.installProgress) }}
                </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="salespersonName" label="销售员" min-width="100" />
          <el-table-column prop="installerName" label="安装师傅" min-width="100">
            <template #default="scope">
              <span v-if="scope.row.installerName">{{ scope.row.installerName }}</span>
              <span v-else class="text-placeholder">未分配</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="260" align="center">
            <template #default="scope">
              <div class="action-cell">
                <el-tooltip content="合同" placement="top" v-if="scope.row.status !== 'DRAFT'">
                  <el-button class="action-btn" circle size="small" type="primary" plain :icon="Document" @click="handleContract(scope.row)" />
                </el-tooltip>
                <el-tooltip content="详情" placement="top">
                  <el-button class="action-btn" circle size="small" type="primary" plain :icon="View" @click="handleDetail(scope.row)" />
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <el-button class="action-btn" circle size="small" type="primary" plain :icon="Edit" @click="handleEdit(scope.row)" />
                </el-tooltip>
                <el-tooltip content="成本核算" placement="top" v-if="scope.row.status !== 'DRAFT' && userStore.currentUser.role === 'ADMIN'">
                  <el-button class="action-btn" circle size="small" type="success" plain :icon="Money" @click="handleCost(scope.row)" />
                </el-tooltip>
                <el-tooltip v-if="userStore.currentUser.role === 'ADMIN' || userStore.currentUser.role === 'SALES'" content="复尺" placement="top">
                  <el-button class="action-btn" circle size="small" type="warning" plain :icon="Tools" @click="handleAssignRemeasure(scope.row)" />
                </el-tooltip>
                <el-tooltip v-if="userStore.currentUser.role === 'ADMIN' || (userStore.currentUser.role === 'SALES' && scope.row.salespersonId === userStore.currentUser.id)" content="删除" placement="top">
                  <el-button class="action-btn" circle size="small" type="danger" plain :icon="Delete" @click="handleDelete(scope.row)" />
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="queryForm.pageNo"
            v-model:page-size="queryForm.pageSize"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="fetchData"
            @current-change="fetchData"
            background
          />
        </div>
      </el-card>
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'create' ? '新建订单' : '编辑订单'" width="800px" top="5vh" destroy-on-close>
      <el-form :model="form" label-width="100px" class="dialog-form">
        <div class="form-section-title">客户信息</div>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="选择客户">
              <el-select 
                v-model="form.customerId" 
                placeholder="直接下拉选择，或输入姓名/手机号搜索" 
                filterable 
                clearable 
                @change="handleCustomerSelect" 
                style="width: 100%">
                <el-option v-for="c in allCustomers" :key="c.id" :label="`${c.name} - ${c.phone}`" :value="c.id">
                  <span style="float: left">{{ c.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ c.phone }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户姓名" required>
              <el-input v-model="form.customerName" :prefix-icon="User" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
             <el-form-item label="联系电话" required>
              <el-input v-model="form.customerPhone" :prefix-icon="Phone" placeholder="请输入电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="安装区域">
            <el-cascader
                v-model="form.regionCodes"
                :options="regionData"
                placeholder="请选择省/市/区"
                style="width: 100%"
                clearable
            />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="form.detailAddress" type="textarea" :rows="2" placeholder="请输入街道、小区、楼号等详细信息" />
        </el-form-item>

        <div class="form-section-title">产品详情</div>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-table :data="form.items" border style="width: 100%; margin-bottom: 15px;" size="default" :header-cell-style="{background:'#f7f9fc', color:'#606266', height: '45px'}">
              <el-table-column label="产品" min-width="180">
                <template #default="scope">
                  <el-select v-model="scope.row.productId" placeholder="选择产品" style="width: 100%" filterable @change="(val) => handleProductChange(val, scope.row)">
                    <el-option v-for="p in productList" :key="p.id" :label="p.name" :value="p.id" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="宽(mm)" width="110">
                <template #default="scope">
                  <el-input-number v-model="scope.row.width" :min="0" :controls="false" placeholder="宽度" style="width: 100%" @change="calculateItemPrice(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="高(mm)" width="110">
                <template #default="scope">
                  <el-input-number v-model="scope.row.height" :min="0" :controls="false" placeholder="高度" style="width: 100%" @change="calculateItemPrice(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="面积(㎡)" width="90" align="center">
                <template #default="scope">
                  <span style="color: #409EFF; font-weight: bold;">{{ scope.row.area ? scope.row.area.toFixed(2) : '0.00' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="数量" width="100">
                <template #default="scope">
                  <el-input-number v-model="scope.row.quantity" :min="1" :controls="false" style="width: 100%" @change="calculateItemPrice(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="单价(元/㎡)" width="110">
                <template #default="scope">
                  <el-input-number v-model="scope.row.unitPrice" :min="0" :controls="false" placeholder="单价" style="width: 100%" @change="calculateItemPrice(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="小计" width="120" align="right">
                <template #default="scope">
                  <span style="color: #f56c6c; font-weight: bold; font-size: 14px;">¥ {{ scope.row.totalPrice ? scope.row.totalPrice.toFixed(2) : '0.00' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="60" align="center" fixed="right">
                <template #default="scope">
                  <el-button type="danger" :icon="Delete" link @click="removeOrderItem(scope.$index)" />
                </template>
              </el-table-column>
              <template #empty>
                <el-empty description="暂无产品明细，请点击下方按钮添加" :image-size="60" />
              </template>
            </el-table>
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background-color: #f8f9fa; padding: 10px 15px; border-radius: 6px; border: 1px dashed #dcdfe6;">
              <el-button type="primary" plain :icon="Plus" @click="addOrderItem">添加产品明细</el-button>
              <div style="display: flex; align-items: center;">
                <span style="margin-right: 15px; color: #606266; font-size: 14px;">合计面积: <strong style="color: #409EFF">{{ totalAreaText }} ㎡</strong></span>
                <span style="color: #606266; font-size: 14px; margin-right: 10px;">订单总价:</span>
                <el-input-number v-model="form.price" :precision="2" :step="100" :min="0" controls-position="right" style="width: 150px" class="total-price-input">
                   <template #prefix>￥</template>
                </el-input-number>
                <el-tooltip content="系统已根据明细自动计算，您也可以手动修改以打折或抹零" placement="top">
                  <el-icon style="margin-left: 8px; color: #909399; cursor: help;"><InfoFilled /></el-icon>
                </el-tooltip>
              </div>
            </div>
          </el-col>
        </el-row>

        <div class="form-section-title">人员信息</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="销售员">
              <el-input :model-value="form.salespersonName || getUserDisplayName(form.salespersonId, salesList)" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装师傅">
              <el-select v-model="form.installerId" placeholder="选择安装师傅" style="width: 100%" filterable>
                <el-option v-for="item in installerList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">订单进度</div>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="订单状态">
              <el-radio-group v-model="form.status" @change="handleStatusChange">
                <el-radio label="DRAFT">草稿</el-radio>
                <el-radio label="SUBMITTED">正式提交</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="dialogType === 'edit'">
          <el-col :span="24">
            <el-form-item label="制作进度">
              <el-select v-model="form.productionProgress" style="width: 100%" :disabled="form.status === 'DRAFT'">
                <el-option label="等待中" value="WAITING" />
                <el-option label="制作中" value="PRODUCING" />
                <el-option label="已完成" value="FINISHED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="dialogType === 'edit'">
          <el-col :span="24">
            <el-form-item label="物流状态">
              <el-select v-model="form.logisticsStatus" style="width: 100%" :disabled="form.status === 'DRAFT' || form.productionProgress !== 'FINISHED'">
                <el-option label="已出库" value="OUTBOUND" />
                <el-option label="送货中" value="SHIPPING" />
                <el-option label="已入库" value="INBOUND" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="dialogType === 'edit'">
          <el-col :span="24">
            <el-form-item label="安装进度">
              <el-select v-model="form.installProgress" style="width: 100%" :disabled="form.status === 'DRAFT'">
                <el-option label="等待中" value="WAITING" />
                <el-option label="已排期" value="SCHEDULED" />
                <el-option label="安装中" value="INSTALLING" :disabled="form.productionProgress !== 'FINISHED'" />
                <el-option label="已完成" value="FINISHED" :disabled="form.productionProgress !== 'FINISHED'" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" v-if="dialogType === 'edit'">
          <el-col :span="12">
            <el-form-item label="预约安装">
              <el-date-picker
                v-model="form.scheduledInstallDate"
                type="datetime"
                placeholder="选择预约安装日期"
                style="width: 100%"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际完成">
              <el-date-picker
                v-model="form.actualInstallEndDate"
                type="datetime"
                placeholder="选择实际完成日期"
                style="width: 100%"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Assign Remeasure Dialog -->
    <el-dialog v-model="assignDialogVisible" title="指派复尺任务" width="500px">
      <el-form :model="assignForm" label-width="100px">
        <el-form-item label="复尺师傅">
          <el-select v-model="assignForm.assigneeId" placeholder="选择师傅" style="width: 100%" filterable>
            <el-option v-for="item in installerList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="assignForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assignDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAssign">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- Cost Dialog -->
    <el-dialog v-model="costDialogVisible" title="订单成本核算" width="500px" destroy-on-close>
      <el-form :model="costForm" label-width="100px">
        <el-form-item label="材料成本(元)">
          <el-input-number v-model="costForm.materialCost" :min="0" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="人工成本(元)">
          <el-input-number v-model="costForm.laborCost" :min="0" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="其他成本(元)">
          <el-input-number v-model="costForm.otherCost" :min="0" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="costForm.remark" type="textarea" :rows="2" />
        </el-form-item>
        <div v-if="costForm.orderAmount" style="margin-top: 20px; padding: 15px; background-color: #f8f9fa; border-radius: 4px;">
          <div style="margin-bottom: 10px;"><strong>订单收入: </strong><span style="color: #409EFF">¥ {{ costForm.orderAmount }}</span></div>
          <div style="margin-bottom: 10px;"><strong>总成本: </strong><span style="color: #E6A23C">¥ {{ (costForm.materialCost + costForm.laborCost + costForm.otherCost).toFixed(2) }}</span></div>
          <div>
            <strong>毛利润: </strong>
            <span :style="{color: costForm.orderAmount - (costForm.materialCost + costForm.laborCost + costForm.otherCost) >= 0 ? '#67C23A' : '#F56C6C', fontWeight: 'bold'}">
              ¥ {{ (costForm.orderAmount - (costForm.materialCost + costForm.laborCost + costForm.otherCost)).toFixed(2) }}
            </span>
          </div>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="costDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitCost">保存核算</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Contract Dialog -->
    <el-dialog v-model="contractDialogVisible" title="订单合同管理" width="600px" destroy-on-close>
      <div v-loading="contractLoading">
        <div v-if="!currentContract" class="text-center py-4" style="text-align: center; padding: 30px 0;">
          <el-empty description="暂无合同">
            <el-button type="primary" @click="handleGenerateContract">生成 PDF 合同</el-button>
          </el-empty>
        </div>
        <div v-else>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="合同编号">{{ currentContract.contractNo }}</el-descriptions-item>
            <el-descriptions-item label="签署状态">
              <el-tag :type="currentContract.signStatus === 'COMPLETED' ? 'success' : (currentContract.signStatus === 'SIGNING' ? 'warning' : 'info')">
                {{ currentContract.signStatus === 'COMPLETED' ? '已签署' : (currentContract.signStatus === 'SIGNING' ? '签署中' : '未签署') }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="合同文件">
              <el-link type="primary" :href="currentContract.pdfUrl" target="_blank">点击查看/下载 PDF</el-link>
            </el-descriptions-item>
          </el-descriptions>
          <div style="margin-top: 20px; text-align: center;">
            <el-button v-if="currentContract.signStatus === 'PENDING' || currentContract.signStatus === 'SIGNING'" type="success" @click="handleSignContract">
              发起/继续在线签署
            </el-button>
            <el-button type="primary" @click="handleCheckSignStatus">
              刷新签署状态
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, User, Phone, Goods, House, SwitchButton, UserFilled, ArrowDown, View, Refresh, Download, Tools, InfoFilled, Money, Document } from '@element-plus/icons-vue'
import { regionData, codeToText } from 'element-china-area-data'
import { listOrders, createOrder, updateOrder, deleteOrder, generateContract, getOrderContracts } from '../api/order'
import { assignRemeasureTask } from '../api/remeasure'
import { listCustomers, getCustomerDetail } from '../api/customer'
import { useUserStore } from '../stores/user'
import request from '@/utils/request'
import { ORDER_STATUS, INSTALL_PROGRESS, PRODUCTION_PROGRESS, LOGISTICS_STATUS } from '../utils/constants'

const router = useRouter()
const userStore = useUserStore()
// Don't destructure currentUser here to maintain reactivity and avoid null errors
// const currentUser = userStore.currentUser 
const route = useRoute()
const loading = ref(false)
const tableData = ref([])
const brandList = ref([])
const productList = ref([])
const salesList = ref([])
const installerList = ref([])
const customerOptions = ref([])
const customerLoading = ref(false)
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref('create')

const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  orderNo: '',
  customerId: null,
  customerName: '',
  customerPhone: '',
  brand: '',
  productionProgress: '',
  logisticsStatus: '',
  searchSalespersonId: null,
  searchInstallerId: null,
  currentUserId: null,
  currentUserRole: ''
})

const form = reactive({
  id: null,
  customerName: '',
  customerPhone: '',
  address: '', // Full display address
  regionCodes: [], // [provCode, cityCode, distCode]
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  brand: '',
  windowType: '',
  color: '',
  glassSpec: '',
  width: 0,
  height: 0,
  price: 0,
  items: [],
  installProgress: 'WAITING',
  productionProgress: 'WAITING',
  scheduledInstallDate: null,
  actualInstallEndDate: null,
  status: 'SUBMITTED',
  salespersonId: null,
  installerId: null,
  currentUserId: null,
  currentUserRole: ''
})

const allCustomers = ref([])

onMounted(async () => {
  // Pinia has user, but check if logged in
  if (!userStore.currentUser?.id) {
    router.push('/login')
    return
  }
  queryForm.currentUserId = userStore.currentUser.id
  queryForm.currentUserRole = userStore.currentUser.role
  
  // Handle customerId from router
  const customerId = route.query.customerId
  if (customerId) {
      queryForm.customerId = Number(customerId)
      // Pre-load customer info for display
      await fetchCustomerDetail(queryForm.customerId)
  }
  
  try {
    await fetchBrands()
  } catch (e) {
    console.error('Fetch brands failed', e)
  }
  
  try {
    await fetchUsers()
  } catch (e) {
    console.error('Fetch users failed', e)
  }
  
  try {
    await fetchProducts()
  } catch (e) {
    console.error('Fetch products failed', e)
  }
  
  try {
    await fetchAllCustomers()
  } catch (e) {
    console.error('Fetch customers failed', e)
  }
  
  fetchData()
})

const fetchCustomerDetail = async (id) => {
    try {
        const res = await getCustomerDetail(id)
        if (res.code === 200 && res.data) {
            // No longer needed for options, just for display if necessary
        }
    } catch(e) {}
}

const fetchAllCustomers = async () => {
    try {
        // Fetch a large number of customers to populate the dropdown
        const res = await listCustomers({ pageNo: 1, pageSize: 1000 })
        if (res.code === 200) {
            allCustomers.value = res.data.list
        }
    } catch (e) {
        console.error(e)
    }
}

const handleCustomerSelect = (val) => {
  if (val) {
    const customer = allCustomers.value.find(c => c.id === val)
    if (customer) {
      form.customerName = customer.name
      form.customerPhone = customer.phone
      if (customer.address) {
         form.detailAddress = customer.address
      }
    }
  } else {
    form.customerName = ''
    form.customerPhone = ''
  }
}

const fetchBrands = async () => {
    try {
        const res = await request.get('/brand/all')
        if (res.code === 200) {
            brandList.value = res.data
        }
    } catch (e) {
        console.error(e)
    }
}

const fetchProducts = async () => {
    try {
        const res = await request.get('/product/listAllActive')
        if (res.code === 200) {
            productList.value = res.data
        }
    } catch (e) {
        console.error(e)
    }
}

const fetchUsers = async () => {
    try {
        const salesRes = await request.get('/auth/role/SALES')
        if (salesRes.code === 200) {
            salesList.value = salesRes.data
        }
        const installerRes = await request.get('/auth/role/INSTALLER')
        if (installerRes.code === 200) {
            installerList.value = installerRes.data
        }
    } catch (e) {
        console.error(e)
    }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listOrders(queryForm)
    if (res.code === 200) {
      tableData.value = res.data.list
      total.value = res.data.total
    }
  } catch (error) {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryForm.pageNo = 1
  fetchData()
}

const handleReset = () => {
  queryForm.orderNo = ''
  queryForm.customerId = null
  queryForm.customerName = ''
  queryForm.customerPhone = ''
  queryForm.brand = ''
  queryForm.searchSalespersonId = null
  queryForm.searchInstallerId = null
  queryForm.pageNo = 1
  fetchData()
}

const handleExport = async () => {
    try {
        const res = await request.post('/dashboard/export', queryForm)
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
            .catch(() => {
                // stay
            })
        }
    } catch (e) {
        console.error(e)
    }
}

const logout = () => {
  userStore.clearUser()
  router.push('/login')
}

const handleCreate = () => {
  dialogType.value = 'create'
  Object.assign(form, {
    id: null,
    customerId: null,
    customerName: '',
    customerPhone: '',
    address: '',
    regionCodes: [],
    province: '',
    city: '',
    district: '',
    detailAddress: '',
    brand: '',
    windowType: '',
    color: '',
    glassSpec: '',
    width: 0,
    height: 0,
    price: 0,
    items: [],
    installProgress: INSTALL_PROGRESS.WAITING,
    productionProgress: PRODUCTION_PROGRESS.WAITING,
    scheduledInstallDate: null,
    actualInstallEndDate: null,
    status: ORDER_STATUS.SUBMITTED,
    salespersonId: userStore.currentUser.id,
    salespersonName: userStore.currentUser.realName || userStore.currentUser.username,
    installerId: null
  })
  
  // 提前加载客户列表用于下拉选择
  if (allCustomers.value.length === 0) {
      fetchAllCustomers() // 获取所有客户
  }
  
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  if (userStore.currentUser.role === 'SALES' && row.salespersonId !== userStore.currentUser.id) {
      ElMessage.warning('您只能修改自己的订单')
      return
  }
  dialogType.value = 'edit'
  
  // Fetch full order details including items
  try {
    const res = await request.get(`/order/detail/${row.id}`)
    if (res.code === 200) {
      Object.assign(form, res.data)
      if (!form.items) form.items = []
    } else {
      Object.assign(form, row)
      if (!form.items) form.items = []
    }
  } catch (e) {
    Object.assign(form, row)
    if (!form.items) form.items = []
  }

  if (!form.salespersonId) {
      form.salespersonId = userStore.currentUser.id
  }
  
  // Handle address parsing
  // Priority: Use individual fields if available, else fallback to regionCodes, else empty
  if (row.province && row.city && row.district) {
      form.regionCodes = [row.province, row.city, row.district]
  } else if (row.regionCodes) {
      if (Array.isArray(row.regionCodes)) {
          form.regionCodes = row.regionCodes
      } else {
          form.regionCodes = row.regionCodes.split(',')
      }
  } else {
      form.regionCodes = []
      // Legacy compatibility: if no structured address, put full address in detail
      if (!row.detailAddress && row.address) {
          form.detailAddress = row.address
      }
  }
  
  // Default status if missing
  if (!form.status) {
      form.status = ORDER_STATUS.SUBMITTED
  }

  // Enforce DRAFT logic on open
  if (form.status === ORDER_STATUS.DRAFT) {
      form.installProgress = INSTALL_PROGRESS.WAITING
      form.productionProgress = PRODUCTION_PROGRESS.WAITING
  }
  
  dialogVisible.value = true
}

const handleDetail = (row) => {
    router.push(`/order/detail/${row.id}`)
}

const handleDelete = (row) => {
  // Permission check logic
  let canDelete = false
  if (userStore.currentUser.role === 'ADMIN') {
      canDelete = true
  } else if (userStore.currentUser.role === 'SALES' && row.salespersonId === userStore.currentUser.id) {
      canDelete = true
  }
  
  if (!canDelete) {
      ElMessage.warning('无权删除此订单')
      return
  }
  
  ElMessageBox.confirm('确认删除该订单吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await deleteOrder(row.id, {
        currentUserId: userStore.currentUser.id,
        currentUserRole: userStore.currentUser.role
    })
    if (res.code === 200) {
      ElMessage.success('已删除')
      fetchData()
    }
  })
}

const handleStatusChange = (val) => {
    if (val === ORDER_STATUS.DRAFT) {
        form.installProgress = INSTALL_PROGRESS.WAITING
        form.productionProgress = PRODUCTION_PROGRESS.WAITING
    }
}

// Product Items Methods
const addOrderItem = () => {
  form.items.push({
    productId: null,
    width: 0,
    height: 0,
    area: 0,
    quantity: 1,
    unitPrice: 0,
    totalPrice: 0,
    color: '',
    glassSpec: ''
  })
}

const removeOrderItem = (index) => {
  form.items.splice(index, 1)
  recalculateTotal()
}

const handleProductChange = (productId, row) => {
  const product = productList.value.find(p => p.id === productId)
  if (product) {
    row.unitPrice = product.basePrice
    row.color = product.colorOptions ? product.colorOptions.split(',')[0] : ''
    row.glassSpec = product.glassOptions ? product.glassOptions.split(',')[0] : ''
    calculateItemPrice(row)
  }
}

const calculateItemPrice = (row) => {
  if (row.width && row.height) {
    row.area = (row.width / 1000) * (row.height / 1000)
  } else {
    row.area = 0
  }
  
  if (row.area && row.unitPrice && row.quantity) {
    row.totalPrice = row.area * row.unitPrice * row.quantity
  } else {
    row.totalPrice = 0
  }
  
  recalculateTotal()
}

const recalculateTotal = () => {
  let total = 0
  form.items.forEach(item => {
    total += Number(item.totalPrice || 0)
  })
  
  if (total > 0) {
    form.price = Number(total.toFixed(2))
  }
}

const totalAreaText = computed(() => {
  let totalArea = 0
  if (form.items && form.items.length > 0) {
    form.items.forEach(item => {
      totalArea += Number(item.area || 0) * Number(item.quantity || 1)
    })
  }
  return totalArea.toFixed(2)
})

const submitForm = async () => {
  // Attach auth info
  form.currentUserId = userStore.currentUser.id
  form.currentUserRole = userStore.currentUser.role
  
  // Enforce DRAFT logic before submit
  if (form.status === ORDER_STATUS.DRAFT) {
      form.installProgress = INSTALL_PROGRESS.WAITING
      form.productionProgress = PRODUCTION_PROGRESS.WAITING
  }
  
  // Construct display address and split codes
  if (form.regionCodes && form.regionCodes.length > 0) {
      const regionText = form.regionCodes.map(code => codeToText[code]).join(' / ')
      form.address = `${regionText} ${form.detailAddress || ''}`
      
      // Split into individual fields
      if (form.regionCodes.length >= 1) form.province = form.regionCodes[0]
      if (form.regionCodes.length >= 2) form.city = form.regionCodes[1]
      if (form.regionCodes.length >= 3) form.district = form.regionCodes[2]
  } else {
      form.address = form.detailAddress
      form.province = ''
      form.city = ''
      form.district = ''
  }
  
  const payload = { ...form }
  // We still send regionCodes as string for backward compatibility or just ignore it on backend if we use individual fields
  if (payload.regionCodes && Array.isArray(payload.regionCodes)) {
      payload.regionCodes = payload.regionCodes.join(',')
  }
  
  try {
    const res = dialogType.value === 'create' ? await createOrder(payload) : await updateOrder(payload)
    if (res.code === 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    // handled by interceptor
  }
}

const assignDialogVisible = ref(false)
const assignForm = reactive({
    orderId: null,
    assigneeId: null,
    remark: ''
})

// Cost related
const costDialogVisible = ref(false)
const costForm = reactive({
  orderId: null,
  orderAmount: 0,
  materialCost: 0,
  laborCost: 0,
  otherCost: 0,
  remark: ''
})

const handleAssignRemeasure = (row) => {
    assignForm.orderId = row.id
    assignForm.assigneeId = null
    assignForm.remark = ''
    assignDialogVisible.value = true
}

const submitAssign = async () => {
    if (!assignForm.assigneeId) {
        ElMessage.warning('请选择复尺师傅')
        return
    }
    try {
        const res = await assignRemeasureTask(assignForm)
        if (res.code === 200) {
            ElMessage.success('指派成功')
            assignDialogVisible.value = false
        } else {
            ElMessage.error(res.message)
        }
    } catch (e) {}
}

const getUserDisplayName = (id, listRef) => {
  if (!id || !listRef?.value) return ''
  const u = listRef.value.find(item => item.id === id)
  return u ? (u.realName || u.username) : ''
}

// Contract Methods
const contractDialogVisible = ref(false)
const contractLoading = ref(false)
const currentContract = ref(null)
const currentOrderForContract = ref(null)

const handleContract = async (row) => {
  currentOrderForContract.value = row
  contractDialogVisible.value = true
  contractLoading.value = true
  currentContract.value = null
  try {
    const res = await getOrderContracts(row.id)
    if (res.code === 200 && res.data && res.data.length > 0) {
      currentContract.value = res.data[res.data.length - 1] // 取最新的一条
    }
  } catch (e) {
    console.error(e)
  } finally {
    contractLoading.value = false
  }
}

const handleGenerateContract = async () => {
  if (!currentOrderForContract.value) return
  contractLoading.value = true
  try {
    const res = await generateContract({
      orderId: currentOrderForContract.value.id,
      remark: '系统自动生成'
    })
    if (res.code === 200) {
      ElMessage.success('合同生成成功')
      currentContract.value = res.data
    } else {
      ElMessage.error(res.message || '合同生成失败')
    }
  } catch (e) {
    console.error(e)
  } finally {
    contractLoading.value = false
  }
}

const handleSignContract = () => {
  if (currentContract.value && currentContract.value.signUrl) {
    window.open(currentContract.value.signUrl, '_blank')
    ElMessageBox.confirm('是否已在第三方平台完成签署？', '签署确认', {
      confirmButtonText: '已完成',
      cancelButtonText: '未完成/取消',
      type: 'info'
    }).then(async () => {
      // 模拟回调更新状态
      try {
        await request.post('/order/contract/sign-callback', {
          contractNo: currentContract.value.contractNo,
          status: 'COMPLETED'
        })
        ElMessage.success('签署状态已更新')
        handleContract(currentOrderForContract.value)
      } catch (e) {
        console.error(e)
      }
    }).catch(async () => {
      try {
        await request.post('/order/contract/sign-callback', {
          contractNo: currentContract.value.contractNo,
          status: 'SIGNING'
        })
        handleContract(currentOrderForContract.value)
      } catch (e) {}
    })
  }
}

const handleCheckSignStatus = () => {
  handleContract(currentOrderForContract.value)
}

// Helpers
const getProgressType = (status) => {
  const map = {
    'WAITING': 'info',
    'SCHEDULED': 'warning',
    'INSTALLING': 'primary',
    'PRODUCING': 'primary',
    'FINISHED': 'success'
  }
  return map[status] || 'info'
}

const getProgressLabel = (status) => {
  const map = {
    'WAITING': '等待中',
    'SCHEDULED': '已排期',
    'INSTALLING': '安装中',
    'PRODUCING': '制作中',
    'FINISHED': '已完成'
  }
  return map[status] || status
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

const getLogisticsStatusType = (status) => {
    const map = {
        'OUTBOUND': 'warning',
        'SHIPPING': 'primary',
        'INBOUND': 'success'
    }
    return map[status] || 'info'
}

const getLogisticsStatusLabel = (status) => {
    const map = {
        'OUTBOUND': '已出库',
        'SHIPPING': '送货中',
        'INBOUND': '已入库'
    }
    return map[status] || status
}

// Cost Methods
const handleCost = async (row) => {
  costForm.orderId = row.id
  costForm.orderAmount = row.price || 0
  costForm.materialCost = 0
  costForm.laborCost = 0
  costForm.otherCost = 0
  costForm.remark = ''
  
  try {
    const res = await request.get(`/inventory/cost/order/${row.id}`)
    if (res.code === 200 && res.data) {
      costForm.materialCost = res.data.materialCost || 0
      costForm.laborCost = res.data.laborCost || 0
      costForm.otherCost = res.data.otherCost || 0
      costForm.remark = res.data.remark || ''
    }
  } catch (e) {}
  
  costDialogVisible.value = true
}

const submitCost = async () => {
  try {
    const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
    const res = await request.post(`/inventory/cost/save?currentUserId=${currentUser.id}`, costForm)
    if (res.code === 200) {
      ElMessage.success('成本核算保存成功')
      costDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
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
}

.price-text {
  color: #f56c6c;
  font-weight: bold;
}

.text-placeholder {
  color: #909399;
  font-size: 12px;
}

.action-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.action-btn {
  box-shadow: 0 10px 22px rgba(17, 24, 39, 0.08);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 30px rgba(17, 24, 39, 0.14);
}

.status-tags {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}

.form-section-title {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
  margin: 10px 0 15px 0;
  padding-left: 10px;
  border-left: 3px solid #409EFF;
  line-height: 1.2;
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

.total-price-input :deep(.el-input__inner) {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}
</style>
