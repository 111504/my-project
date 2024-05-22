<template>
  <div class="app-container">


    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <!--        <el-input class="search_input" placeholder="请输入用户名..." v-model="queryForm.query" clearable ></el-input>-->
        <el-input
            class="search_input"
            placeholder="請輸入入用戶姓名..."
            v-model="queryForm.query"
            clearable
            aria-label="請輸入入用戶姓名..."
        ></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initUserList">搜索</el-button>
      <el-button type="success" :icon="DocumentAdd" @click="handleDialogValue()" v-if="store.HAS_AUTH('system:user:add')">新增</el-button>
      <el-popconfirm title="您確定要多重刪除嗎？" @confirm="handleDelete(null)">
        <template #reference>
          <el-button type="danger" :disabled="delBtnStatus" :icon="Delete" v-if="store.HAS_AUTH('system:user:delete')">批量删除</el-button>
        </template>
      </el-popconfirm>
    </el-row>


    <el-table :data="tableData"   stripe  style="width: 100%" >
      <el-table-column type="selection" width="55" />
      <el-table-column  fixed  prop="username" label="姓名" width="100" align="center"/>
      <el-table-column prop="department" label="部門" width="200" align="center"/>
      <el-table-column prop="level" label="職等" width="200" align="center"/>
      <el-table-column prop="boss" label="主管" width="120" align="center"/>
      <el-table-column prop="time" label="入職時間" width="200" align="center" />


    </el-table>
  </div>

  <div class="demo-pagination-block">
    <el-pagination
        v-model:current-page="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="[5,10,30,40]"
        layout="total, sizes, prev, pager, next jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
  <!--  <Dialog v-model="dialogVisible" :dialogVisible="dialogVisible" :id="id" :dialogTitle="dialogTitle" @initUserList="initUserList"/>-->
  <!--  <RoleDialog v-model="roleDialogVisible" :sysRoleList="sysRoleList" :roleDialogVisible="roleDialogVisible" :id="id" @initUserList="initUserList"></RoleDialog>-->
</template>

<script setup>
// import Dialog from "./component/Dialog.vue"
// import RoleDialog from "./component/RoleDialog.vue"

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


tableData = [
  {
    username: '林大張',
    department: '人事部門',
    level: '新進員工',
    boss:'張美蘭',
    time:'2016-05-03'
  },
  {
    username: '李小華',
    department: '銷售部門',
    level: '中級員工',
    boss: '王志強',
    time: '2017-07-15'
  },
  {
    username: '王小明',
    department: '研發部門',
    level: '資深員工',
    boss: '鄭麗華',
    time: '2018-08-20'
  },
  {
    username: '張三豐',
    department: '市場部門',
    level: '新進員工',
    boss: '王志強',
    time: '2019-09-10'
  },
  {
    username: '劉小龍',
    department: '人事部門',
    level: '中級員工',
    boss: '張美蘭',
    time: '2020-10-02'
  },
  {
    username: '陳麗君',
    department: '銷售部門',
    level: '資深員工',
    boss: '王志強',
    time: '2021-11-17'
  },
  {
    username: '黃美珠',
    department: '研發部門',
    level: '新進員工',
    boss: '鄭麗華',
    time: '2022-12-01'
  },
  {
    username: '馮海生',
    department: '市場部門',
    level: '中級員工',
    boss: '王志強',
    time: '2023-01-10'
  },
  {
    username: '楊子儀',
    department: '人事部門',
    level: '資深員工',
    boss: '張美蘭',
    time: '2024-02-14'
  },
  {
    username: '林珊珊',
    department: '銷售部門',
    level: '新進員工',
    boss: '王志強',
    time: '2025-03-20'
  },
  {
    username: '林大張',
    department: '人事部門',
    level: '新進員工',
    boss: '張美蘭',
    time: '2016-05-03'
  },
  {
    username: '李小華',
    department: '銷售部門',
    level: '中級員工',
    boss: '王志強',
    time: '2017-07-15'
  },
  {
    username: '王小明',
    department: '研發部門',
    level: '資深員工',
    boss: '鄭麗華',
    time: '2018-08-20'
  },
  {
    username: '張三豐',
    department: '市場部門',
    level: '新進員工',
    boss: '王志強',
    time: '2019-09-10'
  },
  {
    username: '劉小龍',
    department: '人事部門',
    level: '中級員工',
    boss: '張美蘭',
    time: '2020-10-02'
  },
  {
    username: '陳麗君',
    department: '銷售部門',
    level: '資深員工',
    boss: '王志強',
    time: '2021-11-17'
  },
  {
    username: '黃美珠',
    department: '研發部門',
    level: '新進員工',
    boss: '鄭麗華',
    time: '2022-12-01'
  },
  {
    username: '馮海生',
    department: '市場部門',
    level: '中級員工',
    boss: '王志強',
    time: '2023-01-10'
  },
  {
    username: '楊子儀',
    department: '人事部門',
    level: '資深員工',
    boss: '張美蘭',
    time: '2024-02-14'
  },
  {
    username: '林珊珊',
    department: '銷售部門',
    level: '新進員工',
    boss: '王志強',
    time: '2025-03-20'
  }
]




const  initUserList=async()=>{
  // post('api/user/list',queryForm.value,(response)=>{
  //   tableData.value=response.userList;
  //   total.value=response.total;
  //   console.log("initUserList",response.userList);
  //   // console.log("total.value",total.value);
  // })
}
//
initUserList()
//
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
// const handleSelectionChange = (selection)=>{
//   // 有选择项才有批量删除
//   if(selection.length>0) delBtnStatus.value = false;
//   else delBtnStatus.value = true
//   multipleSelection.value = selection
// }
//
//
// const handleRoleDialogValue = (userId,roleList)=>{
//   console.log("handleRoleDialogValue id="+userId)
//   id.value=userId;
//   sysRoleList.value=roleList;
//   roleDialogVisible.value=true
// }
//
// const handleDialogValue=(userId)=>{
//   if(userId){
//     console.log("用户修改 id="+userId)
//     id.value=userId;
//     dialogTitle.value="用户修改"
//   }else{
//
//     id.value=-1;
//     console.log("用户添加 id.value="+id.value)
//     dialogTitle.value="用户添加"
//   }
//   dialogVisible.value=true
// }
//
// const statusChangeHandle = async (row) => {
//   if (store.HAS_AUTH('system:user:edit')) {
//     get(`api/user/updateStatus/${row.id}/status/${row.status}`, (response) => {
//       ElMessage.success("成功更新狀態",response)
//     })
//
//   }
// }
//
// const handleResetPassword = async (id)=>{
//   await get(`api/user/resetPassword/${id}`,()=>{
//     ElMessage.success("成功重置密碼")
//   })
//
// }
//
// const handleDelete=async (id)=>{
//   let ids = []
//   //單選刪除
//   if(id){
//
//     ids.push(id)
//   }else{
//     //多選刪除
//     multipleSelection.value.forEach(row=>{
//       if(row.id !=1 )
//         ids.push(row.id)
//     })
//   }
//
//   await post("api/user/delete",ids,(response)=>{
//     ElMessage.success("刪除成功！")
//     initUserList();
//   })
//
//
// }

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