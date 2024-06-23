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
            <el-form-item label="内容">
                <el-input v-model="parms.content"></el-input>
            </el-form-item>
            <el-form-item label="租户姓名">
                <el-input v-model="parms.cname"></el-input>
            </el-form-item>
            <el-form-item label="手机号">
                <el-input v-model="parms.cphone"></el-input>
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
        <el-table :data="tableList" @expand-change="expandChange" :expand-row-keys="expands" row-key='complaintId'
            empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
            <el-table-column type="expand" label="详情">
                <template slot-scope="props">
                    <el-form label-position="left" class="demo-table-expand">
                        <el-form-item label="标题">
                            <span>{{ props.row.title }}</span>
                        </el-form-item>
                        <el-form-item label="内容">
                            <br>
                            <pre style="font: 1em sans-serif;">{{ props.row.content }}</pre>
                        </el-form-item>
                        <el-form-item label="姓名">
                            <span>{{ props.row.cname }}</span>
                        </el-form-item>
                        <el-form-item label="电话">
                            <span>{{ props.row.cphone }}</span>
                        </el-form-item>
                        <el-form-item label="投诉时间">
                            <span>{{ props.row.createTime }}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column label="标题" prop="title"></el-table-column>
            <!-- <el-table-column label="内容" prop="content"></el-table-column> -->
            <el-table-column align="center" width="100" prop="cname" label="姓名"></el-table-column>
            <el-table-column prop="cphone" label="电话"></el-table-column>
            <el-table-column align="center" width="200" prop="createTime" label="投诉时间"></el-table-column>
            <el-table-column align="center" width="100" label="处理状态" prop="status">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.status == '1'" type="success" size="small">已处理</el-tag>
                    <el-tag v-if="scope.row.status == '0'" type="danger" size="small">未处理</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" width="200" label="操作">
                <template slot-scope="scope">
                    <el-button v-if="hasPerm('wy:complaint:do')" type="success" icon="el-icon-circle-check" size="small"
                        @click="doBtn(scope.row)">处理</el-button>
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
import { getComplaintListApi, doComplaintApi } from "@/api/paas/complaint"

export default {
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
                title: "",
                content: "",
                cname: "",
                cphone: "",
                total: 0,
            },
        };
    },
    created() {
        this.getListData();
    },
    methods: {
        //表格当前行被展开或收起
        expandChange(row, expandedRows) {
            if (expandedRows.length) {//说明展开了
                this.expands = []
                if (row) {
                    this.expands.push(row.complaintId)//只展开当前行id
                }
            } else {//说明收起了
                this.expands = []
            }
        },
        //处理按钮
        async doBtn(row) {
            if (row.status == '1') {
                this.$message.warning('该投诉已经处理，无需重复处理！')
                return;
            }
            const confirm = await this.$myconfirm("确定处理该投诉吗?");
            if (confirm) {
                let res = await doComplaintApi({ complaintId: row.complaintId });
                if (res && res.code == 200) {
                    //刷新表格
                    this.getListData();
                    this.$message.success(res.msg);
                }
            }
        },
        async getListData() {
            let res = await getComplaintListApi(this.parms);
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
            this.parms.content = "";
            this.parms.cname = "";
            this.parms.cphone = "";
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