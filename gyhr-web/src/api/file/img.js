import http from "@/utils/http";

// 图片上传
export async function imgUpload(parm) {
    return http.upload("/api/img/upload", parm);
}