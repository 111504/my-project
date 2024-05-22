<template>
  <el-form ref="pwdRef" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="舊密碼" prop="oldPassword">
      <el-input v-model="form.oldPassword" placeholder="請輸入舊密碼" type="password" show-password />
    </el-form-item>
    <el-form-item label="新密碼" prop="newPassword">
      <el-input v-model="form.newPassword" placeholder="請輸入新密碼" type="password" show-password />
    </el-form-item>
    <el-form-item label="確認密碼" prop="confirmPassword">
      <el-input v-model="form.confirmPassword" placeholder="請確認密碼" type="password" show-password/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleSubmit">保存</el-button>

    </el-form-item>
  </el-form>
</template>

<script setup>
import {defineProps, ref} from "vue";
import { ElMessage } from 'element-plus'
import {useMenuStore} from "@/store/Store.js";
import {get, logout, post} from "@/net"
const store = useMenuStore()
let  id=localStorage.getItem("id")

const props=defineProps(
    {
      user:{
        type:Object,
        default:()=>{},
        required:true
      }
    }
)

const form=ref({
  id:-1,
  oldPassword:'',
  newPassword:'',
  confirmPassword:''
})

const pwdRef=ref(null)

// form.value=props.user;



const equalToPassword = (rule, value, callback) => {
  if (form.value.newPassword !== value) {
    callback(new Error("兩次的密碼不一致"));
  } else {
    callback();
  }
};

const rules = ref({
  oldPassword: [{ required: true, message: "舊密碼不能為空", trigger: "blur" }],
  newPassword: [{ required: true, message: "新密碼不能為空", trigger: "blur" }, { min: 6, max: 20, message: "長度在6~ 20個長度", trigger: "blur" }],
  confirmPassword: [{ required: true, message: "确確認密碼不能為空", trigger: "blur" }, { required: true, validator: equalToPassword, trigger: "blur" }]
});


const handleSubmit=()=>{

  pwdRef.value.validate(async (valid)=>{
    if(valid) {
      form.value.id=id;

      await post("api/user/updateUserPwd", form.value,()=>{
        ElMessage.success("密碼修改成功！即將登出帳號")
        store.SET_USER(store.$state,form.value)
        form.value.oldPassword = ''
        form.value.newPassword = ''
        form.value.confirmPassword =''
        setTimeout(() => {
          logout(()=>{
            window.location.reload()
          }); // 假設你的登出函數叫做 logout
        }, 4000); // 4000 毫秒，即 4 秒

      });
    }
  })
}

</script>

<style lang="scss" scoped>

</style>
