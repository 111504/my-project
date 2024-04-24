<script setup>
import {onMounted} from "vue";
import {checkTokenEnable, requestUsersInformation,TestGet} from "@/net"
onMounted(() => {
  // 頁面載入或刷新時 向後端發送檢查token是否合法
  checkTokenEnable();
  TestGet("/customers",() => {});
 // requestUsersInformation('/api/auth/requestUserInformation');
});
</script>

<template>
<div>
  <router-view/>
</div>
</template>

<style >
/* 重置样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
/* 设置 html 和 body 高度为 100% */
html,
body {
  height: 100vh;
  user-select: none; /* 禁止选择文本 */
  overflow: hidden;
}

/* 设置根元素高度为 100% */
#app {
  height:100%;
}
</style>
