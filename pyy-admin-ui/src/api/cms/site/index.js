import { createAPI } from '@/utils/request'

export const getSiteById = (id) => createAPI(`/cms/site/${id}`, 'get')

export const getSiteList = (params) => createAPI('/cms/sites', 'get', params)

export const getSitePageList = (page, size, params) => createAPI(`/cms/sites/${page}/${size}`, 'get', params)

export const addSite = (site) => createAPI('/cms/site', 'post', site)

export const updateSite = (id, site) => createAPI(`/cms/site/${id}`, 'put', site)

export const deleteSite = (id) => createAPI(`/cms/site/${id}`, 'delete')


