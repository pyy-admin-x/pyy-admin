import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [

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
    path: '/system',
    component: Layout,
    redirect: '/system/dept',
    name: '系统管理',
    meta: { title: '系统管理', icon: 'sys' },
    children: [
      {
        path: '/system/dept',
        name: '部门管理',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'dept' }
      },
      {
        path: '/system/job',
        name: '岗位管理',
        component: () => import('@/views/system/job/index.vue'),
        meta: { title: '岗位管理', icon: 'job' }
      },
      {
        path: '/system/menu',
        name: '菜单管理',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'menu' }
      },
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

  {
    path: '/nested',
    component: Layout,
    redirect: '/nested/menu1',
    name: 'Nested',
    meta: {
      title: 'Nested',
      icon: 'nested'
    },
    children: [
      {
        path: 'menu1',
        component: () => import('@/views/nested/menu1/index'), // Parent router-view
        name: 'Menu1',
        meta: { title: 'Menu1' },
        children: [
          {
            path: 'menu1-1',
            component: () => import('@/views/nested/menu1/menu1-1'),
            name: 'Menu1-1',
            meta: { title: 'Menu1-1' }
          },
          {
            path: 'menu1-2',
            component: () => import('@/views/nested/menu1/menu1-2'),
            name: 'Menu1-2',
            meta: { title: 'Menu1-2' },
            children: [
              {
                path: 'menu1-2-1',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
                name: 'Menu1-2-1',
                meta: { title: 'Menu1-2-1' }
              },
              {
                path: 'menu1-2-2',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
                name: 'Menu1-2-2',
                meta: { title: 'Menu1-2-2' }
              }
            ]
          },
          {
            path: 'menu1-3',
            component: () => import('@/views/nested/menu1/menu1-3'),
            name: 'Menu1-3',
            meta: { title: 'Menu1-3' }
          }
        ]
      },
      {
        path: 'menu2',
        component: () => import('@/views/nested/menu2/index'),
        meta: { title: 'menu2' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
