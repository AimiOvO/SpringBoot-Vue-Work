<template>
    <el-main height="">
        <!--
            搜索框
            :model 表单组件绑定的数据域
            :ref 组件在当前页面唯一的 id
            :inline true排列一行 false排列一列
            size 组件大小
        -->
        <el-form :model="parms" ref="searchForm" label-width="80px" :inline="true" size="small">
            <el-form-item label="房屋编号">
                <el-input v-model="parms.homeNum"></el-input>
            </el-form-item>
            <el-form-item label="缴费状态">
                <el-select v-model="parms.payStatus" clearable filterable style="width: 184px;">
                    <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
                <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
            </el-form-item>
        </el-form>
        <!--
            楼栋列表
            :data 表格的数据
            column 的 prop 需要跟返回的数据属性名对应
        -->
        <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column prop="feeNo" label="订单编号"></el-table-column>
            <el-table-column align="center" width="100" prop="buildName" label="楼栋"></el-table-column>
            <el-table-column align="center" width="100" prop="unitName" label="单元"></el-table-column>
            <el-table-column align="center" width="100" prop="homeNum" label="房屋编号"></el-table-column>
            <el-table-column align="center" width="100" prop="cname" label="租户姓名"></el-table-column>
            <el-table-column align="center" width="100" prop="payMonth" label="所属月份"></el-table-column>
            <el-table-column align="center" prop="powerNum" label="表显"></el-table-column>
            <el-table-column align="center" prop="payMoney" label="缴费金额"></el-table-column>
            <el-table-column prop="payNo" label="支付单编号"></el-table-column>
            <el-table-column align="center" width="200" prop="payTime" label="缴费时间"></el-table-column>
            <el-table-column align="center" width="100" prop="payStatus" label="缴费状态">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.payStatus == '0'" type="danger" size="small">未缴费</el-tag>
                    <el-tag v-if="scope.row.payStatus == '1'" type="success" size="small">已缴费</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" width="100" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.payStatus == '0' && hasPerm('wy:powerfee:pay')" type="success" icon="el-icon-wallet" size="small"
                        @click="alipay(scope.row)">缴费</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--
            分页
            @size-change 页容量改变时触发的事件
            @current-change 页数改变时触发
            :current-page.sync 当前页数
            :page-sizes 每页几条数据的选项
            :page-size 每页几条数据
            :total 总条数
        -->
        <el-pagination @size-change="sizeChange" @current-change="currentChange" :current-page.sync="parms.currentPage"
            :page-sizes="[10, 20, 50, 100]" :page-size="parms.pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="parms.total" background>
        </el-pagination>
    </el-main>
</template>

<script>
import { getPowerFeeListApi, payPowerFeeApi } from "@/api/fee/power"
import { getUserId, getToken } from "@/utils/auth";

export default {
    data() {
        return {
            // 表格高度
            tableHeight: window.innerHeight - 210,
            //表格数据
            tableList: [],
            //查询参数
            parms: {
                currentPage: 1,
                pageSize: 10,
                total: 0,
                customerId: "",
                homeNum: "",
                payStatus: "",
            },
            // 选项
            statusOptions: [
                {
                    value: "0",
                    label: "未缴费",
                },
                {
                    value: "1",
                    label: "已缴费",
                },
            ],
        }
    },
    created() {
        this.getListData();
    },
    methods: {
        async alipay(row) {
            let confrim = await this.$myconfirm("确定缴费吗？");
            if (confrim) {
                location.replace( process.env.VUE_APP_BASE_API_PRO + '/api/alipay/pay?token=' + getToken() + "&feeNo=" + row.feeNo )
            }
        },
        // 缴费按钮事件
        async payBtn(row) {
            let confrim = await this.$myconfirm("确定缴费吗？");
            if (confrim) {
                let res = await payPowerFeeApi({powerfeeId: row.powerfeeId,});
                if (res && res.code == 200) {
                    // 删除成功刷新列表
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        async getListData() {
            this.parms.customerId = getUserId()
            let res = await getPowerFeeListApi(this.parms);
            console.log(res);
            if (res && res.code == 200) {
                this.tableList = res.data.records;
                this.parms.total = res.data.total;
            }
        },
        // 搜索按钮事件
        searchBtn() {
            this.getListData();
        },
        // 重置按钮
        resetBtn() {
            this.parms.homeNum = "";
            this.parms.payStatus = "";
            this.getListData();
        },
        // 页容量改变触发
        sizeChange(val) {
            this.parms.pageSize = val;
            this.getListData();
        },
        // 页数改变触发
        currentChange(val) {
            this.parms.currentPage = val;
            this.getListData();
        }
    }
}
</script>

<style lang="scss" scoped></style>