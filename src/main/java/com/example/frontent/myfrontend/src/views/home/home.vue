<script setup>
import { ref, onMounted } from 'vue'
import { Cloudy, Share, Location, TrendCharts, Histogram, Warning, Cpu } from '@element-plus/icons-vue'
import { getKpi } from '@/api/statisticApi'
import Footer from '@/components/footer.vue'

// KPI List Data
// 1. 将 kpiList 改为 ref
const kpiList = ref([
  { label: '年均气温（20年均值）', value: 0, unit: '℃', trend: '正在计算...', trendClass: 'neu', color: '#3a8fff' },
  { label: '极端高温阈值', value: 0, unit: '℃', trend: '正在计算...', trendClass: 'neu', color: '#00f58a' },
  { label: '极端低温阈值', value: 0, unit: '℃', trend: '正在计算...', trendClass: 'neu', color: '#ff7b45' },
  { label: '气温日较差均值', value: 0, unit: '℃', trend: '正在计算...', trendClass: 'neu', color: '#c07fff' },
])

// 处理数据
const load = async () => {
    const res = await getKpi() // 这里获取到整个 Object { code, msg, data }
    if (res.data) {
        const d = res.data
        // 2. 将返回的单条数据手动映射到数组
        kpiList.value = [
            { label: '年均气温（20年均值）', value: d.avgAll.toFixed(1), unit: '℃', trend: '基于20年全量数据', trendClass: 'neu', color: '#3a8fff' },
            { label: '极端高温阈值', value: d.highThreshold.toFixed(1), unit: '℃', trend: 'P95 分位界定', trendClass: 'up', color: '#00f58a' },
            { label: '极端低温阈值', value: d.lowThreshold.toFixed(1), unit: '℃', trend: 'P05 分位界定', trendClass: 'dn', color: '#ff7b45' },
            { label: '气温日较差均值', value: d.avgRangeAll.toFixed(1), unit: '℃', trend: '大陆性气候特征', trendClass: 'neu', color: '#c07fff' },
        ]
    }
}



// Number Rolling Counter Logic
const counterDisplay = ref('0')
onMounted(() => {
  load()
  const target = 7305
  let cur = 0
  const tick = () => {
    if (cur < target) {
      cur = Math.min(cur + Math.ceil((target - cur) / 12), target)
      counterDisplay.value = cur.toLocaleString()
      requestAnimationFrame(tick)
    }
  }
  setTimeout(tick, 400) // Initial delay
})

// Data Governance Chain Steps
const chainSteps = [
  { name: 'ODS 原始采集', sub: '日增数据入库' },
  { name: 'DWD 清洗转换', sub: '缺失值补全 · 格式标准化' },
  { name: 'DWS 特征工程', sub: '日较差计算 · 四季维度打标' },
  { name: 'ADS 聚合统计', sub: '极端值计算 · 趋势预计算' },
  { name: '可视化看板', sub: '基于 ADS 高效渲染' },
]



// Functional Entry List
const funcList = [
  { name: '气温趋势分析', desc: '年 / 季 / 月三维度 · 20年变化规律', route: '/statistic/trend',    icon: TrendCharts, tag: 'TREND', color: '#3a8fff', borderColor: '#1a4a8a' },
  { name: '极端气温统计', desc: '高温 / 低温频次 · 科学阈值界定',    route: '/statistic/extreme',  icon: Warning,     tag: 'ALERT', color: '#ff7b45', borderColor: '#4a2010' },
  { name: '日较差分布',   desc: '大陆性气候特征 · 季节差异分析',     route: '/statistic/diurnal',  icon: Histogram,   tag: 'DIST',  color: '#c07fff', borderColor: '#3a1050' },
  { name: '预测建模',     desc: '时序回归模型 · 未来气温研判',       route: '/statistic/forecast', icon: Cpu,         tag: 'AI',    color: '#00f58a', borderColor: '#004020' },
]
</script>


