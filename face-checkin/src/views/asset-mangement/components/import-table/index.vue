<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 15:25:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-29 12:12:06
-->
<template>
  <div>
    <v-modal
      v-model:visible="currentVisible"
      title="导入"
      :width="780"
      class="operate-dialog"
      @ok="confirm"
      @cancel="resetFields();emit('operate:success')"
    >
      <div class="add-user">
        <a-form
          :model="modelRef"
          :label-col="{span:5}"
        >
          <a-form-item
            label="所属园区"
            v-bind="validateInfos.parkOrgId"
          >
            <v-select
              v-model:value="modelRef.parkOrgId"
              placeholder="请选择所属园区"
            >
              <a-select-option
                v-for="opt in parkList"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
          <a-form-item
            label="选择导入文件"
            v-bind="validateInfos.file"
          >
            <a-upload
              name="file"
              :file-list="fileList"
              :remove="handleRemove"
              :before-upload="beforeUpload"
              accept="application/msword,.xls,.xlsx"
            >
              <a-button> <a-icon type="upload" /> 选择文件 </a-button>
            </a-upload>
          </a-form-item>
          <a-form-item
            label=""
          >
            <span>注意：请先<span
              style="color: #f2a455; cursor: pointer;"
              @click="downloadTemplate"
            >下载模板文件</span>，然后根据模板文件的填写说明，录入数据后提交上传</span>
          </a-form-item>
        </a-form>
      </div>
    </v-modal>
    <v-modal
      v-model:visible="importResultVisible"
      :width="500"
      class="operate-dialog"
      @cancel="importResultVisible = false"
    >
      <div class="import-result">
        已成功导入<span>{{ importResult.successTotal }}</span>条数据，导入失败<span>{{ importResult.failTotal }}</span>条数据
      </div>
      <template #footer>
        <div>
          <a-button @click="importResultVisible=false">
            关闭
          </a-button>
          <a-button
            v-if="importResult.failTotal"
            type="primary"
            @click="downloadFailResult"
          >
            下载失败记录及原因
          </a-button>
        </div>
      </template>
    </v-modal>
  </div>
</template>
<script lang='ts'>
import {
  defineComponent, toRefs, computed, onMounted, ref, watch,
} from 'vue';
import { message } from 'ant-design-vue';
import Park from '~models/park';
import PatrolInspection from '~models/asset-mangement';
import { UploadParams } from '~components/global-components/v-upload/type';
import { useDownload } from '~composition/useDownload';
import { useAddPatrolPlace } from './useFormConf';
import { useUpload } from './upload';

export default defineComponent({
  name: '',
  components: {},
  props: {
    defaultFormData: {
      type: Object as any,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    importUrl: {
      type: String,
      default: '',
    },
    templateKey: {
      type: String,
      default: '',
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const {
      visible, defaultFormData, importUrl, templateKey,
    } = toRefs(props);
    const {
      modelRef, validateInfos, fill, validate, resetFields,
    } = useAddPatrolPlace();

    const { uploadFile } = useUpload();

    const parkList = ref<SelectOptions>();
    const importResult = ref({
      failTotal: 0,
      successTotal: 0,
      fileKey: '',
    });
    const fileList = computed(() => (modelRef.file ? [modelRef.file] : []));
    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
        if (show) resetFields();
      },
      get() {
        return visible.value;
      },
    });

    const importResultVisible = ref(false);

    const beforeUpload = (file:UploadParams & {name: string}) => {
      const fileType = file?.name.slice(file?.name.lastIndexOf('.')).toLowerCase();
      if (!['.xls', '.xlsx'].includes(fileType)) {
        message.info('只能上传.XLS.XLSX格式的文件!');
        return false;
      }
      modelRef.file = file;
      return false;
    };
    const handleRemove = () => {
      modelRef.file = null;
    };

    const downloadFailResult = async () => {
      const data = await patrolInspectionInessInstance.downloadFailFile(importResult.value.fileKey);
      useDownload(data);
      importResultVisible.value = false;
    };

    const confirm = async () => {
      try {
        await validate();
        importResultVisible.value = true;
        const res: any = await uploadFile({
          file: modelRef.file,
          uploadUrl: importUrl.value,
          parkOrgId: modelRef.parkOrgId,
        });
        if (!res || (!res.failTotal && !res.successTotal)) return;
        importResult.value.failTotal = res.failTotal || 0;
        importResult.value.successTotal = res.successTotal || 0;
        importResult.value.fileKey = res.fileKey || '';
        emit('operate:success');
        resetFields();
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };

    const downloadTemplate = () => {
      console.log('下载模板');
      window.location.href = `${window.location.origin}/smartPark/park-common/file/download?key=${templateKey.value}`;
    };

    const init = async () => {
      parkList.value = await new Park().searchParkListName();
      fill(defaultFormData.value);
    };

    onMounted(() => {
      init();
    });
    return {
      modelRef,
      validateInfos,
      currentVisible,
      parkList,
      confirm,
      fileList,
      beforeUpload,
      handleRemove,
      importResultVisible,
      downloadFailResult,
      importResult,
      emit,
      downloadTemplate,
      resetFields,
      // roleIds,
      // organizationOpts,
      // onLoadData,
    };
  },
});
</script>
<style lang="less" scoped>
.import-result {
    span {
        color: var(--ant-primary-color);
    }
}
</style>
