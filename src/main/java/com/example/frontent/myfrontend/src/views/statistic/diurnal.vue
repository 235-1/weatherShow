<script setup>
import StackedAreaChart from "@/components/distributionChart/stackedAreaChart.vue";
import Footer from "@/components/footer.vue";
import { ref, onMounted, watch } from "vue";
import { getDiurnalDeviation } from "@/api/statisticApi";

const diurnalData = ref([]);
const load = async () => {
    const res = await getDiurnalDeviation();
    diurnalData.value = res.data;
    console.log("日差温度统计分析:",diurnalData.value);
}
onMounted(async () => {
    load();
});
</script>

<template>
    <div>
        <StackedAreaChart 
        v-if="diurnalData.length > 0"
        :data="diurnalData" style="flex: 1" />
        <Footer />
    </div>
</template>

<style scoped>
    h1 {
        color: white;
    }
</style>