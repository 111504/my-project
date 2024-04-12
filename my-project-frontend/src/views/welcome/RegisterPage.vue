<script setup>
import {reactive,ref} from "vue"
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";
const form=reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})


const coldTime = ref(0)
const formRef=ref()
const isEmailValid = ref(false)

const validateUsername=(rule,value,callback)=>{
  if(value ===''){
    console.log("空")
     callback(new Error('請輸入用戶名稱'))
  }else if(!/^[a-zA-Z0-9]+$/.test(value)){
    callback(new Error('請輸入英文、數字'))
  }else{
    callback()
  }

}

const validataPassword=(rule,value,callback)=>{
  if(value===''){
    callback(new Error('請再次輸入密碼'))
  }else if(value!==form.password){
    callback(new Error('兩次輸入的密碼不一致'))
  }else{
    callback()
  }
}


const onValidate=(prop,isValid)=>{
  if(prop==='email'){
    console.log("驗證email",isValid)

     isEmailValid.value=isValid
  }
  if(prop === 'password'){
    console.log("驗證密碼")

  }
  if(prop === 'password_repeat'){
    console.log("驗證重複密碼")

  }
}
const  register= ()=>{
  //觸發表單的驗證。驗證結果（有效則為 true，無效則為 false）將作為 isValid 參數傳遞給回調函數。
  formRef.value.validate((isValid)=>{
    if(isValid){
      post('/api/auth/register',{
        username:form.username,
        password:form.password,
        email:form.email,
        code:form.code,
        phonenumber:form.phonenumber,
      },()=>{
        ElMessage.success('註冊成功')
        router.push('/')
      })
    }else{
      ElMessage.warning('錯誤, 請填寫完整註冊內容')
    }
  })
}


const  validateEmail=()=> {


    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      console.log("此為求職專案，故冷卻時間設定為10秒，方便展示")
      coldTime.value = 10
      ElMessage.success(`驗證碼已發送到:${form.email},請查看`)
      const handle = setInterval(
          () => {
            coldTime.value--
            console.log(coldTime.value)
            if (coldTime.value === 0) {
              clearInterval(handle)
            }
          }, 1000)
    }, undefined, (message) => {
      console.log("請求驗證碼結束",message)
      ElMessage.warning(message)
      coldTime.value = 0
    })

}




const rule={
  username: [
      {validator: validateUsername,trigger:['blur'] },
  ],
  password: [
      { required: true, message: '請輸入密碼', trigger: 'blur' },
      {min:6,max:20,message:"密碼長度在6~20字元之間",trigger:['blur']}
  ],
  password_repeat: [
      {validator: validataPassword,trigger:['blur'] },

  ],
  email: [
      {require:true,message:'請輸入信箱',trigger:['blur'] },
      {type:'email',message:'請輸入合法的信箱',trigger:['blur','change']}
  ],
  code:[
    {required:true,message:'請輸入驗證碼',trigger:['blur']}
  ],
  phonenumber: [{ required: true, message: "手機號碼不能為空", trigger: "blur" }, { pattern: /^09\d{8}$/, message: "請輸入正確的手機號碼", trigger: "blur" }],
}




</script>
<!--當你調用 formRef.value.validate() 時，Vue.js 會執行表單的驗證規則並更新表單的狀態。-->
<template>
<div style="text-align: center;margin: 0 20px">
  <div style="margin-top: 100px">
    <div style="font-size: 25px;font-weight: bold">註冊新用戶</div>
    <div style="font-size: 14px;color: grey">歡迎註冊，請在下方填寫信息</div>
  </div>
  <div style="margin-top: 50px">
    <el-form :model="form"  :rules="rule" @validate="onValidate"  ref="formRef">
      <el-form-item prop="username" >
        <el-input v-model="form.username" maxlength="15" type="text" placeholder="用戶名稱">
        <template #prefix>
          <el-icon><User></User></el-icon>

        </template>
        </el-input>
      </el-form-item>
      <el-form-item  prop="password">
        <el-input type="password" v-model="form.password"  maxlength="20"  placeholder="密碼">
        <template #prefix>
          <el-icon><Lock></Lock></el-icon>
        </template>
        </el-input>
      </el-form-item>
      <el-form-item  prop="password_repeat">
        <el-input type="password" v-model="form.password_repeat"  maxlength="20"  placeholder="重複密碼">
          <template #prefix>
            <el-icon><Lock></Lock></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item  prop="email">
        <el-input v-model="form.email"  type="email" placeholder="信箱帳號">
          <template #prefix>
            <el-icon><Message></Message></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="phonenumber">
        <el-input v-model="form.phonenumber" placeholder="電話號碼"/>
        <template #prefix>
          <el-icon><Message></Message></el-icon>
        </template>
      </el-form-item>
      <el-form-item  prop="code">
        <el-row :gutter="10" style="width: 100%">
          <el-col :span="17">
            <el-input v-model="form.code" maxlength="6" type="text" placeholder="請輸入驗證碼">
              <template #prefix>
                <el-icon><EditPen></EditPen></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="5">
            <el-button type="success" @click="validateEmail"  :disabled="!isEmailValid||coldTime>0">{{coldTime>0?`請稍後${coldTime}秒`:'獲取驗證碼'}}</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
  </div>

  <div style="margin-top: 80px">
    <el-button style="width: 270px" type="warning"  @click="register"  plain>立刻註冊</el-button>
  </div>
  <div style="margin-top: 20px">
    <span style="font-size: 12px;line-height: 15px;color: #282828;padding-right: 5px">已經有帳號?</span>
    <el-link style="translate: 0 -2px"  type="primary" @click="router.push('/')">立即登入</el-link>
  </div>
</div>
</template>

<style scoped>

</style>