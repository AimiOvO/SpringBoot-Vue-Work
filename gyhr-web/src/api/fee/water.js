import http from '@/utils/http'

// 获取房屋列表
export async function getOnHomeList(parm) {
    return await http.get("/api/order/getOnHomeList", parm)
}
// 查询电费列表
export async function getWaterFeeListApi(parm) {
    return await http.get("/api/feeWater/waterfeelist", parm)
}
// 新增
export async function addWaterFeeApi(parm) {
    return await http.post("/api/feeWater", parm)
}
// 编辑
export async function editWaterFeeApi(parm) {
    return await http.put("/api/feeWater", parm)
}
//删除
export async function deleteWaterFeeApi(parm){
    return await http.delete("/api/feeWater",parm)
}
// 缴费
export async function payWaterFeeApi(parm) {
    return await http.post("/api/feeWater/payWater", parm)
}