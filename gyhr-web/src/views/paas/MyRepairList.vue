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
            <el-form-item>
                <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
                <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
                <el-button v-if="hasPerm('wy:myrepair:add')" @click="addBtn" type="primary"
                    icon="el-icon-plus">新增</el-button>
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
                                v-for="url in props.row.imgurl.split(',')" :key="url" :src='url'
                                :preview-src-list="[url]"></el-image>
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
                <template v-if="scope.row.status == '0'" slot-scope="scope">
                    <el-button v-if="hasPerm('wy:myrepair:edit')" type="primary" icon="el-icon-edit" size="small"
                        @click="editBtn(scope.row)">编辑</el-button>
                    <el-button v-if="hasPerm('wy:myrepair:delete')" type="danger" icon="el-icon-delete" size="small"
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
                    style="display: grid;justify-content: center;grid-template-columns: auto;">
                    <el-form-item prop="repairTitle" label="维修标题">
                        <el-input v-model="addModel.repairTitle" maxlength="15" show-word-limit
                            style="width: 300px;"></el-input>
                    </el-form-item>
                    <el-form-item prop="repairAddress" label="维修地址">
                        <el-input v-model="addModel.repairAddress" maxlength="30" show-word-limit
                            style="width: 300px;"></el-input>
                    </el-form-item>
                    <el-form-item prop="repairContent" label="维修内容">
                        <el-input type="textarea" v-model="addModel.repairContent" maxlength="300" show-word-limit
                            :autosize="{ minRows: 2, maxRows: 12 }" style="width: 300px;"></el-input>
                    </el-form-item>
                    <el-form-item prop="phone" label="联系电话">
                        <el-input v-model="addModel.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="上传图片" size="normal">
                        <el-upload ref="imgUpload" :on-change="Upload" action="#" :auto-upload="false"
                            :on-preview="handlePreview" :limit="5" :on-exceed="handleExceed" accept=".png,.jpeg,.jpg"
                            style="width: 320px;" :on-remove="handleRemove" :file-list="fileList" list-type="picture-card">
                            <el-button size="small" type="primary">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过3MB</div>
                        </el-upload>
                    </el-form-item>
                </el-form>
            </div>
        </sys-dialog>
    </el-main>
</template>

<script>
import { getRepairListApi, addRepairApi, editRepairApi, deleteRepairApi } from "@/api/paas/repair"
import { imgUpload } from "@/api/file/img"
import { getUserId, getToken } from "@/utils/auth";
import sysDialog from "@/components/system/sysDialog";

export default {
    // 组件在使用之间需要注册
    components: {
        sysDialog,
    },
    data() {
        return {
            delfileList: "",
            fileList: [],
            //表单验证
            rules: {
                repairTitle: [{ max: 15, trigger: "change", required: true, message: "请填写维修标题" }],
                repairAddress: [{ trigger: "change", required: true, message: "请填写维修地址" }],
                repairContent: [{ trigger: "change", required: true, message: "请填写维修内容" }],
                phone: [
                    { trigger: "change", required: true, message: "请填写联系电话" },
                    { pattern: /^1[3|4|5|7|8][0-9]\d{8}$/, message: '手机号码格式不正确', trigger: 'change' }
                ],
            },
            // 表单数据
            addModel: {
                formtype: "", //0 新增 1：编辑
                repairId: "",
                customerId: "",
                repairTitle: "",
                repairContent: "",
                repairAddress: "",
                phone: "",
                imgurl: "",
            },
            //弹框属性
            dialog: {
                title: "",
                height: 450,
                width: 550,
                visible: false,
            },
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
                customerId: "",
                total: 0,
            },
        };
    },
    created() {
        this.getListData();
    },
    methods: {
        handleExceed(files, fileList) {
            this.$message.warning("限制上传 5 张图片！");
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
            this.fileList = this.fileList.filter(item => item.name != file.name)
        },
        handlePreview(file) {
            window.open(file.url)
        },
        async Upload(file) {
            const typeArr = ["png", "jpeg", "jpg"];
            const fileExt = file.name.split('.').pop().toLowerCase();
            const isImg = typeArr.indexOf(fileExt) !== -1;
            const isMore3M = file.size / 1024 / 1024 < 3;
            if (!isMore3M || !isImg) {
                this.$message.warning("只能上传jpg/png文件，且不超过3MB！");
                this.fileList = this.fileList.filter(item => item.name != file.name)
                return
            }
            const formData = new FormData();
            formData.append("file", file.raw);
            formData.append("token", getToken());
            let res = await imgUpload(formData);
            if (res && res.code == 200) {
                let imgurl = process.env.VUE_APP_BASE_API_PRO + res.data
                file.url = imgurl
                file.name = imgurl.split("/").pop()
                this.fileList.push(file)
                this.$message.success(res.msg);
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
        // 删除按钮事件
        async deleteBtn(row) {
            let confrim = await this.$myconfirm("确定删除该维修吗？");
            if (confrim) {
                let res = await deleteRepairApi({ repairId: row.repairId });
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
            if (this.addModel.imgurl != null) {
                let imgList = this.addModel.imgurl.split(",")
                imgList.forEach((item) => {
                    this.fileList.push({ name: item.split('/').pop(), url: item })
                })
            }
            console.log(this.fileList)
            this.dialog.title = "编辑维修";
            this.addModel.formtype = "1";
            this.dialog.visible = true;
        },
        // 新增按钮事件
        addBtn() {
            this.addModel.formtype = "0";
            this.dialog.title = "新增维修";
            this.dialog.visible = true;
        },
        // 关闭对话框
        onClose() {
            this.dialog.visible = false;
            this.fileList = [];
            this.$refs.imgUpload.clearFiles();
            // 清空表单数据
            this.$resetForm("addForm", this.addModel);
        },
        // 确认对话框事件
        onConfirm() {
            this.$refs.addForm.validate(async (valid) => {
                if (valid) {
                    let imgurl = "";
                    this.fileList.forEach((item) => {
                        imgurl = imgurl + item.url + ","
                    })
                    this.addModel.imgurl = imgurl.substring(0, imgurl.lastIndexOf(","));
                    this.addModel.customerId = getUserId()
                    let res = null;
                    if (this.addModel.formtype == "0") {
                        // 新增
                        res = await addRepairApi(this.addModel);
                    } else if (this.addModel.formtype == "1") {
                        // 编辑
                        res = await editRepairApi(this.addModel);
                    }
                    if (res && res.code == 200) {
                        // 请求成功刷新列表数据
                        this.getListData();
                        this.dialog.visible = false;
                        this.$message.success(res.msg);
                    }
                    // 清空表单数据
                    this.fileList = [];
                    this.$refs.imgUpload.clearFiles();
                    this.$resetForm("addForm", this.addModel);
                }
            });
        },
        async getListData() {
            this.parms.customerId = getUserId()
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
@import url("//unpkg.com/element-ui@2.15.14/lib/theme-chalk/index.css");

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