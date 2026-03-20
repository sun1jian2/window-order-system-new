import request from '@/utils/request'

export const listOrders = (data) => request.post('/order/list', data)
export const createOrder = (data) => request.post('/order/create', data)
export const updateOrder = (data) => request.post('/order/update', data)
export const deleteOrder = (id, params) => request.delete(`/order/${id}`, { params })
export const getOrder = (id) => request.get(`/order/detail/${id}`)

export const getDashboardStats = (params) => request.get('/dashboard/stats', { params })

// 合同相关接口
export const generateContract = (data) => request.post('/order/contract/generate', data)
export const getOrderContracts = (orderId) => request.get(`/order/contract/list/${orderId}`)
