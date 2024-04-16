<script setup>
// import avatar from './avatar.vue'
import {ref} from "vue";
import userInfo from './userInfo.vue'
import ResetPwd from './resetPwd.vue'
import {ArrowDown, Plus} from "@element-plus/icons-vue";
import {useMenuStore} from "@/store/Store.js";
import {get ,post} from "@/net"
import { formatDateFromString} from '@/util/formatDate.js'
import SvgIcon from "@/components/SvgIcon.vue";

const store = useMenuStore()
const user=ref(store.GET_USER());
const str=localStorage.getItem("access_token")
const currentUser=JSON.parse(str)
console.log("currentUser=",currentUser)
const activeTab = ref("userInfo");
// const currentUser = (store.GET_USER);
</script>

<template>
<!--<div>這是個人中心頁面</div>-->
  <div class="app-container">
    <el-row>
      <el-col :span="8">
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>個人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <Avatar :user="currentUser"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon  class="acitonButton"  name="user"/>&nbsp;&nbsp;用戶名稱

                <div class="pull-right">{{currentUser.username}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon class="acitonButton"  name="phone" />&nbsp;&nbsp;手機號碼
                <div class="pull-right">{{currentUser.phoneNumber}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon class="acitonButton"  name="email" />&nbsp;&nbsp;用戶郵箱
                <div class="pull-right">{{currentUser.email}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon class="acitonButton" name="peoples" />&nbsp;&nbsp;所屬角色
                <div class="pull-right">{{currentUser.role}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon class="acitonButton" name="date" />&nbsp;&nbsp;登入時間
                <div class="pull-right">{{formatDateFromString(currentUser.loginDate)}}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template v-slot:header>
            <div class="clearfix">
              <span>基本資料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userInfo">
              <userInfo :user="currentUser"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <ResetPwd :user="user"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.app-container{
  height: 100%;
  background-color: #304156;
}

.list-group-striped>.list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 13px;
  height: 50px;
}

.pull-right{
  float: right!important;
  font-size: 15px;
}

.acitonButton{

  width: 25px;
  height: 25px;
}


:deep(.el-card__body){
  height:75vh;
}

:deep(.box-card){
  height:100%;
}
</style>