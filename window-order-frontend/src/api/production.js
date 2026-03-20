import request from '../utils/request'

// 排产计划 API
export function getProductionPlanList(data) {
  return request({
    url: '/production/plan/list',
    method: 'post',
    data
  })
}

export function createProductionPlan(data) {
  return request({
    url: '/production/plan/create',
    method: 'post',
    data
  })
}

export function updateProductionPlan(data) {
  return request({
    url: '/production/plan/update',
    method: 'post',
    data
  })
}

export function deleteProductionPlan(id) {
  return request({
    url: `/production/plan/${id}`,
    method: 'delete'
  })
}

// 生产工序 API
export function getProductionProcessList(data) {
  return request({
    url: '/production/process/list',
    method: 'post',
    data
  })
}

export function createProductionProcess(data) {
  return request({
    url: '/production/process/create',
    method: 'post',
    data
  })
}

export function updateProductionProcess(data) {
  return request({
    url: '/production/process/update',
    method: 'post',
    data
  })
}

export function deleteProductionProcess(id) {
  return request({
    url: `/production/process/${id}`,
    method: 'delete'
  })
}

// 质检记录 API
export function getQcRecordList(data) {
  return request({
    url: '/production/qc/list',
    method: 'post',
    data
  })
}

export function createQcRecord(data) {
  return request({
    url: '/production/qc/create',
    method: 'post',
    data
  })
}

export function updateQcRecord(data) {
  return request({
    url: '/production/qc/update',
    method: 'post',
    data
  })
}

export function deleteQcRecord(id) {
  return request({
    url: `/production/qc/${id}`,
    method: 'delete'
  })
}
