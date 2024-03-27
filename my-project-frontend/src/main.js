import './assets/main.css'
import {useMenuStore} from "@/store/Store.js";
import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from "@/router";
import 'element-plus/dist/index.css'
import ElementPlus  from 'element-plus';
import axios from "axios";
import 'element-plus/theme-chalk/dark/css-vars.css'
axios.defaults.baseURL="http://localhost:8080"
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const pinia = createPinia()
const app = createApp(App)
app.use(pinia)
app.use(ElementPlus)
app.use(router)

console.log("enter pinia")

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}


app.mount('#app')