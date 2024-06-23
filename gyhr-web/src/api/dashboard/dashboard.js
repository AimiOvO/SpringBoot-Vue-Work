import http from '@/utils/http'

// 获取租户总数
export async function getCustomerCountApi(parm) {
    return await http.get("/api/customer/customerCount", parm)
}
// 获取在期租单总数
export async function getOrderCountApi(parm) {
    return await http.get("/api/order/orderCount", parm)
}
// 获取待处理投诉总数
export async function getComplaintCountApi(parm) {
    return await http.get("/api/complaint/complaintCount", parm)
}
// 获取待派修维修总数
export async function getRepairCountApi(parm) {
    return await http.get("/api/repair/repairCount", parm)
}

// 获取待缴电费总数
export async function getFeePowerCountApi(parm) {
    return await http.get("/api/feePower/myPowerFeeCount", parm)
}
// 获取待缴水费总数
export async function getFeeWaterCountApi(parm) {
    return await http.get("/api/feeWater/myWaterFeeCount", parm)
}
// 获取待缴租凭费总数
export async function getFeeRentalCountApi(parm) {
    return await http.get("/api/feeRental/myRentalFeeCount", parm)
}
// 获取待缴停车费总数
export async function getParkFeeCountApi(parm) {
    return await http.get("/api/feePark/myParkFeeCount", parm)
}

// 获取待处理维修总数
export async function getDoRepairCountApi(parm) {
    return await http.get("/api/repair/doRepairCount", parm)
}