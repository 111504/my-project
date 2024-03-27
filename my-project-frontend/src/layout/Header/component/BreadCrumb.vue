<script setup>

import {HomeFilled} from "@element-plus/icons-vue";
import {ref, watch} from "vue";
import {useRoute} from "vue-router";

const route = useRoute()
const breadCrumbList = ref([])
const parentName = ref("")

const initBreadCrumbList = ()=>{
  breadCrumbList.value = route.matched;
  console.log(route.matched)
  parentName.value = route.meta.parentName
}
//監聽router 當router發生變化時觸發
watch(route,()=>{
  initBreadCrumbList()
},{deep:true,immediate:true})


</script>

<template>
  <el-icon><HomeFilled/></el-icon>&nbsp;&nbsp;
  <el-breadcrumb separator="/">
    <el-breadcrumb-item v-for="(item,index) in breadCrumbList" :key="index">
      <span class="root" v-if="parentName && index>0">{{parentName}}&nbsp;/&nbsp;</span>
      <span>{{item.name}}</span>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<style scoped>
.root{
  color: #666;
  font-weight: 600;
}
</style>