<script setup>
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {get,post} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {reactive,ref} from "vue"
import router from "@/router";

const formRef=ref()
const coldTime = ref(0)
const isEmailValid = ref(false)
const active=ref(0)


const form=reactive({
  email:'',
  code:'',
  password:'',
  password_repeat:''
})

const confirmReset = () => {
  formRef.value.validate((isValid) => {
    if(isValid) {
      post('/api/auth/reset-confirm', {
        email: form.email,
        code: form.code
      }, () => active.value++)
    }
  })
}

const onValidate = (prop, isValid) => {
  if(prop === 'email'){
    console.log("驗證email")
    isEmailValid.value = isValid
  }

}

const  validateEmail=()=> {

  coldTime.value = 60
  get(`/api/auth/ask-code?emial=${form.email}&type=reset`, () => {
    ElMessage.success(`驗證碼已發送到:${form.email},請查看`)
    const handle = setInterval(
        () => {
          coldTime.value--
          if (coldTime.value === 0) {
            clearInterval(handle)
          }
        }, 1000)
  }, undefined, (message) => {
    ElMessage.warning(message)
    coldTime.value = 0
  })
}
  const doReset = () => {
    formRef.value.validate((isValid) => {
      if (isValid) {
        post('/api/auth/reset-password', {
          email: form.email,
          code: form.code,
          password: form.password
        }, () => {
          ElMessage.success('密码重置成功，请重新登录')
          router.push('/')
        })
      }
    })
  }


  const validatePassword = (rule, value, callback) => {
    if(value===''){
      callback(new Error('請再次輸入密碼'))
    }else if(value!==form.password){
      callback(new Error('兩次輸入的密碼不一致'))
    }else{
      callback()
    }
  }


const rule = {
    email: [
      {require:true,message:'請輸入信箱',trigger:['blur','change'] },
      {type:'email',message:'請輸入合法的信箱',trigger:['blur','change']}
    ],
    code: [
      {required: true, message: '請輸入獲取的驗證碼', trigger: 'blur'},
    ],
    password: [
      { required: true, message: '請輸入密碼', trigger: 'blur' },
      {min:6,max:20,message:"密碼長度在6~20字元之間",trigger:['blur','change']}
    ],
    password_repeat: [
      {validator: validatePassword, trigger: ['blur', 'change']},
    ]

  }



</script>

<template>

  <div style="text-align: center">
    <div style="margin-top: 30px">
      <el-steps :active="active"   finish-status="success"  align-center>
         <el-step title="驗證電子郵件"></el-step>
         <el-step title="重新設定密碼"></el-step>
      </el-steps>
    </div>
    <div style="margin: 0 20px" v-if="active ===0">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">重製密碼</div>
        <div style="font-size: 14px;color: grey">請輸入重置密碼的電子郵件</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" @validate="onValidate" ref="formRef">
          <el-form-item prop="email" >
            <el-input v-model="form.email" type="email" placeholder="電子郵件地址">
              <template #prefix>
                <el-icon><Message/></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code" >
            <el-row :gutter="10" style="width: 100%">
              <el-col :span="17">
                <el-input v-model="form.code"  maxlength="6" type="text" placeholder="請輸入驗證碼">
                  <template #prefix>
                    <el-icon><EditPen/></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button type="success" @click="validateEmail"  :disabled="!isEmailValid||coldTime>0">
                  {{coldTime>0?`請稍後${coldTime}秒`:'獲取驗證碼'}}</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="confirmReset()" style="width: 270px" type="warning" plain>開始重置密碼</el-button>
      </div>
    </div>
    <div style="margin: 0 20px" v-if="active ===1">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">重製密碼</div>
        <div style="font-size: 14px;color: grey">請填寫新密碼</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" @validate="onValidate" ref="formRef">
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
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="doReset()" style="width: 270px" type="danger" plain>立刻重置密碼</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>