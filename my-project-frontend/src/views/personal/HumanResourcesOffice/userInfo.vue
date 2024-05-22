<template>
  <el-form ref="userRef" :model="form" :rules="rules" label-width="100px" >
<!-- <el-button type="success" :icon="DocumentAdd" @click="handleDialogValue()" v-if="store.HAS_AUTH('system:user:add')">新增</el-button>-->


    <el-button type="primary" v-if="store.HAS_AUTH('system:person:cauculate')"  >計算薪資</el-button>
    <el-button type="primary" v-else plain disabled >計算薪資</el-button>

    <el-button type="primary" v-if="store.HAS_AUTH('system:person:vacation')"  >假期安排</el-button>
    <el-button type="primary" v-else plain disabled >假期安排</el-button>

    <el-button type="primary" v-if="store.HAS_AUTH('system:person:performance')"  >假期安排</el-button>
    <el-button type="primary" v-else plain disabled >假期安排</el-button>


  </el-form>
</template>

<script setup>
import {defineProps, ref} from "vue";
import { ElMessage } from 'element-plus'
import {useMenuStore} from "@/store/Store.js";
import {get ,post} from "@/net"
import {DocumentAdd} from "@element-plus/icons-vue";
const store = useMenuStore()
let  id=localStorage.getItem("id")

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
const abc=ref(true)

const rules = ref({
  email: [{ required: true, message: "信箱不能為空", trigger: "blur" }, { type: "email", message: "請輸入正確的郵箱地址", trigger: ["blur", "change"] }],
  phonenumber: [{ required: true, message: "手機號碼不能為空", trigger: "blur" }, {pattern: /^09\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }],
});

// form.value=props.user;

console.log("props=",props.user)

const handleSubmit=()=>{

  userRef.value.validate(async (valid)=>{
    if(valid) {
      form.value.id=id;

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
