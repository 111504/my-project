import {useMenuStore} from "@/store/Store.js";
import {getUserInfo, unauthorized} from "@/net/index.js";
import router from "@/router/index.js";
const modules = import.meta.glob('../views/**/*.vue');
//console.log(modules)

console.log("進入permisiions",router.options.routes)
// 动态导入 /src/views 下的所有 Vue 组件

//pinia變數不能放置在函數外 會報錯
//let store = useMenuStore()
router.beforeEach(async (to, from, next) => {
    console.log("進入router.beforeEach")
    const whiteList = ['/register', '/reset', '/', '']
    let store = useMenuStore()
    let isUnauthorized = unauthorized();//判斷用戶是否認證
    //判斷是否經過動態處理router 注意該變數狀態再經過頁面刷新後，狀態會初始化
    let hasRoutes = store.$state.hasRoutes
    const menus = store.GET_MENUS()
    if(menus==null) {
      //  hasRoutes = true
        console.log("菜單為空")
    }
    console.log("态处理过路由过", hasRoutes)
    // if (!isUnauthorized) {
    //     console.log("用戶通過認證")
    // } else {
    //     console.log("用戶沒通過認證")
    //
    // }

    const userInfo = isUnauthorized ? null : getUserInfo();//如果用戶認證則獲取用戶訊息
        if (isUnauthorized) {
         //   console.log("用戶未通過認證")
            // 如果用户未授权，并且尝试访问的路由不在白名单中，重定向到登录页
            if (!whiteList.includes(to.path)) {
         //       console.log("用戶未通過認證，並且訪問的頁面需要權限")
                next("/");
                return; // 提前返回，避免执行下面的代码
            }
            // 对于未授权访问白名单路由的情况，直接放行
      //      console.log("用戶未通過認證，並且訪問的頁面不需要權限")
            next();
            return;
        }

// 处理已认证用户
    if (!hasRoutes) {
        if(menus==null) {
            //  hasRoutes = true
       //     console.log("菜單為空不能創建動態路由")
        }
        else{
     //       console.log("菜單不為空能創建動態路由")
            bindRoute(menus);
            store.SET_ROUTES_STATE(store.$state, true);
            next({...to, replace: true})
        }

    }
  //  console.log("用戶已經通過認證了，訪問index")
    if(to.path === '/index'){
   //     console.log("用戶已經通過認證了，直接去主業")
        next()
        return;
    }


    if (whiteList.includes(to.path)) {
        // 如果已认证用户访问白名单路由，重定向到首页
  //      console.log("白名單但是用戶已經通過認證了");
        next("/index");
        return;
    }

// 对于其他情况，直接放行
 //   console.log("用戶已經通過認證了，訪問需要權限的路由 要去",to.path);
//    console.log("訪問需要權限的路由，先來印出路由看看",router.options.routes)
  //  console.log("訪問需要權限的路由，先來印出路由看看",router.getRoutes())
    next();



})

function menuToRoute(menu, parentName) {
    if(!menu.component){
        return null;
    }else{
        const route = {

            path: menu.path,
            name: menu.name,

            meta:{
                parentName:parentName
            }

        }
        //經過測試在這裡不行使用@來替代/src
        const componentPath = `../views/${menu.component}.vue`;
      //  console.log("componentPath",componentPath)
        const componentVue=modules[componentPath];
     //   console.log("componentVue",componentVue)
        route.component = modules[componentPath];

        //使用import 貌似是使用懶加載
        //  route.component = () => import(`../views/${menu.component}.vue`);

        // console.log("產生route.component",route)
        return  route;
    }
}

function bindRoute (menus){
 //   console.log("進入bindRoute")
    const newRoutes = router.options.routes;
  //  console.log("進入bindRoute,newRoutes=",newRoutes)
    menus.forEach(menu=>{
        if (menu.children) {
            menu.children.forEach(m=>{
                const Route = menuToRoute(m,menu.name)
       //         console.log("產生",Route)
                if(Route){
                    newRoutes[1].children.push(Route)
                }
            })
        }
    //    console.log("component",menu.name)
    })

    // 重新添加路由
    newRoutes.forEach(route=>{
        router.addRoute(route)
    })

    console.log("動態路由創建完成",router.getRoutes())
    console.log("結束bindRoute")
}

