import http from "@/utils/http";

// 获取权限列表
export async function getMenuListApi() {
    return await http.get("/api/menu/menulist", null);
}

// 获取权限列表
export async function getParentListApi() {
    return await http.get("/api/menu/parent", null);
}

// 新增权限
export async function addMenuApi(parm) {
    return await http.post("/api/menu", parm);
}

// 删除角色
export async function deleteMenuApi(parm) {
return await http.delete("/api/menu", parm)
}

// 编辑角色
export async function editMenuApi(parm) {
    return await http.put("/api/menu", parm)
}