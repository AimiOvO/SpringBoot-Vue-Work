<template>
    <el-main height="">
        <!-- Main content -->
        <!--
            搜索框
            :model 表单组件绑定的数据域
            :ref 组件在当前页面唯一的 id
            :inline true排列一行 false排列一列
            size 组件大小
        -->
        <el-form :model="parms" ref="searchForm" label-width="80px" :inline="true" size="small">
            <el-form-item label="租单编号">
                <el-input v-model="parms.orderNum"></el-input>
            </el-form-item>
            <el-form-item label="租单状态">
                <el-select v-model="parms.orderStatus" clearable filterable style="width: 184px;">
                    <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="租户姓名">
                <el-input v-model="parms.cname"></el-input>
            </el-form-item>
            <el-form-item label="所属楼栋">
                <el-select v-model="parms.buildId" @change="selectBuild" clearable filterable style="width: 184px;">
                    <el-option v-for="item in buildList" :key="item.buildId" :label="item.buildName" :value="item.buildId">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="所属单元">
                <el-select v-model="parms.unitId" no-data-text="请选择楼栋" clearable filterable style="width: 184px;">
                    <el-option v-for="item in unitList" :key="item.unitId" :label="item.unitName" :value="item.unitId">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="房屋编号">
                <el-input v-model="parms.homeNum"></el-input>
            </el-form-item>
            <el-form-item label="起始时间">
                <el-date-picker v-model="parms.startTime" :picker-options="startOptions" type="date"
                    value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="结束时间">
                <el-date-picker v-model="parms.endTime" :picker-options="endOptions" type="date"
                    value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
                <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
                <el-button v-if="hasPerm('wy:order:add')" @click="addBtn" type="primary" icon="el-icon-plus">新增</el-button>
            </el-form-item>
        </el-form>
        <!--
            楼栋列表
            :data 表格的数据
            column 的 prop 需要跟返回的数据属性名对应
        -->
        <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column prop="orderNum" label="租单编号"></el-table-column>
            <el-table-column align="center" width="100" prop="cname" label="租户姓名"></el-table-column>
            <el-table-column align="center" width="100" prop="buildName" label="楼栋"></el-table-column>
            <el-table-column align="center" width="100" prop="unitName" label="单元"></el-table-column>
            <el-table-column align="center" width="100" prop="homeNum" label="房屋编号"></el-table-column>
            <el-table-column align="center" width="100" prop="month" label="租凭月数"></el-table-column>
            <el-table-column align="center" width="200" prop="createTime" label="起始时间"></el-table-column>
            <el-table-column align="center" width="100" prop="orderStatus" label="租单状态">
                <template slot-scope="scope">
                    <el-tag type="success" size="normal" v-if="scope.row.orderStatus == '0'">在期</el-tag>
                    <el-tag type="danger" size="normal" v-if="scope.row.orderStatus == '1'">结束</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" width="200" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.orderStatus == '0' && hasPerm('wy:order:return')" type="warning" icon="el-icon-circle-close" size="small" @click="leaveHome(scope.row)">退房</el-button>
                    <el-button v-if="scope.row.orderStatus == '1' && hasPerm('wy:order:edit')" type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
                    <el-button v-if="hasPerm('wy:order:delete')" type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
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
                        <el-button type="primary" size="small" @click="selectHome()">选择房屋</el-button>
                    </el-form-item>
                    <el-form-item prop="cname" label="所属租户">
                        <el-input v-model="addModel.cname" disabled style="width: 104px;"></el-input>
                        <el-button type="primary" size="small" @click="selectCustomer()">选择租户</el-button>
                    </el-form-item>
                    <el-form-item prop="month" label="租凭月数">
                        <el-select v-model="addModel.month" clearable filterable style="width: 184px;">
                            <el-option v-for="item in [3,4,5,6,7,8,9,10,11,12]" :key="item" :label="item + '个月'"
                                :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="createTime" label="起始时间">
                        <el-date-picker v-model="addModel.createTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期" style="width: 200px;">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item prop="orderStatus" label="状态">
                        <el-radio-group v-model="addModel.orderStatus">
                            <el-radio :label="'0'">在期</el-radio>
                            <el-radio :label="'1'">结束</el-radio>
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
                        <el-table-column prop="buildName" label="楼栋名称"></el-table-column>
                        <el-table-column prop="unitName" label="单元名称"></el-table-column>
                        <el-table-column prop="homeNum" label="房屋编号"></el-table-column>
                        <el-table-column prop="homeArea" label="使用面积" align="center" width="100"></el-table-column>
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
        <!-- 选择用户弹框 -->
        <sys-dialog :title="customerDialog.title" :height="customerDialog.height" :width="customerDialog.width"
            :visible="customerDialog.visible" @onClose="customerOnClose" @onConfirm="customerOnConfirm">
            <template slot="content">
                <el-main style="padding: 0px">
                    <!-- 搜索框 -->
                    <el-form :model="customerParms" label-width="80px" :inline="true" size="small">
                        <el-form-item label="租户姓名">
                            <el-input v-model="customerParms.cname"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input v-model="customerParms.cphone"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button icon="el-icon-search" @click="customerSearchBtn">查询</el-button>
                            <el-button style="color: #ff7670" icon="el-icon-close" @click="customerResetBtn">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <!-- 表格 -->
                    <el-table :data="customerList" border stripe>
                        <el-table-column align="center" label="操作" width="50">
                            <template slot-scope="scope">
                                <el-radio v-model="assignCustomerParm.customerId" :label="scope.row.customerId"
                                    @change="getAssignUserId(scope.row)">
                                    {{ "" }}
                                </el-radio>
                            </template>
                        </el-table-column>
                        <el-table-column align="center" width="100" prop="cname" label="姓名"></el-table-column>
                        <el-table-column align="center" width="100" prop="sex" label="性别">
                            <template slot-scope="scope">
                                <el-tag type="success" size="normal" v-if="scope.row.sex == '0'">男</el-tag>
                                <el-tag type="danger" size="normal" v-if="scope.row.sex == '1'">女</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column prop="cphone" label="电话"></el-table-column>
                        <el-table-column prop="idCard" label="身份证号"></el-table-column>
                    </el-table>
                    <!-- 分页 -->
                    <el-pagination @current-change="assignCustomerCurrentChange"
                        :current-page.sync="customerParms.currentPage" layout="total, prev, pager, next, jumper"
                        :total="customerParms.total" background>
                    </el-pagination>
                </el-main>
            </template>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getBuildingListApi, getUnitListByBuildId } from "@/api/house/home"
