import request from '@/utils/request'
import http from '@/utils/http'
import { getUserId } from '@/utils/auth'

// 用户登录
export async function login(parm) {
  return await http.post("/api/user/login", parm)
}
// 获取用户信息
export function getInfo() {
  return http.get("/api/user/getInfo", { userId: getUserId() })
}
// 用户登出
export function loginOutApi() {
  return http.post("/api/user/loginOut", null)
}
// export function logout() {
//   return request({
//     url: '/vue-admin-template/user/logout',
//     method: 'post'
//   })
// }

// 获取员工列表
export async function getUserListApi(parm) {
  return await http.get("/api/user/userlist", parm)
}
// 新增员工
export async function addUserApi(parm) {
  return await http.post("/api/user", parm)
}
// 编辑员工
export async function editUserApi(parm) {
  return await http.put("/api/user", parm)
}
// 删除员工
export async function deleteUserApi(parm) {
  return await http.delete("/api/user", parm)
}
// 根据用户id查询角色id
export async function getRoleByUserId(parm) {
  return await http.get("/api/user/getRoleByUserId", parm);
}
// 分配角色保存
export async function assignSave(parm) {
  return await http.post("/api/user/saveRole", parm)
}
// 获取菜单数据
export async function getMenuList(parm) {
  return await http.get("/api/user/getMenuList", null)
}