import http from '@/utils/http'

// 缴费
export async function aliPayApi(parm) {
    return await http.get("/api/alipay/pay", parm)
}