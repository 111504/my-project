<template>
  <el-dialog
      model-value="roleDialogVisible"
      title="分配角色"
      width="30%"
      @close="handleClose"
  >
    <el-form
        ref="formRef"
        :model="form"
        label-width="100px"
    >

      <el-checkbox-group v-model="form.checkedRoles">
        <el-checkbox v-for="role in form.roleList" :id="role.id.toString()" :key="role.id" :label="role.id"  name="checkedRoles" >{{role.name}}</el-checkbox>
      </el-checkbox-group>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">確認</el-button>
        <el-button  @click="handleClose">取消</el-button>
      </span>
    </template>

  </el-dialog>
</template>

<script setup>

import {defineEmits, defineProps, ref, watch} from "vue";
import {post,get} from "@/net"
import { ElMessage } from 'element-plus'


//從父組件傳送過來的信息，給子組件
const props=defineProps(
    {
      id:{
        type:Number,
        default:-1,
        required:true
      },
      roleDialogVisible:{
        type:Boolean,
        default:false,
        required:true
      },
      sysRoleList:{
        type:Array,
        default:[],
        required:true
      }
    }
)


const form=ref({
  id:-1,
  roleList:[],
  checkedRoles:[],
})


const formRef=ref(null)

// const initFormData=async(id)=>{
//   const res=await requestUtil.get("sys/role/listAll");
//   form.value.roleList=res.data.data
//   form.value.id=id;
// }

const initFormData=async(id)=>{
  await get("sys/role/listAll",(response)=>{
    ElMessage.success("初始化成功")
    // console.log(Object.prototype.toString.call(response.data.roleList))
    console.log( response)
     form.value.roleList=response.roleList;
     form.value.id=id;
  })
}
watch(
    ()=>props.roleDialogVisible,
    ()=>{
      let id=props.id;
      console.log("id="+id)
      if(id!==-1){
        form.value.checkedRoles=[]
        props.sysRoleList.forEach(item=>{
          form.value.checkedRoles.push(item.id);
        })
        initFormData(id)
      }
      console.log("checkedRoles", form.value.checkedRoles)
    }
)


const emits=defineEmits(['update:modelValue','initUserList'])

const handleClose=()=>{
  emits('update:modelValue',false)
}

const handleConfirm=()=>{
  formRef.value.validate(async(valid)=>{
    if(valid){
      console.log("form.value.checkedRoles",form.value.checkedRoles)
       await post(`api/user/grantRole/${form.value.id}`,form.value.checkedRoles,(response)=>{
       ElMessage.success("執行成功！",response)
       emits("initUserList")
       handleClose();
       });
    }else{
      console.log("fail")
    }
  })
}
</script>