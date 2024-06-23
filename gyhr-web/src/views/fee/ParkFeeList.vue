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
            <el-form-item label="租户姓名">
                <el-input v-model="parms.cname"></el-input>
            </el-form-item>
            <el-form-item label="车位名称">
                <el-input v-model="parms.parkName"></el-input>
            </el-form-item>
            <el-form-item label="车位类型">
                <el-select v-model="parms.parkType" clearable filterable style="width: 184px;">
                    <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
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
                <el-button v-if="hasPerm('wy:parkfee:add')" @click="addBtn" type="primary"
                    icon="el-icon-plus">新增</el-button>
            </el-form-item>
        </el-form>
        <!--
            楼栋列表
            :data 表格的数据
            column 的 prop 需要跟返回的数据属性名对应
        -->
        <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column prop="feeNo" label="订单编号"></el-table-column>
            <el-table-column prop="parkName" label="车位名称"></el-table-column>
            <el-table-column prop="parkType" align="center" width="100" label="车位类型">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.parkType == '0'" type="danger" size="small">地上</el-tag>
                    <el-tag v-if="scope.row.parkType == '1'" type="success" size="small">地下</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" width="100" prop="cname" label="租户姓名"></el-table-column>
            <el-table-column align="center" width="100" prop="payMonth" label="所属月份"></el-table-column>
            <el-table-column align="center" prop="payMoney" label="缴费金额"></el-table-column>
            <el-table-column prop="payNo" label="支付单编号"></el-table-column>
            <el-table-column align="center" width="200" prop="payTime" label="缴费时间"></el-table-column>
            <el-table-column align="center" width="100" prop="payStatus" label="缴费状态">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.payStatus == '0'" type="danger" size="small">未缴费</el-tag>
                    <el-tag v-if="scope.row.payStatus == '1'" type="success" size="small">已缴费</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" width="200" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.payStatus == '0' && hasPerm('wy:parkfee:pay')" type="success"
                        icon="el-icon-wallet" size="small" @click="payBtn(scope.row)">缴费</el-button>
                    <el-button v-if="hasPerm('wy:parkfee:edit') && scope.row.payStatus == '0'" type="primary"
                        icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
                    <el-button v-if="hasPerm('wy:parkfee:delete')" type="danger" icon="el-icon-delete" size="small"
                        @click="deleteBtn(scope.row)">删除</el-button>
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
        <!-- 新增或编辑弹框 -->
        <sys-dialog :title="dialog.title" :visible="dialog.visible" :width="dialog.width" :height="dialog.height"
            @onClose="onClose" @onConfirm="onConfirm">
            <div slot="content">
                <!--
                    新增表单
                    :model 表单组件绑定的数据域
                    :ref 组件在当前页面唯一的 id
                    :rules 表单验证规则
                        1. 需要在 el-form-item 上绑定 prop属性
                        2. 需要在 data 里面定义验证规则
                        3. 需要在 el-form 上绑定验证规则 :rules="rules"
                -->
                <el-form :model="addModel" ref="addForm" :rules="rules" label-width="100px" :inline="true" size="small"
                    style="display: grid;justify-content: center;grid-template-columns: auto auto;">
                    <el-form-item prop="parkName" label="所属车位">
                        <el-input v-model="addModel.parkName" disabled style="width: 104px;"></el-input>
                        <el-button v-if="this.addModel.formtype == '0'" type="primary" size="small"
                            @click="selectPark()">选择车位</el-button>
                    </el-form-item>
                    <el-form-item prop="payMonth" label="所属月份">
                        <el-date-picker style="width: 184px;" v-model="addModel.payMonth" type="month"
                            value-format="yyyy-MM" placeholder="选择月份">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item prop="payMoney" label="缴费金额" size="small">
                        <el-input v-model="addModel.payMoney"></el-input>
                    </el-form-item>
                    <el-form-item v-if="this.addModel.formtype == 0" prop="payStatus" label="缴费状态" size="small">
                        <el-radio-group v-model="addModel.payStatus">
                            <el-radio :label="'0'">未缴费</el-radio>
                            <el-radio :label="'1'">已缴费</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>
        </sys-dialog>
        <!-- 选择车位弹框 -->
        <sys-dialog :title="parkDialog.title" :height="parkDialog.height" :width="parkDialog.width"
            :visible="parkDialog.visible" @onClose="parkOnClose" @onConfirm="parkOnConfirm">
            <template slot="content">
                <el-main style="padding: 0px">
                    <!-- 搜索框 -->
                    <el-form :model="parkParms" label-width="80px" :inline="true" size="small">
                        <el-form-item label="车位名称">
                            <el-input v-model="parkParms.parkName"></el-input>
                        </el-form-item>
                        <el-form-item label="车位类型">
                            <el-select v-model="parkParms.parkType" clearable filterable style="width: 184px;">
                                <el-option v-for="item in typeOptions" :key="item.value" :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button icon="el-icon-search" @click="parkSearchBtn">查询</el-button>
                            <el-button style="color: #ff7670" icon="el-icon-close" @click="parkResetBtn">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <!-- 表格 -->
                    <el-table :data="parkList" border stripe>
                        <el-table-column align="center" label="操作" width="50">
                            <template slot-scope="scope">
                                <el-radio v-model="assignParkParm.parkId" :label="scope.row.parkId"
                                    @change="getAssignParkId(scope.row)">
                                    {{ "" }}
                                </el-radio>
                            </template>
                        </el-table-column>
                        <el-table-column prop="parkName" label="车位名称"></el-table-column>
                        <el-table-column prop="cname" label="所属用户"></el-table-column>
                        <el-table-column prop="parkType" align="center" width="100" label="车位类型">
                            <template slot-scope="scope">
                                <el-tag v-if="scope.row.parkType == '0'" type="danger" size="small">地上</el-tag>
                                <el-tag v-if="scope.row.parkType == '1'" type="success" size="small">地下</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column prop="parkName" align="center" width="100" label="使用状态">
                            <template slot-scope="scope">
                                <el-tag v-if="scope.row.parkStatus == '0'" type="danger" size="small">未使用</el-tag>
                                <el-tag v-if="scope.row.parkStatus == '1'" type="success" size="small">已使用</el-tag>
                            </template>
                        </el-table-column>
                    </el-table>
                    <!-- 分页 -->
                    <el-pagination @current-change="assignParkCurrentChange" :current-page.sync="parkParms.currentPage"
                        layout="total, prev, pager, next, jumper" :total="parkParms.total" background>
                    </el-pagination>
                </el-main>
            </template>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getParkFeeListApi, getOnParkList, addParkFeeApi, editParkFeeApi, deleteParkFeeApi, payParkFeeApi } from "@/api/fee/park"
