<template>
  <el-main height="">
    <!--
        新增按钮
        :model 表单组件绑定的数据域
        :ref 组件在当前页面唯一的 id
        :inline true排列一行 false排列一列
        size 组件大小
    -->
    <el-form label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-button v-if="hasPerm('sys:menu:add')" @click="addBtn" type="primary" icon="el-icon-plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!--
          菜单列表
          :data 表格的数据
          column 的 prop 需要跟返回的数据属性名对应
      -->
    <el-table :data="tableList" row-key="menuId" :tree-props="{ children: 'children' }" empty-text="暂无数据"
      :height="tableHeight" :expand-row-keys="tableList.map((item) => item.menuId + '')" size="small" border stripe>
      <el-table-column prop="menuLabel" label="菜单名称"></el-table-column>
      <el-table-column align="center" width="100" prop="type" label="菜单类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == '0'">目录</el-tag>
          <el-tag v-if="scope.row.type == '1'" type="success">菜单</el-tag>
          <el-tag v-if="scope.row.type == '2'" type="warning">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" width="100" prop="icon" label="菜单图标">
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="路由名称"></el-table-column>
      <el-table-column prop="path" label="路由地址"></el-table-column>
      <el-table-column prop="url" label="组件路径"></el-table-column>
      <el-table-column prop="menuCode" label="权限字段"></el-table-column>
      <el-table-column align="center" width="200" label="操作">
        <template slot-scope="scope">
          <el-button v-if="hasPerm('sys:menu:edit')" type="primary" icon="el-icon-edit" size="small" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-if="hasPerm('sys:menu:delete')" type="danger" icon="el-icon-delete" size="small" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

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
            grid-template-columns: auto auto;">
          <el-form-item prop="type" label="菜单类型" style="grid-column-start: 1; grid-column-end: 3">
            <el-radio-group v-model="addModel.type">
              <el-radio :label="'0'">目录</el-radio>
              <el-radio :label="'1'">菜单</el-radio>
              <el-radio :label="'2'">按钮</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="parentName" label="上级菜单">
            <!-- 选择上级部门弹框弹框 -->
            <el-select v-model="addModel.parentName" style="width: 184px" clearable @visible-change="selectVisible">
              <el-option style="height: 100%; padding: 0" value="null">
                <el-tree ref="parentTree" :data="parentList" node-key="menuId" :props="treeProps" empty-text="暂无数据"
                  :show-checkbox="false" :default-expanded-keys="[0]" :highlight-current="true" @node-click="nodeClick"></el-tree>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="menuLabel" label="菜单名称">
            <el-input v-model="addModel.menuLabel"></el-input>
          </el-form-item>
          <el-form-item prop="icon" label="菜单图标">
            <el-input v-model="addModel.icon"></el-input>
          </el-form-item>
          <el-form-item prop="menuCode" label="权限字段">
            <el-input v-model="addModel.menuCode"></el-input>
          </el-form-item>
          <el-form-item prop="name" label="路由名称" v-if="addModel.type == '1'">
            <el-input v-model="addModel.name"></el-input>
          </el-form-item>
          <el-form-item prop="url" label="组件路径" v-if="addModel.type == '1'">
            <el-input v-model="addModel.url"></el-input>
          </el-form-item>
          <el-form-item prop="path" label="路由地址" v-if="addModel.type != '2'">
            <el-input v-model="addModel.path"></el-input>
          </el-form-item>
          <el-form-item prop="remark" label="备注">
            <el-input v-model="addModel.remark"></el-input>
          </el-form-item>
          <el-form-item prop="orderNum" label="权限序号">
            <el-input v-model="addModel.orderNum"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import {
  getMenuListApi,
  getParentListApi,
  deleteMenuApi,
  addMenuApi,
  editMenuApi,
} from "@/api/system/menu";
import sysDialog from "@/components/system/sysDialog";

export default {
  // 组件在使用之间需要注册
  components: {
    sysDialog,
  },
  data() {
    return {
      // 表单验证规则
      rules: {
        type: [{ required: true, trigger: "change", message: "请选择菜单类型" }],
        parentName: [{ required: true, trigger: "change", message: "请选择上级菜单" }],
        menuLabel: [{ required: true, trigger: "change", message: "请填写菜单名称" }],
        name: [{ required: true, trigger: "change", message: "请填写路由名称" }],
        path: [{ required: true, trigger: "change", message: "请填写路由地址" }],
        url: [{ required: true, trigger: "change", message: "请填写组件路径" }],
        menuCode: [{ required: true, trigger: "change", message: "请填写权限字段" }],
      },
      // 表单绑定的数据
      addModel: {
        menuId: "",
        formtype: "", // 0新增 1编辑
        parentId: "",
        menuLabel: "",
        menuCode: "",
        name: "",
        path: "",
        url: "",
        type: "",
        icon: "",
        orderNum: "",
        remark: "",
        parentName: "",
      },
      // 弹框属性
      dialog: {
        title: "",
        visible: false,
        height: 350,
        width: 700,
      },
      // 表格高度
      tableHeight: window.innerHeight - 210,
      // 表格的数据
      tableList: [],
      // 上级菜单的数据
      parentList: [],
      // 上级菜单树属性配置
      treeProps: {
        children: "children",
        label: "menuLabel",
      },
    };
  },
  created() {
    this.getListData();
  },
  methods: {
    // 上级树节点击事件
    nodeClick(node) {
      this.addModel.parentId = node.menuId;
      this.addModel.parentName = node.menuLabel;
    },
    async selectVisible(visible) {
      if (visible) {
        // 获取上级部门的数据
        let res = await getParentListApi();
        if (res && res.code == 200) {
          console.log(res);
          this.parentList = res.data;
        }
      }
    },
    // 删除按钮事件
    async deleteBtn(row) {
      let confrim = await this.$myconfirm("确定删除该菜单吗？");
      if (confrim) {
        let res = await deleteMenuApi({ menuId: row.menuId });
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
      this.dialog.title = "编辑菜单";
      this.addModel.formtype = "1";
      this.dialog.visible = true;
    },
    // 新增按钮事件
    addBtn() {
      this.addModel.formtype = "0";
      this.dialog.title = "新增菜单";
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
            res = await addMenuApi(this.addModel);
          } else if (this.addModel.formtype == "1") {
            // 编辑
            res = await editMenuApi(this.addModel);
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
      let res = await getMenuListApi();
      if (res && res.code == 200) {
        console.log(res);
        this.tableList = res.data;
      }
    },
  },
};
</script>

<style lang="scss" scoped></style>