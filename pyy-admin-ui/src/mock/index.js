import Mock from 'mockjs'
import data from './data/data.json'

Mock.setup({
    timeout: '200'
})

//如果发送请求的api路径匹配，拦截
//第一个参数匹配的请求api路径，第二个参数匹配请求的方式，第三个参数相应数据如何替换
Mock.mock(/\/user\/login/, 'post', data.token)

Mock.mock(/\/user\/info/, 'get', data.userInfo)

Mock.mock(/\/user\/logout/, 'post', data.logout)

//配置模拟数据接口
//  /company/12
//Mock.mock(/\/company\/+/, 'get', CompanyAPI.sassDetail)//根据id查询
//Mock.mock(/\/company/, 'get', CompanyAPI.list)  //访问企业列表


