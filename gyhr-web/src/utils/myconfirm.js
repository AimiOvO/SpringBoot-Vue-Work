import { MessageBox, Message } from "element-ui";
// 信息确认弹框封装
export default async function myconfirm(text) {
    return await new Promise((resolve, reject) => {
        MessageBox.confirm(text, '系统提示', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            resolve(true);
        }).catch(() => {
            Message.warning('已取消');
            reject(false);
        });
    });
}