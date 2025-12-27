import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue()],
    resolve: {
        // 关键配置：解决 WangEditor 报错 "default is not exported"
        alias: {
            'vue': 'vue/dist/vue.esm-bundler.js'
        }
    },
    server: {
        host: '0.0.0.0', // 允许局域网访问
        port: 5173,      // 前端端口
        open: true,      // 自动打开浏览器
        proxy: {
            '/api': {
                // ⚠️ 如果你后端 Tomcat 部署在根目录 (Application Context 为 /)，这里写 http://localhost:8080
                target: 'http://localhost:8080',
                changeOrigin: true,
            }
        }
    }
})