<script setup>
  const url='../src/img/loginPage.png';
</script>

<template>
<div style="width: 100vw; height: 100vh; overflow: hidden;display: flex">
  <div style="flex: 1;background-color: #181818">
    <el-image style="width: 100%; height: 100%" :src="url"  fit="cover" />
  </div>
  <div class="welcome-title">
    <div style="font-size: 30px;font-weight: bold">lorem ndfvn mvkmfvmlsdmfvadfmv</div>
    <div style="margin-top: 10px">lorem ndfvn mvkmfvmlsdmfvadfmv</div>
    <div style="margin-top: 5px">lorem ndfvn mvkmfvmlsdmfvadfmv</div>
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
</style>