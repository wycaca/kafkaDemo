import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    proxy: {
      '/test': {
        target: 'http://localhost:8189',
        changeOrigin: true,
        // rewrite: path => path.replace(/^\/test/, '')
      }
    },
    host: '0.0.0.0'
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
