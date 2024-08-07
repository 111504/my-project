<template>
  <div class="chart-container">
    <Line :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale } from 'chart.js';

// 註冊 Chart.js 的組件
ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale);

// 定義圖表的數據
const chartData = ref({
  labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11','12', '13','14','15', '16', '17', '18','19', '20', '21', '22', '23'],
  datasets: [
    {
      label: '今日用電',
      backgroundColor: '#f87979',
      borderColor: '#f87979',
      data: [12, 39, 10, 40, 39, 80, 40, 41,12, 39, 10, 40, 39, 77, 40,12, 39, 10, 23, 11, 32, 2],
      fill: false
    }
  ]
});

// 定義圖表的選項
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    x: {
      display: true,
      title: {
        display: true,
        text: 'Month'
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
});
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 40vh;
  background-color: #ffffff;
}
</style>
