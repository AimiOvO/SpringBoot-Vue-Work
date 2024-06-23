import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  // 获取token
  const hasToken = getToken()

  if (hasToken) {
    // 判断是否是从登录页面来的请求
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      // 如果是从登录页面来的 跳转到首页
      next({ path: '/' })
      NProgress.done()
    } else {
      // 从store获取用户信息
      // const hasGetUserInfo = store.getters.name
      // 从store里面获取权限信息
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) { // 如果权限存在直接放行
        next()
      } else { // 如果不存在 从服务器获取数据
        try {
          // get user info
          // 如果用户信息不存在 从服务器获取用户信息
          const { roles } = await store.dispatch('user/getInfo')
          // 从服务器获取菜单、路由信息
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          // 404页面放在最后面
          let obj =  { path: '*', redirect: '/404', hidden: true };
          accessRoutes.push(obj)
          // 把返回的数据添加到路由
          router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
          // next()
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
      // 如果在白名单中就放行
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      // 如果不在白名单中 跳转到登录页面
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
