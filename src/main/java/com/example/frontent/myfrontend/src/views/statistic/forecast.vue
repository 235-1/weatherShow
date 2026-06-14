<script setup>  
import { getPrediction, getActualData } from '@/api/statisticApi'
import { onMounted, ref } from 'vue'
const forecastData = ref([])
const actualData = ref([])
const load = async () => {
    const res = await getPrediction()
    forecastData.value = res.data
    console.log("预报温度统计分析:", res.data)
    console.log("预报温度统计分析:", forecastData.value)
    const actualRes = await getActualData()
    actualData.value = actualRes.data
    console.log("真实数据2024:", actualRes.data)
    console.log("真实数据2024:", actualData.value)
}

onMounted(async () => {
    load()
})


</script>

<template>
  <div class="chart-container">
    <linearChart 
      v-if="forecastData.length > 0 && actualData.length > 0" 
      :data="forecastData"
      :actualData="actualData"
    />
  </div>
</template>

<style scoped>
.chart-container {
  display: flex;             
  flex-direction: column;
  justify-content: center;  
  align-items: center;       
  width: 100%;               
  padding: 40px 0px;         
}
</style>
