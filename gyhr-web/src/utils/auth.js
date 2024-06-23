import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

// 用户id的key
const userIdkey = 'userId';
// 存储用户的id
export function setUserId(value) {
  return Cookies.set(userIdkey, value)
}
// 获取用户的id
export function getUserId() {
  return Cookies.get(userIdkey)
}
// 删除用户id
export function removeUserId(){
  return Cookies.remove(userIdkey)
}
//清空sessionStorage
export function clearSession(){
  return sessionStorage.clear();
}