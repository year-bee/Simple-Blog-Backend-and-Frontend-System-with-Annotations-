<template>
  <div class="dashboard-container">
    <el-card class="welcome-card" shadow="never">
      <div class="welcome-content">
        <el-avatar :size="64" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" class="avatar-hover" />
        <div class="info">
          <div class="title">早安，{{ user.nickname || user.username }}，祝你开心每一天！</div>
          <div class="subtitle">今日天气晴朗，适合写代码和创作文章。</div>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="data-row">
      <el-col :span="6" v-for="(item, index) in statCards" :key="index">
        <el-card shadow="hover" class="stat-card" :style="{ '--card-color': item.color }">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">{{ item.title }}</div>
              <div class="stat-num">
                <count-to :startVal="0" :endVal="item.value" :duration="2000"></count-to>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card shadow="hover" header="近7天文章发布趋势">
          <div style="height: 350px;">
            <v-chart class="chart" :option="lineChartOption" autoresize />
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" header="快捷入口">
          <div class="quick-links">
            <el-button type="primary" plain icon="Edit" size="large" @click="$router.push('/article/publish')">发布文章</el-button>
            <el-button type="success" plain icon="Folder" size="large" @click="$router.push('/category')">分类管理</el-button>
            <el-button type="warning" plain icon="CollectionTag" size="large" @click="$router.push('/tag')">标签管理</el-button>
          </div>
        </el-card>
        <el-card shadow="hover" header="系统信息" style="margin-top: 20px">
          <div class="system-info">
            <p><span>当前版本：</span>V1.0.0 (Pro版)</p>
            <p><span>前端技术：</span>Vue3 + Vite + ElementPlus + ECharts</p>
            <p><span>后端技术：</span>Java 原生 Servlet + MySQL</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import request from '../utils/request'
// 引入 vue-count-to 实现数字滚动
import { CountTo } from 'vue3-count-to'
// 引入 echarts
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const user = JSON.parse(localStorage.getItem('user') || '{}') || {}

// 统计数据源
const stats = reactive({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0,
  viewCount: 0
})

// 卡片配置 (用于循环渲染，方便管理颜色和图标)
const statCards = computed(() => [
  { title: '文章总数', value: stats.articleCount, icon: 'Document', color: '#409EFF' },
  { title: '分类数量', value: stats.categoryCount, icon: 'Folder', color: '#67C23A' },
  { title: '标签数量', value: stats.tagCount, icon: 'PriceTag', color: '#E6A23C' },
  { title: '总阅读量', value: stats.viewCount, icon: 'View', color: '#F56C6C' },
])

// 加载统计数据
const loadStats = async () => {
  try {
    const res: any = await request.get('/statistics/home')
    Object.assign(stats, res)
  } catch (error) {
    console.error('获取统计失败', error)
  }
}

// --- 图表配置 (模拟数据) ---
const lineChartOption = ref({
  tooltip: {
    trigger: 'axis'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    axisLine: { lineStyle: { color: '#999' } }
  },
  yAxis: {
    type: 'value',
    axisLine: { show: false },
    axisTick: { show: false },
    splitLine: { lineStyle: { color: '#eee', type: 'dashed' } }
  },
  series: [
    {
      name: '发布数量',
      type: 'line',
      // 数据暂时写死，展示效果
      data: [1, 3, 2, 5, 3, 7, 4],
      smooth: true, // 平滑曲线
      itemStyle: { color: '#409EFF' },
      areaStyle: {
        // 渐变填充色
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [{ offset: 0, color: 'rgba(64,158,255, 0.5)' }, { offset: 1, color: 'rgba(64,158,255, 0)' }]
        }
      }
    }
  ]
})

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard-container {
  /* 增加整体容器的内边距 */
}

/* 1. 欢迎卡片美化 */
.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(to right, #ffffff, #f0f2f5);
  border: none;
}
.welcome-content {
  display: flex;
  align-items: center;
}
.avatar-hover {
  transition: transform 0.3s;
}
.avatar-hover:hover {
  transform: rotate(360deg);
}
.info {
  margin-left: 20px;
}
.title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}
.subtitle {
  color: #909399;
  font-size: 14px;
}

/* 2. 数据卡片美化 */
.data-row {
  margin-bottom: 20px;
}
.stat-card {
  border: none;
  /* 使用 CSS 变量实现鼠标悬停时的彩色阴影 */
  transition: all 0.3s;
}
.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px -10px var(--card-color);
}
.stat-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  /* 使用 CSS 变量设置背景色（带透明度）和图标颜色 */
  background-color: color-mix(in srgb, var(--card-color) 15%, white);
  color: var(--card-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
}
.stat-info {
  text-align: right;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}
.stat-num {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

/* 3. 快捷入口和系统信息 */
.quick-links {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}
.quick-links .el-button {
  flex: 1 0 40%; /* 让按钮平分宽度 */
  margin-left: 0 !important; /* 覆盖 Element 默认 margin */
  height: 50px;
}
.system-info p {
  line-height: 2;
  color: #606266;
}
.system-info span {
  font-weight: bold;
  color: #303133;
  margin-right: 10px;
}

/* 图表容器高度 */
.chart {
  height: 100%;
  width: 100%;
}
</style>