import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import {getUserInfo, unauthorized} from "@/net/index.js";

const router=createRouter({
    history:createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path:'/',
            name:'welcome',
            component:()=>import('@/views/WelcomeView.vue'),
            children:[
                {
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
            name:'index',
            component:()=>import('@/views/indexView.vue')
        },
        {
            path: '/admin',
            name: 'AdminHome',
            component: ()=>import('@/views/adminView.vue'),
            meta: { requiresAuth: true, role: 'admin' }
        },
        {
            path: '/user',
            name: 'UserHome',
            component: ()=>import('@/views/userView.vue'),
            meta: { requiresAuth: true, role: 'user' }
        }

    ]
})


router.beforeEach((to,from,next)=>{
   const useInfo=getUserInfo();
   console.log(useInfo);
    const isUnauthorized=unauthorized();
   if(to.name.startsWith('welcome-')&&!isUnauthorized){
       const redirectTo=useInfo.role;
       console.log(redirectTo);
       if(redirectTo==='admin'){
           console.log("enter admin");
           next('/admin')
       }
       if(redirectTo==='user'){
           console.log("enter user");
           next('/user')
       }
       else{
           next('/index')
       }

   }else if(to.fullPath.startsWith('/index')&&isUnauthorized){
       next('/')
   }else if(to.matched.some(record => record.meta.requiresAuth)){
       if(isUnauthorized){
           next('/login')
       }
       else if(to.meta.role&&to.meta.role!=useInfo.role){

           const redirectTo = userInfo.role === 'admin' ? '/admin' : '/user';
           next(redirectTo);
       }
       else{
            next()
       }
   }
   else{
       next()
   }
})
export default router