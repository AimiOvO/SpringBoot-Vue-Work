<template>
    <el-upload :limit="5" action="#" list-type="picture-card" :auto-upload="false">
        <i slot="default" class="el-icon-plus"></i>
        <div slot="file" slot-scope="{file}">
            <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
            <span class="el-upload-list__item-actions">
                <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
                    <i class="el-icon-delete"></i>
                </span>
            </span>
        </div>
    </el-upload>
</template>
<script>
export default {
    data() {
        return {
            disabled: false
        };
    },
    methods: {
        handleRemove(file) {
            console.log(file);
        }
    }
}
</script>