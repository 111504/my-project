<script setup>

import {ArrowDown, Plus} from "@element-plus/icons-vue";
import {useMenuStore} from "@/store/Store.js";

const store = useMenuStore()
const str=localStorage.getItem("access_token")
const currentUser=JSON.parse(str)
console.log(currentUser)
// const currentUser = (store.GET_USER);
</script>

<template>
<!--<div>這是個人中心頁面</div>-->
  <div class="app-container">
    <el-row>
      <el-col :span="7">
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
                <el-icon ><Plus /></el-icon>名稱
                <div class="pull-right">{{currentUser.username}}</div>
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>手機號碼
                <div class="pull-right">{{currentUser.phonenumber}}</div>
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>用戶信箱
                <div class="pull-right">{{currentUser.email}}</div>
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>所屬角色
                <div class="pull-right">{{currentUser.roles}}</div>
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>創建日期
                <div class="pull-right">{{formatDate(currentUser.loginDate)}}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card>
          <template v-slot:header>
            <div class="clearfix">
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
<!--            <el-tabs>-->
            <el-tab-pane label="基本资料" name="userinfo">
<!--              <UserInfo :user="user"/>-->
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
<!--              <ResetPwd :user="user"/>-->
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
</style>