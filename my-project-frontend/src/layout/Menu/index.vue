<<template>
<!--  左邊側邊欄-->
  <el-menu
      active-text-color="#ffd04b"
      background-color="#2d3a4b"
      class="el-menu-vertical-demo"
      default-active="1"
      text-color="#fff"
      @open="handleOpen"
      @close="handleClose"
      :default-active="activeIndex"
      :router="true"
  >
    <el-menu-item index="/index">
      <el-icon><home-filled /></el-icon>
      <span>首页</span>
    </el-menu-item>
    <el-sub-menu v-for="(item,index) in menuList" :index="item.path"  :key="index" >
      <template #title>

        <el-icon><svg-icon :name="item.icon" /></el-icon>
        <span>{{item.name}}</span>
      </template>
      <el-menu-item
          v-for="(itemChild,index) in item.children"
          :index="itemChild.path"
          :key="index"
          @click="openTab(itemChild)"
      >
        <el-icon><svg-icon :name="itemChild.icon" /></el-icon>
        <span>{{itemChild.name}}</span>
      </el-menu-item>
    </el-sub-menu>
  </el-menu>

</template>
<script setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting, HomeFilled, Menu,User,UserFilled,Avatar,OfficeBuilding
,Guide} from '@element-plus/icons-vue'
import {useMenuStore} from "@/store/Store.js";
import {ref,reactive} from "vue";
import SvgIcon from "@/components/SvgIcon.vue";
const activeIndex = ref('/index')
const store = useMenuStore()
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
//  console.log(key, keyPath)
}


const menuList=store.GET_MENUS();

console.log("拿到菜單",JSON.stringify(menuList) )

function openTab(itemChild) {
  // 模擬點擊子菜單項時的行為
  console.log('Opening tab:', itemChild.path);
}

</script>


<style lang="scss">

</style>