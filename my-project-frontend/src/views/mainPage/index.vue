<template>
  <div class="dashboard-container">
<!--    &lt;!&ndash; github角标 &ndash;&gt;-->
<!--    <github-corner class="github-corner" />-->

    <el-card shadow="never" class="border border-2  rounded ">
      <el-row justify="space-between">
        <el-col :span="12" :xs="24">
          <div class="left-bar">
<!--            <img-->
<!--                class="w-20 h-20 mr-5 rounded-full"-->
<!--                :src="userStore.user.avatar + '?imageView2/1/w/80/h/80'"-->
<!--            />-->
            <div>
              <p>{{ greetings }}</p>
              <p class="left-bar-weather">
                天氣晴氣溫介於5~15度
              </p>
            </div>
          </div>
        </el-col>

        <el-col :span="12" :xs="24">
          <div class="right-bar">
            <el-statistic :value="99">
              <template #title>
                <div class="flex items-center">
                  <svg-icon  class="icon-style"  name="message2"/>&nbsp;&nbsp;
                  <span class="fs-6 fw-medium ">消息</span>
                </div>
              </template>
            </el-statistic>

            <el-statistic :value="50" class="el-statistic-custom">
              <template #title>
                <div class="flex items-center">
                  <svg-icon class="icon-style"  name="todolist2"/>
                  <span class="fs-6 fw-medium  ">代辦事項</span>
                </div>
              </template>
              <template #suffix>/100</template>
            </el-statistic>

            <el-statistic :value="10">
              <template #title>
                <div class="flex items-center">
                  <svg-icon class="icon-style"  name="project2" />
                  <span class="fs-6 fw-medium  ">項目</span>
                </div>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据卡片 -->
    <el-row :gutter="10" class="mt-3">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never">
          <template #header>
            <div class="d-flex items-center justify-content-between">
              <span class="text-secondary">訪客數</span>
              <el-tag type="success" class="fs-6">日</el-tag>
            </div>
          </template>

          <div class="d-flex items-center justify-content-between  mt-5">
            <div class="lh-base fs-5 text-start">
             {{visitCountAmount().total}}
            </div>
            <svg-icon class="icon-style"  name="visit" />
          </div>

          <div
              class="d-flex items-center justify-content-between mt-5 text-sm text-secondary"
          >
            <span> 總訪客數 </span>
            <span> {{visitCountAmount().total}} </span>
          </div>
        </el-card>
      </el-col>

      <!--消息数-->
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never">
          <template #header>
            <div class="d-flex items-center justify-content-between">
              <span class="text-secondary">IP數</span>
              <el-tag type="success" class="fs-6">日</el-tag>
            </div>
          </template>

          <div class="d-flex items-center justify-content-between mt-5">
            <div class="lh-base fs-5 text-start">
             {{dauCountAmount().current}}
            </div>
            <svg-icon class="icon-style"  name="ip" />
          </div>

          <div
              class="d-flex items-center justify-content-between mt-5 text-sm text-secondary"
          >
            <span> 總ip數 </span>
            <span>  {{dauCountAmount().total}} </span>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never">
          <template #header>
            <div class="d-flex items-center justify-content-between">
              <span class="text-secondary">銷售額</span>
              <el-tag class="fs-6">月</el-tag>
            </div>
          </template>

          <div class="d-flex items-center justify-content-between mt-5">
            <div class="lh-base fs-5 text-start">
              {{ roundedAmount().current }}
            </div>
            <svg-icon class="icon-style"  name="money" />
          </div>

          <div
              class="d-flex items-center justify-content-between mt-5 text-sm text-secondary"
          >
            <span> 總銷售額 </span>
            <span> {{roundedAmount().total}} </span>
          </div>
        </el-card>
      </el-col>


      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never">
          <template #header>
            <div class="d-flex items-center justify-content-between">
              <span class="text-secondary">訂單量</span>
              <el-tag type="danger" class="fs-6">季</el-tag>
            </div>
          </template>

          <div class="d-flex items-center justify-content-between mt-5">
            <div class="lh-base fs-5 text-start">
             {{orderCountAmount().current}}
            </div>
            <svg-icon class="icon-style"  name="order" />
          </div>

          <div
              class="d-flex items-center justify-content-between mt-5 text-sm text-secondary"
          >
            <span> 總訂單量 </span>
            <span>  {{orderCountAmount().total}}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

<!--     Echarts 图表 -->
<!--    <el-row :gutter="10" class="mt-3">-->
<!--      <el-col :sm="24" :lg="8" class="mb-2">-->
<!--        <BarChart-->
<!--            id="barChart"-->
<!--            height="400px"-->
<!--            width="100%"-->
<!--            class="bg-[var(&#45;&#45;el-bg-color-overlay)]"-->
<!--        />-->
<!--      </el-col>-->

