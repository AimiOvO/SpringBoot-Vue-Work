import http from '@/utils/http'

// 获取房屋列表
export async function getOnHomeList(parm) {
    return await http.get("/api/order/getOnHomeList", parm)
}
// 查询电费列表
export async function getPowerFeeListApi(parm) {
    return await http.get("/api/feePower/powerfeelist", parm)
}
// 新增电费
export async function addPowerFeeApi(parm) {
    return await http.post("/api/feePower", parm)
}
// 编辑电费
export async function editPowerFeeApi(parm) {
    return await http.put("/api/feePower", parm)
}
//删除
export async function deletePowerFeeApi(parm){
    return await http.delete("/api/feePower",parm)
}
// 缴费
export async function payPowerFeeApi(parm) {
    return await http.post("/api/feePower/payPower", parm)
}