import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import {useMenuStore} from "@/store/Store.js";
import {getUserInfo, unauthorized} from "@/net/index.js";

console.log("router index")
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
            component:()=>import('@/layout/index.vue'),
            children:[
                {
                    path: '/user',
                    name:'user-info',
                    component:()=>import('@/views/userCenter/index.vue')
                }
            ]
        }
    ]
})


router.beforeEach((to,from,next)=> {

    const store = useMenuStore()
    let isUnauthorized = unauthorized();//判斷用戶是否認證
    //判斷是否經過動態處理router
    const hasRoutes = store.$state.hasRoutes;
    const menus = store.GET_MENUS()
    console.log("态处理过路由过",store.$state.hasRoutes)
    if (!isUnauthorized) {
        console.log("用戶通過認證")
    } else {
        console.log("用戶沒通過認證")
    }

    const userInfo = isUnauthorized ? null : getUserInfo();//如果用戶認證則獲取用戶訊息

// 使用for...in循环遍历对象
    for (let key in userInfo) {
        if (userInfo.hasOwnProperty(key)) { // 确保key是对象自身的属性
            console.log("Key:", key, "Value:", userInfo[key]); // 打印键和值
        }
    }



    if(to.name.startsWith('welcome') && !isUnauthorized) {
        next('/index')
    } else if(to.fullPath.startsWith('/index') && isUnauthorized) {
        next('/')
    } else {
        next()
    }


    // if(!isUnauthorized) {
    //     if(!hasRoutes){
    //         //還未註冊動態路由
    //         bindRoute(menus)
    //     }
    //
    // }

    // if(!isUnauthorized){
    //     if(!hasRoutes){
    //         bindRoute(menus)
    //         store.SET_ROUTES_STATE(store.$state,true)
    //     }
    //     next()
    // }else{
    //     if(whiteList.includes(to.path)){
    //         next();
    //     }else{
    //         next("/login")
    //     }
    // }


})

const bindRoute = (menus)=>{
    const newRoutes = router.options.routes;
    menus.forEach(menu=>{
        if (menu.children) {
            menu.children.forEach(m=>{
                const route = menuToRoute(m,menu.name)
                if(route){
                    newRoutes[0].children.push(route)
                }
            })
        }
    })
    console.log('000')
    // 重新添加路由
    newRoutes.forEach(route=>{
        router.addRoute(route)
    })
}


   //  if(to.name.startsWith('welcome-')&&!isUnauthorized){
   //
   //      console.log("userInfo.role=",userInfo.role);
   //      if ((userInfo.role === 'admin' && to.path === '/admin') || (userInfo.role === 'user' && to.path === '/user')) {
   //          next();
   //      }
   //      else if(userInfo.role==='admin'){
   //         console.log("enter admin");
   //         next('/admin')
   //      } else if(userInfo.role==='user'){
   //         console.log("enter user");
   //         next('/user')
   //      } else{
   //         next('/index')
   //      }
   //
   // }
   //  //如果用戶嘗試訪問index頁面但未認證
   //  else if(to.fullPath.startsWith('/index') && isUnauthorized){
   //     next('/')
   //  }
   //  else if(to.fullPath.startsWith('/index')&& userInfo.role!=='index'){
   //      next(userInfo.role.toLowerCase() === 'admin' ? '/admin' : '/user');
   //  }
   //  //訪問需要認證的頁面
   //  else if(to.matched.some(record => record.meta.requiresAuth)){
   //     if(isUnauthorized){
   //         console.log("enter isUnauthorized");
   //         next('/')//未認證用戶重新定位到首頁
   //     } else if(to.meta.role && to.meta.role !== userInfo.role){
   //         console.log("enter 角色與頁面要求不匹配");
   //         // 用戶角色與頁面要求不匹配，根據角色重新分配
   //         next(userInfo.role.toLowerCase() === 'admin' ? '/admin' : '/user');
   //     }
   //     else{
   //         //用戶認證且符合身分要求，允許訪問
   //          next()
   //     }
   // }
   // else{
   //
   //     next()
   // }
   //})
export default router