import sysDialog from "@/components/system/sysDialog";

export default {
    // 组件在使用之间需要注册
    components: {
        sysDialog,
    },
    data() {
        return {
            //表单验证
            rules: {
                parkName: [{ trigger: "change", required: true, message: "请选择所属车位" }],
                payMonth: [{ trigger: "change", required: true, message: "请选择所属月份" }],
                payMoney: [{ trigger: "change", required: true, message: "请填写缴费金额" }],
                payStatus: [{ trigger: "change", required: true, message: "请选择缴费状态" }],
            },
            // 表单数据
            addModel: {
                formtype: "", //0 新增 1：编辑
                parkfeeId: "",
                parkId: "",
                parkName: "",
                payMonth: "",
                payMoney: "",
                payStatus: "",
                payTime: ""
            },
            //弹框属性
            dialog: {
                title: "",
                height: 200,
                width: 650,
                visible: false,
            },
            // 表格高度
            tableHeight: window.innerHeight - 210,
            //表格数据
            tableList: [],
            //查询参数
            parms: {
                currentPage: 1,
                pageSize: 10,
                total: 0,
                cname: "",
                parkName: "",
                parkType: "",
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
            typeOptions: [
                {
                    value: "0",
                    label: "地上",
                },
                {
                    value: "1",
                    label: "地下",
                },
            ],
            //分配车位列表高度
            parkTableHeight: window.innerHeight - 940,
            //分配车位参数
            assignParkParm: {
                parkId: "",
                parkName: "",
            },
            //车位列表
            parkList: [],
            //车位查询参数
            parkParms: {
                currentPage: 1,
                pageSize: 7,
                total: 0,
                parkType: "",
                parkName: "",
            },
            //车位弹框属性
            parkDialog: {
                title: "",
                height: 520,
                width: 900,
                visible: false,
            },
        }
    },
    created() {
        this.getListData();
    },
    methods: {
        /**
         * 选择车位
         */
        selectPark() {
            this.parkParms.parkName = "";
            this.assignParkParm.parkId = "";
            this.getAssignParkList();
            this.parkDialog.title = "选择所属车位";
            this.parkDialog.visible = true;
        },
        //分配车位页数改变触发
        assignParkCurrentChange(val) {
            this.parkParms.currentPage = val;
            this.getAssignParkList();
        },
        //分配车位点击
        getAssignParkId(row) {
            this.assignParkParm.parkId = row.parkId;
            this.assignParkParm.parkName = row.parkName;
        },
        //分配车位弹框确认
        async parkOnConfirm() {
            //验证是否选择车位
            if (!this.assignParkParm.parkId) {
                this.$message.warning("请选择车位！");
                return;
            }
            this.addModel.parkId = this.assignParkParm.parkId
            this.addModel.parkName = this.assignParkParm.parkName
            this.parkDialog.visible = false;
        },
        //分配车位弹框关闭
        parkOnClose() {
            this.parkDialog.visible = false;
        },
        //分配车位列表重置事件
        parkResetBtn() {
            this.parkParms.parkName = "";
            //获取车位列表
            this.getAssignParkList();
        },
        //分配车位查询按钮
        parkSearchBtn() {
            //获取车位列表
            this.getAssignParkList();
        },
        async getAssignParkList() {
            let res = await getOnParkList(this.parkParms);
            if (res && res.code == 200) {
                console.log(res);
                //把数据赋值给车位列表
                this.parkList = res.data.records;
                this.parkParms.total = res.data.total;
            }
        },
        // 缴费按钮事件
        async payBtn(row) {
            let confrim = await this.$myconfirm("确定缴费吗？");
            if (confrim) {
                let parm = {
                    parkfeeId: row.parkfeeId,
                    payStatus: '1'
                }
                let res = await payParkFeeApi(parm);
                if (res && res.code == 200) {
                    // 删除成功刷新列表
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        // 删除按钮事件
        async deleteBtn(row) {
            let confrim = await this.$myconfirm("确定删除该停车费吗？");
            if (confrim) {
                let res = await deleteParkFeeApi({ parkfeeId: row.parkfeeId });
                if (res && res.code == 200) {
                    // 删除成功刷新列表
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        // 编辑按钮事件
        editBtn(row) {
            this.$objCopy(row, this.addModel);
            this.dialog.title = "编辑停车费";
            this.addModel.formtype = "1";
            this.dialog.visible = true;
        },
        // 新增按钮事件
        addBtn() {
            this.addModel.formtype = "0";
            this.dialog.title = "新增停车费";
            this.dialog.visible = true;
        },
        // 关闭对话框
        onClose() {
            this.dialog.visible = false;
            // 清空表单数据
            this.$resetForm("addForm", this.addModel);
        },
        // 确认对话框事件
        onConfirm() {
            this.$refs.addForm.validate(async (valid) => {
                if (valid) {
                    let res = null;
                    if (this.addModel.formtype == "0") {
                        // 新增
                        res = await addParkFeeApi(this.addModel);
                    } else if (this.addModel.formtype == "1") {
                        // 编辑
                        res = await editParkFeeApi(this.addModel);
                    }
                    if (res && res.code == 200) {
                        // 请求成功刷新列表数据
                        this.getListData();
                        this.dialog.visible = false;
                        this.$message.success(res.msg);
                    }
                    // 清空表单数据
                    this.$resetForm("addForm", this.addModel);
                }
            });
        },
        async getListData() {
            let res = await getParkFeeListApi(this.parms);
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
            this.parms.cname = "";
            this.parms.parkName = "";
            this.parms.parkType = "";
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