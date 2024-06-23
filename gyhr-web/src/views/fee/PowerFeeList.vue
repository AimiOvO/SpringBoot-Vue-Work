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
                <el-button v-if="hasPerm('wy:powerfee:add')" @click="addBtn" type="primary"
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
            <el-table-column align="center" width="100" prop="buildName" label="楼栋"></el-table-column>
            <el-table-column align="center" width="100" prop="unitName" label="单元"></el-table-column>
            <el-table-column align="center" width="100" prop="homeNum" label="房屋编号"></el-table-column>
            <el-table-column align="center" width="100" prop="cname" label="租户姓名"></el-table-column>
            <el-table-column align="center" width="100" prop="payMonth" label="所属月份"></el-table-column>
            <el-table-column align="center" prop="powerNum" label="表显（度）"></el-table-column>
            <el-table-column align="center" prop="payMoney" label="缴费金额（元）"></el-table-column>
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
                    <el-button v-if="scope.row.payStatus == '0' && hasPerm('wy:powerfee:pay')" type="success"
                        icon="el-icon-wallet" size="small" @click="payBtn(scope.row)">缴费</el-button>
                    <el-button v-if="hasPerm('wy:powerfee:edit') && scope.row.payStatus == '0'" type="primary"
                        icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
                    <el-button v-if="hasPerm('wy:powerfee:delete')" type="danger" icon="el-icon-delete" size="small"
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
                    <el-form-item prop="homeNum" label="所属房屋">
                        <el-input v-model="addModel.homeNum" disabled style="width: 104px;"></el-input>
                        <el-button v-if="this.addModel.formtype == '0'" type="primary" size="small"
                            @click="selectHome()">选择房屋</el-button>
                    </el-form-item>
                    <el-form-item prop="payMonth" label="所属月份">
                        <el-date-picker style="width: 184px;" v-model="addModel.payMonth" type="month"
                            value-format="yyyy-MM" placeholder="选择月份">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item prop="payMoney" label="缴费金额" size="small">
                        <el-input v-model="addModel.payMoney" disabled></el-input>
                        <div>1度电 = 2元</div>
                    </el-form-item>
                    <el-form-item prop="powerNum" label="表显" size="small">
                        <el-input @change="NumToMoney" v-model="addModel.powerNum"></el-input>
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
        <!-- 选择房屋弹框 -->
        <sys-dialog :title="homeDialog.title" :height="homeDialog.height" :width="homeDialog.width"
            :visible="homeDialog.visible" @onClose="homeOnClose" @onConfirm="homeOnConfirm">
            <template slot="content">
                <el-main style="padding: 0px">
                    <!-- 搜索框 -->
                    <el-form :model="homeParms" label-width="80px" :inline="true" size="small">
                        <el-form-item label="所属楼栋">
                            <el-select v-model="homeParms.buildId" @change="selectBuild" clearable filterable
                                style="width: 184px;">
                                <el-option v-for="item in buildList" :key="item.buildId" :label="item.buildName"
                                    :value="item.buildId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属单元">
                            <el-select v-model="homeParms.unitId" no-data-text="请选择楼栋" clearable filterable
                                style="width: 184px;">
                                <el-option v-for="item in unitList" :key="item.unitId" :label="item.unitName"
                                    :value="item.unitId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="房屋编号">
                            <el-input placeholder="请输入房屋编号" v-model="homeParms.homeNum"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button icon="el-icon-search" @click="homeSearchBtn">查询</el-button>
                            <el-button style="color: #ff7670" icon="el-icon-close" @click="homeResetBtn">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <!-- 表格 -->
                    <el-table :data="homeList" border stripe>
                        <el-table-column align="center" label="操作" width="50">
                            <template slot-scope="scope">
                                <el-radio v-model="assignHomeParm.homeId" :label="scope.row.homeId"
                                    @change="getAssignHomeId(scope.row)">
                                    {{ "" }}
                                </el-radio>
                            </template>
                        </el-table-column>
                        <el-table-column prop="buildName" label="楼栋名称" align="center" width="100"></el-table-column>
                        <el-table-column prop="unitName" label="单元名称" align="center" width="100"></el-table-column>
                        <el-table-column prop="homeNum" label="房屋编号" align="center" width="100"></el-table-column>
                        <el-table-column prop="cname" label="所属用户"></el-table-column>
                        <el-table-column prop="status" label="使用状态" align="center" width="100">
                            <template slot-scope="scope">
                                <el-tag v-if="scope.row.status == '0'" type="danger" size="small">未使用</el-tag>
                                <el-tag v-if="scope.row.status == '1'" type="success" size="small">已使用</el-tag>
                            </template>
                        </el-table-column>
                    </el-table>
                    <!-- 分页 -->
                    <el-pagination @current-change="assignHomeCurrentChange" :current-page.sync="homeParms.currentPage"
                        layout="total, prev, pager, next, jumper" :total="homeParms.total" background>
                    </el-pagination>
                </el-main>
            </template>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getBuildingListApi, getUnitListByBuildId } from "@/api/house/home"
