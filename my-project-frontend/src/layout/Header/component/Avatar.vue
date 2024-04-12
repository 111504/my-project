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
        <el-dropdown-item>用戶姓名:{{currentUser}}</el-dropdown-item>
        <el-dropdown-item class="dropdown-item">
          <router-link class="myrouter-link" :to="{name:'帳戶資訊'}">帳戶資訊</router-link>
        </el-dropdown-item>
        <el-dropdown-item @click="userLogout()">登出</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>


<script setup>
import {ArrowDown, UserFilled} from '@element-plus/icons-vue'
import {useMenuStore} from '@/store/Store.js'
import {ref} from "vue";
import {useRouter} from "vue-router";
import {logout} from "@/net/index.js";
const router = useRouter()
const avatarUrl = ref('')
const store = useMenuStore()
// const store = useMenuStore()
const currentUser=ref(store.GET_USER());
console.log(currentUser.value)

avatarUrl.value = `@/assets/user.png`
function userLogout() {
  logout(() =>{
 //  store.CLEAR_MENUS()
    router.push("/")

  })
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