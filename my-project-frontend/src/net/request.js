import axios from "axios";

// axios.defaults.baseURL="http://localhost:8080"
const apiUrl=import.meta.env.VITE_APP_API_URL
// 创建axios實例
// const httpService = axios.create({
//     // url前缀-'http:xxx.xxx'
//     baseURL: baseURL, // API 的基础URL
//     // 请求超时时间
//     timeout: 3000  // 请求超时时间，单位是毫秒
// });
/*
 *  文件上传
 *  url:请求地址
 *  params:参数
 * */
// export function fileUpload(url, params = {}) {
//     return new Promise((resolve, reject) => {
//         httpService({
//             url: url,
//             method: 'post',
//             data: params,
//             headers: { 'Content-Type': 'multipart/form-data' }
//         }).then(response => {
//             resolve(response);
//         }).catch(error => {
//             reject(error);
//         });
//     });
// }

 function getServerUrl(){
    return apiUrl;
}

export  {

    getServerUrl
}
