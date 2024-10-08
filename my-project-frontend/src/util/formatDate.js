//方法一
export   function formatDateFromString(dateStr) {
    var date = new Date(dateStr); // 直接將日期字符串轉為 Date 對象
    var Y = date.getFullYear() + "-";
    var M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "-";
    var D = (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + " ";
    var h = (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
    var m = (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) + ":";
    var s = (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
    return Y + M + D + h + m + s;   // 返回格式化的日期時間字符串
}