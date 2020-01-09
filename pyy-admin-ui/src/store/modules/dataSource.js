/**
 * 数据源管理
 */
import { getDataSourceById } from '../../api/generate/dataSource/index'

const state = {
  currentDataSource: null,
}

const mutations = {
  SET_CURRENT_DATASOURCE: (state, currentDataSource) => {
    state.currentDataSource = currentDataSource
  },
}

const actions = {
  // 根据ID获取数据源
  getDataSourceById({commit}, id) {
    return new Promise(async (resolve, reject) => {
      getDataSourceById(id).then(response => {
        commit('SET_CURRENT_DATASOURCE', response.data)
        resolve()
      }).catch(error => {
        reject(error)
      })

    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