import { getPowerFeeListApi, getOnHomeList, addPowerFeeApi, editPowerFeeApi, deletePowerFeeApi, payPowerFeeApi } from "@/api/fee/power"
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
                homeNum: [{ trigger: "change", required: true, message: "请选择所属房屋" }],
                payMonth: [{ trigger: "change", required: true, message: "请选择所属月份" }],
                payMoney: [{ trigger: "change", required: true, message: "请填写缴费金额" }],
                powerNum: [{ trigger: "change", required: true, message: "请填写表显" }],
                payStatus: [{ trigger: "change", required: true, message: "请选择缴费状态" }],
            },
            // 表单数据
            addModel: {
                formtype: "", //0 新增 1：编辑
                powerfeeId: "",
                homeId: "",
                homeNum: "",
                payMonth: "",
                payMoney: "",
                powerNum: "",
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
            //单元列表
            unitList: [],
            //栋数列表
            buildList: [],
            //楼栋查询参数
            buildParm: {
                currentPage: 1,
                pageSize: null,
                total: 0,
            },
            //分配房屋列表高度
            homeTableHeight: window.innerHeight - 940,
            //分配房屋参数
            assignHomeParm: {
                homeId: "",
                homeNum: ""
            },
            //房屋列表
            homeList: [],
            //房屋查询参数
            homeParms: {
                currentPage: 1,
                pageSize: 7,
                total: 0,
                buildId: "",
                status: "0",
                unitId: "",
                homeNum: "",
            },
            //房屋弹框属性
            homeDialog: {
                title: "",
                height: 520,
                width: 900,
                visible: false,
            },
        }
    },
    created() {
        this.getListData();
        this.getBuildList();
    },
    methods: {
        NumToMoney() {
            this.addModel.payMoney = this.addModel.powerNum * 2;
        },
        /**
         * 选择房屋
         */
        selectHome() {
            this.homeParms.buildId = "";
            this.homeParms.unitId = "";
            this.homeParms.homeNum = "";
            this.assignHomeParm.homeId = "";
            this.getAssignHomeList();
            this.homeDialog.title = "选择所属房屋";
            this.homeDialog.visible = true;
        },
        //分配房屋页数改变触发
        assignHomeCurrentChange(val) {
            this.homeParms.currentPage = val;
            this.getAssignHomeList();
        },
        //分配房屋点击
        getAssignHomeId(row) {
            this.assignHomeParm.homeId = row.homeId;
            this.assignHomeParm.homeNum = row.homeNum;
        },
        //分配房屋弹框确认
        async homeOnConfirm() {
            //验证是否选择房屋
            if (!this.assignHomeParm.homeId) {
                this.$message.warning("请选择房屋！");
                return;
            }
            this.addModel.homeId = this.assignHomeParm.homeId
            this.addModel.homeNum = this.assignHomeParm.homeNum
            this.homeDialog.visible = false;
        },
        //分配房屋弹框关闭
        homeOnClose() {
            this.homeDialog.visible = false;
        },
        //分配房屋列表重置事件
        homeResetBtn() {
            this.homeParms.buildId = "";
            this.homeParms.unitId = "";
            this.homeParms.homeNum = "";
            //获取房屋列表
            this.getAssignHomeList();
        },
        //分配房屋查询按钮
        homeSearchBtn() {
            //获取房屋列表
            this.getAssignHomeList();
        },
        async getAssignHomeList() {
            let res = await getOnHomeList(this.homeParms);
            if (res && res.code == 200) {
                console.log(res);
                //把数据赋值给房屋列表
                this.homeList = res.data.records;
                this.homeParms.total = res.data.total;
            }
        },
        //选择栋数触发事件
        selectBuild(val) {
            //清空原来的数据
            this.homeParms.unitId = "";
            //根据栋数id查询对应的单元
            this.getUnitListByBuildId(val);
        },
        //获取楼栋列表
        async getBuildList() {
            let res = await getBuildingListApi(this.buildParm);
            console.log(res);
            if (res && res.code == 200) {
                this.buildList = res.data.records;
            }
        },
        //单元列表
        async getUnitListByBuildId(buildId) {
            let res = await getUnitListByBuildId({ buildId: buildId });
            console.log(res);
            if (res && res.code == 200) {
                //赋值给单元列表
                this.unitList = res.data;
            }
        },
        // 缴费按钮事件
        async payBtn(row) {
            let confrim = await this.$myconfirm("确定缴费吗？");
            if (confrim) {
                let parm = {
                    powerfeeId: row.powerfeeId,
                    payStatus: '1'
                }
                let res = await payPowerFeeApi(parm);
                if (res && res.code == 200) {
                    // 删除成功刷新列表
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        // 删除按钮事件
        async deleteBtn(row) {
            let confrim = await this.$myconfirm("确定删除该电费吗？");
            if (confrim) {
                let res = await deletePowerFeeApi({ powerfeeId: row.powerfeeId });
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
            this.dialog.title = "编辑电费";
            this.addModel.formtype = "1";
            this.dialog.visible = true;
        },
        // 新增按钮事件
        addBtn() {
            this.addModel.formtype = "0";
            this.dialog.title = "新增电费";
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
                        res = await addPowerFeeApi(this.addModel);
                    } else if (this.addModel.formtype == "1") {
                        // 编辑
                        res = await editPowerFeeApi(this.addModel);
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
            this.parms.cname = "";
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