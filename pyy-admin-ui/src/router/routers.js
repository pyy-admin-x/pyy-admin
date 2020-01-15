import Vue from 'vue'
import Router from 'vue-router'
import Layout from '../layout/index'

Vue.use(Router)

// 静态路由
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    meta: { title: '登录', noCache: true },
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: '/dashboard',
      name: '首页',
      component: () => import('@/views/home/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: '/center',
        component: () => import('@/views/system/user/center'),
        name: '个人中心',
        meta: { title: '个人中心' }
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/system/dept',
  //   name: '系统管理',
  //   meta: { title: '系统管理', icon: 'sys' },
  //   children: [
  //     {
  //       path: '/system/dept',
  //       name: '部门管理',
  //       component: () => import('@/views/system/dept/index.vue'),
  //       meta: { title: '部门管理', icon: 'dept' }
  //     },
  //     {
  //       path: '/system/job',
  //       name: '岗位管理',
  //       component: () => import('@/views/system/job/index.vue'),
  //       meta: { title: '岗位管理', icon: 'job' }
  //     },
  //     {
  //       path: '/system/menu',
  //       name: '菜单管理',
  //       component: () => import('@/views/system/menu/index.vue'),
  //       meta: { title: '菜单管理', icon: 'menu' }
  //     },
  //     {
  //       path: '/system/role',
  //       name: 'Role',
  //       component: () => import('@/views/system/role/index.vue'),
  //       meta: { title: '角色管理', icon: 'role' }
  //     },
  //     {
  //       path: '/system/user',
  //       name: 'User',
  //       component: () => import('@/views/system/user/index.vue'),
  //       meta: { title: '用户管理', icon: 'user' }
  //     },
  //   ]
  // },
  {
    path: '/cms',
    component: Layout,
    redirect: '/cms/site/list',
    name: 'CMS内容管理',
    meta: { title: 'CMS内容管理', icon: 'tree' },
    children: [
      {
        path: '/cms/site/list',
        name: '站点管理',
        component: () => import('@/views/cms/site/index.vue'),
        meta: { title: '站点管理', icon: 'site' }
      },
      {
        path: '/cms/template/list',
        name: '模板管理',
        component: () => import('@/views/cms/template/index.vue'),
        meta: { title: '模板管理', icon: 'table' }
      }
      ,
      {
        path: '/cms/page/list',
        name: '页面管理',
        component: () => import('@/views/cms/page/index.vue'),
        meta: { title: '页面管理', icon: 'page' }
      }
    ]
  },
  {
    path: '/teach',
    component: Layout,
    redirect: '/teach/course',
    name: '教学管理',
    meta: { title: '教学管理', icon: 'teach' },
    children: [
      {
        path: '/teach/course',
        name: '我的课程',
        component: () => import('@/views/teach/course/index.vue'),
        meta: { title: '我的课程', icon: 'course' }
      },
      {
        path: '/teach/media',
        name: '我的媒资',
        component: () => import('@/views/generate/codeGenerate/index.vue'),
        meta: { title: '我的媒资', icon: 'media' }
      }
    ]
  },
  {
    path: '/monitor',
    component: Layout,
    redirect: '/monitor/operationLog',
    name: '系统监控',
    meta: { title: '系统监控', icon: 'sysMonitor' },
    children: [
      {
        path: '/monitor/operationLog',
        name: '操作日志',
        component: () => import('@/views/monitor/operationLog/index.vue'),
        meta: { title: '操作日志', icon: 'operationLog' }
      },
      {
        path: '/systemMonitoring/exceptionLog',
        name: '异常日志',
        component: () => import('@/views/monitor/exceptionLog/index.vue'),
        meta: { title: '异常日志', icon: 'exceptionLog' }
      },
      {
        path: 'http://10.10.50.189:8848/nacos/',
        name: '服务监控',
        meta: { title: '服务监控', icon: 'service-nacos' }
      },
    ]
  },
  {
    path: '/generate',
    component: Layout,
    redirect: '/generate/dataSource',
    name: '代码生成器',
    meta: { title: '代码生成器', icon: 'code' },
    children: [
      {
        path: '/generate/dataSource',
        name: '数据源配置',
        component: () => import('@/views/generate/dataSource/index.vue'),
        meta: { title: '数据源配置', icon: 'ds' }
      },
      {
        path: '/generate/codeGenerate',
        name: '代码生成',
        component: () => import('@/views/generate/codeGenerate/index.vue'),
        meta: { title: '代码生成', icon: 'multi_table' }
      }
    ]
  },
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
