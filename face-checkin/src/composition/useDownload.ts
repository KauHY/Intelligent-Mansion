import { message } from 'ant-design-vue';

/*
 * @Description:文件下载
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 19:54:44
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-30 10:38:57
 */
const getFileName = (contentDisposition:string) => {
  const fields = contentDisposition.split(';');
  const fileNameStr = fields.find((str) => str.includes('filename'));
  const fileName = fileNameStr?.split('=')[1];
  return decodeURIComponent(fileName || '');
};

export const useDownload = async (data:DownloadReturnData) => {
  // 构造一个blob对象来处理数据
  // const blob = new Blob([content])
  const { data: content, contentDisposition } = data;
  const fileName = getFileName(contentDisposition);
  if (!fileName) {
    message.warning('文件名称有误，下载失败');
    return;
  }
  const blob = new Blob([content]);
  console.log(content);
  if ('download' in document.createElement('a')) {
    // 支持a标签download的浏览器
    const link = document.createElement('a'); // 创建a标签
    link.download = fileName; // a标签添加属性
    link.style.display = 'none';
    link.href = URL.createObjectURL(blob);
    document.body.appendChild(link);
    link.click(); // 执行下载
    URL.revokeObjectURL(link.href); // 释放url
    document.body.removeChild(link); // 释放标签
  } else {
    // 其他浏览器
    (window.navigator as any).msSaveBlob(blob, fileName);
  }
};
