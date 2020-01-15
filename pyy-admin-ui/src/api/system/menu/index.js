import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'


export const getMenuById = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysMenu/${id}`, 'get')

export const getMenuTreeList = (params) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysMenu/treeList`, 'get', params)

export const addMenu = (sysMenu) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysMenu`, 'post', sysMenu)

export const updateMenu = (id, sysMenu) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysMenu/${id}`, 'put', sysMenu)

export const deleteMenu = (id) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysMenu/${id}`, 'delete')

export const getRouteMenus = () => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/sysMenu/routeMenus`, 'get')
