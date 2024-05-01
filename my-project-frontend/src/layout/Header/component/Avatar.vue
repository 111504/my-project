<template>
  <el-dropdown class="el-drop" trigger="click">
    <span class="el-dropdown-link">
      <div class="m-2"> {{currentUser.username}}</div>
       <el-icon><HomeFilled/></el-icon>&nbsp;&nbsp;
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-avatar :size="40" :src="avatarUrl" />
        <el-dropdown-item>{{currentUser.username}}</el-dropdown-item>
        <el-dropdown-item class="dropdown-item">
          <router-link class="myrouter-link" :to="{name:'帳戶資訊'}">帳戶資訊</router-link>
        </el-dropdown-item>
        <el-dropdown-item @click="userLogout()">登出</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>


<script setup>
import {ArrowDown, HomeFilled, UserFilled} from '@element-plus/icons-vue'
import {useMenuStore} from '@/store/Store.js'
import {ref} from "vue";
import {useRouter} from "vue-router";
import {logout} from "@/net/index.js";
import {getServerUrl} from "@/net/request.js";
const router = useRouter()
const avatarUrl = ref('')
const store = useMenuStore()
const currentUser=ref(store.GET_USER());
const avatar=store.GET_AVATAR();
 avatarUrl.value = getServerUrl()+`api/picture/fileSystem/${avatar}`

function userLogout() {
  logout(() =>{
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

.el-dropdown-menu {
  display: flex;
  flex-direction: column;
  align-items: center;

}



</style>