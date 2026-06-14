<script setup>
import { onMounted, ref, watch } from "vue";
import * as echarts from "echarts";

const props = defineProps({
  data: { type: Array, default: () => [] },
  actualData: { type: Array, default: () => [] },
});

const chartRef = ref(null);
let chart = null;

const renderChart = () => {
  if (!chart || props.data.length === 0) return;

  const actualMap = new Map();
  props.actualData.forEach((item) => {
    if (item.date) actualMap.set(item.date, item.avgTemp);
  });

  const dates = props.data.map((item) => item.ds);
  const yhat = props.data.map((item) => item.yhat);
  const lower = props.data.map((item) => item.yhatLower);
  const band = props.data.map((item) => item.yhatUpper - item.yhatLower);
  const actualValues = dates.map((d) => actualMap.get(d) ?? null);
  console.log("日期数组长度:", dates.length);
  console.log("预测值数组长度:", yhat.length);
  chart.setOption({
    // 交互优化：鼠标悬停显示十字准线
    tooltip: {
      trigger: "axis",
      axisPointer: { type: "cross", label: { backgroundColor: "#6a7985" } },
    },
    legend: { data: ["预测值", "真实值", "置信区间"], top: 5 },
    grid: { left: "5%", right: "10%", bottom: "15%", containLabel: true },
    dataZoom: [
      { type: "inside", start: 0, end: 30 },
      { type: "slider", start: 0, end: 30, bottom: 5 },
    ],
   xAxis: {
      type: "category",
      data: dates,
      boundaryGap: false,
      axisLine: { show: false },
      axisLabel: {
        // 使用 formatter 对每个刻度标签进行处理
         interval: 0, // 强制显示所有标签
        formatter: (value) => {
          // 假设你的日期格式是 "2024-01-01"
          // 我们只返回月份部分，例如 "01" 或 "1月"
          const month = value.split("-")[1]; 
          return parseInt(month) + "月"+parseInt(value.split("-")[2])+"日";
        },
        // 设置间隔，避免 365 个标签挤在一起
        interval: 30, // 大约每个月显示一个标签 (365/12 ≈ 30)
        rotate: 0,    // 月份标签较短，不需要旋转
      },
    },
    yAxis: {
      type: "value",
      name: "气温(℃)",
      splitLine: { lineStyle: { type: "dashed" } },
    },
    series: [
      {
        name: "预测值",
        type: "line",
        data: yhat,
        smooth: true,
        symbol: "none",
        lineStyle: { color: "#d85a30", width: 2.5 },
        emphasis: { focus: "series" }, // 悬停高亮
      },
      {
        name: "真实值",
        type: "line",
        data: actualValues,
        smooth: true,
        symbol: "circle",
        symbolSize: 6,
        lineStyle: { color: "#5470c6", width: 2, type: "solid" }, // 改为实线更清晰
        itemStyle: { color: "#5470c6", borderWidth: 2, borderColor: "#fff" },
        emphasis: { focus: "series" },
      },
      {
        name: "置信区间",
        type: "line",
        data: lower,
        lineStyle: { opacity: 0 },
        stack: "confidence-band",
        symbol: "none",
      },
      {
        name: "置信区间",
        type: "line",
        data: band,
        lineStyle: { opacity: 0 },
        areaStyle: { color: "#85b7eb", opacity: 0.15 }, // 阴影调淡，不干扰数据线
        stack: "confidence-band",
        symbol: "none",
      },
    ],
  });
};

onMounted(() => {
  chart = echarts.init(chartRef.value);
  renderChart();
  window.addEventListener("resize", () => chart.resize());
});

watch(() => [props.data, props.actualData], renderChart, { deep: true });
</script>

<template>
  <div ref="chartRef" style="width: 100%; height: 420px"></div>
</template>
