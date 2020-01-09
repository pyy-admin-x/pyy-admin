import { createAPI } from '@/utils/request'

export const getTemplateById = (id) => createAPI(`/cms/template/${id}`, 'get')

export const getTemplateList = (params) => createAPI('/cms/templates', 'get', params)

export const getTemplatePageList = (page, size, params) => createAPI(`/cms/templates/${page}/${size}`, 'get', params)

export const addTemplate = (template) => createAPI('/cms/template', 'post', template)

export const updateTemplate = (id, template) => createAPI(`/cms/template/${id}`, 'put', template)

export const deleteTemplate = (id) => createAPI(`/cms/template/${id}`, 'delete')


