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
            <el-form-item label="标题">
                <el-input v-model="parms.title"></el-input>
            </el-form-item>
            <el-form-item label="用户姓名">
                <el-input v-model="parms.uname"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
                <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
                <el-button v-if="hasPerm('wy:notice:add')" @click="addBtn" type="primary" icon="el-icon-plus">新增</el-button>
            </el-form-item>
        </el-form>
        <!--
            楼栋列表
            :data 表格的数据
            column 的 prop 需要跟返回的数据属性名对应
        -->
        <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left" class="demo-table-expand">
                        <el-form-item label="标题">
                            <span>{{ props.row.title }}</span>
                        </el-form-item>
                        <el-form-item label="内容">
                            <br>
                            <pre style="font: 1em sans-serif;">{{ props.row.content }}</pre>
                        </el-form-item>
                        <el-form-item label="用户姓名">
                            <span>{{ props.row.uname }}</span>
                        </el-form-item>
                        <el-form-item label="发布时间">
                            <span>{{ props.row.createTime }}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column label="标题" prop="title"></el-table-column>
            <!-- <el-table-column label="内容" prop="content"></el-table-column> -->
            <el-table-column label="用户姓名" prop="uname"></el-table-column>
            <el-table-column label="发布时间" prop="createTime"></el-table-column>
            <el-table-column align="center" width="300" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="hasPerm('wy:notice:edit')" type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
                    <el-button v-if="hasPerm('wy:notice:delete')" type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
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
                    style="display: grid;justify-content: center;grid-template-columns: auto;">
                    <el-form-item prop="title" label="标题">
                        <el-input v-model="addModel.title" maxlength="15" show-word-limit style="width: 300px;"></el-input>
                    </el-form-item>
                    <el-form-item prop="content" label="公告内容">
                        <el-input type="textarea" v-model="addModel.content" maxlength="300" show-word-limit
                            :autosize="{ minRows: 2, maxRows: 12 }" style="width: 300px;"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getNoticeListApi, addNoticeApi, editNoticeApi, deleteNoticeApi } from "@/api/paas/notice"
import { getUserId } from "@/utils/auth";
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
                title: [{ max: 15, trigger: "change", required: true, message: "请填写投诉标题" }],
                content: [{ trigger: "change", required: true, message: "请填写投诉内容" }],
            },
            // 表单数据
            addModel: {
                formtype: "", //0 新增 1：编辑
                noticeId: "",
                userId: "",
                title: "",
                content: "",
            },
            //弹框属性
            dialog: {
                title: "",
                height: 350,
                width: 550,
                visible: false,
            },
            // 表格高度
            tableHeight: window.innerHeight - 210,
            //表格数据
            tableList: [],
            //列表查询参数
            parms: {
                currentPage: 1,
                pageSize: 10,
                title: "",
                uname: "",
                total: 0,
            },
        };
    },
    created() {
        this.getListData();
    },
    methods: {
        // 删除按钮事件
        async deleteBtn(row) {
            let confrim = await this.$myconfirm("确定删除该投诉吗？");
            if (confrim) {
                let res = await deleteNoticeApi({ noticeId: row.noticeId });
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
            this.dialog.title = "编辑投诉";
            this.addModel.formtype = "1";
            this.dialog.visible = true;
        },
        // 新增按钮事件
        addBtn() {
            this.addModel.formtype = "0";
            this.dialog.title = "新增投诉";
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
                    this.addModel.userId = getUserId()
                    let res = null;
                    if (this.addModel.formtype == "0") {
                        // 新增
                        res = await addNoticeApi(this.addModel);
                    } else if (this.addModel.formtype == "1") {
                        // 编辑
                        res = await editNoticeApi(this.addModel);
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
            let res = await getNoticeListApi(this.parms);
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
            this.parms.title = "";
            this.parms.uname = "";
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