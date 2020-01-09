import { createAPI } from '../utils/request'

export const login = (data) => createAPI('/user/login', 'post', data)

export const getInfo = (token) => createAPI('/user/info', 'get', token)

export const logout = (token) => createAPI('/user/logout', 'post')

