<template>
  <div class="chart-container">
    <canvas  class="cover-fit" width="100%" ref="chartElement" />
  </div>
</template>

<script setup>
import { ref, shallowRef, nextTick, onMounted, watch } from 'vue';
import { Chart , Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale } from 'chart.js';
import {get} from "@/net/index.js";


const chartData1=ref();


// 儲存生成的圖表
const dataChart1 = shallowRef(null);

// 註冊 Chart.js 的組件
Chart.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale);
// 定義 chartElement 的引用
const chartElement = ref(null);
// 定義圖表的數據
const initChart = (ws) => {
  return new Chart(chartElement.value.getContext('2d'), {
    type: 'line',
    data: {
      labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11','12', '13','14','15', '16', '17', '18','19', '20', '21', '22', '23'],
      datasets: [
        {
          label: '今日用電',
          backgroundColor: '#f87979',
          borderColor: '#f87979',
          data: ws.value,
          fill: false
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: {
          display: true,
          title: {
            display: true,
            text: 'Hour'  // 这里是时间范围，应该使用 'Hour' 而不是 'Month'
          }
        },
        y: {
          display: true,
          title: {
            display: true,
            text: 'Value'
          }
        }
      }
    }
  });
}
// 在组件挂载时初始化图表
onMounted(() => {
  nextTick(() => {
    dataChart1.value = initChart(chartData1);
  });
});

watch(chartData1, (newData) => {

  if (dataChart1.value) {
    dataChart1.value.data.datasets[0].data = newData;
    dataChart1.value.update();
  }
}, {deep: true});

const initUserList = async () => {
  get('api/test/lineChat', (response) => {
    chartData1.value = response;
    console.log("filteredData", response);
  })
}

initUserList()

</script>

<style scoped>
.chart-container {
  position: relative;
  height: 40vh;
  background-color: #ffffff;
}
</style>
