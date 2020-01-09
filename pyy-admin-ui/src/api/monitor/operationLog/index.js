import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'

export const getExceptionDetailById = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysLog/${id}`, 'get')

export const getOperationLogPageList = (page, size, params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysLog/page?page=${page}&size=${size}`, 'get', params)





