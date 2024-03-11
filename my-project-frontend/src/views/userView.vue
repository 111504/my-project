<script setup>
import {logout} from "@/net/index.js";
import router from "@/router/index.js";

function userLogout(){
  logout(()=>router.push('/'))
}
</script>

<template>
  <div>
    <el-text tag="p">這是user頁面</el-text>
    <el-button @click="userLogout()">退出登入</el-button>

  </div>
</template>

<style scoped>

</style>