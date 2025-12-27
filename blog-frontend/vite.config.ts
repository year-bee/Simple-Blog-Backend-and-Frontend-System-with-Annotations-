import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue()],
    server: {
        host: '0.0.0.0', // 允许局域网访问
        port: 5173,      // 前端端口
        open: true,      // 自动打开浏览器
        proxy: {
            '/api': {
                // ⚠️ 确保这里指向正确的后端地址
                target: 'http://localhost:8080',
                changeOrigin: true,
            }
        }
    }
})