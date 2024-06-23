import http from '@/utils/http'

// 获取租单列表
export async function getRentalParkListApi(parm) {
    return await http.get("/api/customerPark/RentalParkList", parm)
}
// 获取停车位列表
export async function getParkListApi(parm) {
    return await http.get("/api/park/parklist", parm)
}
// 获取房屋租户列表
export async function getCustomerListApi(parm) {
    return await http.get("/api/customer/customerlist", parm)
}
// 新增租单parkName
export async function addRentalParkApi(parm) {
    return await http.post("/api/customerPark", parm)
}
// 编辑租单
export async function editRentalParkApi(parm) {
    return await http.put("/api/customerPark", parm)
}
// 删除租单
export async function deleteRentalParkApi(parm) {
    return await http.delete("/api/customerPark", parm)
}
// 退房
export async function returnParkApi(parm) {
    return await http.post("/api/customerPark/returnPark", parm)
}