<template>
  <div class="chart-container">
    <canvas   class="cover-fit" width="100%" height="100%" ref="chartElement"></canvas>
  </div>
</template>

<script setup>
import { ref, shallowRef, nextTick, onMounted, watch } from 'vue';
import { Pie } from 'vue-chartjs';
import Chart from 'chart.js/auto';
import axios from 'axios';
import {get} from "@/net/index.js";

// 設置圖表數據
const chartData1 = ref([12, 19, 3, 5, 2, 3]);

// 取得要放圖表的 canvas 元素
const chartElement = ref(null);

// 儲存生成的圖表
const dataChart1 = shallowRef(null);
// 定義圖表的數據
const initChart = (ws)=> {
  return new Chart(chartElement.value.getContext('2d'), {
    type: 'pie',
    data: {
      labels: ['火力', '水力', '風力', '核能', '太陽能', '煤氣'],
      datasets: [
        {
          label: '能源消耗',
          backgroundColor: ['#ff6384', '#36a2eb', '#ffcd56', '#4bc0c0', '#9966ff', '#ff9f40'],
          data: ws.value,
          borderColor: '#fff',
          borderWidth: 1,
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  });
}



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
    get('api/test/pieChat', (response) => {
      chartData1.value = response;
      console.log("filteredData", response);
    })
  }


initUserList()



</script>

<style scoped>
/* 添加任何你需要的樣式 */
.chart-container {
  position: relative;
  height: 40vh;
  background-color: #ffffff;
}
</style>
