<script setup>
import {reactive,ref} from "vue";
import {User,Lock} from '@element-plus/icons-vue'
import  {login} from "@/net";
import router from "@/router/index.js";

const formRef=ref()

const form=reactive({
  username:'',
  password:'',
  remember: false
})

const rule={
  username:[
    {required:true,message:'請輸入使用者名稱'}
  ],
  password:[
    {required:true,message:'請輸入密碼'}
  ]
}

function userLogin(){
    formRef.value.validate((valid)=>{
      if(valid){
        if(typeof form.username != "undefined" && typeof form.password !="undefined"){
          console.log("Enter Func() userLogin "+form.username+" "+form.password)
          login(form.username,form.password,form.remember,
              (data) => {
                  console.log("Func() userLogin ",data.role)
                // 假设登录成功后，login函数的回调会接收到一个包含用户角色的data对象
                router.push('/index');
                // 根据用户角色跳转到不同的页面
                // switch (data.role) {
                //   case 'admin':
                //     router.push('/admin');
                //     break;
                //   case 'user':
                //     router.push('/user');
                //     break;
                //   default:
                //     // 如果角色既不是admin也不是user，跳转到默认页面
                //     router.push('/index');
                //     break;
                // }
              }
          )
        }
        else{
          console.log("Enter Func() userLogin form.username or form.password have undefined value")
          router.push('/');
        }


      }
    })



}

</script>



<template>
<div style="text-align: center; margin: 0 20px">
  <div style="margin-top: 150px">
    <div style="font-size: 25px;font-weight: bold">登入</div>
    <div style="font-size: 14px;color: grey">請輸入帳號密碼登入</div>
  </div>
  <div style="margin-top: 50px">
    <el-form :model="form" :rules="rule" ref="formRef">
      <el-form-item prop="username">
        <el-input v-model="form.username" maxlength="25" type="text" placeholder="用戶名稱/信箱">
          <template #prefix>
            <el-icon>
              <User/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  type="password" maxlength="20" placeholder="密碼">
          <template #prefix>
            <el-icon>
              <Lock>
              </Lock>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12"  style="text-align: left">
          <el-form-item prop="remember">
            <el-checkbox v-model="form.remember" label="記住我">

            </el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="text-align: right">
           <el-link @click="router.push('/reset')">忘記密碼?</el-link>
        </el-col>
      </el-row>
    </el-form>
    <div style="margin-top: 40px; margin-bottom:10px">
      <el-button @click="userLogin()" style="width: 270px" type="success" plain>登入</el-button>
    </div>
    <el-divider>
      <span style="font-size: 13px;color:grey">創建帳號</span>
    </el-divider>
    <div>
      <el-button @click="router.push('/register')"   style="width: 270px" type="warning" plain>註冊</el-button>
    </div>
    <div style="margin-top: 40px; margin-bottom:10px">
      <el-button @click="" style="width: 270px"  plain>測試提取資料</el-button>
    </div>
  </div>
</div>
</template>

<style scoped>

</style>