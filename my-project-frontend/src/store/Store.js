import {defineStore} from "pinia";


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
        SET_USER(state,username){
            // 将用户对象转换为 JSON 字符串

            sessionStorage.setItem("user",JSON.stringify({username: username}))
        },
        SET_AUTH(state,authority){
            sessionStorage.setItem("authority",JSON.stringify(authority))
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
            return sessionStorage.getItem("token")
        },
        GET_MENUS(){
            return JSON.parse(sessionStorage.getItem("menuList"))
        },
        GET_USER(){
            return  JSON.parse(sessionStorage.getItem("user"))
        },
        GET_AUTH(){
            return JSON.parse(sessionStorage.getItem("authority"))
        },
        GET_ROUTES_STATE(){
            return localStorage.getItem("state")
        },
        HAS_AUTH(needAuth){
            const authObjList  = this.GET_AUTH();
            // const authStrList = []
            // authObjList.forEach(authObj=>authStrList.push(authObj.authority))
            const authStrList = authObjList.split(',').map(permission => permission.trim());
          //  console.log(authStrList)
            if(authStrList.includes(needAuth)){
                return true
            }
            return false
        }
    },
})