<!--      <el-col :xs="24" :sm="12" :lg="8" class="mb-2">-->
<!--        <PieChart-->
<!--            id="pieChart"-->
<!--            height="400px"-->
<!--            width="100%"-->
<!--            class="bg-[var(&#45;&#45;el-bg-color-overlay)]"-->
<!--        />-->
<!--      </el-col>-->

<!--      <el-col :xs="24" :sm="12" :lg="8" class="mb-2">-->
<!--        <RadarChart-->
<!--            id="radarChart"-->
<!--            height="400px"-->
<!--            width="100%"-->
<!--            class="bg-[var(&#45;&#45;el-bg-color-overlay)]"-->
<!--        />-->
<!--      </el-col>-->
<!--    </el-row>-->
  </div>
</template>

<script setup lang="ts">
import SvgIcon from "@/components/SvgIcon.vue";
import {TransitionPresets, useTransition} from "@vueuse/core";
import {computed, ref,reactive} from "vue";

import {useMenuStore} from "@/store/Store.js";

defineOptions({
  name: "Dashboard",
  inheritAttrs: false,
});

// const userStore = useUserStore();
const store = useMenuStore()

const username  = store.GET_USER().username;
const date: Date = new Date();

//根據時間判斷回話
const greetings = computed(() => {
  const hours = date.getHours();
  if (hours >= 6 && hours < 8) {
    return "早晨天氣涼喔~";
  } else if (hours >= 8 && hours < 12) {
    return "上午好，" + username + "！";
  } else if (hours >= 12 && hours < 18) {
    return "下午好，" + username + "！";
  } else if (hours >= 18 && hours < 24) {
    return "晚上好，" + username + "！";
  } else if (hours >= 0 && hours < 6) {
    return "熬夜有害身體健康，休息一下🌛！";
  }
});





const duration = 1000;
//這四個維數字的動畫效果 1000為 1秒


// 访客数
const visitCount = ref(0);
const visitCountTotal=ref(0);

const visitCountOutput = useTransition(visitCount, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
const visitCountTotalOutput = useTransition(visitCountTotal, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
})

visitCount.value = 2000;
visitCountTotal.value =13102


// IP数
const dauCount = ref(0);
const dauCountTotal = ref(0);
const dauCountOutput = useTransition(dauCount, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
const dauCountTotalOutput = useTransition(dauCountTotal, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
dauCount.value = 2036;
dauCountTotal.value = 11001;

// 销售额
const amount = ref(0);
const amountTotal=ref(0);
const amountOutput = useTransition(amount, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
const amountTotalOutput = useTransition(amount, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
amount.value = 1800;
amountTotal.value = 2500;


// 訂單量
const orderCount = ref(0);
const orderCountTotal=ref(0);
const orderCountOutput = useTransition(orderCount, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
const orderCountTotalOutput = useTransition(orderCountTotal, {
  duration: duration,
  transition: TransitionPresets.easeOutExpo,
});
orderCount.value = 9000;
orderCountTotal.value=75000






//訪客動畫
function  visitCountAmount(){

  return {
    current:Math.round(this.visitCountOutput),
    total:Math.round(this.visitCountOutput)
  };
}
//ip數
function  dauCountAmount(){
  return{
    current:Math.round(this.dauCountOutput),
    total:Math.round(this.dauCountTotalOutput)
  }
}

//銷售額
function  roundedAmount(){
  return {
    current:Math.round(this.amountOutput),
    total:Math.round(this.amountTotalOutput)
  }
}

//訂單量
function  orderCountAmount(){
  return {
    current:Math.round(this.orderCountOutput),
    total:Math.round(this.orderCountTotalOutput)
  };
}











</script>

<style lang="scss" scoped>
.dashboard-container {
  position: relative;
  padding: 24px;

  .user-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }

  .github-corner {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 1;
    border: 0;
  }

  .data-box {
    display: flex;
    justify-content: space-between;
    padding: 20px;
    font-weight: bold;
    color: var(--el-text-color-regular);
    background: var(--el-bg-color-overlay);
    border-color: var(--el-border-color);
    box-shadow: var(--el-box-shadow-dark);
  }

  .svg-icon {
    fill: currentcolor !important;
  }
  .left-bar{
    display: flex;
    height: 100%;
    align-items: center;
  }
  .left-bar-weather{
    font-size: 0.875rem; /* 14.0px */
    line-height: 1.25rem; /* 20.0px */
    color: rgba(156,163,175,1)
  }

  .right-bar{
    display: flex;
    height: 100%;
    align-items: center;
    justify-content: space-around;
  }


}

.icon-style{
  height: 2rem;
  width: 2rem;
  margin-right: 10px;
}

::v-deep(.el-statistic__number){
  font-size: 20px; /* 設定為你希望的字體大小 */
  font-weight: bold;
}


</style>
