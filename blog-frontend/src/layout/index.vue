<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside-container">
      <div class="logo">
        <img src="https://element-plus.org/images/element-plus-logo.svg" alt="logo" style="height: 24px; margin-right: 10px;">
        <span>Blog Admin Pro</span>
      </div>
      <el-menu
          active-text-color="#409EFF"
          background-color="#2b3649"
          text-color="rgba(255,255,255,0.7)"
          :default-active="$route.path"
          router
          :border="false"
          class="el-menu-vertical"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>

        <el-sub-menu index="/article">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </template>
          <el-menu-item index="/article/list">文章列表</el-menu-item>
          <el-menu-item index="/article/publish">发布文章</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/category">
          <el-icon><Folder /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/tag">
          <el-icon><PriceTag /></el-icon>
          <span>标签管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header-container">
        <div class="header-left">
          <el-icon size="20" color="#606266" style="margin-right: 15px; cursor:pointer"><Fold /></el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title || $route.name }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-tooltip content="全屏" placement="bottom">
            <el-icon size="20" class="header-icon"><FullScreen /></el-icon>
          </el-tooltip>
          <el-tooltip content="消息通知" placement="bottom">
            <el-badge is-dot class="header-icon-badge">
              <el-icon size="20" class="header-icon"><Bell /></el-icon>
            </el-badge>
          </el-tooltip>

          <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link user-info">
              <el-avatar :size="36" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
              <span class="username">管理员</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-container">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>

      <el-footer class="footer">
        Blog Admin Pro ©2025 Created by JavaWeb全栈
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const handleCommand = (command: string) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('user')
      ElMessage.success('退出成功')
      router.push('/login')
    })
  } else if (command === 'profile') {
    ElMessage.info('个人中心功能开发中...')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

/* --- 侧边栏美化 --- */
.aside-container {
  background-color: #2b3649; /* 更深沉的蓝色 */
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35); /* 右侧阴影 */
  z-index: 10; /* 确保阴影在内容之上 */
  overflow-x: hidden;
}
.logo {
  height: 60px;
  line-height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  background-color: #2b3649;
  /* 增加一点光泽感 */
  background-image: linear-gradient(to right, #2b3649, #344058);
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
.el-menu-vertical:not(.el-menu--collapse) {
  border-right: none;
}
/* 菜单选中项样式优化 */
:deep(.el-menu-item.is-active) {
  background-color: #1f2d3d !important;
  border-right: 3px solid #409EFF;
}

/* --- 头部美化 --- */
.header-container {
  background-color: #fff;
  /* 底部柔和阴影 */
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  z-index: 9;
}
.header-left {
  display: flex;
  align-items: center;
}
.header-right {
  display: flex;
  align-items: center;
}
.header-icon {
  margin-right: 20px;
  cursor: pointer;
  color: #606266;
  transition: color 0.3s;
}
.header-icon:hover {
  color: #409EFF;
}
.header-icon-badge {
  margin-right: 20px;
}
.header-icon-badge :deep(.el-badge__content.is-fixed) {
  top: 5px;
  right: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 20px;
  transition: background 0.3s;
}
.user-info:hover {
  background-color: #f5f7fa;
}
.username {
  margin-left: 10px;
  margin-right: 5px;
  font-weight: 500;
  color: #606266;
}

/* --- 主体区域 --- */
.main-container {
  /* 使用更柔和的灰色背景 */
  background-color: #f6f8f9;
  padding: 20px;
  overflow-x: hidden;
}

/* 页面切换动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .4s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.footer {
  text-align: center;
  color: #c0c4cc;
  font-size: 12px;
  line-height: 60px;
  background: #f6f8f9;
}
</style>