import axios from '~utils/axios';
import { getPreviewUrl, getFileUrl } from '~models/common';
import { UploadReuqestParams } from '~models/common/type';
import { UploadParams } from '~components/global-components/v-upload/type';
// import   from '~models/asset-mangement';
import PatrolInspection from '~models/asset-mangement';

export const upload = async ({
  uploadUrl, key, formData, onProgress,
}:UploadReuqestParams & { uploadUrl: string }) => {
  const { data } = await axios({
    url: `${uploadUrl}?key=${key}`,
    data: formData,
    onUploadProgress: onProgress,
  });
  return data as string;
};

export const useUpload = () => {
  const uploadFile = async ({
    file,
    onSuccess,
    onProgress,
    onError,
    uploadUrl,
    parkOrgId,
  }:(UploadParams & { uploadUrl: string, parkOrgId: string })) => {
    const data = new FormData();
    // const res = {
    //   key: '',
    //   url: '',
    //   fullUrl: '',
    // };
    let res = null;
    data.append('file', file as any);
    data.append('parkOrgId', parkOrgId as any);
    try {
      res = await upload({
        uploadUrl,
        key: btoa(`${file.uid}_${Date.now()}`),
        formData: data,
        onProgress(e) {
          if (e.total > 0) {
            e.percent = (e.loaded / e.total) * 100;
          }
          if (onProgress) onProgress(e);
        },
      });
      if (onSuccess) onSuccess(res);
      // res.url = getPreviewUrl(res.key);
      // res.fullUrl = `${window.origin}${res.url}`;
    } catch (error) {
      if (onError) onError(error);
    }
    return res;
  };
  return {
    uploadFile,
  };
};
