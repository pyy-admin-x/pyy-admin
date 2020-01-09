import { GENERATE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'


export const getDataSourceById = (id) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/dataSource/${id}`, 'get')

export const getDataSourceList = (params) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/dataSource/list`, 'get', params)

export const getDataSourcePageList = (page, size, params) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/dataSource/page?page=${page}&size=${size}`, 'get', params)

export const addDataSource = (dataSource) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/dataSource`, 'post', dataSource)

export const updateDataSource = (id, dataSource) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/dataSource/${id}`, 'put', dataSource)

export const deleteDataSource = (id) => createAPI(`${GENERATE_SERVER_API_GATEWAY_URL}/dataSource/${id}`, 'delete')



