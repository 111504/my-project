import axios from 'axios'
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {useMenuStore} from "@/store/Store.js";
console.log("net index")
const authItemName="access_token"

const defaultFailure=(message,code,url)=>{
   console.warn(`URL:${url},code:${code},錯誤訊息:${message}`)
    ElMessage.warning(message)
}

const defaultError=(err)=>{
    console.log(err)
    ElMessage.warning("錯誤")
}
function internalPost(url,data,header,success,failure,error=defaultError){
    console.log("enter internalPost data"+data.password+" "+data.username)
    axios.post(url,data,{headers:header}).then((response)=>{
        console.log(response.data)
        if(response.data.code===200){
            console.log("enter post"+response.data.data)
            success(response.data.data)
        }else{
            failure(response.data.message,response.data.code,url)
        }

    }).catch(err=>error(err))
}

function internalGet(url,header,success,failure,error=defaultError){
    axios.get(url,{headers:header}).then(response=>{
        console.log(response.data)
        if(response.data.code===200){
            console.log("enter get"+response.data.data)
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
        console.log("登入成功"+data.uuid+'\r\n'+data.token+'\n'+data.role)
        storeAccessToken(remember,data.token,data.tokenId,data.expire,data.uuid,data.role,data.username)
        ElMessage.success(`登入成功 歡迎${data.username}成功登入`)
        success(data)
    },failure)
}

function storeAccessToken(remember,token,tokenId,expire,uuid,role,username){
    console.log("有無勾選remember",remember)
    //如果store放在檔案最上面會報錯
    const store = useMenuStore()
    const authObj={token:token,tokenId:tokenId,expire:expire,uuid:uuid,role:role,username:username}
    const str=JSON.stringify(authObj)
    if(remember) {
        localStorage.setItem(authItemName, str)
         store.SET_TOKEN(store.$state, token)
       // store.SET_MENUS(store.$state, resultObj.menus)
         store.SET_USER(store.$state, username)


    }
    else {
        sessionStorage.setItem(authItemName, str);
         store.SET_TOKEN(store.$state, token)
        // store.SET_MENUS(store.$state, resultObj.menus)
         store.SET_USER(store.$state, username)
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

        authObj.role=authObj.role.toLowerCase();
        return authObj; // 返回包含令牌和角色的对象
    } catch (error) {
        console.error('Parsing error on getting user info from storage', error);
        return null;
    }
}

function checkTokenEnable() {
    console.log("檢查使用者token是否被禁用")
    const token = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName);
    if (!token) return null;
    try {
        const authObj = JSON.parse(token);
        const expireDate = new Date(authObj.expire);
        const now = new Date();
        console.log("現在時間",now,expireDate)
        if (expireDate <= now) {
            console.log("token過期",now)
            deleteAccessToken(); // 如果令牌过期，删除它
            return null;
        }
//提取uuid
        let tokenId=authObj.tokenId;
        console.log("tokenId=", tokenId);
        let url='/api/auth/validateToken';
        axios.get(url, {
            headers: {
                'Authorization': `Bearer ${authObj.token}`,
                'JwtTokenId':tokenId
            }
        }).then(response=>{
            console.log("驗證token"+response.data)
            if(response.data.code===200){

                if(response.data.data==="TOKEN_DISABLE"){
                    console.log(response.data.data)
                    console.log(response.data.message)
                    deleteAccessToken();

                    router.push('/')
                }
                else{
                    console.log(response.data.data)
                    console.log(response.data.message)
                }

            }
            else{
                failure(response.data.message,response.data.code,url)
            }
        }).catch(err=>defaultError(err));

    } catch (error) {
        console.error('Parsing error on getting user info from storage', error);
        return null;
    }

}




export {login,logout,get,post,unauthorized,getUserInfo,checkTokenEnable}