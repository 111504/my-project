<template>
  <el-dropdown class="el-drop" trigger="click">
    <span class="el-dropdown-link">
      <el-avatar :icon="UserFilled" />
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item>
          <router-link class="myrouter-link" :to="{name:'user-info'}">个人中心</router-link>
        </el-dropdown-item>
        <el-dropdown-item >登出</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>


<script setup>
import {ArrowDown, UserFilled} from '@element-plus/icons-vue'
import {useMenuStore} from '@/store/Store.js'
import {ref} from "vue";
import {useRouter} from "vue-router";
const router = useRouter()
const avatarUrl = ref('')
const store = useMenuStore()

avatarUrl.value = `@/assets/user.png`


 function goToUserPage() {
     console.log("跳轉user")
    router.push('/user');
  }


</script>

<style scoped>

.el-dropdown-link {
  cursor: pointer;
  color: coral;
  display: flex;
  align-items: center;
}

.myrouter-link{
  text-decoration: none;
  color: var(--el-text-color-regular);
}

.el-drop {
  outline: none;
}

</style>