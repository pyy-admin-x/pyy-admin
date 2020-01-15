import router from './routers'
import store from '../store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { getRouteMenus } from '@/api/system/menu'
import { filterAsyncRouter } from '@/store/modules/permission'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()
  console.log('token')
  console.log(hasToken)
  if (hasToken) {
    debugger
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasGetUserInfo = store.getters.name
      console.log(hasGetUserInfo + '------------')
      // 判断当前用户是否已拉取完user_info信息
      if (hasGetUserInfo) {
        // 登录时未拉取 菜单，在此处拉取
        if (store.getters.loadMenus) {
          // 修改成false，防止死循环
          store.dispatch('user/updateLoadMenus').then(res => {})
          loadMenus(next, to)
        }
        next()
      } else {
        try {
          // get user info
          await store.dispatch('user/getInfo')
          // 动态路由，拉取菜单
          loadMenus(next, to)
          next()
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

export const loadMenus = async (next, to) => {
  const result = await getRouteMenus()
  const asyncRouter = filterAsyncRouter(result.data)
  asyncRouter.push({ path: '*', redirect: '/404', hidden: true })
  store.dispatch('permission/generateRoutes', asyncRouter).then(() => { // 存储路由
    router.addRoutes(asyncRouter) // 动态添加可访问路由表
    next({ ...to, replace: true })
  })
}

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
