import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from "@/router";
import 'element-plus/dist/index.css'
// import ElementPlus  from 'element-plus';
import axios from "axios";
import 'element-plus/theme-chalk/dark/css-vars.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import SvgIcon from "./components/SvgIcon.vue"
import 'virtual:svg-icons-register'
import "./router/permission.js"
// 國際化中文
import {zhTw} from "element-plus/es/locale/index";
// Import our custom CSS
import '../src/scss/styles.scss'
const apiUrl=import.meta.env.VITE_APP_API_URL
// Import all of Bootstrap's JS
// import * as bootstrap from 'bootstrap'

axios.defaults.baseURL=apiUrl;
const pinia = createPinia()
const app = createApp(App)
app.use(pinia)
// app.use(ElementPlus,{
//     locale:zhTw,
// })


// 配置国际化语言包
app.config.globalProperties.$ELEMENT = { locale: zhTw };

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.component("svg-icon",SvgIcon)
console.log("app.use(router)----")
console.log("在main裡的路由",router.getRoutes())
app.use(router)


console.log("app.use(router) finish----")
app.mount('#app')