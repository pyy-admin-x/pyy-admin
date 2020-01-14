import Vue from 'vue'
//import moment from 'moment'
//import { format, compareAsc } from 'date-fns'
import format from 'date-fns/format'

// 自定义过滤器
Vue.filter('dateFormat', (value, formatStr = 'yyyy-MM-dd HH:mm:ss') => {
  if (!value) {
    return ''
  }
  return format(value, formatStr)
})
