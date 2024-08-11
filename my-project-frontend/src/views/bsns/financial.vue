<template>
  <div>
    <h1>用電量報表</h1>
    <table class="report-table" ref="reportTable">
      <thead>
      <tr>
        <th>月份</th>
        <th>尖峰電量</th>
        <th>半尖峰電量</th>
        <th>離峰電量</th>
        <th>總用電量</th>
        <th>描述/說明</th>
        <th>碳排放量</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item, index) in reportData" :key="index">
        <td>{{ item.month }}</td>
        <td>{{ item.peak }}</td>
        <td>{{ item.midPeak }}</td>
        <td>{{ item.offPeak }}</td>
        <td>{{ item.total }}</td>
        <td>{{ item.description }}</td>
        <td>{{ item.carbonEmission }}</td>
      </tr>
      </tbody>
    </table>
    <div class="chart-container" ref="chartContainer">
      <canvas ref="chartElement"></canvas>
    </div>
    <div ref="buttonsContainer" class="buttons-container">
      <button @click="switchData" style="margin-bottom: 20px">切換數據</button>
      <button @click="exportPDF" style="margin-bottom: 20px">匯出 PDF</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import Chart from 'chart.js/auto';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

// 假數據
const fakeData = [
  { month: 'January', peak: 0, midPeak: 256124, offPeak: 0, total: 256124, description: '電費記錄無無註尖峰或離峰', carbonEmission: 128574.248 },
  { month: 'February', peak: 0, midPeak: 220391, offPeak: 0, total: 220391, description: '電費記錄無無註尖峰或離峰', carbonEmission: 110636.282 },
  { month: 'March', peak: 0, midPeak: 278819, offPeak: 0, total: 278819, description: '電費記錄無無註尖峰或離峰', carbonEmission: 139967.138 },
  { month: 'April', peak: 0, midPeak: 271306, offPeak: 0, total: 271306, description: '電費記錄無無註尖峰或離峰', carbonEmission: 136195.612 },
  { month: 'May', peak: 0, midPeak: 326821, offPeak: 0, total: 326821, description: '電費記錄無無註尖峰或離峰', carbonEmission: 164064.142 },
  { month: 'June', peak: 313664, midPeak: 0, offPeak: 0, total: 313664, description: '電費記錄無無註尖峰或離峰', carbonEmission: 157459.842 },
  { month: 'July', peak: 327087, midPeak: 0, offPeak: 0, total: 327087, description: '電費記錄無無註尖峰或離峰', carbonEmission: 163543.052 },
  { month: 'August', peak: 348489, midPeak: 0, offPeak: 0, total: 348489, description: '電費記錄無無註尖峰或離峰', carbonEmission: 174941.424 },
  { month: 'September', peak: 296776, midPeak: 0, offPeak: 0, total: 296776, description: '電費記錄無無註尖峰或離峰', carbonEmission: 148581.552 },
  // 添加更多假數據...
];

const reportData = ref(fakeData);
const chartElement = ref(null);
const dataChart = ref(null);
const reportTable = ref(null);
const chartContainer = ref(null);
const buttonsContainer = ref(null);

const updateChart = () => {
  const labels = reportData.value.map(item => item.month);
  const data = reportData.value.map(item => item.total);

  if (dataChart.value) {
    dataChart.value.destroy();
  }

  dataChart.value = new Chart(chartElement.value.getContext('2d'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: '總用電量',
        data: data,
        backgroundColor: '#42b983',
        borderColor: '#42b983',
        borderWidth: 1,
        fill: true,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  });
};

const switchData = () => {
  reportData.value = reportData.value.map(item => ({
    ...item,
    total: Math.floor(Math.random() * 400000)
  }));
  updateChart();
};

const exportPDF = async () => {
  // 確保 buttonsContainer 已經設置
  if (!buttonsContainer.value) return;

  // 隱藏按鈕
  buttonsContainer.value.style.display = 'none';

  // 生成 PDF
  const pdf = new jsPDF('p', 'mm', 'a4');

  // 將表格轉換為圖像
  const tableCanvas = await html2canvas(reportTable.value, {
    backgroundColor: null,
    useCORS: true,
    scale: 2,
    willReadFrequently: true
  });
  const tableImgData = tableCanvas.toDataURL('image/png');

  // 將圖像添加到 PDF
  const pdfWidth = pdf.internal.pageSize.getWidth();
  const pdfHeight = (tableCanvas.height * pdfWidth) / tableCanvas.width;
  pdf.addImage(tableImgData, 'PNG', 0, 0, pdfWidth, pdfHeight);

  // 將圖表轉換為圖像
  const chartCanvas = await html2canvas(chartContainer.value, {
    backgroundColor: null,
    useCORS: true,
    scale: 2,
    willReadFrequently: true
  });
  const chartImgData = chartCanvas.toDataURL('image/png');

  // 計算圖表添加位置
  const chartPdfHeight = (chartCanvas.height * pdfWidth) / chartCanvas.width;
  pdf.addPage();
  pdf.addImage(chartImgData, 'PNG', 0, 0, pdfWidth, chartPdfHeight);

  // 保存 PDF
  pdf.save('report.pdf');

  // 恢復按鈕顯示
  buttonsContainer.value.style.display = 'block';
};

onMounted(() => {
  nextTick(() => {
    updateChart();
  });
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'montserrat', sans-serif;
}

body {
  background-color: #fff; /* 設置頁面的背景顏色為白色 */
}

.chart-container {
  display: flex;
  align-items: center;
  width: 100%;
  height: 400px;
  background-color: #fff; /* 設置圖表容器的背景顏色為白色 */
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  background-color: #fff; /* 設置表格的背景顏色為白色 */
  margin-left: 200px;
}

.report-table th, .report-table td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

.report-table th {
  background-color: #f4f4f4;
}

.chart-block {
  display: flex;
  flex-direction: column;
  flex: 1;
  border: 1px solid #ccc;
  padding: 10px;
  height: 600px;
  width: 100%;
  margin-bottom: 20px;
}

.chart-block p {
  text-align: center;
  font-weight: bold;
  margin-bottom: 15px;
  color: #000;
}

.chart-block canvas {
  flex: 1;
  margin: 0 auto;
  height: 100%;
}

.buttons-container {
  text-align: center;
  margin-top: 20px;
}
</style>
