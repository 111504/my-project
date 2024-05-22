import {defineStore} from "pinia";
import {ElMessage} from "element-plus";


export const useMenuStore = defineStore("menuStore",{

    state: () => ({
        hasRoutes: false, // 判斷路由是否處理過動態路由
        editableTabsValue: '/index',
        editableTabs: [
            {
                title: '首页',
                name: '/index'
            }
        ],
        rememberMeToken: ''
    }),
    actions:{
        SET_TOKEN(state,token){
            sessionStorage.setItem("token",token)
        },
        SET_MENUS(state,menuList){
            sessionStorage.setItem("menuList",JSON.stringify(menuList))
        },
        CLEAR_MENUS(){
            sessionStorage.removeItem("menuList")
            localStorage.removeItem("menuList")
        },
        CLEAR_TOKEN(){
            sessionStorage.removeItem("token")
            localStorage.removeItem("token")
        },
        CLEAR_AUTH(){
            sessionStorage.removeItem("authority")
            localStorage.removeItem("authority")
        },
        SET_USER(state,username){
            // 将用户对象转换为 JSON 字符串

            sessionStorage.setItem("user",JSON.stringify({username: username}))
        },
        SET_AUTH(state,authority){
            sessionStorage.setItem("authority",JSON.stringify(authority))

        },
        SET_AVATAR(state,avatar){
            sessionStorage.setItem("avatar",avatar)
        },
        SET_ROUTES_STATE(state,hasRoutes){

            state.hasRoutes = hasRoutes
         //   localStorage.setItem("state",state.hasRoutes)
        },
        ADD_TABS:(state,tab)=>{
            if(state.editableTabs.findIndex(e=>e.name===tab.path)===-1){
                state.editableTabs.push({
                    title: tab.name,
                    name: tab.path
                })
            }
            state.editableTabsValue = tab.path
        },
        RESET_TABS:(state)=>{
            state.editableTabsValue = '/index'
            state.editableTabs = [
                {
                    title: '首页',
                    name: '/index'
                }
            ]
        },
        GET_TOKEN(){
            let token=sessionStorage.getItem("token");
            if (!token) {
                token = localStorage.getItem("token");
            }
            return token
        },
        GET_MENUS(){
            let menus=sessionStorage.getItem("menuList");
            if(!menus){
                menus = localStorage.getItem("menuList");
            }
            return JSON.parse(menus)
        },
        GET_USER(){
            // 嘗試從 sessionStorage 獲取用戶數據
            let user = sessionStorage.getItem("user");

            // 如果在 sessionStorage 中找不到用戶數據，則從 localStorage 獲取
            if (!user) {
                user = localStorage.getItem("user");
            }


            return JSON.parse(user);
        },
        GET_USER_ID(){
            let id = sessionStorage.getItem("id");

            // 如果在 sessionStorage 中找不到用戶數據，則從 localStorage 獲取
            if (!id) {
                id = localStorage.getItem("id");
            }
            return (id);
        },
        GET_AUTH(){
            let authority=sessionStorage.getItem('authority');
            if (!authority) {
                authority = localStorage.getItem("authority");
            }
            return JSON.parse(authority)
        },
        GET_ROUTES_STATE(){
            return localStorage.getItem("state")
        },
        GET_AVATAR(){
            let avatar=sessionStorage.getItem('avatar');
            if (!avatar) {
                avatar = localStorage.getItem("avatar");
            }
            return (avatar)
        },
        HAS_AUTH(needAuth){
            const authObjList  = this.GET_AUTH();
            const authStrList = authObjList.split(',').map(permission => permission.trim());
            if(authStrList.includes(needAuth)){
                return true
            }
            else {
                ElMessage.warning("沒有權限")
                return false
            }
        }
    },
})
