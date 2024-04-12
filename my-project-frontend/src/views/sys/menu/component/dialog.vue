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

      <el-form-item label="上級菜單" prop="parentId">
        <el-select v-model="form.parentId" placeholder="請選擇上級菜單" @change="$forceUpdate()">
          <template v-for="item in tableData">
            <el-option :label="item.name" :value="item.id"></el-option>
            <template v-for="child in item.children">
              <el-option :label="child.name" :value="child.id">
                <span>{{ "    -- " + child.name }}</span>
              </el-option>
            </template>
          </template>
        </el-select>
      </el-form-item>

      <el-form-item label="菜單類型" prop="menuType" label-width="100px">
        <el-radio-group v-model="form.menuType">
          <el-radio :label="'M'">目錄</el-radio>
          <el-radio :label="'C'">菜單</el-radio>
          <el-radio :label="'F'">按鈕</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="菜單圖標" prop="icon">
        <el-input v-model="form.icon" />
      </el-form-item>

      <el-form-item label="菜單名稱" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="權限標示" prop="perms">
        <el-input v-model="form.perms" />
      </el-form-item>

      <el-form-item label="組件路徑" prop="component">
        <el-input v-model="form.component" />
      </el-form-item>

      <el-form-item label="顯示順序" prop="orderNum" >
        <el-input-number v-model="form.orderNum" :min="1" label="顯示順序"></el-input-number>
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

<script setup>

import {defineEmits, defineProps, ref, watch} from "vue";

import {get ,post} from "@/net"
import { ElMessage } from 'element-plus'

const tableData=ref([])


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
      },
      tableData:{
        type:Array,
        default:[],
        required:true
      }
    }
)


const form=ref({
  id:-1,
  parentId:'',
  menuType:"M",
  icon:'',
  name:'',
  perms:'',
  component:'',
  orderNum:1
})


const rules=ref({
  parentId:[
    { required: true, message: '請選擇父級菜單'}
  ],
  name: [{ required: true, message: "菜單名稱不能為空", trigger: "blur" }]
})

const formRef=ref(null)

const initFormData=async(id)=>{
  // const res=await requestUtil.get("sys/menu/"+id);
  await get("sys/menu/"+id,(res)=>{
    console.log("res.sysMenu=",res.sysMenu)
    form.value=res.sysMenu;
  })
}



watch(
    ()=>props.dialogVisible,
    ()=>{
      let id=props.id;
      tableData.value=props.tableData;
      if(id!=-1){
        initFormData(id)
      }else{
        form.value={
          id:-1,
          parentId:'',
          menuType:"M",
          icon:'',
          name:'',
          perms:'',
          component:'',
          orderNum:1
        }
      }
    }
)


const emits=defineEmits(['update:modelValue','initMenuList'])

const handleClose=()=>{
  emits('update:modelValue',false)
}

const handleConfirm=()=>{
  formRef.value.validate(async(valid)=>{
    if(valid){
      await post("sys/menu/save",form.value,(res)=>{
        ElMessage.success("執行成功！")
        formRef.value.resetFields();
        emits("initMenuList")
        handleClose();
      })
    }else{
      console.log("fail")
    }
  })
}

</script>

<style scoped>

</style>
