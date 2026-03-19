import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import Login from '../views/Login.vue'
import OrderList from '../views/OrderList.vue'
import BrandList from '../views/BrandList.vue'
import UserList from '../views/UserList.vue'
import OrderDetail from '../views/OrderDetail.vue'
import OrderPayments from '../views/OrderPayments.vue'
import Dashboard from '../views/Dashboard.vue'
import Logs from '../views/Logs.vue'
import AfterSalesList from '../views/AfterSalesList.vue'
import CustomerList from '../views/CustomerList.vue'
import RemeasureTaskList from '../views/RemeasureTaskList.vue'
import SalesTargetList from '../views/SalesTargetList.vue'
import MainLayout from '../layout/MainLayout.vue'
import PaymentDetail from '../views/PaymentDetail.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true, title: '首页概览' }
      },
      {
        path: 'orders',
        name: 'OrderList',
        component: OrderList,
        meta: { requiresAuth: true, title: '订单管理' }
      },
      {
        path: 'order/detail/:id',
        name: 'OrderDetail',
        component: OrderDetail,
        meta: { requiresAuth: true, title: '订单详情' }
      },
      {
        path: 'order/payments/:id',
        name: 'OrderPayments',
        component: OrderPayments,
        meta: { requiresAuth: true, title: '支付详情' }
      },
      {
        path: 'order/payment/:id',
        name: 'PaymentDetail',
        component: PaymentDetail,
        meta: { requiresAuth: true, title: '支付详情' }
      },
      {
        path: 'products',
        name: 'ProductList',
        component: () => import('../views/ProductList.vue'),
        meta: { requiresAuth: true, title: '产品列表' }
      },
      {
        path: 'product-categories',
        name: 'ProductCategoryList',
        component: () => import('../views/ProductCategoryList.vue'),
        meta: { requiresAuth: true, title: '产品分类' }
      },
      {
        path: 'brands',
        name: 'BrandList',
        component: BrandList,
        meta: { requiresAuth: true, role: 'ADMIN', title: '品牌管理' }
      },
      {
        path: 'materials',
        name: 'MaterialList',
        component: () => import('../views/MaterialList.vue'),
        meta: { requiresAuth: true, title: '材料库存' }
      },
      {
        path: 'suppliers',
        name: 'SupplierList',
        component: () => import('../views/SupplierList.vue'),
        meta: { requiresAuth: true, title: '供应商管理' }
      },
      {
        path: 'purchase-orders',
        name: 'PurchaseOrderList',
        component: () => import('../views/PurchaseOrderList.vue'),
        meta: { requiresAuth: true, title: '采购单管理' }
      },
      {
        path: 'users',
        name: 'UserList',
        component: UserList,
        meta: { requiresAuth: true, role: 'ADMIN', title: '用户管理' }
      }
      ,
      {
        path: 'customers',
        name: 'CustomerList',
        component: CustomerList,
        meta: { requiresAuth: true, title: '客户管理' }
      },
      {
        path: 'after-sales',
        name: 'AfterSalesList',
        component: AfterSalesList,
        meta: { requiresAuth: true, title: '售后管理' }
      },
      {
        path: 'remeasure-tasks',
        name: 'RemeasureTaskList',
        component: RemeasureTaskList,
        meta: { requiresAuth: true, title: '复尺任务' }
      },
      {
        path: 'financial-reports',
        name: 'FinancialReportList',
        component: () => import('../views/FinancialReportList.vue'),
        meta: { requiresAuth: true, title: '财务报表' }
      },
      {
        path: 'sales-targets',
        name: 'SalesTargetList',
        component: SalesTargetList,
        meta: { requiresAuth: true, title: '销售目标' }
      },
      {
        path: 'export-center',
        name: 'ExportCenter',
        component: () => import('../views/ExportCenter.vue'),
        meta: { requiresAuth: true, title: '导出中心' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: Logs,
        meta: { requiresAuth: true, role: 'ADMIN', title: '操作日志' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    const isAuthenticated = !!userStore.currentUser?.id
    
    if (to.meta.requiresAuth && !isAuthenticated) {
        next('/login')
    } else if (to.meta.role && userStore.currentUser?.role !== to.meta.role) {
        next('/orders') // or 403
    } else {
        next()
    }
})

export default router
