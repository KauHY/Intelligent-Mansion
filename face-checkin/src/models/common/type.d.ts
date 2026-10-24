/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-08 14:19:38
 * @LastEditors: your name
 * @LastEditTime: 2022-03-16 12:55:10
 */
interface UploadReuqestParams {
    key:string,
    formData:FormData,
    onProgress?:(arg:any)=>void,
}

interface FileDetails {
  fileName: string,
  fileOriginalName: string,
  fileSize: string,
  fileSuffix: string,
  id: string,
  relativePath: string
}

export {
  UploadReuqestParams,
  FileDetails,
};
