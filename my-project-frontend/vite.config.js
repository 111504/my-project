import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import {resolve } from 'path';
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
// https://vitejs.dev/config/


export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  css:{
    preprocessorOptions: {
      scss: {
        additionalData: `@import "./src/styles/index.scss";`
      }
    }
  },
  resolve: {
    alias: {
      // 设置'@'别名指向'src'目录
      '@': resolve(__dirname, './src'),
    },
  }
})
