<template>
  <div class="demo-type">
    <div class="demo-basic--circle">
      <el-avatar :icon="UserFilled" />
    </div>


  </div>
</template>

<script setup lang="ts">
import { UserFilled } from '@element-plus/icons-vue'
</script>

<style scoped>
.demo-type {
  display: flex;
}
.demo-type > div {
  flex: 1;
  text-align: center;
  background-color: #282828;

}


</style>
