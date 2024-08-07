<template>
  <div class="chart-container">
    <Pie :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Pie } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement } from 'chart.js';

// 註冊 Chart.js 的組件
ChartJS.register(Title, Tooltip, Legend, ArcElement);

// 定義圖表的數據
const chartData = ref({
  labels: ['火力', '水力', '風力'],
  datasets: [
    {
      label: 'Dataset',
      backgroundColor: ['#f87979', '#7C4DFF', '#FFEB3B'],
      data: [80, 15, 5]
    }
  ]
});

// 定義圖表的選項
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false
});
</script>

<style scoped>
/* 添加任何你需要的樣式 */
.chart-container {
  position: relative;
  height: 40vh;
  background-color: #ffffff;
}
</style>