<template>
  <div class="wrap glass-bg">
    <header class="hdr">
      <div class="hdr-left">
        <div class="hdr-icon">
          <Cloudy />
        </div>
        <div>
          <div class="title">呼和浩特市气象智能分析平台</div>
          <div class="subtitle">HOHHOT METEOROLOGICAL INTELLIGENCE SYSTEM · 2004–2023</div>
        </div>
      </div>
      <div class="hdr-right">
        <span>数据年限 <em class="accent">20 年</em></span>
        <span>样本量 <em class="accent tabular">{{ counterDisplay }} 条</em></span>
        <span>更新 <em class="accent">2026-06-09</em></span>
      </div>
    </header>

    <div class="main-grid">

      <div class="panel chain-panel">
        <div class="panel-title">
          <el-icon :size="16"><Share /></el-icon>数据治理链路
        </div>
        <div class="chain">
          <div v-for="(step, i) in chainSteps" :key="i" class="chain-step">
            <div class="step-dot">{{ i + 1 }}</div>
            <div class="step-info">
              <div class="step-name">{{ step.name }}</div>
              <div class="step-sub">{{ step.sub }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="mid-col">
        <div class="kpi-grid">
          <div
            v-for="kpi in kpiList"
            :key="kpi.label"
            class="kpi brighter-kpi"
            :style="{ '--accent': kpi.color }"
          >
            <div class="kpi-label">{{ kpi.label }}</div>
            <div class="kpi-val tabular">{{ kpi.value }}<span class="kpi-unit">{{ kpi.unit }}</span></div>
            <div class="kpi-trend" :class="kpi.trendClass">{{ kpi.trend }}</div>
          </div>
        </div>

        <div class="func-grid">
          <router-link
            v-for="fn in funcList"
            :key="fn.name"
            :to="fn.route"
            class="func-card brighter-card"
          >
            <div class="func-icon" :style="{ color: fn.color, borderColor: fn.borderColor }">
              <el-icon :size="26"><component :is="fn.icon" /></el-icon>
            </div>
            <div class="func-body">
              <div class="func-name">{{ fn.name }}</div>
              <div class="func-desc">{{ fn.desc }}</div>
            </div>
            <span
              class="func-badge"
              :style="{ color: fn.color, borderColor: fn.color + '88', background: fn.color + '22' }"
            >{{ fn.tag }}</span>
          </router-link>
        </div>
      </div>

      <div class="panel map-panel">
        <div class="panel-title">
          <el-icon :size="16"><Location /></el-icon>监测区域
        </div>
        <svg class="map-svg" viewBox="0 0 200 220" xmlns="http://www.w3.org/2000/svg">
          <rect width="200" height="220" fill="transparent"/>
          <line v-for="x in [30,70,110,150,190]" :key="'vl'+x" :x1="x" y1="5" :x2="x" y2="210" stroke="#1a3a6a" stroke-width="0.5"/>
          <line v-for="y in [40,80,120,160,200]" :key="'hl'+y" x1="5" :y1="y" x2="195" :y2="y" stroke="#1a3a6a" stroke-width="0.5"/>
          <path d="M25,95 Q35,48 100,36 Q160,24 172,72 Q184,110 160,148 Q130,188 85,184 Q40,180 25,138 Z"
                fill="none" stroke="#2a5a9a" stroke-width="1.2"/>
          <path d="M50,108 Q65,76 100,64 Q138,52 150,85 Q162,112 142,138 Q120,165 90,162 Q58,159 50,130 Z"
                fill="rgba(10, 24, 48, 0.6)" stroke="#3a8fff" stroke-width="1"/>
          <circle cx="100" cy="112" r="14" fill="#3a8fff" opacity="0.15"/>
          <circle cx="100" cy="112" r="5" fill="#3a8fff" class="pulse-dot"/>
          <text x="110" y="106" font-size="10" fill="#a0cfff" font-family="monospace">呼和浩特</text>
          <text x="110" y="120" font-size="8.5" fill="#4a6aba" font-family="monospace">40.8°N 111.7°E</text>
          <text x="6" y="216" font-size="8" fill="#3a5a8a" font-family="monospace">北方半干旱气候区 · 内蒙古自治区首府</text>
        </svg>
        <div class="map-meta brighter-meta">
          <div class="meta-row"><span>气候类型</span><em>BSk 半干旱</em></div>
          <div class="meta-row"><span>海拔高度</span><em>~1063 m</em></div>
          <div class="meta-row"><span>坐标参考</span><em>WGS-84</em></div>
        </div>
      </div>

    </div>

    <footer class="bottom-bar glass-bar">
      <div class="ticker">
        <span>数据范围 <em class="bright-accent">2004.01.01 → 2023.12.31</em></span>
        <span>覆盖天数 <em class="bright-accent">7305 days</em></span>
        <span>要素字段 <em class="bright-accent">最高温 · 最低温 · 平均温</em></span>
        <span>分析维度 <em class="bright-accent">年 / 季 / 月</em></span>
      </div>
      <span class="copy bright-accent">HOHHOT CLIMATE INTELLIGENCE © 2026</span>
    </footer>

  </div>
</template>


<style scoped>
/* ── Global Styles & Utilities ── */
.tabular { font-variant-numeric: tabular-nums; } /* Equal-width numbers */

/* Optimized Wrap Container (Brighter and Transparent) */
.wrap {
  position: relative;
  width: 100%;
  height: 100vh;
  padding: 16px 24px 0;
  /* RadiantMidnightBlue */
  background: linear-gradient(135deg, #0c1a3d 0%, #060d1a 100%);
  color: #e2eaf6;
  font-family: 'JetBrains Mono', 'Segoe UI', Consolas, monospace;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

/* ── Top Header Bar ── */
.hdr {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 14px;
  border-bottom: 1px solid #2a4a8a; /* Brightened border */
  margin-bottom: 14px;
  flex-shrink: 0;
}
.hdr-left { display: flex; align-items: center; gap: 14px; }
.hdr-icon {
  width: 44px; height: 44px;
  border: none;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  overflow: hidden;
  background: transparent;
  flex-shrink: 0;
  border: 1px solid rgba(58, 143, 255, 0.4); /* Optional bright halo */
}
.logo-img { width: 100%; height: 100%; object-fit: cover; display: block; }
.title { font-size: 20px; font-weight: 600; letter-spacing: 1.5px; color: #f2f7ff; /* Whiter text */ }
.subtitle { font-size: 11px; letter-spacing: 2px; color: #3a6aba; /* Brightened subtitle */ margin-top: 3px; }

.hdr-right { display: flex; align-items: center; gap: 28px; font-size: 13px; color: #4a7aba; }
.hdr-right em.accent { font-style: normal; color: #3a8fff; /* Brighter accent */ font-weight: bold;}

.status-dot {
  display: inline-block; width: 8px; height: 8px;
  background: #00f58a; /* Brighter green */ border-radius: 50%;
  margin-right: 6px; animation: pulse 1.5s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { opacity: 1; box-shadow: 0 0 0 0 #00f58a66; }
  50% { opacity: 0.6; box-shadow: 0 0 0 5px transparent; }
}

/* ── Main Layout ── */
.main-grid {
  display: grid; grid-template-columns: 260px 1fr 240px;
  gap: 14px; align-items: start; flex: 1; min-height: 0;
}

/* ── Brighter Glass Panel ── */
.panel {
  background: rgba(12, 26, 61, 0.6); /* Transparent blue glass */
  border: 1px solid rgba(42, 74, 138, 0.6);
  backdrop-filter: blur(10px); /* Frosted effect */
  padding: 16px 18px;
  position: relative; overflow: hidden;
  height: 100%; box-sizing: border-box;
}
.panel::before {
  content: ''; position: absolute; top: 0; left: 0;
  width: 3px; height: 100%;
  background: var(--line-color, #3a8fff);
  box-shadow: 0 0 10px var(--line-color, #3a8fff);
}
.panel-title {
  display: flex; align-items: center; gap: 8px;
  font-size: 11px; letter-spacing: 2px;
  color: #a0cfff; /* Brighter label */ text-transform: uppercase; margin-bottom: 18px;
}

/* ── Data Chain Panel ── */
.chain-step { display: flex; align-items: flex-start; gap: 14px; padding-bottom: 20px; position: relative; }
.chain-step:not(:last-child)::after {
  content: ''; position: absolute; left: 12px; top: 26px;
  width: 1px; height: calc(100% - 6px);
  background: #2a4a8a; /* Brighter chain line */
}
.step-dot {
  width: 26px; height: 26px;
  border: 1px solid #3a8fff; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 11px; color: #3a8fff; background: rgba(12, 26, 61, 0.8);
  flex-shrink: 0; z-index: 1;
  animation: stepglow 3s ease-in-out infinite;
}
@keyframes stepglow {
  0%, 100% { border-color: #3a8fff; color: #3a8fff; box-shadow: 0 0 5px rgba(58, 143, 255, 0.2); }
  50% { border-color: #00f58a; color: #00f58a; box-shadow: 0 0 10px rgba(0, 245, 138, 0.4); }
}
.step-name { font-size: 14px; color: #e2f0ff; line-height: 1.4; }
.step-sub { font-size: 11px; color: #4a6aba; margin-top: 3px; }

/* ── KPIs & Cards ── */
.mid-col { display: flex; flex-direction: column; gap: 14px; }
.kpi-grid, .func-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }

/* Brighter KPI and Card Visuals */
.kpi, .func-card {
  background: rgba(12, 26, 61, 0.7);
  border: 1px solid rgba(42, 74, 138, 0.5);
  backdrop-filter: blur(5px);
  padding: 16px 18px;
  position: relative; overflow: hidden;
  transition: all 0.3s ease;
}
.kpi:hover, .func-card:hover {
  background: rgba(18, 38, 81, 0.9);
  border-color: #3a8fff;
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(58, 143, 255, 0.15);
}

/* KPI Accent Line on Top for Brightness */
.kpi::after {
  content: ''; position: absolute;
  top: 0; left: 0; right: 0;
  height: 2px; background: var(--accent, #3a8fff);
  box-shadow: 0 0 10px var(--accent, #3a8fff);
}
.kpi-label { font-size: 11px; color: #4a7aba; letter-spacing: 0.5px; margin-bottom: 10px; }
.kpi-val { font-size: 36px; font-weight: 600; line-height: 1; color: #f2f7ff; }
.kpi-unit { font-size: 16px; color: #a0cfff; margin-left: 3px; }
.kpi-trend { font-size: 12px; margin-top: 8px; font-weight: bold;}
.up { color: #00f58a; } .dn { color: #ff7b45; } .neu { color: #c07fff; }

/* Brighter Functional Cards */
.func-icon {
  width: 46px; height: 46px;
  border: 1px solid;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0; transition: transform 0.2s;
  background: rgba(8, 15, 30, 0.4);
}
.func-name { font-size: 14px; font-weight: 600; color: #e2f0ff; }
.func-desc { font-size: 11px; color: #4a6aba; margin-top: 4px; line-height: 1.5; }
.func-badge {
  position: absolute; top: 8px; right: 10px;
  font-size: 9px; padding: 2px 7px;
  border: 1px solid; letter-spacing: 0.8px;
  font-weight: bold;
}

/* ── Map Panel ── */
.map-panel { --line-color: #c07fff; }
.map-svg { width: 100%; margin-top: 6px; }
.pulse-dot { animation: pdot 2s ease-in-out infinite; transform-origin: 100px 112px; }
@keyframes pdot {
  0%, 100% { r: 5; opacity: 1; }
  50% { r: 9; opacity: 0.3; }
}
.brighter-meta { border-top: 1px solid #1a3a6a; }
.meta-row { font-size: 12px; color: #4a6aba; padding: 4px 0; }
.meta-row em { color: #a0cfff; }

/* ── Bottom Bar (Transparent Glass) ── */
.bottom-bar {
  border-top: 1px solid #1a3a6a;
  background: rgba(12, 26, 61, 0.8);
  backdrop-filter: blur(10px);
  padding: 10px 24px;
}
.ticker span em.bright-accent, .copy.bright-accent { color: #3a6aba; }
</style>