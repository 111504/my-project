import axios from 'axios'
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {useMenuStore} from "@/store/Store.js";

const authItemName="access_token"


console.log("當前時區",Intl.DateTimeFormat().resolvedOptions().timeZone)

const defaultFailure=(message,code,url)=>{
   console.warn(`URL:${url},code:${code},錯誤訊息:${message}`)
    ElMessage.warning(message)
}

const defaultError=(err)=>{
    console.log(err)
    ElMessage.warning("錯誤")
}
function internalPost(url,data,header,success,failure,error=defaultError){
  //  console.log("enter internalPost data"+data.password+" "+data.username)
    axios.post(url,data,{headers:header}).then((response)=>{
      //  console.log("response.data internalPost",response.data)
        if(response.data.code===200){
      //      console.log("enter post"+response.data.data)
            success(response.data.data)
        }else{
            failure(response.data.message,response.data.code,url)
        }

    }).catch(err=>error(err))
}

function internalGet(url,header,success,failure,error=defaultError){
    axios.get(url,{headers:header}).then(response=>{
   //     console.log(response.data)
        if(response.data.code===200){
        //    console.log("enter get"+response.data.data)
            success(response.data.data)
        }else{
            failure(response.data.message,response.data.code,url)
        }
    }).catch(err=>error(err))
}


function  get(url,success,failure=defaultFailure){

    internalGet(url,accessHeader(),success,failure)
}




function  post(url,data,success,failure=defaultFailure){
    internalPost(url,data,accessHeader(),success,failure)
}


function  login(username,password,remember,success,failure=defaultFailure){
    internalPost(
        '/api/auth/login', {
        username:username,
        password:password,
    },{
        'Content-Type':'application/x-www-form-urlencoded'
    },(data)=>{

        console.log("登入成功"+data.uuid+'\r\n'+data.token+'\n'+data.authorization)
        storeAccessToken(remember,data.token,data.tokenId,data.expire,data.uuid,data.username,data.authorization,data.phoneNumber,data.email,data.role,data.loginDate,data.id)
        ElMessage.success(`登入成功 歡迎${data.username}成功登入`)
        success(data)
    },failure)
}

function storeAccessToken(remember,token,tokenId,expire,uuid,username,authorization,phoneNumber,email,role,loginDate,id){
    console.log("有無勾選remember",remember)
    //如果store放在檔案最上面會報錯
    const store = useMenuStore()
    const authObj={token:token,tokenId:tokenId,expire:expire,uuid:uuid,username:username,authorization:authorization,phoneNumber:phoneNumber,email:email,role:role,loginDate:loginDate}
    const str=JSON.stringify(authObj)
    if(remember) {
        localStorage.setItem(authItemName, str)

        localStorage.setItem("userId",id);
        localStorage.setItem("user",username);
        localStorage.setItem("authority",JSON.stringify(authorization))
         store.SET_TOKEN(store.$state, token)
         store.SET_USER(store.$state, username)
         store.SET_AUTH(store.$state,authorization)

    }
    else {
        sessionStorage.setItem(authItemName, str);
         store.SET_TOKEN(store.$state, token)
         store.SET_USER(store.$state, username)
        store.SET_AUTH(store.$state,authorization)
    }
}

function takeAccessToken(){
    const token=localStorage.getItem(authItemName)||sessionStorage.getItem(authItemName)
    if(!token)
        return null;
    const authObj=JSON.parse(token)
    const expireDate = new Date(authObj.expire);
    const now = new Date();
    if(expireDate <= now){
        console.log("token過期",now)
        deleteAccessToken();
        ElMessage.warning('登入時間過期，請重新登入')
        return null
    }
    return  authObj.token;
}

function deleteAccessToken(){
    const store = useMenuStore()
    console.log("刪除token 清空菜單")
    store.CLEAR_MENUS()
    store.CLEAR_TOKEN()
    store.CLEAR_AUTH()
    localStorage.removeItem(authItemName);
    sessionStorage.removeItem(authItemName);

}

function logout(success,failure=defaultFailure){
    get('/api/auth/logout',()=>{
      deleteAccessToken()

      ElMessage.success('退出登入成功')
        success()
    },failure)
}

