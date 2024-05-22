<script setup>
//  import imageUrl from '../../public/loginPage.png'
  // const url=require('./assets/loginPage.png');
</script>

<template>
<div class="welcome-container" >
  <div style="flex: 1;background-color: #181818">
    <el-image style="width: 100%; height: 100%"  src="/loginPage.png"  fit="cover" />

<!--    <img src="~@/assets/loginPage.png">-->
  </div>
  <div class="welcome-title">
    <div style="font-size: 30px;font-weight: bold">權限系統專題實作</div>
    <div style="margin-top: 10px">歡迎來到權限系統實作</div>
    <div style="margin-top: 5px">此系統製作者為Hung che wei</div>
  </div>
  <div class="right-card">
     <router-view v-slot="{Component}">
       <transition name="el-fade-in-linear"   mode="out-in">
         <component :is="Component" />
       </transition>
     </router-view>
  </div>
</div>
</template>

<style scoped>

.welcome-title{
   position: absolute;
   bottom: 30px;
   left: 30px;
   color:white;
   text-shadow: 0 0 10px black;
}

.right-card{
  width: 400px;
  z-index: 5;
  background-color: white;
}

.welcome-container{
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  display: flex;
}
</style>