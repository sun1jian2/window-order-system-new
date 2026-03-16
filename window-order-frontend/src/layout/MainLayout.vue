<template>
  <div class="app-wrapper">
    <div class="sidebar-container" :class="{ 'collapsed': isCollapse }">
      <SideMenu :is-collapse="isCollapse" />
    </div>
    
    <div class="main-container" :class="{ 'collapsed': isCollapse }">
      <div class="header-container">
        <div class="left-section">
          <div class="hamburger" @click="toggleCollapse">
            <el-icon :size="20"><component :is="isCollapse ? 'Expand' : 'Fold'" /></el-icon>
          </div>
          <div class="page-title">{{ route.meta.title }}</div>
        </div>
        <HeaderBar :show-brand="false" />
      </div>
      
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { Expand, Fold } from '@element-plus/icons-vue'
import SideMenu from '../components/SideMenu.vue'
import HeaderBar from '../components/HeaderBar.vue'

const route = useRoute()
const isCollapse = ref(false)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.app-wrapper {
  display: flex;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.sidebar-container {
  width: 210px;
  height: 100%;
  background-color: #304156;
  transition: width 0.3s;
  overflow-y: auto;
  overflow-x: hidden;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  z-index: 1001;
}

.sidebar-container.collapsed {
  width: 64px;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  transition: margin-left 0.3s;
  background-color: #f0f2f5;
}

.header-container {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0;
  z-index: 1000;
}

.left-section {
  display: flex;
  align-items: center;
  height: 100%;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-left: 10px;
}

.hamburger {
  padding: 0 15px;
  height: 100%;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: background .3s;
}

.hamburger:hover {
  background: rgba(0, 0, 0, .025);
}

.app-main {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  position: relative;
}

/* Transitions */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>