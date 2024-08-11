<template>
  <div class="app-container">
    <!-- 搜索框 -->
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input
            class="search_input"
            placeholder="請輸入用戶姓名..."
            v-model="queryForm.query"
            clearable
            aria-label="請輸入用戶姓名..."
        ></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="filterData">搜索</el-button>
    </el-row>

    <!-- 表格 -->
    <el-table :data="paginatedData">
      <el-table-column prop="month" label="月份" sortable></el-table-column>
      <el-table-column prop="peak" label="尖峰 (kWh)" sortable></el-table-column>
      <el-table-column prop="halfPeak" label="半尖峰 (kWh)" sortable></el-table-column>
      <el-table-column prop="offPeak" label="離峰 (kWh)" sortable></el-table-column>
      <el-table-column prop="totalUsage" label="總用電量 (kWh)" sortable></el-table-column>
      <el-table-column prop="description" label="描述/說明"></el-table-column>
      <el-table-column prop="emissions" label="碳排放量 (kg)" sortable></el-table-column>
      <el-table-column prop="file" label="說明檔"></el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="[5, 10, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredData.length"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Search } from '@element-plus/icons-vue';
import {get, post} from "@/net";
const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 5
});

const allData = ref([]); // 保存從後端獲取的所有數據
const filteredData = ref([]); // 保存搜索過濾後的數據

const paginatedData = computed(() => {
  const start = (queryForm.value.pageNum - 1) * queryForm.value.pageSize;
  const end = start + queryForm.value.pageSize;
  return filteredData.value.slice(start, end);
});

const filterData = () => {
  if (!queryForm.value.query) {
    filteredData.value = allData.value;
  } else {
    filteredData.value = allData.value.filter(item =>
        item.month.toLowerCase().includes(queryForm.value.query.toLowerCase())
    );
  }
  // 重新设置分页到第一页
  queryForm.value.pageNum = 1;
};

const initUserList = async () => {
  get('/api/test/form',(response)=>{
    allData.value = response;
    filterData(); // 初始化时先进行一次过滤，显示全部数据
  });

};

onMounted(() => {
  initUserList();
});
</script>


<style scoped>
.app-container{
  width:100%;
  overflow: hidden;
}


.header{
  padding-bottom: 16px;
  box-sizing: border-box;
}

.demo-pagination-block{
  float: right;
  padding: 20px;
  box-sizing: border-box;
}

.search_input{

  border: 1.5px solid cornflowerblue;
  border-radius: 5px;

  padding-left: 10px;
  box-sizing: border-box;
}


</style>