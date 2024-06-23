import http from '@/utils/http'

// 获取房屋列表
export async function getHomeListApi(parm) {
    return await http.get("/api/home/homelist", parm)
}
// 获取楼栋列表
export async function getBuildingListApi(parm){
    return await http.get("/api/building/buildlist", parm)
}
// 根据楼栋id查询单元列表
export async function getUnitListByBuildId(parm){
    return await http.get("/api/unit/getUnitListByBuildId", parm)
}
// 新增单元
export async function addHomeApi(parm) {
    return await http.post("/api/home", parm)
}
// 编辑单元
export async function editHomeApi(parm) {
    return await http.put("/api/home", parm)
}
// 删除单元
export async function deleteHomeApi(parm) {
    return await http.delete("/api/home", parm)
}