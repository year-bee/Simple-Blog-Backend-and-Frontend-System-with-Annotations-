<template>
  <el-card>
    <template #header>
      <div class="header">
        <el-input
            v-model="queryParams.keyword"
            placeholder="搜索文章标题"
            style="width: 200px; margin-right: 10px"
            clearable
            @clear="loadList"
            @keyup.enter="loadList"
        />
        <el-button type="primary" icon="Search" @click="loadList">搜索</el-button>
        <el-button type="success" icon="Plus" @click="$router.push('/article/publish')">发布文章</el-button>
      </div>
    </template>

    <el-table :data="tableData" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="title" label="标题" show-overflow-tooltip />
      <el-table-column prop="categoryName" label="分类" width="120" align="center">
        <template #default="scope">
          <el-tag>{{ scope.row.categoryName || '未分类' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="authorName" label="作者" width="120" align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'published' ? 'success' : 'info'">
            {{ scope.row.status === 'published' ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180" align="center" />

      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="primary" link icon="Edit" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 20px; text-align: right;">
      <el-pagination layout="prev, pager, next" :total="total" />
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryParams = reactive({
  keyword: '',
  page: 1,
  pageSize: 10
})

// 获取列表
const loadList = async () => {
  loading.value = true
  try {
    // 这里对应后端 Servlet 的 /list 逻辑
    const res: any = await request.get('/article/list', { params: queryParams })
    tableData.value = res.list
    total.value = res.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 核心修复：编辑跳转
const handleEdit = (row: any) => {
  // 跳转到 /article/publish，并带上 id 参数
  // 例如：/article/publish?id=123
  router.push({
    path: '/article/publish',
    query: { id: row.id }
  })
}

// 删除文章
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除这篇文章吗？此操作不可恢复。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 对应后端 doDelete 方法
      await request.delete(`/article/${row.id}`)
      ElMessage.success('删除成功')
      loadList() // 刷新列表
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>