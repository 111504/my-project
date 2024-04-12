<template>
  <div class="app-container">

    <el-row  class="header">

      <el-button type="success" :icon="DocumentAdd" @click="handleDialogValue()">新增</el-button>

    </el-row>

    <el-table
        :data="tableData"
        row-key="id"
        stripe
        style="width: 100%; margin-bottom: 20px"
        border
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >

      <el-table-column prop="name" label="菜單名稱"  width="200"/>
      <el-table-column prop="icon" label="圖標"  width="70" align="center">
        <template v-slot="scope">
          <el-icon><svg-icon :name="scope.row.icon" /></el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序"  width="70" align="center"/>
      <el-table-column prop="perms" label="權限標示"  width="200" />
      <el-table-column prop="path" label="組件路徑"  width="180" />
      <el-table-column prop="menuType" label="選單類型"  width="120" align="center">
        <template v-slot="scope">
          <el-tag size="small" v-if="scope.row.menuType === 'M'" type="danger" effect="dark">目錄</el-tag>
          <el-tag size="small" v-else-if="scope.row.menuType === 'C'" type="success" effect="dark">菜單</el-tag>
          <el-tag size="small" v-else-if="scope.row.menuType === 'F'" type="warning" effect="dark">按鈕</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="創建時間" align="center"/>
      <el-table-column prop="action" label="操作" width="400" fixed="right" align="center">
        <template v-slot="scope" >
          <el-button  type="primary" :icon="Edit" @click="handleDialogValue(scope.row.id)" />
          <el-popconfirm  title="您確定要刪除這條紀錄嗎?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button  type="danger" :icon="Delete" />
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

  </div>
  <Dialog v-model="dialogVisible" :tableData="tableData" :dialogVisible="dialogVisible" :id="id" :dialogTitle="dialogTitle" @initMenuList="initMenuList"/>

</template>

<script setup>
import {ref} from 'vue';
import {get ,post} from "@/net"
import { Search ,Delete,DocumentAdd ,Edit, Tools, RefreshRight} from '@element-plus/icons-vue'
import Dialog from './component/dialog.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import SvgIcon from "@/components/SvgIcon.vue";


const tableData=ref([])



const dialogVisible=ref(false)

const dialogTitle=ref("")

const id=ref(-1)


const initMenuList=async()=>{
 await get("sys/menu/treeList",(res)=>{
   console.log("res.treeMenu",res.treeMenu)
   tableData.value=res.treeMenu;
 });


}

initMenuList();

const handleDialogValue=(menuId)=>{
  if(menuId){
    id.value=menuId;
    dialogTitle.value="菜單修改"
  }else{
    id.value=-1;
    dialogTitle.value="菜單添加"
  }
  dialogVisible.value=true
}

const handleDelete=async (id)=>{
  await get("sys/menu/delete/"+id,()=>{
    ElMessage({
      type: 'success',
      message: '执行成功!'
    })
    initMenuList();
  })
}

</script>

<style lang="scss" scoped>

.header{
  padding-bottom: 16px;
  box-sizing: border-box;
}

.el-pagination{
  float: right;
  padding: 20px;
  box-sizing: border-box;
}
/*
 :deep(th.el-table__cell){
  word-break: break-word;
  background-color: #f8f8f9 !important;
  color: #515a6e;
  height: 40px;
  font-size: 13px;

}*/
:deep(.el-table .el-button) {
  /* 你的样式修改 */
  /*background-color: #515a6e; /* 示例 */
  height: 30px;
  width: 50px;
}

:deep(.el-table .el-tag) {
  /* 你的样式修改 */
  /*background-color: #515a6e; /* 示例 */
  height: 30px;
  width: 50px;
  margin-left: 5px;
}


</style>
