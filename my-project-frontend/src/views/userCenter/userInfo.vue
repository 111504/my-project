<template>
  <el-form ref="userRef" :model="form" :rules="rules" label-width="100px" >
    <el-form-item label="手機號碼：" prop="phonenumber">
      <el-input v-model="form.phonenumber" maxlength="10" />
    </el-form-item>
    <el-form-item label="用户信箱：" prop="email">
      <el-input v-model="form.email" maxlength="50" />
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
import {get ,post} from "@/net"
const store = useMenuStore()
let  userId=localStorage.getItem("userId")

const props = defineProps({
  user: {
    type: Object,
    default: () => ({}),
    required:true
  }
})

const form=ref({
  id:-1,
  phonenumber:'',
  email:''
})

const userRef=ref(null)


const rules = ref({
  email: [{ required: true, message: "信箱不能為空", trigger: "blur" }, { type: "email", message: "請輸入正確的郵箱地址", trigger: ["blur", "change"] }],
  phonenumber: [{ required: true, message: "手機號碼不能為空", trigger: "blur" }, {pattern: /^09\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }],
});

// form.value=props.user;

console.log("props=",props.user)

const handleSubmit=()=>{

  userRef.value.validate(async (valid)=>{
    if(valid) {
      form.value.id=userId;

      await post("api/user/save", form.value,()=>{
        ElMessage.success("執行成功！")
        store.SET_USER(store.$state,form.value)
      });
    }
  })
}


</script>

<style lang="scss" scoped>

</style>
