<template>
  <el-select 
        :ref="selectRef"
        style="width: 184px;"
        :clearable="clearable">
        <el-option style="height: 100%; padding: 0;" value=null>
            <el-tree
                :ref="treeRef"
                :data="data"
                :node-key="nodeKey"
                :props="props"
                :empty-text="emptytext"
                :show-checkbox="multiple"
                :highlight-current="!multiple"
                @node-click="nodeClick">
            </el-tree>
        </el-option>
    </el-select>
</template>

<script>
  export default {
    props:{
        clearable: {
            type: Boolean,
            default: true
        },
        selectRef: {
            type: String,
            default: 'select'
        },
        treeRef: {
            type: String,
            default: 'tree'
        },
        data: {
            type: Array,
            default: function () {
                return []
            }
        },
        nodeKey: {
            type: [String, Number],
            default: 'id'
        },
        props: {
            type: Object,
            default: function () {
                return {
                    label: 'label',
                    children: 'children'
                }
            }
        },
        emptytext: {
            type: String,
            default: '暂无数据'
        },
        multiple: {
            type: Boolean,
            default: false
        },
        
    },
    methods: {
        selectVisible(visible) {
            this.$emit("selectVisible", visible)
        },
        nodeClick(node) {
            this.$emit("nodeClick", node)
        }
    }
  }
</script>

<style lang="scss" scoped>

</style>