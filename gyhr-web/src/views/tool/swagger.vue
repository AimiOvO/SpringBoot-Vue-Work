<template>
<div :style="'height:' + height">
    <iframe
        :src="url"
        frameborder="no"
        style="width: 100%; height: 100%"
        scrolling="auto"
    />
</div>
</template>
<script>
export default {
data() {
    return {
        height: document.documentElement.clientHeight - 94.5 + "px;",
        url: process.env.VUE_APP_BASE_API_PRO + "/swagger-ui/index.html"
    };
},
};
</script>
  