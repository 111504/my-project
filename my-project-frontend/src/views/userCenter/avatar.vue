<template>


  <el-form
      ref="formRef"
      :model="form"
      label-width="100px"
      style="text-align: center;padding-bottom:10px"
  >
    <el-upload
        :headers="accessHeader()"
        class="avatar-uploader"
        :action="getServerUrl()+'api/user/uploadImage'"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>

    <el-button @click="handleConfirm" >確認更換</el-button>

  </el-form>


</template>

<script setup>

import {defineProps, ref} from "vue";
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {useMenuStore} from "@/store/Store.js";
import {getServerUrl} from "@/net/request.js";
import {post,accessHeader} from "@/net"

const store = useMenuStore()
//
//
//
const props=defineProps(
    {
      user:{
        type:Object,
        default:()=>{},
        required:true
      }
    }
)

const headers=ref({
  token:store.GET_TOKEN()
})

const form=ref({
  id:-1,
  avatar:''
})

const formRef=ref(null)

const imageUrl=ref("")
const avatar=store.GET_AVATAR();
form.value.id=props.user.id;

// form.value.id=Number(store.GET_USER_ID());
// form.value.avatar=String(store.GET_AVATAR);
//localhost+api+使用者名稱
imageUrl.value=getServerUrl()+`api/picture/fileSystem/${avatar}`
//imageUrl.value=http://localhost:8080/api/picture/fileSystem/202405011250100148.jpg
console.log("form.value=",form.value)


//提交成功後執行動作
const handleAvatarSuccess=(res)=>{
  imageUrl.value=getServerUrl()+res.data.src
  form.value.avatar=res.data.title;
}


const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isLt2M = file.size / 1024 / 1024 < 4

  if (!isJPG) {
    ElMessage.error('圖片必須是jpg')
  }
  if (!isLt2M) {
    ElMessage.error('圖片不能超過4M!')
  }
  return isJPG && isLt2M
}

const handleConfirm=async()=>{
  await post("api/user/updateAvatar",form.value,()=>{
    store.SET_AVATAR(store.$state,form.value.avatar);
    localStorage.setItem("avatar",form.value.avatar);
    imageUrl.value=getServerUrl()+`api/picture/fileSystem/${store.GET_AVATAR()}`
      ElMessage.success("更改照片成功！")
  });
}

</script>

<style>

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}



</style>
