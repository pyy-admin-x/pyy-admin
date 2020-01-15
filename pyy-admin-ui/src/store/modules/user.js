
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router/routers'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  roles: [],
  // 第一次加载菜单时用到
  loadMenus: false
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_LOAD_MENUS: (state, loadMenus) => {
    state.loadMenus = loadMenus
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, code, uuid } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, code: code, uuid: uuid }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data)
        // 第一次加载菜单时用到
        commit('SET_LOAD_MENUS', true)
        setToken(data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        if (!data) {
          reject('Verification failed, please Login again.')
        }
        const { name, avatar, roles} = data
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        // 如果没有任何权限，则赋予一个默认的权限，避免请求死循环
        if (!roles) {
          commit('SET_ROLES', ['ROLE_SYSTEM_DEFAULT'])
        } else {
          commit('SET_ROLES', roles.permissionList)
        }
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // update load menus
  updateLoadMenus({ commit }) {
    return new Promise((resolve, reject) => {
      // 更新加载标记为false
      commit('SET_LOAD_MENUS', false)
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

