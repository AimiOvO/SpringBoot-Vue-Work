import http from '@/utils/http'

//获取列表
export async function getComplaintListApi(parm){
    return await http.get("/api/complaint/complaintList",parm)
}
// 新增
export async function addComplaintApi(parm) {
    return await http.post("/api/complaint", parm)
}
// 编辑
export async function editComplaintApi(parm) {
    return await http.put("/api/complaint", parm)
}
// 删除
export async function deleteComplaintApi(parm) {
    return await http.delete("/api/complaint", parm)
}
// 处理
export async function doComplaintApi(parm) {
    return await http.post("/api/complaint/doComplaint", parm)
}