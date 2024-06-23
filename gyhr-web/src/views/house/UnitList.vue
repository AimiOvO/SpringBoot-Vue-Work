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
            <el-form-item prop="buildId" label="楼栋名称">
                <el-select v-model="parms.buildId" clearable filterable style="width: 184px;">
                    <el-option v-for="item in buildList" :key="item.buildId" :label="item.buildName"
                        :value="item.buildId">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="单元名称">
                <el-input v-model="parms.unitName"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
                <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
                <el-button v-if="hasPerm('wy:unit:add')" @click="addBtn" type="primary" icon="el-icon-plus">新增</el-button>
            </el-form-item>
        </el-form>
        <!--
            楼栋列表
            :data 表格的数据
            column 的 prop 需要跟返回的数据属性名对应
        -->
        <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column prop="buildName" label="楼栋名称"></el-table-column>
            <el-table-column prop="unitName" label="单元名称"></el-table-column>>
            <el-table-column align="center" width="200" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="hasPerm('wy:unit:edit')" type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
                    <el-button v-if="hasPerm('wy:unit:delete')" type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
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
                    <el-form-item prop="buildId" label="楼栋名称">
                        <el-select v-model="addModel.buildId" clearable filterable style="width: 184px;">
                            <el-option v-for="item in buildList" :key="item.buildId" :label="item.buildName"
                                :value="item.buildId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="unitName" label="单元名称">
                        <el-input v-model="addModel.unitName"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getUnitListApi, addUnitApi, editUnitApi, deleteUnitApi, getBuildingListApi } from "@/api/house/unit"
import sysDialog from "@/components/system/sysDialog";

export default {
    // 组件在使用之间需要注册
    components: {
        sysDialog,
    },
    data() {
        return {
            rules: {
                buildId: [{ trigger: "change", required: true, message: "请选择栋数" }],
                unitName: [{ trigger: "change", required: true, message: "请填写单元名称" }],
            },
            addModel: {
                formtype: "",
                unitId: "",
                buildId: "",
                unitName: "",
            },
            //弹框属性
            dialog: {
                title: "",
                height: 160,
                width: 450,
                visible: false,
            },
            //表格高度
            tableHeight: window.innerHeight - 210,
            //存放列表的数据
            tableList: [],
            //列表查询参数
            parms: {
                buildId: "",
                unitName: "",
                currentPage: 1,
                pageSize: 10,
                total: 0,
            },
            buildList: [],
            //栋数查询参数
            buildParm: {
                currentPage: 1,
                pageSize: null,
                total: 0,
            },
        };
    },
    created() {
        this.getListData();
        this.getBuildList();
    },
    methods: {
        // 删除按钮事件
        async deleteBtn(row) {
            let confrim = await this.$myconfirm("确定删除该单元吗？");
            if (confrim) {
                let res = await deleteUnitApi({ unitId: row.unitId });
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
            this.dialog.title = "编辑单元";
            this.addModel.formtype = "1";
            this.dialog.visible = true;
        },
        // 新增按钮事件
        addBtn() {
            this.addModel.formtype = "0";
            this.dialog.title = "新增单元";
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
                        res = await addUnitApi(this.addModel);
                    } else if (this.addModel.formtype == "1") {
                        // 编辑
                        res = await editUnitApi(this.addModel);
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
        //获取栋数列表
        async getBuildList() {
            let res = await getBuildingListApi(this.buildParm);
            console.log(res);
            if (res && res.code == 200) {
                this.buildList = res.data.records;
                this.buildParm.total = res.data.total;
            }
        },
        async getListData() {
            let res = await getUnitListApi(this.parms);
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
            this.parms.buildName = "";
            this.parms.unitName = "",
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