import { getOrderListApi, addOrderApi, editOrderApi, deleteOrderApi, returnHomeApi,getHomeListApi, getCustomerListApi } from '@/api/rental/order'
import sysDialog from "@/components/system/sysDialog";

export default {
    // 组件在使用之间需要注册
    components: {
        sysDialog,
    },
    data() {
        return {
            //表单验证规则
            rules: {
                homeNum: [{ trigger: "change", required: true, message: "请选择所属房屋" }],
                cname: [{ trigger: "change", required: true, message: "请选择所属用户" }],
                month: [{ trigger: "change", required: true, message: "请选择租单时间" }],
                creatTime: [{ trigger: "change", required: true, message: "请选择起始时间" }],
                orderStatus: [{ trigger: "change", required: true, message: "请租单状态" }],
            },
            addModel: {
                formtype: "",
                orderId: "",
                homeId: "",
                homeNum: "",
                customerId: "",
                cname: "",
                month: "",
                createTime: "",
                orderStatus: "",
            },
            //弹框属性
            dialog: {
                title: "",
                height: 200,
                width: 700,
                visible: false,
            },
            statusOptions: [
                {
                    value: "0",
                    label: "在期",
                },
                {
                    value: "1",
                    label: "结束",
                },
            ],
            // 表格高度
            tableHeight: window.innerHeight - 270,
            //表格数据
            tableList: [],
            //查询参数
            parms: {
                currentPage: 1,
                pageSize: 10,
                total: 0,
                orderNum: "",
                orderStatus: "",
                cname: "",
                buildId: "",
                unitId: "",
                homeNum: "",
                startTime: "",
                endTime: "",
            },
            startOptions: {
                disabledDate: (time) => {
                    if (this.parms.endTime) {
                        return time.getTime() > new Date(this.parms.endTime).getTime() - 24 * 60 * 60 * 1000;
                    }
                }
            },
            endOptions: {
                disabledDate: (time) => {
                    if (this.parms.startTime) {
                        return time.getTime() < new Date(this.parms.startTime).getTime() + 24 * 60 * 60 * 1000;
                    }
                }
            },
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

            //分配租户列表高度
            customerTableHeight: window.innerHeight - 880,
            //分配租户参数
            assignCustomerParm: {
                customerId: "",
                cname: ""
            },
            //租户列表
            customerList: [],
            //租户查询参数
            customerParms: {
                currentPage: 1,
                pageSize: 7,
                total: 0,
                cname: "",
                cphone: ""
            },
            //租户弹框属性
            customerDialog: {
                title: "",
                height: 550,
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
        /**
         * 选择用户
         */
        selectCustomer() {
            this.customerParms.cname = "";
            this.customerParms.cphone = "";
            this.assignCustomerParm.customerId = this.addModel.customerId;
            this.getAssignCustomerList();
            this.customerDialog.title = "选择所属用户";
            this.customerDialog.visible = true;
        },
        //分配房屋页数改变触发
        assignCustomerCurrentChange(val) {
            this.homeParms.currentPage = val;
            this.getAssignCustomerList();
        },
        //分配房屋点击
        getAssignUserId(row) {
            this.assignCustomerParm.customerId = row.customerId;
            this.assignCustomerParm.cname = row.cname;
        },
        //分配房屋弹框确认
        async customerOnConfirm() {
            //验证是否选择房屋
            if (!this.assignCustomerParm.customerId) {
                this.$message.warning("请选择用户！");
                return;
            }
            this.addModel.customerId = this.assignCustomerParm.customerId
            this.addModel.cname = this.assignCustomerParm.cname
            this.customerDialog.visible = false;
        },
        //分配房屋弹框关闭
        customerOnClose() {
            this.customerDialog.visible = false;
        },
        //分配房屋列表重置事件
        customerResetBtn() {
            this.customerParms.cname = "";
            this.customerParms.cphone = "";
            //获取房屋列表
            this.getAssignCustomerList();
        },
        //分配房屋查询按钮
        customerSearchBtn() {
            //获取房屋列表
            this.getAssignCustomerList();
        },
        async getAssignCustomerList() {
            let res = await getCustomerListApi(this.customerParms);
            console.log(res);
            if (res && res.code == 200) {
                this.customerList = res.data.records;
                this.customerParms.total = res.data.total;
            }
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
            let res = await getHomeListApi(this.homeParms);
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
            this.addModel.unitId = "";
            //根据栋数id查询对应的单元
            this.getUnitListByBuildId(val);
        },
        // 退房按钮事件
        async leaveHome(row) {
            let confrim = await this.$myconfirm("确定退房吗？");
            if (confrim) {
                let parm = {
                    homeId: row.homeId,
                    customerId: row.customerId,
                    orderId: row.orderId
                }
                let res = await returnHomeApi(parm);
                if (res && res.code == 200) {
                    // 删除成功刷新列表
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        // 删除按钮事件
        async deleteBtn(row) {
            let confrim = await this.$myconfirm("确定删除该租单吗？");
            if (confrim) {
                let res = await deleteOrderApi({ orderId: row.orderId });
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
            this.dialog.title = "编辑租单";
            this.addModel.formtype = "1";
            this.dialog.visible = true;
        },
        // 新增按钮事件
        addBtn() {
            this.addModel.formtype = "0";
            this.dialog.title = "新增租单";
            this.dialog.visible = true;
            this.unitList = [];
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
                        res = await addOrderApi(this.addModel);
                    } else if (this.addModel.formtype == "1") {
                        // 编辑
                        res = await editOrderApi(this.addModel);
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
            let res = await getOrderListApi(this.parms);
            console.log(res);
            if (res && res.code == 200) {
                this.tableList = res.data.records;
                this.parms.total = res.data.total;
            }
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
        // 搜索按钮事件
        searchBtn() {
            this.getListData();
            console.log(this.parms);
        },
        // 重置按钮
        resetBtn() {
            this.parms.orderNum = '';
            this.parms.orderStatus = '';
            this.parms.cname = '';
            this.parms.buildId = '';
            this.parms.unitId = '';
            this.parms.homeNum = '';
            this.parms.startTime = '';
            this.parms.endTime = '';
            this.parms.currentPage = 1;
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
        },
    }
}
</script>

<style lang="scss" scoped></style>