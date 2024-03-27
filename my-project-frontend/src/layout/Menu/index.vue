<<template>
  <el-menu
      active-text-color="#ffd04b"
      background-color="#2d3a4b"
      class="el-menu-vertical-demo"
      default-active="1"
      text-color="#fff"
      @open="handleOpen"
      @close="handleClose"
      :default-active="activeIndex"
  >
    <el-menu-item index="/index">
      <el-icon><home-filled /></el-icon>
      <span>首页</span>
    </el-menu-item>
    <el-sub-menu v-for="(item,index) in menus" :index="item.path" :key="index" >
      <template #title>
        <el-icon><component :is="item.icon" ></component></el-icon>
        <span>{{item.name}}</span>
      </template>
      <el-menu-item
          v-for="(itemChild,index) in item.children"
          :index="itemChild.path"
          :key="index"
          @click="openTab(itemChild)"
      >
        <el-icon ><component :is="itemChild.icon" ></component></el-icon>
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

import {ref,reactive} from "vue";
const activeIndex = ref('/index')

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}

const menus = ref([
  {
    path: '1',
    icon: 'Setting',
    name: '系統管理',
    children: [
      {
        path: '1-1',
        icon: 'User',
        name: '用戶管理',
      },
      {
        path: '1-2',
        icon: 'UserFilled',
        name: '角色管理',
      },
      {
        path: '1-3',
        icon: 'Menu',
        name: '菜單管理',
      },
    ],
  },
  {
    path: '2',
    icon: 'OfficeBuilding',
    name: '業務管理',
    children: [
      {
        path: '2-1',
        icon: 'Guide',
        name: '部門管理',
      },
      {
        path: '2-2',
        icon: 'Avatar',
        name: '職位管理',
      },
    ],
  },
]);

function openTab(itemChild) {
  // 模擬點擊子菜單項時的行為
  console.log('Opening tab:', itemChild);
}

</script>


<style lang="scss">

</style>