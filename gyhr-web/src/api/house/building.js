import http from "@/utils/http";

// 查询楼栋列表
export async function getBuildingListApi(parm){
    return await http.get("/api/building/buildlist", parm)
}

// 新增楼栋
export async function addBuildingApi(parm) {
    return await http.post("/api/building", parm);
}

// 删除楼栋
export async function deleteBuildingApi(parm) {
return await http.delete("/api/building", parm)
}

// 编辑楼栋
export async function editBuildingApi(parm) {
    return await http.put("/api/building", parm)
}