<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>{{ isEdit ? '编辑文章' : '发布文章' }}</span>
        <el-button @click="$router.back()">返回列表</el-button>
      </div>
    </template>

    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="文章标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入文章标题" maxlength="100" show-word-limit />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文章分类" prop="categoryId">
            <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
              <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio value="published">立即发布</el-radio>
              <el-radio value="draft">存为草稿</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="文章摘要" prop="summary">
        <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入摘要（选填，如不填将自动截取正文前100字）"
        />
      </el-form-item>

      <el-form-item label="文章内容" prop="content">
        <div style="border: 1px solid #ccc; width: 100%;">
          <div id="toolbar-container" style="border-bottom: 1px solid #ccc"></div>
          <div id="editor-container" style="height: 500px; overflow-y: hidden;"></div>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" size="large" @click="handleSubmit" :loading="loading">
          {{ isEdit ? '保存修改' : '确认发布' }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { createEditor, createToolbar } from '@wangeditor/editor' // 引入原生 API

const router = useRouter()
const route = useRoute()
const formRef = ref()
const loading = ref(false)

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 表单数据
const form = reactive({
  id: null,
  title: '',
  categoryId: null,
  status: 'published',
  summary: '',
  content: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const categoryList = ref<any[]>([])
const isEdit = ref(false)

// 初始化编辑器
const initEditor = () => {
  const editorConfig = {
    placeholder: '请输入内容...',
    onChange(editor: any) {
      const html = editor.getHtml()
      form.content = html // 同步内容到表单
    }
  }

  const editor = createEditor({
    selector: '#editor-container',
    html: form.content || '<p></p>',
    config: editorConfig,
    mode: 'default'
  })
  editorRef.value = editor

  const toolbar = createToolbar({
    editor,
    selector: '#toolbar-container',
    config: {},
    mode: 'default'
  })
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const loadCategories = async () => {
  try {
    const res: any = await request.get('/category/list')
    categoryList.value = res
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = () => {
  // 手动触发一次校验，因为 v-model 没绑定到 div 上
  if (!editorRef.value.getHtml()) {
    ElMessage.warning('文章内容不能为空')
    return
  }

  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true
      try {
        if (isEdit.value) {
          await request.put('/article', form)
          ElMessage.success('修改成功')
        } else {
          await request.post('/article', form)
          ElMessage.success('发布成功')
        }
        router.push('/article/list')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

const loadDetail = async (id: string) => {
  try {
    const res: any = await request.get(`/article/${id}`)
    Object.assign(form, res)
    // 详情加载完后，手动设置编辑器内容
    if (editorRef.value) {
      editorRef.value.setHtml(res.content)
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(async () => {
  await loadCategories()
  // 先渲染 DOM，再初始化编辑器
  initEditor()

  if (route.query.id) {
    isEdit.value = true
    loadDetail(route.query.id as string)
  }
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>