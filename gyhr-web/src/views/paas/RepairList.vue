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
            <el-form-item label="维修标题">
                <el-input v-model="parms.repairTitle"></el-input>
            </el-form-item>
            <el-form-item label="维修内容">
                <el-input v-model="parms.repairContent"></el-input>
            </el-form-item>
            <el-form-item label="租户姓名">
                <el-input v-model="parms.cname"></el-input>
            </el-form-item>
            <el-form-item label="联系号码">
                <el-input v-model="parms.phone"></el-input>
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
        <el-table :data="tableList" @expand-change="expandChange" :expand-row-keys="expands" row-key='repairId'
            empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column type="expand" label="详情">
                <template slot-scope="props">
                    <el-form label-position="left" class="demo-table-expand">
                        <el-steps align-center :active="Number(props.row.status) + 1" finish-status="finish">
                            <el-step title="未派修"></el-step>
                            <el-step title="已派修"></el-step>
                            <el-step title="已维修"></el-step>
                        </el-steps>
                        <el-form-item label="维修标题">
                            <span>{{ props.row.repairTitle }}</span>
                        </el-form-item>
                        <el-form-item label="维修地址">
                            <span>{{ props.row.repairAddress }}</span>
                        </el-form-item>
                        <el-form-item label="维修内容">
                            <br>
                            <pre style="font: 1em sans-serif;">{{ props.row.repairContent }}</pre>
                        </el-form-item>
                        <el-form-item label="姓名">
                            <span>{{ props.row.cname }}</span>
                        </el-form-item>
                        <el-form-item label="电话">
                            <span>{{ props.row.phone }}</span>
                        </el-form-item>
                        <el-form-item label="报修时间">
                            <span>{{ props.row.createTime }}</span>
                        </el-form-item>
                        <el-form-item label="报修图片" v-if="props.row.imgurl != null">
                            <br>
                            <el-image style="width: 150px;height: 150px;margin-right: 20px;"
                                v-for="url in props.row.imgurl.split(',')" :key="url" :src='$apiUrl + url'
                                :preview-src-list="[$apiUrl + url]"></el-image>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column label="维修标题" prop="repairTitle"></el-table-column>
            <el-table-column label="维修地址" prop="repairAddress"></el-table-column>
            <!-- <el-table-column label="维修内容" prop="repairContent"></el-table-column> -->
            <el-table-column align="center" width="100" prop="cname" label="姓名"></el-table-column>
            <el-table-column prop="phone" label="电话"></el-table-column>
            <el-table-column align="center" width="100" prop="uname" label="维修人员姓名"></el-table-column>
            <el-table-column prop="uphone" label="维修人员电话"></el-table-column>
            <el-table-column align="center" width="200" prop="createTime" label="报修时间"></el-table-column>
            <el-table-column align="center" width="100" label="处理状态" prop="status">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.status == '2'" type="success" size="small">已维修</el-tag>
                    <el-tag v-if="scope.row.status == '1'" type="warning" size="small">已派修</el-tag>
                    <el-tag v-if="scope.row.status == '0'" type="danger" size="small">未派修</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" width="200" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="hasPerm('wy:repair:assign')" type="success" icon="el-icon-circle-check" size="small"
                        @click="selectUser(scope.row)">派修</el-button>
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
        <!-- 选择用户弹框 -->
        <sys-dialog :title="userDialog.title" :height="userDialog.height" :width="userDialog.width"
            :visible="userDialog.visible" @onClose="userOnClose" @onConfirm="userOnConfirm">
            <template slot="content">
                <el-main style="padding: 0px">
                    <!-- 搜索框 -->
                    <el-form :model="userParms" label-width="80px" :inline="true" size="small">
                        <el-form-item label="租户姓名">
                            <el-input v-model="userParms.uname"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input v-model="userParms.uphone"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button icon="el-icon-search" @click="userSearchBtn">查询</el-button>
                            <el-button style="color: #ff7670" icon="el-icon-close" @click="userResetBtn">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <!-- 表格 -->
                    <el-table :data="userList" border stripe>
                        <el-table-column align="center" label="操作" width="50">
                            <template slot-scope="scope">
                                <el-radio v-model="assignUserParm.userId" :label="scope.row.userId"
                                    @change="getAssignUserId(scope.row)">
                                    {{ "" }}
                                </el-radio>
                            </template>
                        </el-table-column>
                        <el-table-column align="center" width="100" prop="uname" label="姓名"></el-table-column>
                        <el-table-column align="center" width="100" prop="sex" label="性别">
                            <template slot-scope="scope">
                                <el-tag type="success" size="normal" v-if="scope.row.sex == '0'">男</el-tag>
                                <el-tag type="danger" size="normal" v-if="scope.row.sex == '1'">女</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column prop="uphone" label="电话"></el-table-column>
                    </el-table>
                    <!-- 分页 -->
                    <el-pagination @current-change="assignUserCurrentChange" :current-page.sync="userParms.currentPage"
                        layout="total, prev, pager, next, jumper" :total="userParms.total" background>
                    </el-pagination>
                </el-main>
            </template>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getRepairListApi, getRepairTorListApi, assignRepairApi } from "@/api/paas/repair"
