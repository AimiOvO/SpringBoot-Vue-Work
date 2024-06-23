import { login, loginOutApi, getInfo } from '@/api/system/user'
import { getToken, setToken, removeToken, setUserId, removeUserId, clearSession } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
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
  }
}

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    // 解构用户信息
    const { username, password, userType } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, userType: userType }).then(response => {
        const { data } = response
        // 设置token
        commit('SET_TOKEN', data.token)
        // 设置到cookies里面
        setToken(data.token)
        // 保存用户id
        setUserId(data.userId)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  // 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        if (!data) {
          return reject('获取用户信息失败，请重新登录')
        }
        const { name, avatar, roles } = data
        if (!roles || roles.length <= 0) {
          reject('getInfo: 用户的权限信息必须是一个数组!')
        }
        //把权限字段放到sessionStorage里面
        sessionStorage.setItem('codeList', JSON.stringify(roles))
        //把roles存到store里面
        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, dispatch, state }) {
    return new Promise((resolve, reject) => {
      loginOutApi(state.token).then(() => {
        removeToken() // 首先删除token
        resetRouter()
        commit('RESET_STATE')
        removeUserId(); // 清空用户
        clearSession(); // 清空sessionStorage
        dispatch('tagsView/delAllViews', {}, { root: true }) // 清空tagsView的数据
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit, dispatch }) {
    return new Promise(resolve => {
      removeToken()  // 首先删除token
      commit('RESET_STATE')
      removeUserId(); // 清空用户
      clearSession(); // 清空sessionStorage
      dispatch('tagsView/delAllViews', {}, { root: true }) // 清空tagsView的数据
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

