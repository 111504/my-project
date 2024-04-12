<template>
  <el-dialog
      model-value="menuDialogVisible"
      title="分配权限"
      width="30%"
      @close="handleClose"
  >

    <el-form
        ref="formRef"
        :model="form"
        label-width="100px"
    >

      <el-tree
          ref="treeRef"
          :data="treeData"
          :props="defaultProps"
          show-checkbox
          :default-expand-all=true
          node-key="id"
          :check-strictly=true
      />

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
import {post,get,accessHeader} from "@/net"
import { ElMessage } from 'element-plus'

const defaultProps = {
  children: 'children',
  label: 'name'
}


const props=defineProps(
    {
      id:{
        type:Number,
        default:-1,
        required:true
      },
      menuDialogVisible:{
        type:Boolean,
        default:false,
        required:true
      }
    }
)

const treeRef=ref(null)

const treeData=ref([])

const form=ref({
  id:-1
})


const formRef=ref(null)

const initFormData=async(id)=>{
  //列出所有權限選單
  await get("sys/menu/treeList",(res)=>{
    console.log("sys/menu/treeList",res)
    treeData.value=res.treeMenu;
    form.value.id=id;
  });

//傳入該用戶的id，查詢該用戶所擁有的權限
  await get("sys/role/menus/"+id,(res2)=>{
    console.log("sys/role/menus/",res2)
    treeRef.value.setCheckedKeys(res2);
  });

}


watch(
    ()=>props.menuDialogVisible,
    ()=>{
      let id=props.id;
      console.log("id="+id)
      if(id!=-1){
        initFormData(id)
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
      let menuIds=treeRef.value.getCheckedKeys();
      await post("sys/role/updateMenus/"+form.value.id,menuIds,(res)=>{
        ElMessage.success("执行成功！")
        emits("initRoleList")
        handleClose();
      });
    }else{
      console.log("fail")
    }
  })
}

</script>

<style scoped>

</style>
