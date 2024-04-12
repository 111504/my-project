<script setup>
import {defineEmits, defineProps,ref,watch } from "vue"
import {post,get,accessHeader} from "@/net"
import { ElMessage } from 'element-plus'
import axios from "axios";

const props=defineProps(
    {
      id:{
        type:Number,
        default:-1,
        required:true
      },
      dialogTitle:{
        type:String,
        default:'',
        required:true
      },
      dialogVisible:{
        type:Boolean,
        default:false,
        required:true
      }
    }
)


const form=ref({
  id:-1,
  username:"",
  password:"123456",
  status:"0",
  phonenumber:"",
  email:"",
  remark:""
})

// const checkUsername = async (rule, value, callback) => {
//   if(form.value.id==-1){
//     const res=await requestUtil.post("sys/user/checkUserName",{username:form.value.username});
//     if (res.data.code==500) {
//       callback(new Error("用户名已存在！"));
//     } else {
//       callback();
//     }
//   }else{
//     callback();
//   }
//
// }
function postCheckUsername(url, data, header) {
  // 返回一个 Promise 对象
  return new Promise((resolve, reject) => {
    axios.post(url, data, { headers: header })
        .then(response => {
          console.log("response.data internalPost", response.data);
          if (response.data.code === 200) {
            // 如果请求成功，解决 Promise
            resolve(response.data.data);
          } else {
            ElMessage.warning("Error: " + response.data.message)
            // 如果请求成功但业务逻辑上不符合预期，也拒绝 Promise
            reject(new Error(response.data.message));
          }
        })
        .catch(err => {
          // 如果请求失败（网络错误等），拒绝 Promise
          reject(err);
        });
  });
}
//檢查使用者姓名是否重複
// const checkUsername=(rule, value, callback)=>{
//   console.log("新增用戶觸發，先剪成用戶名稱是否重複")
//   if(form.value.id==-1){
//      internalPost("api/user/checkUserName",{username:form.value.username},accessHeader(),(reponse)=>{
//        ElMessage.success(reponse)
//      })
//   }
// }
const checkUsername = async (rule, value, callback) => {
  console.log("新增用户触发，先检查用户名是否重复");
  if (form.value.id === -1 && value) {
    try {
      // 等待 internalPost 完成
      await postCheckUsername("api/user/checkUserName", {username:form.value.username}, accessHeader());
      // 如果没有错误，则调用 callback() 表示验证通过
      callback();
    } catch (error) {
      // 如果有错误（用户名重复或网络错误等），调用 callback(error)
      callback(new Error("用户名重复或检查失败"));
    }
  } else {
    // 如果不需要检查（例如，id 不为 -1），直接调用 callback() 通过验证
    callback();
  }
};
const rules=ref({
  username:[
    { required: true, message: '請輸入用戶名稱'},
    { required: true, validator: checkUsername, trigger: "blur" }
  ],
  email: [{ required: true, message: "信箱不能為空", trigger: "blur" }, { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
  phonenumber: [{ required: true, message: "手機號碼不能為空", trigger: "blur" }, { pattern: /^09\d{8}$/, message: "請輸入正確的手機號碼", trigger: "blur" }],
})

const formRef=ref(null)
// const initFormData=async(id)=>{
//   const res=await requestUtil.get("sys/user/"+id);
//   form.value=res.data.sysUser;
// }

//在用戶修該資料前，先顯示用戶內容
const initFormData=async(id)=>{
  await get("api/user/"+id,(response)=>{
    ElMessage.success("初始化成功")
    console.log(response)
    form.value=response.sysUser;
  })
}
//監控按鈕，當你點擊修改按鈕，id!=-1，觸發初始化表單，向後端
watch(
    ()=>props.dialogVisible,
    ()=>{
      let id=props.id;
      console.log("id="+id)
      if(id!==-1){
        initFormData(id);
      }else{
        form.value={
          id:-1,
          username:"",
          password:"123456",
          status:"0",
          phonenumber:"",
          email:"",
          remark:""
        }
      }
    }
)
const emits=defineEmits(['update:modelValue','initUserList'])

const handleClose=()=>{
  emits('update:modelValue',false)
}

const handleConfirm=()=>{
  console.log("按下handleConfirm")
  formRef.value.validate(async(valid)=>{
    if(valid){
      console.log("form.value",form.value)
     await post("api/user/save",form.value,(response)=>{
         ElMessage.success("执行成功！")
         formRef.value.resetFields();
         emits("initUserList")
         handleClose();

      });

    }else{
      console.log("fail")
    }
  })
}

</script>

<template>
  <el-dialog
      model-value="dialogVisible"
      :title="dialogTitle"
      width="30%"
      @close="handleClose"
  >

    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="用戶名稱" prop="username">
        <el-input v-model="form.username" :disabled="form.id==-1?false:'disabled'"  />
        <el-alert
            v-if="form.id==-1"
            title="默認初始密碼：123456"
            :closable="false"
            style="line-height: 10px;"
            type="success" >
        </el-alert>
      </el-form-item>

      <el-form-item label="手機號碼" prop="phonenumber">
        <el-input v-model="form.phonenumber" />
      </el-form-item>

      <el-form-item label="信箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>

      <el-form-item label="帳號狀態" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="'0'">正常</el-radio>
          <el-radio :label="'1'">禁用</el-radio>
        </el-radio-group>
      </el-form-item>


      <el-form-item label="備註" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="4"/>
      </el-form-item>


    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">確認</el-button>
        <el-button  @click="handleClose">取消</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>