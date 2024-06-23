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
      <el-form-item label="用户姓名">
        <el-input v-model="parms.uname"></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="parms.uphone"></el-input>
      </el-form-item>
      <el-form-item label="所属部门">
        <el-select v-model="parms.roleId" clearable filterable style="width: 184px;">
          <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
        <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
        <el-button v-if="hasPerm('sys:user:add')" @click="addBtn" type="primary" icon="el-icon-plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!--
        员工列表
        :data 表格的数据
        column 的 prop 需要跟返回的数据属性名对应
    -->
    <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
      <el-table-column prop="uname" label="姓名"></el-table-column>
      <el-table-column prop="uphone" label="电话"></el-table-column>
      <el-table-column prop="idCard" label="身份证"></el-table-column>
      <el-table-column align="center" width="100" prop="sex" label="性别">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sex == '1'">男</el-tag>
          <el-tag v-if="scope.row.sex == '0'" type="success">女</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleName" label="部门名称"></el-table-column>
      <el-table-column align="center" width="100" prop="status" label="是否离职">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="'1'" :inactive-value="'0'"
            @change="changeStatus(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" width="100" prop="enabled" label="是否启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enabled" :active-value="true" :inactive-value="false"
            @change="changeUsed(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" width="300" label="操作">
        <template slot-scope="scope">
          <el-button v-if="hasPerm('sys:user:edit')" type="primary" icon="el-icon-edit" size="small"
            @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-if="hasPerm('sys:user:saverole')" type="primary" icon="el-icon-edit" size="small"
            @click="assignRole(scope.row)">分配部门</el-button>
          <el-button v-if="![1].includes(scope.row.userId) && hasPerm('sys:user:delete')" type="danger"
            icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
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
        <el-form :model="addModel" ref="addForm" :rules="rules" label-width="80px" :inline="true" size="small" style="
            display: grid;
            justify-content: center;
            grid-template-columns: auto auto;
          ">
          <el-form-item prop="uname" label="姓名">
            <el-input v-model="addModel.uname"></el-input>
          </el-form-item>
          <el-form-item prop="sex" label="性别">
            <el-radio-group v-model="addModel.sex">
              <el-radio :label="'1'">男</el-radio>
              <el-radio :label="'0'">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="uphone" label="电话">
            <el-input v-model="addModel.uphone"></el-input>
          </el-form-item>
          <el-form-item prop="idCard" label="身份证">
            <el-input v-model="addModel.idCard"></el-input>
          </el-form-item>
          <el-form-item prop="username" label="登录名">
            <el-input v-model="addModel.username"></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input type="password" v-model="addModel.password"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="离职">
            <el-radio-group v-model="addModel.status">
              <el-radio :label="'1'">是</el-radio>
              <el-radio :label="'0'">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="enabled" label="启用">
            <el-radio-group v-model="addModel.enabled">
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
    <!-- 分配部门弹框 -->
    <sys-dialog :title="roleDialog.title" :height="roleDialog.height" :width="roleDialog.width"
      :visible="roleDialog.visible" @onClose="roleClose" @onConfirm="roleConfirm">
      <template slot="content">
        <el-table :data="roleList" border stripe>
          <el-table-column width="50" align="center" label="选择" style="align-items: center;">
            <template slot-scope="scope">
              <el-radio v-model="radio" :label="scope.row.roleId" @change="getCurrentRow(scope.row)">
                {{ "" }}
              </el-radio>
            </template>
          </el-table-column>
          <el-table-column prop="roleName" label="部门名称"></el-table-column>
          <el-table-column prop="remark" label="部门备注"></el-table-column>
        </el-table>
        <el-pagination @current-change="roleCurrentChange" :current-page.sync="roleParm.currentPage"
          :total="roleParm.total" layout="total, prev, pager, next, jumper" background>
        </el-pagination>
      </template>
    </sys-dialog>
  </el-main>
</template>

<script>
import {
  getUserListApi,
  addUserApi,
  editUserApi,
  deleteUserApi,
  getRoleByUserId,
  assignSave
} from "@/api/system/user";
import { getRoleListApi } from '@/api/system/role'
import sysDialog from "@/components/system/sysDialog";


