import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'


export const getDeptById = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysDept/${id}`, 'get')

export const getDeptTreeList = (params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysDept/treeList`, 'get', params)

export const addDept = (sysDept) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysDept`, 'post', sysDept)

export const updateDept = (id, sysDept) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysDept/${id}`, 'put', sysDept)

export const deleteDept = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysDept/${id}`, 'delete')



