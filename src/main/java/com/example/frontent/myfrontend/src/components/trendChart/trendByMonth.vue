<!-- 气温月度趋势图 -->
<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from "vue";
import * as echarts from "echarts";

const props = defineProps({
  trendMonth: {
    type: Object,
    required: true,
  },
});

const chartRef = ref(null);
let myChart = null;

const initChart = () => {
  if (!chartRef.value) return;
  myChart = echarts.init(chartRef.value);

  const option = {
    title: { text: "月度气温趋势 (20年回顾)", textStyle: { color: "#fff" } },
    tooltip: { trigger: "axis" },
    // 关键配置：添加数据缩放区域
    dataZoom: [
      { type: "slider", show: true, start: 80, end: 100 }, // 默认显示最后20%数据
      { type: "inside" }, // 支持鼠标滚轮缩放
    ],
    legend: {
      data: ["最高温", "平均温", "最低温"],
      bottom: "5%", // 靠底部
      left: "center", // 水平居中
      textStyle: { color: "#fff" },
    },
    // 同时需要增大 grid 的 bottom，防止图例遮挡底部数据
    legend: {
      data: ["最高温", "平均温", "最低温"],
      bottom: "15%", // 靠底部
      left: "center", // 水平居中
      textStyle: { color: "#fff" },
    },
    // 同时需要增大 grid 的 bottom，防止图例遮挡底部数据
    grid: {
      top: "80px",
      left: "5%",
      right: "5%",
      bottom: "100px",
      containLabel: true,
    },
    xAxis: { type: "category", data: [], axisLabel: { color: "#fff" } },
    yAxis: {
      type: "value",
      name: "温度 (°C)",
      nameTextStyle: { color: "#fff" },
      axisLabel: { color: "#fff" },
    },
    series: [
      {
        name: "最高温",
        type: "line",
        data: [],
        itemStyle: { color: "#ff4d4f" },
      },
      {
        name: "平均温",
        type: "line",
        smooth: true,
        areaStyle: { opacity: 0.1 },
        itemStyle: { color: "#1890ff" },
        data: [],
      },
      {
        name: "最低温",
        type: "line",
        data: [],
        itemStyle: { color: "#52c41a" },
      },
    ],
  };
  myChart.setOption(option);
};

const updateChart = (data) => {
  if (!myChart || !data) return;
  myChart.setOption({
    xAxis: { data: data.xaxis },
    series: [
      { data: data.maxSeries },
      { data: data.avgSeries },
      { data: data.minSeries },
    ],
  });
};

watch(
  () => props.trendMonth,
  (newData) => {
    if (newData) updateChart(newData);
  },
  { deep: true },
);

const handleResize = () => myChart?.resize();

onMounted(() => {
  initChart();
  if (props.trendMonth) updateChart(props.trendMonth);
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize);
  myChart?.dispose();
});
</script>

<style scoped>
.chart-container {
  width: 100%; /* 充满父容器 */
  height: 350px; /* 统一设定高度，避免上下图高度不一致 */
  margin-bottom: 20px;
  background: rgba(16, 24, 48, 0.4);
  border: 1px solid #1e293b;
  border-radius: 8px;
  padding: 10px;
}
</style>
