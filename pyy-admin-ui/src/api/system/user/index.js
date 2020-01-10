import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'

/** 注意：这里实际应用时 #号 要替换为 $ */
export const getSysUserById = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser/${id}`, 'get')

export const getSysUserList = (params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser/list`, 'get', params)

export const getSysUserPageList = (page, size, params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser/page?page=${page}&size=${size}`, 'get', params)

export const addSysUser = (sysUser) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser`, 'post', sysUser)

export const updateSysUser = (id, sysUser) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser/${id}`, 'put', sysUser)

export const deleteSysUser = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser/${id}`, 'delete')

export const deleteBatchSysUser = (ids) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysUser/delBatch`, 'delete', ids)
