/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-15 10:33:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-13 16:47:48
 */
interface FileItem {
    uid: string;
    name?: string;
    status?: string;
    response?: string;
    percent?: number;
    type?: string;
    url?: string;
    preview?: string;
    size?: number;
    originFileObj?: any;
    key:string, // 文件key
}

interface FileInfo {
    file: FileItem;
    fileList: FileItem[];
}

interface UploadParams {
  file: FileItem,
  onSuccess?: (...ary:any)=>void,
  onError?: (...ary:any)=>void,
  onProgress?:(e:any)=>void,
}

export {
  FileItem,
  FileInfo,
  UploadParams,
};
