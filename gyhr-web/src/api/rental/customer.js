import http from '@/utils/http'

// 获取房屋租户列表
export async function getCustomerListApi(parm) {
    return await http.get("/api/customer/customerlist", parm)
}
// 新增房屋租户
export async function addCustomerApi(parm) {
    return await http.post("/api/customer", parm)
}
// 编辑房屋租户
export async function editCustomerApi(parm) {
    return await http.put("/api/customer", parm)
}
// 删除房屋租户
export async function deleteCustomerApi(parm) {
    return await http.delete("/api/customer", parm)
}
