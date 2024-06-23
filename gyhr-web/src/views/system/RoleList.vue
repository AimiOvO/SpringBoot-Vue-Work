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
      <el-form-item label="部门名称">
        <el-input v-model="parms.roleName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" icon="el-icon-search">查询</el-button>
        <el-button @click="resetBtn" style="color: tomato" icon="el-icon-delete">重置</el-button>
        <el-button v-if="hasPerm('sys:role:add')" @click="addBtn" type="primary" icon="el-icon-plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!--
        部门列表
        :data 表格的数据
        column 的 prop 需要跟返回的数据属性名对应
    -->
    <el-table :data="tableList" empty-text="暂无数据" :height="tableHeight" size="small" border stripe>
      <el-table-column prop="roleName" label="部门名称"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column align="center" width="300" label="操作">
        <template slot-scope="scope">
          <el-button v-if="hasPerm('sys:role:edit')" type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-if="hasPerm('sys:role:saveassign')" type="primary" icon="el-icon-edit" size="small" @click="assignRoleBtn(scope.row)">分配权限</el-button>
          <el-button v-if="![2, 3, 5, 7].includes(scope.row.roleId) && hasPerm('sys:role:delete')" type="danger" icon="el-icon-delete" size="small"
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
        <el-form :model="addModel" ref="addForm" :rules="rules" label-width="100px" :inline="true" size="small" style="
            display: grid;
            justify-content: center;
            grid-template-columns: auto;
          ">
          <el-form-item prop="roleName" label="部门名称">
            <el-input v-model="addModel.roleName"></el-input>
          </el-form-item>
          <el-form-item prop="remark" label="备注">
            <el-input v-model="addModel.remark"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
    <!-- 分配权限弹框 -->
    <sys-dialog :title="assignDialog.title" :height="assignDialog.height" :width="assignDialog.width"
      :visible="assignDialog.visible" @onClose="assignClose" @onConfirm="assignConfirm">
      <template slot="content">
        <el-tree ref="assignTree" :data="assignTreeData" node-key="menuId" :props="defaultProps" empty-text="暂无数据"
          :show-checkbox="true" :default-expand-all="false" :default-checked-keys="assignTreeChecked">

        </el-tree>
      </template>
    </sys-dialog>
  </el-main>
</template>

<script>
import {
  getRoleListApi,
  addRoleApi,
  editRoleApi,
  deleteRoleApi,
  getAssignTreeApi,
  assignSaveApi
} from "@/api/system/role";
import sysDialog from "@/components/system/sysDialog";
import { getUserId } from '@/utils/auth'
export default {
  // 组件在使用之间需要注册
  components: {
    sysDialog,
  },
  data() {
    return {
      // 表单验证规则
      rules: {
        roleName: [{ required: true, trigger: "change", message: "请填写部门名称" }],
        remark: [{ required: false, trigger: "change", message: "请填写备注" }],
      },
      // 表单绑定的数据
      addModel: {
        roleId: "",
        formtype: "", // 0新增 1编辑
        roleName: "",
        remark: "",
      },
      // 弹框属性
      dialog: {
        title: "",
        visible: false,
        height: 250,
        width: 500,
      },
      // 表格高度
      tableHeight: window.innerHeight - 210,
      // 表格的数据
      tableList: [],
      // 搜索框的数据绑定
      parms: {
        roleName: "",
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
      // 分配权限树的数据
      assignTreeData: [],
      roleId: '',
      // 树默认选中的节点
      assignTreeChecked: [],
      defaultProps: {
        children: 'children',
        label: 'menuLabel'
      },
      // 分配权限弹框属性
      assignDialog: {
        title: "",
        width: 300,
        height: 500,
        visible: false
      }
    };
  },
  created() {
    this.getListData();
  },
  methods: {
    //分配权限按钮
    async assignRoleBtn(row) {
      // 清空数据
      this.assignTreeData = [];
      this.assignTreeChecked = [];
      this.roleId = row.roleId;
      // 弹框属性设置
      this.assignDialog.title = '为【' + row.roleName + '】分配权限';
      this.assignDialog.visible = true;
      // 获取树的数据
      let parm = {
        userId: getUserId(),
        roleId: row.roleId,
      };
      let res = await getAssignTreeApi(parm);
      if (res && res.code == 200) {
        console.log(res);
        this.assignTreeData = res.data.listmenu;
        this.assignTreeChecked = res.data.checkList;
        // 如果默认选中中有数据
        if (this.assignTreeChecked.length > 0) {
          let newArr = [];
          this.assignTreeChecked.forEach((item) => {
            this.checked(item, this.assignTreeData, newArr);
          });
          this.assignTreeChecked = newArr;
        }
      }
    },
    checked(id, data, newArr) {
      data.forEach((item) => {
        if (item.menuId == id) {
          if (item.children && item.children.length == 0) {
            newArr.push(item.menuId);
          }
        } else {
          if (item.children && item.children.length != 0) {
            this.checked(id, item.children, newArr);
          }
        }
      });
    },
    // 分配权限关闭按钮
    assignClose() {
      this.assignDialog.visible = false;
    },
    // 分配权限提交按钮
    async assignConfirm() {
      // 获取选择的节点id
      let ids = this.$refs.assignTree.getCheckedKeys().concat(this.$refs.assignTree.getHalfCheckedKeys());
      if (ids.length == 0) {
        this.$message.warning("请勾选权限！");
        return;
      }
      let parm = {
        roleId: this.roleId,
        list: ids,
      };
      let res = await assignSaveApi(parm);
      if (res && res.code == 200) {
        this.assignDialog.visible = false;
        this.$message.success(res.msg);
        this.assignDialog.visible = false;
      }
    },
    // 删除按钮事件
    async deleteBtn(row) {
      let confrim = await this.$myconfirm("确定删除该部门吗？");
      if (confrim) {
        let res = await deleteRoleApi({ roleId: row.roleId });
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
      this.dialog.title = "编辑部门";
      this.addModel.formtype = "1";
      this.dialog.visible = true;
    },
    // 新增按钮事件
    addBtn() {
      this.addModel.formtype = "0";
      this.dialog.title = "新增部门";
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
            res = await addRoleApi(this.addModel);
          } else if (this.addModel.formtype == "1") {
            // 编辑
            res = await editRoleApi(this.addModel);
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
      let res = await getRoleListApi(this.parms);
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
      this.parms.roleName = "";
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