export default {
  // 组件在使用之间需要注册
  components: {
    sysDialog,
  },
  // 所有需要调用的数据都要在 data 里定义
  data() {
    return {
      roleList: [],
      // 表单验证规则
      rules: {
        uname: [{ required: true, trigger: "change", message: "请填写姓名" }],
        sex: [{ required: true, trigger: "change", message: "请填写性别" }],
        uphone: [
          { trigger: "change", required: true, message: "请填写电话" },
          { pattern: /^1[3|4|5|7|8][0-9]\d{8}$/, message: '手机号码格式不正确', trigger: 'change' }
        ],
        idCard: [
          { required: true, message: '请填写身份证', trigger: 'change' },
          { pattern: /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X|x)$/, message: '身份证号码格式不正确', trigger: 'change' }
        ],
        status: [{ required: true, trigger: "change", message: "请选择是否离职" }],
        enabled: [{ required: true, trigger: "change", message: "请选择是否启用" }],
      },
      // 表单绑定的数据
      addModel: {
        userId: "",
        formtype: "", // 0新增 1编辑
        uname: "",
        sex: "",
        uphone: "",
        idCard: "",
        username: "",
        password: "",
        status: "",
        enabled: "",
      },
      // 弹框属性
      dialog: {
        title: "",
        visible: false,
        height: 250,
        width: 600,
      },
      // 表格高度
      tableHeight: window.innerHeight - 210,
      // 搜索框的数据绑定
      parms: {
        uname: "",
        uphone: "",
        roleId: "",
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
      // 表格的数据
      tableList: [],
      // 部门列表的数据
      roleList: [],
      //部门列表高度
      roleHeight: window.innerHeight - 940,
      // 分配部门弹框
      roleDialog: {
        title: "",
        height: 420,
        width: 800,
        visible: false,
      },
      //部门列表分页
      roleParm: {
        pageSize: 7,
        currentPage: 1,
        total: 0,
        roleName: "部门",
      },
      // 分配部门保存参数
      assignParm: {
        roleId: '',
        userId: ''
      },
      // 分配部门单选框数据
      radio: ''
    };
  },
  created() {
    this.getListData();
    this.getRoleListData();
  },
  methods: {
    // 获取列表数据
    async getRoleListData() {
      let res = await getRoleListApi(this.roleParm);
      if (res.code == 200) {
        console.log(res);
        this.roleList = res.data.records;
      }
    },
    // 分配部门按钮事件
    async assignRole(row) {
      this.getRoleList();
      this.assignParm.userId = row.userId;
      this.roleDialog.title = "为【" + row.uname + "】分配部门";
      this.roleDialog.visible = true;
      // 获取用户原来的部门
      let res = await getRoleByUserId({ userId: row.userId });
      if (res && res.code == 200) {
        if (res.data) {
          this.radio = res.data.roleId;
        }
      }
    },
    // 获取部门列表
    async getRoleList() {
      let res = await getRoleListApi(this.roleParm);
      if (res && res.code == 200) {
        console.log(res);
        this.roleList = res.data.records;
        this.roleParm.total = res.data.total;

      }
    },
    // 部门选中事件
    getCurrentRow(row) {
      this.assignParm.roleId = row.roleId
      console.log(row)
    },
    // 分配部门弹框关闭
    roleClose() {
      this.roleDialog.visible = false;
      this.radio = ''
    },
    //分配部门确认
    async roleConfirm() {
      if (!this.radio) {
        this.$message.warning('请选择部门!');
        return;
      }
      let res = await assignSave(this.assignParm);
      if (res && res.code == 200) {
        this.$message.success(res.msg);
        this.roleDialog.visible = false;
      }
      this.radio = ''
    },
    // 分配部门页数改变
    roleCurrentChange(val) {
      this.roleParm.currentPage = val;
      this.getRoleList();
    },
    // 表格是否启用点击事件
    async changeUsed(row) {
      let parm = {
        userId: row.userId,
        enabled: row.enabled,
      };
      let res = await editUserApi(parm);
      if (res && res.code == 200) {
        this.getListData();
        this.$message.success(res.msg);
      }
    },
    // 表格是否离职点击事件
    async changeStatus(row) {
      let parm = {
        userId: row.userId,
        status: row.status,
      };
      let res = await editUserApi(parm);
      if (res && res.code == 200) {
        this.getListData();
        this.$message.success(res.msg);
      }
    },
    // 删除按钮事件
    async deleteBtn(row) {
      let confrim = await this.$myconfirm("确定删除该员工吗？");
      if (confrim) {
        let res = await deleteUserApi({ userId: row.userId });
        if (res && res.code == 200) {
          // 删除成功刷新列表数据
          this.getListData();
          this.$message.success(res.msg);
        }
      }
    },
    // 编辑按钮事件
    editBtn(row) {
      this.$objCopy(row, this.addModel);
      this.dialog.title = "编辑员工";
      this.addModel.formtype = "1";
      this.dialog.visible = true;
    },
    // 新增按钮事件
    addBtn() {
      this.addModel.formtype = "0";
      this.dialog.title = "新增员工";
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
            res = await addUserApi(this.addModel);
          } else if (this.addModel.formtype == "1") {
            // 编辑
            res = await editUserApi(this.addModel);
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
    // 获取列表数据
    async getListData() {
      let res = await getUserListApi(this.parms);
      if (res.code == 200) {
        console.log(res);
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
      this.parms.uname = "";
      this.parms.uphone = "";
      this.parms.roleId = "";
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
  },
};
</script>

<style lang="scss" scoped></style>
