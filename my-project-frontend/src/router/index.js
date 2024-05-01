import {createRouter, createWebHistory} from "vue-router";
import {useMenuStore} from "@/store/Store.js";
import {getUserInfo, unauthorized} from "@/net/index.js";

// const {name}=router.currentRoute.value;
// if(!name){
//     router.addRoute({
//         path:window.location.pathname,
//         name:'TempRouter',
//         component:temprouter
//
//     })
//
// }

const router=createRouter({


    history:createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path:'/',
            name:'welcome-',
            component:()=>import('@/views/WelcomeView.vue'),
            children:[
                {
                    //  '/'為根目錄， 當根目錄匹配時 會渲染空的子router
                    path: '',
                    name:'welcome-login',
                    component:()=>import('@/views/welcome/LoginPage.vue')
                }, {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/RegisterPage.vue')
                },{
                    path:'reset',
                    name: 'welcome-reset',
                    component: () => import('@/views/welcome/ResetPage.vue')
                }
            ]
        },{
            path:'/index',
            name:'首頁',
            // component:()=>import('@/layout/index2.vue'),
            component:()=>import('../layout/index.vue'),
            redirect:'/mainPage',
            children:[
                {
                    path: '/user',
                    name:'帳戶資訊',
                    component:()=>import('@/views/userCenter/index.vue')
                },
                {
                    path: '/mainPage',
                    name:'個人主頁',
                    component:()=>import('@/views/mainPage/index.vue')
                },
                {
                    path: '/mainPage2',
                    name:'個人測試',
                    component:()=>import('@/views/mainPage/indexTest.vue')
                },

            ]
        }
    ]
})
// 初始路由配置


console.log("創建router",router.options.routes)
console.log("創建router",router.getRoutes())
export default router