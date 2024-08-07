<template>
  <div class="chart-container">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'

// 註冊 Chart.js 的組件
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const chartData = {
  labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
  datasets: [
    {
      label: '年用電量',
      backgroundColor: '#42b983',
      data: [188, 220, 112, 139, 210, 140, 39]
    }
  ]
}

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false
}
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 40vh;
background-color: #ffffff;
}
</style>
