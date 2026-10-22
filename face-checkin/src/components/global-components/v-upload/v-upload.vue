<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-15 10:24:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 15:31:34
-->
<template>
  <div class="v-upload">
    <!--       @preview="handlePreview" -->
    <a-upload
      v-model:file-list="fileList"
      :disabled="currentDisabled"
      :accept="accept"
      :custom-request="uploadRequest"
      :before-upload="beforeUpload"
      :list-type="listType"
      :multiple="multiple"
      @remove="remove"
      @preview="handlePreview"
      @download="download"
    >
      <template v-if="fileList.length < count">
        <div v-if="!$slots.default">
          <!-- <plus-outlined /> -->
          <template v-if="listType==='text'">
            <a-button>
              <v-svg name="upload">
                上传
              </v-svg>
            </a-button>
          </template>
          <template v-else>
            <v-svg name="add-select" />
            <div class="ant-upload-text">
              点击上传
            </div>
          </template>
        </div>
        <slot v-else />
      </template>
    </a-upload>
    <p
      v-if="!disabled"
      class="form-tip"
    >
      {{ tip }}
    </p>
    <v-modal
      :visible="previewVisible"
      :footer="null"
      @cancel="handleCancel"
    >
      <img
        alt="example"
        style="width: 100%;"
        :src="previewImage"
      >
    </v-modal>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, onMounted, nextTick, watch, Ref, inject, computed,
} from 'vue';

