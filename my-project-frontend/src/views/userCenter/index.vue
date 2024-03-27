<script setup>

import {ArrowDown, Plus} from "@element-plus/icons-vue";
</script>

<template>
<!--<div>這是個人中心頁面</div>-->
  <div class="app-container">
    <el-row>
      <el-col :span="7">
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
<!--              <Avatar :user="user"/>-->
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>名稱
<!--                <div class="pull-right">{{user.username}}</div>-->
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>手机号码
<!--                <div class="pull-right">{{user.phonenumber}}</div>-->
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>用户邮箱
<!--                <div class="pull-right">{{user.email}}</div>-->
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>所属角色
<!--                <div class="pull-right">{{user.roles}}</div>-->
              </li>
              <li class="list-group-item">
                <el-icon ><Plus /></el-icon>创建日期
<!--                <div class="pull-right">{{date.toISOString().replace('T', ' ').slice(0, 19)}}</div>-->
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
<!--          <el-tabs v-model="activeTab">-->
            <el-tabs>
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