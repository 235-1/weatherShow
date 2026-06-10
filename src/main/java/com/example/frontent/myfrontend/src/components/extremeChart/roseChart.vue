<!-- 
玫瑰图 
-->
<template>
  <div class="chart-container" ref="chartRef"></div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({ data: Array }) // 接收从父组件传来的数组
const chartRef = ref(null)
let chart = null

const initChart = async (dataList) => {
  await nextTick()
  if (!chartRef.value || !dataList || dataList.length === 0) return

  if (chart) chart.dispose()
  chart = echarts.init(chartRef.value)

  // 1. 数据拆解
  const seasons = dataList.map(item => item.season)
  const highSeries = dataList.map(item => item.highCount)
  const lowSeries = dataList.map(item => item.lowCount)

  const option = {
    title: { 
      text: '极端天气季节分布', 
      left: 'center', 
      textStyle: { color: '#fff', fontSize: 16 } 
    },
    tooltip: { trigger: 'item' },
    legend: { 
      top: '10%', 
      textStyle: { color: '#fff' }, 
      data: ['极端高温', '极端低温'] 
    },
    polar: { radius: [40, '70%'] }, // 内部留空，形成玫瑰环效果
    angleAxis: {
      type: 'category',
      data: seasons,
      axisLine: { lineStyle: { color: '#ffffff55' } }
    },
    radiusAxis: { 
      min: 0, 
      axisLabel: { color: '#8a99ad' } 
    },
    series: [
      {
        type: 'bar',
        name: '极端高温',
        data: highSeries,
        coordinateSystem: 'polar',
        stack: 'total',
        itemStyle: { color: '#ef5350', borderRadius: 4 },
        emphasis: { focus: 'series' }
      },
      {
        type: 'bar',
        name: '极端低温',
        data: lowSeries,
        coordinateSystem: 'polar',
        stack: 'total',
        itemStyle: { color: '#42a5f5', borderRadius: 4 },
        emphasis: { focus: 'series' }
      }
    ]
  }
  chart.setOption(option)
}

// 监听数据变化
watch(() => props.data, (newData) => {
  if (newData) initChart(newData)
}, { deep: true, immediate: true })

onMounted(() => {
  window.addEventListener('resize', () => chart?.resize())
})
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px;
  background: rgba(16, 24, 48, 0.4);
  border: 1px solid #1e293b;
  border-radius: 8px;
  padding: 10px;
}
</style>