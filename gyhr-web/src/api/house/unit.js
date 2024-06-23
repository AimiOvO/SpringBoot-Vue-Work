import http from '@/utils/http'

// 获取单元列表
export async function getUnitListApi(parm) {
    return await http.get("/api/unit/unitlist", parm)
}
//获取楼栋列表
export async function getBuildingListApi(parm){
    return await http.get("/api/building/buildlist", parm);
}
// 新增单元
export async function addUnitApi(parm) {
    return await http.post("/api/unit", parm)
}
// 编辑单元
export async function editUnitApi(parm) {
    return await http.put("/api/unit", parm)
}
// 删除单元
export async function deleteUnitApi(parm) {
    return await http.delete("/api/unit", parm)
}