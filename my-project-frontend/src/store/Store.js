import {defineStore} from "pinia";


export const useMenuStore = defineStore("menuStore",{

    state: () => ({
        hasRoutes: false, // 判断是否已经动态处理过路由过
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
        SET_MENUS(state,menus){
            sessionStorage.setItem("menus",JSON.stringify(menus))
        },
        SET_USER(state,user){
            sessionStorage.setItem("user",JSON.stringify(user))
        },
        // SET_AUTH(state,authority){
        //     sessionStorage.setItem("authority",JSON.stringify(authority))
        // },
        // SET_ROUTES_STATE(state,hasRoutes){
        //     state.hasRoutes = hasRoutes
        // },
        // ADD_TABS:(state,tab)=>{
        //     if(state.editableTabs.findIndex(e=>e.name===tab.path)===-1){
        //         state.editableTabs.push({
        //             title: tab.name,
        //             name: tab.path
        //         })
        //     }
        //     state.editableTabsValue = tab.path
        // },
        // RESET_TABS:(state)=>{
        //     state.editableTabsValue = '/index'
        //     state.editableTabs = [
        //         {
        //             title: '首页',
        //             name: '/index'
        //         }
        //     ]
        // },
        // GET_TOKEN(){
        //     return sessionStorage.getItem("token")
        // },
        GET_MENUS(){
            return JSON.parse(sessionStorage.getItem("menus"))
        },
        // GET_USER(){
        //     return JSON.parse(sessionStorage.getItem("user"))
        // },
        // GET_AUTH(){
        //     return JSON.parse(sessionStorage.getItem("authority"))
        // },
        // HAS_AUTH(needAuth){
        //     const authObjList  = this.GET_AUTH();
        //     const authStrList = []
        //     authObjList.forEach(authObj=>authStrList.push(authObj.authority))
        //     /*console.log(authStrList)*/
        //     if(authStrList.includes(needAuth)){
        //         return true
        //     }
        //     return false
        // }
    },
})
console.log("丟出pinia");