function accessHeader(){

    const token=takeAccessToken();
    if(token){
        console.log("token===",token)
    }

    return token ? { 'Authorization':`Bearer ${takeAccessToken()}` } :{}
}


function unauthorized(){
    return  !takeAccessToken()
}

function getUserInfo() {
    const token = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName);
    if (!token)
        return null;
    try {
        const authObj = JSON.parse(token);
        const expireDate = new Date(authObj.expire);
        const now = new Date();
        if(expireDate <= now){
            console.log("token過期",now)
            deleteAccessToken();
            ElMessage.warning('登入時間過期，請重新登入')
            return null
        }

        // authObj.role=authObj.role.toLowerCase();
        // return authObj; // 返回包含令牌和角色的对象
    } catch (error) {
        console.error('Parsing error on getting user info from storage', error);
        return null;
    }
}

function checkTokenEnable() {
    console.log("檢查使用者token是否被禁用")
    const store = useMenuStore()
    const token = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName);
    if (!token) return null;
    try {
        const authObj = JSON.parse(token);
        const expireDate = new Date(authObj.expire);
        const now = new Date();
     //   console.log("現在時間",now,expireDate)
        if (expireDate <= now) {
      //      console.log("token過期",now)
            deleteAccessToken(); // 如果令牌过期，删除它
            store.CLEAR_MENUS();
            router.push('/').then(r => {
                console.log("由於TOKEN過期，跳轉頁面")
                ElMessage.warning('由於TOKEN過期，跳轉頁面')
            })
            return null;
        }
//提取uuid
        let tokenId=authObj.tokenId;
     //   console.log("tokenId=", tokenId);
        let url='/api/auth/validateToken';
        axios.get(url, {
            headers: {
                'Authorization': `Bearer ${authObj.token}`,
                'JwtTokenId':tokenId
            }
        }).then(response=>{
        //    console.log("驗證token"+response.data)
            if(response.data.code===200){

                if(response.data.data==="TOKEN_DISABLE"){
                    console.log("token失效")
       //             console.log(response.data.data)
        //            console.log(response.data.message)
                    deleteAccessToken();
                    store.CLEAR_MENUS();
                    router.push('/').then(r => {
                        console.log("由於TOKEN失效，跳轉頁面")
                        ElMessage.warning('由於TOKEN失效，跳轉頁面')
                    })
                }
                else{
                    console.log(response.data.data)
                    console.log(response.data.message)
                }

            }
            else{
                defaultFailure(response.data.message,response.data.code,url)
            }
        }).catch(err=>defaultError(err));

    } catch (error) {
        console.error('Parsing error on getting user info from storage', error);
        return null;
    }
    console.log("檢查使用者token是否被禁用結束")
}


function requestUsersInformation(url,remember,error=defaultError) {
    const store = useMenuStore()
    let token;
    if(remember){
         token = localStorage.getItem(authItemName)
    }else{
         token = sessionStorage.getItem(authItemName)
    }

    const authObj = JSON.parse(token);
    const userUuid=authObj.uuid;
    // 添加userUuid到URL的查询参数
    const params = new URLSearchParams({ userUuid }).toString();
    const urlWithParams = `${url}?${params}`;
    axios.get(urlWithParams).then((response) => {

        if(response.data.code===200){
            console.log("使用者相關資訊"+JSON.stringify(response.data.data))
            let menulist=response.data.data;
            if(!menulist){
                ElMessage.warning("錯誤!菜單為空")
            }
            if(remember){
                localStorage.setItem("menuList",JSON.stringify(menulist))
            }
            store.SET_MENUS(store.$state, menulist)
            console.log("儲存菜單資料")
            store.SET_ROUTES_STATE(store.$state, false)
            console.log("重置動態菜單flag")
            //請求完資料後，才執行頁面跳轉，而不是放在登入成功後就執行
            router.push('/index').then(r => {
                console.log("執行頁面跳轉")
            });
        }else{
            defaultFailure(response.data.message,response.data.code,url)
        }
    }).catch(err=>error(err))
}


export {login,logout,get,post,unauthorized,getUserInfo,checkTokenEnable,requestUsersInformation,accessHeader}