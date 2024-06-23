import http from '@/utils/http'

//获取列表
export async function getNoticeListApi(parm){
    return await http.get("/api/notice/noticelist",parm)
}
// 新增
export async function addNoticeApi(parm) {
    return await http.post("/api/notice", parm)
}
// 编辑
export async function editNoticeApi(parm) {
    return await http.put("/api/notice", parm)
}
// 删除
export async function deleteNoticeApi(parm) {
    return await http.delete("/api/notice", parm)
}