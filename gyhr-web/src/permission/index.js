//判断按钮权限
export default function hasPermission(code) {
    let tag = false;
    // 获取用户当前的所有权限
    let codeList = JSON.parse(sessionStorage.getItem('codeList'))
    if (codeList==null) {
        return tag;
    }
    for (let i = 0; i < codeList.length; i++) {
        if (codeList[i] === code) {
            tag = true;
            break;
        }
    }
    return tag;
}