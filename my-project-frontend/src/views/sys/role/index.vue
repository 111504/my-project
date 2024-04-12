<template>
  <div class="app-container">

    <!--搜尋框-->

    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input placeholder="請輸入角色名..." v-model="queryForm.query" clearable ></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initRoleList">搜索</el-button>
      <el-button type="success" :icon="DocumentAdd" @click="handleDialogValue()">新增</el-button>
      <el-popconfirm title="您確定要刪除這些紀錄" @confirm="handleDelete(null)">
        <template #reference>
          <el-button type="danger" :disabled="delBtnStatus" :icon="Delete" >批量刪除</el-button>
        </template>
      </el-popconfirm>
    </el-row>

    <el-table :data="tableData" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="角色名"  width="100" align="center"/>
      <el-table-column prop="code" label="權限名稱" width="200" align="center"/>
      <el-table-column prop="createTime" label="創建時間" width="200" align="center"/>
      <el-table-column prop="remark" label="備註"  />
      <el-table-column prop="action" label="操作" width="400" fixed="right" align="center">
        <template v-slot="scope" >
          <el-button  type="primary" :icon="Tools" @click="handleMenuDialogValue(scope.row.id)">分配權限</el-button>
          <el-button v-if="scope.row.code!='admin'" type="primary" :icon="Edit" @click="handleDialogValue(scope.row.id)" />
          <el-popconfirm  v-if="scope.row.code!='admin'" title="您確定要刪除這項紀錄？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button  type="danger" :icon="Delete" />
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:currentPage="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
  <Dialog v-model="dialogVisible" :dialogVisible="dialogVisible" :id="id" :dialogTitle="dialogTitle" @initRoleList="initRoleList"/>
  <MenuDialog v-model="menuDialogVisible"  :menuDialogVisible="menuDialogVisible" :id="id" @initRoleList="initRoleList"></MenuDialog>
</template>

<script setup>
import {ref} from 'vue';
import {post,get} from '@/net'
import { Search ,Delete,DocumentAdd ,Edit, Tools, RefreshRight} from '@element-plus/icons-vue'
import Dialog from './component/Dialog.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import MenuDialog from './component/menuDialog.vue'

const tableData=ref([])

const total=ref(0)

const queryForm=ref({
  query:'',
  pageNum:1,
  pageSize:10
})

const dialogVisible=ref(false)

const dialogTitle=ref("")

const id=ref(-1)

const delBtnStatus=ref(true)

const multipleSelection=ref([])

const sysRoleList=ref([])

const menuDialogVisible=ref(false)

const handleSelectionChange=(selection)=>{
  console.log("勾選了")
  console.log(selection)
  multipleSelection.value=selection;
  delBtnStatus.value=selection.length===0;
}

const handleMenuDialogValue=(roleId)=>{
  console.log("roleId="+roleId)
  id.value=roleId;
  menuDialogVisible.value=true
}

const initRoleList=async()=>{
  await post("sys/role/list",queryForm.value,(res)=>{
    console.log("res.roleList",Array.isArray(res.roleList))
    tableData.value=res.roleList;
    total.value=res.total;
  });
}

initRoleList();

const handleSizeChange=(pageSize)=>{
  queryForm.value.pageNum=1;
  queryForm.value.pageSize=pageSize;
  initRoleList();
}

const handleCurrentChange=(pageNum)=>{
  queryForm.value.pageNum=pageNum;
  initRoleList();
}

const handleDialogValue=(roleId)=>{
  if(roleId){
    id.value=roleId;
    dialogTitle.value="角色修改"
  }else{
    id.value=-1;
    dialogTitle.value="角色添加"
  }
  dialogVisible.value=true
}

const handleDelete=async (id)=>{
  var ids = []
  if(id){
    ids.push(id)
  }else{
    multipleSelection.value.forEach(row=>{
      ids.push(row.id)
    })
  }
 await post("sys/role/delete",ids,()=>{
   ElMessage({
     type: 'success',
     message: '执行成功!'
   })
   initRoleList();
 })
}

const handleResetPassword=async (id)=>{
  await get("sys/role/resetPassword/"+id,(res)=>{
    ElMessage({
      type: 'success',
      message: '执行成功!'
    })
    initRoleList();
  })
}

const statusChangeHandle=async (row)=>{
  await get("sys/role/updateStatus/"+row.id+"/status/"+row.status,()=>{
    ElMessage({
      type: 'success',
      message: '执行成功!'
    })

  },(res)=>{
    ElMessage({
      type: 'error',
      message: res.data.message,
    })
    initRoleList();
  });

}

</script>

<style lang="scss" scoped>

.header{
  padding-bottom: 16px;
  box-sizing: border-box;
}
.app-container{
  width:100%;
  overflow: hidden;
}
.el-pagination{
  float: right;
  padding: 20px;
  box-sizing: border-box;
}

::v-deep(.th.el-table__cell){
  word-break: break-word;
  background-color: #f8f8f9 !important;
  color: #515a6e;
  height: 50px;
  font-size: 13px;

}

.el-tag--small {
  margin-left: 5px;
}
</style>
