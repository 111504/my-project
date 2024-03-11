import axios from 'axios'
import {ElMessage} from "element-plus";
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
    axios.post(url,data,{headers:header}).then(({data})=>{
        if(data.code==200){
            success(data.data)
        }else{
            failure(data.message,data.code,url)
        }
    }).catch(err=>error(err))
}

function internalGet(url,header,success,failure,error=defaultError){
    axios.get(url,{headers:header}).then(response=>{
        console.log(response)
        if(response.data.code===200) {

            success(response.data)
        }
        else{
            failure(response.message,response.code,url)
        }
    }).catch(err=>error(err))
}


function  get(url,success,failure=defaultFailure){
    console.log("accessHeader return ",accessHeader())
    internalGet(url,accessHeader(),success,failure)
}

function  post(url,data,success,failure=defaultFailure){

        internalPost(url,data,accessHeader(),success,failure)




}


function  login(username,password,remember,success,failure=defaultFailure){
    internalPost('/api/auth/login',{
        username:username,
        password:password,
    },{
        'Content-Type':'application/x-www-form-urlencoded'
    },(data)=>{
        console.log(data.uuid+'\r\n'+data.token+'\n'+data.role)
        storeAccessToken(remember,data.token,data.expire,data.uuid,data.role)
        ElMessage.success(`登入成功 歡迎${data.username}成功登入`)
        success(data)
    },failure)
}

function storeAccessToken(remember,token,expire,uuid,role){
    console.log("有無勾選remember",remember)
    const authObj={token:token,expire:expire,uuid:uuid,role:role}
    const str=JSON.stringify(authObj)
    if(remember)
        localStorage.setItem(authItemName,str)
    else
        sessionStorage.setItem(authItemName,str);
}

function takeAccessToken(){
    const str=localStorage.getItem(authItemName)||sessionStorage.getItem(authItemName)
    if(!str)
        return null;
    const authObj=JSON.parse(str)
    if(authObj.expire<=new Date()){
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

function logout(success,failure=defaultFailure()){
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
    if (!token) return null;
    try {
        const authObj = JSON.parse(token);
        const now = new Date().getTime();
        if (authObj.expire <= now) {
            deleteAccessToken(); // 如果令牌过期，删除它
            return null;
        }
        return authObj; // 返回包含令牌和角色的对象
    } catch (error) {
        console.error('Parsing error on getting user info from storage', error);
        return null;
    }
}






export {login,logout,get,post,unauthorized,getUserInfo}