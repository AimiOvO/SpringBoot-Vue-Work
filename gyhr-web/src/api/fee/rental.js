import http from '@/utils/http'

// 获取房屋列表
export async function getOnHomeList(parm) {
    return await http.get("/api/order/getOnHomeList", parm)
}
// 查询租费列表
export async function getRentalFeeListApi(parm) {
    return await http.get("/api/feeRental/rentalfeelist", parm)
}
// 新增租费
export async function addRentalFeeApi(parm) {
    return await http.post("/api/feeRental", parm)
}
// 编辑租费
export async function editRentalFeeApi(parm) {
    return await http.put("/api/feeRental", parm)
}
// 删除
export async function deleteRentalFeeApi(parm){
    return await http.delete("/api/feeRental",parm)
}
// 缴费
export async function payRentalFeeApi(parm) {
    return await http.post("/api/feeRental/payRental", parm)
}