import http from '@/utils/http'

//获取列表
export async function getRepairListApi(parm){
    return await http.get("/api/repair/repairlist",parm)
}
// 新增
export async function addRepairApi(parm) {
    return await http.post("/api/repair", parm)
}
// 编辑
export async function editRepairApi(parm) {
    return await http.put("/api/repair", parm)
}
// 删除
export async function deleteRepairApi(parm) {
    return await http.delete("/api/repair", parm)
}
// 处理
export async function doRepairApi(parm) {
    return await http.post("/api/repair/doRepair", parm)
}
// 派修
export async function assignRepairApi(parm) {
    return await http.post("/api/repair/assignRepair", parm)
}
// 获取维修人员列表
export async function getRepairTorListApi(parm){
    return await http.get("/api/user/repairTorList",parm)
}