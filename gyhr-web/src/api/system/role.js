import http from "@/utils/http";

// 获取角色列表
export async function getRoleListApi(parm) {
    return await http.get("/api/role/rolelist", parm)
}
// 新增角色
export async function addRoleApi(parm) {
    return await http.post("/api/role", parm)
  }
// 编辑角色
export async function editRoleApi(parm) {
  return await http.put("/api/role", parm)
}
// 删除角色
export async function deleteRoleApi(parm) {
  return await http.delete("/api/role", parm)
}
//分配角色回显查询
export async function getAssignTreeApi(parm){
  return await http.get("/api/role/getAssignTree",parm)
}
//保存权限
export async function assignSaveApi(parm){
  return await http.post("/api/role/saveAssignTree",parm)
}