import { message, Form } from 'ant-design-vue';
import { IMAGE_TYPE } from '@/constants/config';
import { upload, getPreviewUrl, getFilesDetails } from '~models/common';
import { FileItem, FileInfo, UploadParams } from './type';
import { useUpload } from './useUpload';
// 'text'|'picture'|'picture-card'
export default defineComponent({
  name: 'VUpload',
  components: {},
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    value: {
      type: String,
      default: '',
    },
    listType: {
      type: String as PropType<'text'|'picture'|'picture-card'>,
      default: 'picture-card',
    },
    accept: {
      type: String as PropType<'image/*'>,
      default: 'image/*',
    },
    size: {
      type: Number,
      default: 5,
    },
    count: {
      type: Number,
      default: 1,
    },
    tip: {
      type: String,
      default: '支持5M以内的jpg、bmp、png、gif格式文件',
    },
    multiple: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    'update:value': (key:string) => true,
    change: (key:string, names?:string) => true,
  },

  setup(props, { emit }) {
    const {
      size, value, listType, disabled, count, accept,
    } = toRefs(props);
    const previewVisible = ref<boolean>(false);
    const previewImage = ref<string | undefined>('');

    const { fileList, isSyncFileList } = useFileList(value, listType.value);
    const { uploadFile } = useUpload();

    // uploadFile
    const handleCancel = () => {
      previewVisible.value = false;
    };
    const handlePreview = async (file: FileItem) => {
      console.log(file, '----file');
      const { type, url, preview } = file;
      if (type && (type.indexOf('image/') !== -1 || IMAGE_TYPE.includes(type))) { // 图片预览
        if (!url && !preview) {
          file.preview = (await getBase64(file.originFileObj)) as string;
        }
        previewImage.value = file.url || file.preview;
        previewVisible.value = true;
        return;
      }
      // 文件预览
      let openUrl = url;
      if (!openUrl && file.response) {
        openUrl = getPreviewUrl(file.response);
      }
      if (openUrl) {
        window.open(openUrl, 'blank');
      }
    };

    const { onFieldChange } = Form.useInjectFormItemContext();
    const setValue = () => {
      const keys:string[] = [];
      const names:string[] = [];
      fileList.value.forEach((file) => {
        // console.log(file, '----file');
        const tempKey = file.key || file.response;
        if (tempKey) {
          keys.push(tempKey);
          names.push(file.name || '');
        }
      });
      emit('update:value', keys.join(','));
      emit('change', keys.join(','), names.join(','));
      onFieldChange();
    };
    const beforeUpload = (file: FileItem, UpFileList:any) => {
      // console.log(file, 'file');
      const valueType = file.name ? file.name : '';
      isSyncFileList.value = true; // 已经初始化完成不在

      const isLt2M = file.size as number / 1024 / 1024 < size.value;
      // return isLt2M;
      return new Promise((resolve, reject) => {
        if (value.value && !(value.value.split(',').length < count.value)) {
          message.warn('上传文件超过最大限制');
          return reject(false);
        }
        if (!isLt2M) {
          message.error(`最大不能超过${size.value}m!`);
          return reject(false);
        }
        const pos = valueType.lastIndexOf('.');
        const su = valueType.substring(pos + 1);
        const toUpper = su.toLowerCase();
        if (accept.value.indexOf(toUpper) === -1) {
          message.warn('不支持以当前类型的文件或图片上传!');
          UpFileList.forEach((item: any, index:any) => {
            if (item.name === file.name) {
              file.status = 'error';
            }
          });
          return reject(false);
        }
        return resolve(true);
      });
    };
    const uploadRequest = async (params:UploadParams) => {
      // console.log(params, '-----params');
      const { key } = await uploadFile(params);
      // params.file.key = key;
      // params.file.url = getPreviewUrl(key);
      setValue();
      // console.log(fileList.value, '-----');
    };
    const remove = async (file:FileItem) => {
      setTimeout(() => {
        setValue();
      });
      return true;
    };
    const download = async (file:FileItem) => {
      console.log(file, 'file');
    };
    const parentDisabled = inject<Ref<boolean>>('parentDisabled', ref(false));
    const currentDisabled = computed(() => disabled.value || parentDisabled?.value);
    // const change = async ({ file, fileList, event }:{file:FileItem, fileList:FileItem[], event:any}) => {
    //   debugger;
    //   console.log(file.response, '----file.response');
    //   file.url = 'http://localhost:9000/smartPark/park-common/file/imagePreview?key=dmMtdXBsb2FkLTE2NDczMzMwMDgxNTgtMl8xNjQ3MzMzMDQzOTE2';
    //   // nextTick(() => {
    //   //   if (file.response) {
    //   //     file.url = `${window.origin}${getPreviewUrl(file.response)}`;
    //   //     console.log(file, fileList, event);
    //   //   }
    //   // });
    // };

    return {
      previewVisible,
      previewImage,
      fileList,
      handlePreview,
      handleCancel,
      // handleChange,
      beforeUpload,
      uploadRequest,
      remove,
      download,
      currentDisabled,
    };
  },
});

function getBase64(file: File) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });
}

// 文件列表，用于回显
function useFileList(value:Ref<string>, listType:string) {
  const fileList = ref<FileItem[]>([]);
  const isSyncFileList = ref(false); // 是否继续同步fileList
  const initFileList = async () => {
    if (!value.value) return;
    const keys = value.value.split(',');
    if (listType === 'text') {
      const filesDetails = await getFilesDetails(keys);
      fileList.value = Object.entries(filesDetails).map(([key, file]) => (
        {
          uid: `init_${Date.now()}_${file.id}`,
          url: getPreviewUrl(key),
          key,
          type: file.fileSuffix,
          name: file.fileOriginalName,
        }
      ));
      return;
    }
    fileList.value = keys.map((key, index) => (
      {
        uid: `init_${Date.now()}_${index}`,
        url: getPreviewUrl(key),
        key,
      }
    ));
  };
  watch(value, (newValue) => {
    if (!isSyncFileList.value) {
      initFileList();
      // console.log('触发了-------');
    }
  }, {
    immediate: true,
    deep: true,
  });
  return {
    fileList,
    isSyncFileList,
    // initFileList,
  };
}
export * from './type';
</script>
<style lang="less" scoped>
:deep(.ant-upload-list-item-name) {
    color: var(--ant-primary-color);
    cursor: pointer;
}
</style>
