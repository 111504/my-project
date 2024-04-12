<script setup>
import {logout} from "@/net/index.js";
import router  from "@/router/index.js";
import Menu from "@/layout/Menu/index.vue";
import Header from "@/layout/Header/index.vue";
import Footer from "@/layout/Footer/index.vue";
import Tabs from "@/layout/Tabs/index.vue";

function userLogout(){
  logout(()=>router.push('/'))
}


</script>

<template>
  <div class="common-layout">
    <el-container class="el-container">
      <el-aside width="200px" class="common-aside">
       <Menu/>
      </el-aside>
      <el-container>
        <el-header class="common-header"><Header></Header></el-header>
        <el-main class="common-main">
<!--          <Tabs/>-->
          <router-view/>
        </el-main>
<!--        <el-main  class="common-main"><Tabs></Tabs></el-main>-->
        <el-footer><Footer></Footer></el-footer>
      </el-container>
    </el-container>
    <!--  <el-button @click="userLogout()">退出登入</el-button>-->

  </div>
</template>

<style scoped lang="css">
.el-container {
  height: 100%;
}

.common-layout {
  position: relative;
  width: 100%;
  height: 100vh;
}

.common-layout el-container {
  height: 100%;
}

.common-aside {
  //background: #a3aa83;
  border-right: 1px solid #ccc;
  height: 100%;
}

.common-header {
  background: #fff;
  border-bottom: 1px solid #ccc;
  padding: 0px;
}

.common-main:hover {
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
}

.common-main {
  background: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease-in-out;
  padding: 0px 0px 20px;
}


</style>