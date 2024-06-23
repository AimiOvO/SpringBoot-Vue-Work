import http from '@/utils/http'

// 获取车位列表
export async function getOnParkList(parm) {
    return await http.get("/api/customerPark/getOnParkList", parm)
}
// 查询停车费列表
export async function getParkFeeListApi(parm) {
    return await http.get("/api/feePark/parkfeelist", parm)
}
// 新增停车费
export async function addParkFeeApi(parm) {
    return await http.post("/api/feePark", parm)
}
// 编辑停车费
export async function editParkFeeApi(parm) {
    return await http.put("/api/feePark", parm)
}
//删除
export async function deleteParkFeeApi(parm){
    return await http.delete("/api/feePark",parm)
}
// 缴费
export async function payParkFeeApi(parm) {
    return await http.post("/api/feePark/payParkfee", parm)
}