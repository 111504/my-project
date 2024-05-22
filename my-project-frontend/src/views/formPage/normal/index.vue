<template>
  <el-row :gutter="20" class="header">
    <el-button class="buttonCustom" :icon="DocumentAdd" type="primary" plain @click="dialogFormVisible = true">
      醫療表單
    </el-button>
  </el-row>
  <el-dialog v-model="dialogFormVisible" title="證明申請附表" width="500">
    <el-form :model="form">
      <div>姓名</div>
      <el-form-item aria-label="姓名"   >
        <el-input  class="border border-primary  border-1 rounded" v-model="form.name" autocomplete="off" />
      </el-form-item>
      <div>性別</div>
      <el-radio-group v-model="form.gender"  aria-label="性別" >
        <div class=" " >
          <el-radio value="boy" size="large">男</el-radio>
          <el-radio value="girl" size="large">女</el-radio>
        </div>
      </el-radio-group>
      <div>身分證字號</div>
      <el-form-item aria-label="身分證字號" >
        <el-input class="border border-primary  border-1 rounded" v-model="form.idNumber" autocomplete="off" />
      </el-form-item>
      <div class="block" aria-label="出生日期">
        <span class="demonstration">出生日期 </span>
        <el-date-picker
            v-model="form.birthday"
            type="date"
            placeholder="選擇出生日期"
            :size="size"
        />
      </div>
      <div>居住地址</div>
      <el-form-item aria-label="居住地址" >
        <el-input class="border border-primary  border-1 rounded" v-model="form.address" autocomplete="off" />
      </el-form-item>

      <div class="flex flex-wrap gap-6 items-center">
        <div>相關疾病:</div>
        <el-select
            v-model="form.disease"
            placeholder="(請務必勾選)"
            size="large"
            style="width: 240px"
        >
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </div>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">
          提交
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import {Delete, DocumentAdd, Search} from "@element-plus/icons-vue";
import axios from "axios";
const value = ref('')

const options = [
  {
    value: 'diabetes',
    label: '糖尿病',
  },
  {
    value: 'hypertension',
    label: '高血壓',
  },
  {
    value: 'heartfailure',
    label: '心臟衰竭',
  },
  {
    value: 'heartdisease',
    label: '心臟病',
  },
  {
    value: 'cerebrovasculardisease',
    label: '腦血管病變',
  },
]
const dialogFormVisible = ref(false)
const form = ref({
  name: '王大明',
  delivery: false,
  type: [],
  resource: '',
  desc: '',
  address: '豐勢路二段305巷18弄',
  disease: '',
  birthday: '',
  gender: 'boy',
  idNumber: 'L125178444',
})
const size = ref<'default' | 'large' | 'small'>('default')


const submitForm = async () => {


  try {
   const response = await axios.post('http://localhost:8787/api/submitForm', form.value);
   // console.log('Form submitted successfully:', response.data);
    console.log('Form submitted successfully',form.value);
    // 处理成功响应，如关闭对话框或显示成功消息
    dialogFormVisible.value = false;
  } catch (error) {
    console.error('Failed to submit form:', error);
    // 处理错误响应，如显示错误消息
  }
};

</script>

<style scoped>
.search_input{

  border: 1.5px solid cornflowerblue;
  border-radius: 5px;

  padding-left: 10px;
  box-sizing: border-box;
}
.buttonCustom{
  background-color: #bcd0f3;
}

.right-aligned {
  background-color: #b7c8e0;

}



</style>