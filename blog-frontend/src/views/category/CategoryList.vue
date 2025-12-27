<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>分类管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增分类</el-button>
      </div>
    </template>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="name" label="分类名称" width="150" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
      <el-table-column prop="articleCount" label="文章数" width="100" align="center">
        <template #default="scope">
          <el-tag type="info">{{ scope.row.articleCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        v-model="dialogVisible"
        :title="form.id ? '编辑分类' : '新增分类'"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

// 数据定义
const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref()

const form = reactive({
  id: null,
  name: '',
  description: '',
  sortOrder: 0
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

// 加载列表
const loadList = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/category/list')
    tableData.value = res // 假设后端直接返回 List<Category>
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 打开新增
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 打开编辑
const handleEdit = (row: any) => {
  // 复制数据到表单
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交表单
const submitForm = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (form.id) {
          // 修改 PUT
          await request.put('/category', form)
          ElMessage.success('修改成功')
        } else {
          // 新增 POST
          await request.post('/category', form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadList() // 刷新
      } catch (error) {
        console.error(error)
      }
    }
  })
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定删除分类 "${row.name}" 吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/category/${row.id}`)
      ElMessage.success('删除成功')
      loadList()
    } catch (error) {
      // ignore
    }
  })
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.name = ''
  form.description = ''
  form.sortOrder = 0
  if (formRef.value) formRef.value.resetFields()
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