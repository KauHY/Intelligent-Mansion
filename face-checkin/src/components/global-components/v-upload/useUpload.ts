/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-24 10:52:25
 * @LastEditors: your name
 * @LastEditTime: 2022-03-15 21:43:58
 */
import { upload, getPreviewUrl } from '~models/common';
import { UploadParams } from './type';

export const useUpload = () => {
  const uploadFile = async ({
    file,
    onSuccess,
    onProgress,
    onError,
  }:UploadParams) => {
    const data = new FormData();
    const res = {
      key: '',
      url: '',
      fullUrl: '',
    };
    data.append('file', file as any);
    try {
      res.key = await upload({
        key: btoa(`${file.uid}_${Date.now()}`),
        formData: data,
        onProgress(e) {
          if (e.total > 0) {
            e.percent = (e.loaded / e.total) * 100;
          }
          if (onProgress) onProgress(e);
        },
      });
      if (onSuccess) onSuccess(res.key, file);
      res.url = getPreviewUrl(res.key);
      res.fullUrl = `${window.origin}${res.url}`;
    } catch (error) {
      if (onError) onError(error);
    }
    return res;
  };
  return {
    uploadFile,
  };
};