import sysDialog from "@/components/system/sysDialog";

export default {
    // 组件在使用之间需要注册
    components: {
        sysDialog,
    },
    data() {
        return {
            // 只展开一行放入当前行id
            expands: [],
            // 表格高度
            tableHeight: window.innerHeight - 210,
            //表格数据
            tableList: [],
            //列表查询参数
            parms: {
                currentPage: 1,
                pageSize: 10,
                repairTitle: "",
                repairContent: "",
                cname: "",
                phone: "",
                total: 0,
            },
            //分配租户列表高度
            userTableHeight: window.innerHeight - 880,
            //分配租户参数
            assignUserParm: {
                userId: ""
            },
            repairId: "",
            //租户列表
            userList: [],
            //租户查询参数
            userParms: {
                currentPage: 1,
                pageSize: 7,
                total: 0,
                uname: "",
                uphone: ""
            },
            //租户弹框属性
            userDialog: {
                title: "",
                height: 550,
                width: 900,
                visible: false,
            },
        };
    },
    created() {
        this.getListData();
    },
    methods: {
        /**
         * 选择用户
         */
        selectUser(row) {
            if (row.status == '1') {
                this.$message.warning('该维修已经派修，无需重复派修！')
                return;
            }
            if (row.status == '2') {
                this.$message.warning('该维修已经维修，不能再次派修！')
                return;
            }
            this.userParms.uname = "";
            this.userParms.uphone = "";
            this.repairId = row.repairId;
            this.getAssignUserList();
            this.userDialog.title = "选择派修员工";
            this.userDialog.visible = true;
        },
        //分配房屋页数改变触发
        assignUserCurrentChange(val) {
            this.homeParms.currentPage = val;
            this.getAssignUserList();
        },
        //分配房屋点击
        getAssignUserId(row) {
            this.assignUserParm.userId = row.userId;
        },
        //分配房屋弹框确认
        async userOnConfirm() {
            //验证是否选择房屋
            if (!this.assignUserParm.userId) {
                this.$message.warning("请选择维修人员！");
                return;
            }
            const confirm = await this.$myconfirm("确定派修该人员吗?");
            if (confirm) {
                let parm = {
                    repairId: this.repairId,
                    userId: this.assignUserParm.userId
                }
                let res = await assignRepairApi(parm);
                if (res && res.code == 200) {
                    //刷新表格
                    this.getListData();
                    this.$message.success(res.msg);
                    this.assignUserParm.userId = "";
                    this.userDialog.visible = false;
                }
            }
        },
        //分配房屋弹框关闭
        userOnClose() {
            this.assignUserParm.userId = "";
            this.userDialog.visible = false;
        },
        //分配房屋列表重置事件
        userResetBtn() {
            this.userParms.uname = "";
            this.userParms.uphone = "";
            //获取房屋列表
            this.getAssignUserList();
        },
        //分配房屋查询按钮
        userSearchBtn() {
            //获取房屋列表
            this.getAssignUserList();
        },
        async getAssignUserList() {
            let res = await getRepairTorListApi(this.userParms);
            console.log(res);
            if (res && res.code == 200) {
                this.userList = res.data.records;
                this.userParms.total = res.data.total;
            }
        },
        //表格当前行被展开或收起
        expandChange(row, expandedRows) {
            if (expandedRows.length) {//说明展开了
                this.expands = []
                if (row) {
                    this.expands.push(row.repairId)//只展开当前行id
                }
            } else {//说明收起了
                this.expands = []
            }
        },
        //处理按钮
        async doBtn(row) {
            if (row.status == '2') {
                this.$message.warning('该维修已经处理，无需重复处理！')
                return;
            }
            const confirm = await this.$myconfirm("确定派修该维修吗?");
            if (confirm) {
                let parm = {
                    repairId: row.repairId,
                    userId: row.userId
                }
                let res = await assignRepairApi(parm);
                if (res && res.code == 200) {
                    //刷新表格
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        async getListData() {
            let res = await getRepairListApi(this.parms);
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
            this.parms.repairTitle = "";
            this.parms.repairContent = "";
            this.parms.cname = "";
            this.parms.phone = "";
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

<style lang="scss" scoped>
.demo-table-expand {
    font-size: 0;
    display: flex;
    flex-direction: column;
}

.demo-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: auto;
    overflow: auto;
}
</style>