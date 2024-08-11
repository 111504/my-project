<template>
  <div class="app-container">
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input
            class="search_input"
            placeholder="請輸入入用戶姓名..."
            v-model="queryForm.query"
            clearable
            aria-label="請輸入入用戶姓名..."
        ></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initUserList">搜索</el-button>
      <el-button type="success" :icon="DocumentAdd" @click="handleDialogValue(-1)" v-if="store.HAS_AUTH('system:user:add')">新增</el-button>
      <el-popconfirm title="您確定要多重刪除嗎？" @confirm="handleDelete(null)">
        <template #reference>
          <el-button type="danger" :disabled="delBtnStatus" :icon="Delete" v-if="store.HAS_AUTH('system:user:delete')">批量删除</el-button>
        </template>
      </el-popconfirm>
    </el-row>

<!--多選框選擇事件-->
<!--@selection-change="handleSelectionChange"-->
    <el-table :data="tableData"  stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
<!--      <el-table-column prop="avatar" label="头像" width="80" align="center">-->
<!--        <template v-slot="scope">-->
<!--          &lt;!&ndash;          <img :src="getServerUrl()+'image/userAvatar/'+scope.row.avatar" width="50" height="50"/>&ndash;&gt;-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column prop="username" label="用戶名稱" width="100" align="center"/>

      <el-table-column prop="roles" label="拥有角色" width="200" align="center">
        <template v-slot="scope">
          <el-tag size="small" type="warning" v-for="(item,index) in scope.row.sysRoleList" :key="index">   {{item.name}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="信箱" width="200" align="center"/>
      <el-table-column prop="phonenumber" label="手機號碼" width="120" align="center"/>
      <el-table-column prop="status" label="啟用" width="200" align="center" >
        <template v-slot="{row}" >
          <el-switch  v-model="row.status" @change="statusChangeHandle(row)" active-text="正常"
                      inactive-text="禁用" active-value="0" inactive-value="1" ></el-switch>
        </template>
      </el-table-column>
<!--      <el-table-column prop="createTime" label="创建时间" width="200" align="center"/>-->
      <el-table-column prop="loginDate" label="最后登录时间" width="200" align="center"/>
<!--      <el-table-column prop="remark" label="备注"  />-->
      <el-table-column prop="action" label="操作" width="400" fixed="right" align="center">
        <template v-slot="scope"  >
          <el-button  type="primary" :icon="Tools"  @click="handleRoleDialogValue(scope.row.id,scope.row.sysRoleList)" v-if="store.HAS_AUTH('system:user:role')">分配</el-button>
          <el-button v-if="scope.row.username!=='admin' && store.HAS_AUTH('system:user:edit')" type="primary" :icon="Edit" @click="handleDialogValue(scope.row.id)" >修改</el-button>

          <el-popconfirm v-if="scope.row.username!=='admin'" title="您確定要重製這用戶密碼嗎?" @confirm="handleResetPassword(scope.row.id)">
            <template #reference>
              <el-button  type="warning" :icon="RefreshRight" v-if="store.HAS_AUTH('system:user:resetPwd')">重置密码</el-button>
            </template>
          </el-popconfirm>

          <el-popconfirm  v-if="scope.row.username!='admin'" title="您確定要刪除這用戶?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button  type="danger" :icon="Delete" v-if="store.HAS_AUTH('system:user:delete')"/>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="demo-pagination-block">
    <el-pagination
        v-model:current-page="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="[10,20,30,40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
  <Dialog v-model="dialogVisible" :dialogVisible="dialogVisible" :id="id" :dialogTitle="dialogTitle" @initUserList="initUserList"/>
  <RoleDialog v-model="roleDialogVisible" :sysRoleList="sysRoleList" :roleDialogVisible="roleDialogVisible" :id="id" @initUserList="initUserList"></RoleDialog>
</template>

<script setup>
import Dialog from "./component/Dialog.vue"
import RoleDialog from "./component/RoleDialog.vue"

import {ref} from "vue"
import {Delete, DocumentAdd, Edit, RefreshRight, Search, Tools} from "@element-plus/icons-vue";
import {ElMessage,ElNotification} from 'element-plus'
import {useMenuStore} from "@/store/Store.js";
import {get, post} from "@/net";

const store = useMenuStore()
const delBtnStatus = ref(true) // 删除标识符，true表示不能删除
const multipleSelection=ref([])  // 批量删除id
let tableData = ref([])
const sysRoleList = ref([])
const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 5
})
const total = ref(0)
const dialogVisible=ref(false)
const roleDialogVisible=ref(false)
const dialogTitle=ref('')
const id=ref(-1)
const  initUserList=async()=>{
  post('api/user/list',queryForm.value,(response)=>{
    tableData.value=response.userList;
    total.value=response.total;
     console.log("initUserList",response.userList);

  })
}

initUserList()

const handleSizeChange=(pageSize)=>{
  console.log("pageSize",pageSize);
  queryForm.value.pageNum = 1
  queryForm.value.pageSize=pageSize
  initUserList()
}

const handleCurrentChange = (pageNum)=>{
  queryForm.value.pageNum = pageNum
  initUserList()
}
const handleSelectionChange = (selection)=>{
  // 有选择项才有批量删除
  if(selection.length>0) delBtnStatus.value = false;
  else delBtnStatus.value = true
  multipleSelection.value = selection
}


const handleRoleDialogValue = (userId,roleList)=>{
  console.log("handleRoleDialogValue id="+userId)
  id.value=userId;
  sysRoleList.value=roleList;
  roleDialogVisible.value=true
}

const handleDialogValue=(userId)=>{
  id.value=userId;
  if(userId!==-1){
    dialogTitle.value="用户修改"
  }else{
    dialogTitle.value="用户添加"
  }
  dialogVisible.value=true
}

const statusChangeHandle = async (row) => {
  if (store.HAS_AUTH('system:user:edit')) {
    get(`api/user/updateStatus/${row.id}/status/${row.status}`, (response) => {
      ElMessage.success("成功更新狀態",response)
    })

  }
}

const handleResetPassword = async (id)=>{
  await get(`api/user/resetPassword/${id}`,()=>{
    ElMessage.success("成功重置密碼")
  })

}

const handleDelete=async (id)=>{
  let ids = []
  //單選刪除
  if(id){

    ids.push(id)
  }else{
    //多選刪除
    multipleSelection.value.forEach(row=>{
      if(row.id !=1 )
        ids.push(row.id)
    })
  }

  await post("api/user/delete",ids,(response)=>{
    ElMessage.success("刪除成功！")
    initUserList();
  })


}

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