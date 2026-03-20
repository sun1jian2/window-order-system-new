<template>
  <div class="side-menu">
    <div class="brand">
      <el-icon class="brand-icon"><House /></el-icon>
      <span class="brand-name" v-if="!isCollapse">窗户订单</span>
    </div>
    <el-menu
      :default-active="activeMenu"
      class="el-menu-vertical"
      :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      router
    >
      <el-menu-item index="/dashboard">
        <el-icon><Odometer /></el-icon>
        <template #title>首页概览</template>
      </el-menu-item>
      
      <el-menu-item index="/orders">
        <el-icon><List /></el-icon>
        <template #title>订单管理</template>
      </el-menu-item>

      <el-menu-item index="/financial-reports" v-if="userStore.currentUser.role === 'ADMIN'">
        <el-icon><DataBoard /></el-icon>
        <template #title>财务报表</template>
      </el-menu-item>

      <el-menu-item index="/sales-targets" v-if="userStore.currentUser.role === 'ADMIN' || userStore.currentUser.role === 'SALES'">
        <el-icon><Trophy /></el-icon>
        <template #title>销售目标</template>
      </el-menu-item>

      <el-menu-item
        index="/remeasure-tasks"
        v-if="
          userStore.currentUser.role === 'INSTALLER' ||
          userStore.currentUser.role === 'ADMIN' ||
          userStore.currentUser.role === 'SALES'
        "
      >
        <el-icon><Tools /></el-icon>
        <template #title>复尺任务</template>
      </el-menu-item>

      <el-menu-item index="/after-sales">
        <el-icon><Service /></el-icon>
        <template #title>售后工单</template>
      </el-menu-item>
      
      <el-menu-item index="/customers">
        <el-icon><UserFilled /></el-icon>
        <template #title>客户档案</template>
      </el-menu-item>

      <el-menu-item index="/users" v-if="userStore.currentUser.role === 'ADMIN'">
        <el-icon><User /></el-icon>
        <template #title>账号管理</template>
      </el-menu-item>
      
      <el-sub-menu index="catalog" v-if="userStore.currentUser.role === 'ADMIN'">
        <template #title>
          <el-icon><Menu /></el-icon>
          <span>产品品牌</span>
        </template>
        <el-menu-item index="/products">产品列表</el-menu-item>
        <el-menu-item index="/product-categories">产品分类</el-menu-item>
        <el-menu-item index="/brands">品牌管理</el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="inventory" v-if="userStore.currentUser.role === 'ADMIN'">
        <template #title>
          <el-icon><Goods /></el-icon>
          <span>库存采购</span>
        </template>
        <el-menu-item index="/materials">材料库存</el-menu-item>
        <el-menu-item index="/suppliers">供应商管理</el-menu-item>
        <el-menu-item index="/purchase-orders">采购单管理</el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="production" v-if="userStore.currentUser.role === 'ADMIN'">
        <template #title>
          <el-icon><DataLine /></el-icon>
          <span>生产管理</span>
        </template>
        <el-menu-item index="/production-plan">排产计划</el-menu-item>
        <el-menu-item index="/production-process">工序报工</el-menu-item>
        <el-menu-item index="/qc-record">质检记录</el-menu-item>
      </el-sub-menu>
      
      <el-menu-item index="/export-center">
        <el-icon><Download /></el-icon>
        <template #title>导出中心</template>
      </el-menu-item>
      
      <el-menu-item index="/logs" v-if="userStore.currentUser.role === 'ADMIN'">
        <el-icon><Document /></el-icon>
        <template #title>日志中心</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { House, Odometer, List, User, Goods, Document, Service, UserFilled, Tools, Trophy, Download, Menu, DataBoard, DataLine } from '@element-plus/icons-vue'

const route = useRoute()
const userStore = useUserStore()

const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})

const activeMenu = computed(() => {
  return route.path
})
</script>

<style scoped>
.side-menu {
  height: 100%;
  background-color: #304156;
  display: flex;
  flex-direction: column;
}

.brand {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b2f3a;
}

.brand-icon {
  margin-right: 8px;
  font-size: 22px;
  color: #409EFF;
}

.brand-name {
  white-space: nowrap;
}

.el-menu-vertical {
  border-right: none;
  flex: 1;
}

:deep(.el-menu-item:hover) {
  background-color: #263445 !important;
}
</style>
