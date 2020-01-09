import { GENERATE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'

export const testConnection = (params) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/testConnection`, 'post', params)

export const getGenerateModel = (params) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/getGenerateModel`, 'post', params)

export const getTables = (params) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/getTables`, 'post', params)

export const generateModels = (params) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/generateModels`, 'post', params)

