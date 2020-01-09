import axios from 'axios'
import { MessageBox, Message , Notification} from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: '/api', // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['Authorization'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    if (res.code !== 200) {
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      } else {
        Notification({ title: '提示', message: res.message || 'Error', type: 'error'});
        // Message({
        //   message: res.message || 'Error',
        //   type: 'error',
        //   duration: 5 * 1000
        // })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  err => {
    console.log('err' + err) // for debug
    if (err && err.response) {
      switch (err.response.status) {
        case 400:
          err.message = '请求错误'
          break
        case 401:
          err.message = '未授权，请登录'
          break
        case 403:
          err.message = '拒绝访问'
          break
        case 404:
          err.message = `请求地址出错: ${err.response.config.url}`
          break
        case 408:
          err.message = '请求超时'
          break
        case 500:
          err.message = '服务器内部错误'
          break
        case 501:
          err.message = '服务未实现'
          break
        case 502:
          err.message = '网关错误'
          break
        case 503:
          err.message = '服务不可用'
          break
        case 504:
          err.message = '网关超时'
          break
        case 505:
          err.message = 'HTTP版本不受支持'
          break
        default:
      }
    }

    Message({
      message: err.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(err) // 返回接口返回的错误信息
  }
)

// rest 请求
export const createAPI = (url, method, data) => {
  const config = {}
  if ('get' === method) {
    config.params = data
  } else {
    config.data = data
  }

  return new Promise((resolve, reject) => {
    // 执行axios请求
    service({
      url,
      method,
      ...config
    }).then(response => {
      // 成功调用resolve()
      resolve(response)
    }).catch(error => {
      // 失败调用reject()
      reject(error)
    })
  })
}

// form表单请求
export const createFormAPI = (url, method, data) => {
  const config = {}
  config.data = data
  config.headers = {
    'Cache-Control': 'no-cache',
    'Content-Type': 'application/x-www-form-urlencoded'
  }
  config.responseType = 'json'
  config.transformRequest = [
    function(data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }
  ]
  return new Promise((resolve, reject) => {
    // 执行axios请求
    service({
      url,
      method,
      ...config
    }).then(response => {
      // 成功调用resolve()
      resolve(response)
    }).catch(error => {
      // 失败调用reject()
      reject(error)
    })
  })
}



// 下载
export const createDown = (url, method, data) => {
  let config = {}
  if (method === 'get') {
    config.params = data
  } else {
    config.data = data
  }
  config.headers = {
    'Cache-Control': 'no-cache',
    'Content-Type': 'application/x-www-form-urlencoded'
  }
  config.responseType = 'blob'
  return new Promise((resolve, reject) => {
    // 执行axios请求
    service({
      url,
      method,
      ...config
    }).then(response => {
      // 成功调用resolve()
      resolve(response)
    }).catch(error => {
      // 失败调用reject()
      reject(error)
    })
  })
}
