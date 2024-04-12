<script setup>
import {defineEmits, defineProps,ref,watch } from "vue"
import {post,get,accessHeader} from "@/net"
import { ElMessage } from 'element-plus'

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
  name:"",
  code:"",
  remark:""
})




const rules=ref({
  name:[
    { required: true, message: '請輸入角色名稱'}
  ],
  code:[
    { required: true, message: '請輸入權限名稱'}
  ],
})

const formRef=ref(null)

const initFormData=async(id)=>{
  await get("sys/role/"+id,(res)=>{
    form.value=res.sysRole;
  });
}

watch(
    ()=>props.dialogVisible,
    ()=>{
      let id=props.id;
      console.log("id="+id)
      if(id!=-1){
        initFormData(id);
      }else{
        form.value={
          id:-1,
          name:"",
          code:"",
          remark:""
        }
      }
    }
)

const emits=defineEmits(['update:modelValue','initRoleList'])

const handleClose=()=>{
  emits('update:modelValue',false)
}

const handleConfirm=()=>{
  formRef.value.validate(async(valid)=>{
    if(valid){
      await post("sys/role/save",form.value,()=>{
        ElMessage.success("执行成功！")
        formRef.value.resetFields();
        emits("initRoleList")
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
      <el-form-item label="角色名稱" prop="name">
        <el-input v-model="form.name"  />
      </el-form-item>

      <el-form-item label="權限名稱" prop="code">
        <el-input v-model="form.code" />
      </el-form-item>


      <el-form-item label="備註" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="4"/>
      </el-form-item>


    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">確認</el-button>
        <el-button @click="handleClose"
        >取消</el-button
        >
      </span>
    </template>
  </el-dialog>

</template>



<style lang="scss" scoped>

</style>
