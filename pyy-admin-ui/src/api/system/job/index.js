import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'


export const getJobById = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob/${id}`, 'get')

export const getJobList = (params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob/list`, 'get', params)

export const getJobPageList = (page, size, params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob/page?page=${page}&size=${size}`, 'get', params)

export const addJob = (job) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob`, 'post', job)

export const updateJob = (id, job) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob/${id}`, 'put', job)

export const deleteJob = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob/${id}`, 'delete')

export const deleteBatchJob = (ids) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysJob/delBatch`, 'delete', ids)



