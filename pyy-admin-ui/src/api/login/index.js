import { BASE_SERVER_API_GATEWAY_URL } from '@/api/serverUrl'
import { createAPI } from '@/utils/request'


export const getCode = () => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/auth/imgCode?type=5`, 'get', null)

export const login = (data) => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/auth/login`, 'post', data)

export const getInfo = (token) => createAPI('/user/info', 'get', token)

export const logout = () => createAPI(`${BASE_SERVER_API_GATEWAY_URL}/auth/logout`, 'post')



