import http from '@/utils/http'

// 获取租单列表
export async function getOrderListApi(parm) {
    return await http.get("/api/order/orderlist", parm)
}
// 获取楼栋列表
export async function getBuildingListApi(parm){
    return await http.get("/api/building/buildlist", parm)
}
// 根据楼栋id查询单元列表
export async function getUnitListByBuildId(parm){
    return await http.get("/api/unit/getUnitListByBuildId", parm)
}
// 获取房屋列表
export async function getHomeListApi(parm) {
    return await http.get("/api/home/homelist", parm)
}
// 获取房屋租户列表
export async function getCustomerListApi(parm) {
    return await http.get("/api/customer/customerlist", parm)
}
// 新增租单
export async function addOrderApi(parm) {
    return await http.post("/api/order", parm)
}
// 编辑租单
export async function editOrderApi(parm) {
    return await http.put("/api/order", parm)
}
// 删除租单
export async function deleteOrderApi(parm) {
    return await http.delete("/api/order", parm)
}
// 退房
export async function returnHomeApi(parm) {
    return await http.post("/api/order/returnHome", parm)
}