import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
    // 1. 登录页 (不需要 Layout)
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { title: '登录' }
    },

    // 2. 主布局路由 (所有后台页面都在这里面)
    {
        path: '/',
        component: () => import('../layout/index.vue'),
        redirect: '/dashboard', // 访问根目录自动跳到仪表盘
        children: [
            // --- 仪表盘 ---
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('../views/Dashboard.vue'),
                meta: { title: '仪表盘' }
            },

            // --- 文章管理 ---
            {
                path: 'article/list', // 访问地址: /article/list
                name: 'ArticleList',
                component: () => import('../views/article/ArticleList.vue'),
                meta: { title: '文章列表' }
            },
            {
                path: 'article/publish', // 访问地址: /article/publish
                name: 'ArticlePublish',
                component: () => import('../views/article/ArticlePublish.vue'),
                meta: { title: '发布文章' }
            },

            // --- 分类管理 ---
            {
                path: 'category', // 访问地址: /category
                name: 'Category',
                component: () => import('../views/category/CategoryList.vue'),
                meta: { title: '分类管理' }
            },

            // --- 标签管理 ---
            {
                path: 'tag', // 访问地址: /tag
                name: 'Tag',
                component: () => import('../views/tag/TagList.vue'),
                meta: { title: '标签管理' }
            }
        ]
    },

    // 3. 兜底路由 (访问不存在的页面跳转到仪表盘)
    {
        path: '/:pathMatch(.*)*',
        redirect: '/dashboard'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 全局路由守卫 (可选：用于设置浏览器标题)
router.beforeEach((to, from, next) => {
    // 如果路由有 meta.title，就设置给 document.title
    if (to.meta.title) {
        document.title = `${to.meta.title} - Blog Admin`
    }
    next()
})

export default router