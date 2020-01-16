import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'

/** 注意：这里实际应用时 #号 要替换为 $ */
export const getRoleById = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/${id}`, 'get')

export const getRoleList = (params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/list`, 'get', params)

export const getRolePageList = (page, size, params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/page?page=${page}&size=${size}`, 'get', params)

export const addRole = (sysRole) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole`, 'post', sysRole)

export const updateRole = (id, sysRole) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/${id}`, 'put', sysRole)

export const deleteRole = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/${id}`, 'delete')

export const deleteBatchRole = (ids) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/delBatch`, 'delete', ids)

export const setPermission = (id, menuIds) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysRole/${id}/permissions`, 'put', menuIds)

