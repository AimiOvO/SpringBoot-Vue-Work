import http from '@/utils/http'

// 获取车位列表
export async function getParkListApi(parm) {
    return await http.get("/api/park/parklist", parm)
}
// 新增车位
export async function addParkApi(parm) {
    return await http.post("/api/park", parm)
}
// 编辑车位
export async function editParkApi(parm) {
    return await http.put("/api/park", parm)
}
// 删除车位
export async function deleteParkApi(parm) {
    return await http.delete("/api/park", parm)
}