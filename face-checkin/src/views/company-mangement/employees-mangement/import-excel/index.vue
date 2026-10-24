<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-13 16:59:28
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <v-modal
    class="import-excel"
    :visible="true"
    :width="730"
    :title="importExcelConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-spin
        tip="上传中，请稍等..."
        :spinning="isUpoad"
      >
        <a-form
          ref="formRef"
          :model="modelRef"
          :rules="rulesRef"
          :label-col="{ span: 5 }"
        >
          <a-form-item
            v-if="modelRef.time"
            label="所属园区"
            name="parkOrgId"
          >
            <v-select
              v-model:value="modelRef.parkOrgId"
              placeholder="请选择所属园区"
              filter-option
              @change="parkOrgIdChange"
            >
              <a-select-option
                v-for="opt in parkOpts"
                :key="opt.label"
                :value="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
          <a-form-item
            v-if="modelRef.time"
            label="所属企业"
            name="companyId"
          >
            <v-select
              v-model:value="modelRef.companyId"
              placeholder="请选择企业"
              filter-option
            >
              <a-select-option
                v-for="opt in companyOpts"
                :key="opt.label"
                :value="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
          <a-form-item
            label="选择导入文件"
            name="file"
          >
            <a-upload
              v-model:value="modelRef.file"
              name="file"
              :file-list="fileList"
              :remove="handleRemove"
              :before-upload="beforeUpload"
              :on-change="onChange"
              :size="1024"
              :accept="importExcelConfig.isZip? '.zip,.rar' : accept"
            >
              <a-button> 选择文件 </a-button>
            </a-upload>
          </a-form-item>
          <a-form-item
            v-if="templateKey"
            label="模板文件"
          >
            <span>注意：请先<span
              style="color: #f2a455; cursor: pointer;"
              @click="downloadTemplate"
            >下载模板文件</span>，然后根据模板文件的填写说明，录入数据后提交上传</span>
          </a-form-item>
          <slot name="tips" />
          <a-form-item
            v-show="importResult.failTotal && importResult.failTotal > 0"
            label="导入结果"
          >
            <div class="import-result">
              <div>
                已成功导入<span>{{ importResult.successTotal }}</span>条数据，导入失败<span>{{ importResult.failTotal }}</span>条数据
              </div>
              <div>
                <a-button
                  type="primary"
                  @click="downloadFailResult()"
                >
                  下载失败记录及原因
                </a-button>
              </div>
            </div>
          </a-form-item>
        </a-form>
      </a-spin>
    </div>
    <template #footer>
      <a-button
        type="primary"
        @click="emit('update:show', false)"
      >
        取消
      </a-button>
      <a-button
        v-if="modelRef.time"
        type="primary"
        @click="confirm"
      >
        确认
      </a-button>
      <a-button
        v-if="!modelRef.time"
        type="primary"
        @click="uploadFilesButton"
      >
        上传
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import PatrolInspection from '~models/asset-mangement';
import Park from '~models/park';
import { UploadParams } from '~components/global-components/v-upload/type';
import { useDownload } from '~composition/useDownload';
import CarPass, { CarInfo } from '~models/car-pass';
import CompanyMangement, { EmployeeInfo } from '~models/company-mangement';
import { ImportExcelConfig, ImportResultKind } from './type';
import { importExcelForm } from './useForm';

export * from './type';
const carPassInstance = new CarPass();
const companyMangementInstance = new CompanyMangement();
export default defineComponent({
  props: {
    importExcelConfig: {
      type: Object as PropType<ImportExcelConfig>,
      default: () => ({
        title: '导入',
        isZip: false,
        postData: null, // 多余传参放在这里
        onConfim: (data: object) => true, // 导入确定
        // getdownloadUrl: (data: object) => true, // 下载模板
        // downloadFailResult: (data: object) => true, // 下载失败记录
      }),
    },
    accept: {
      type: String,
      default: 'application/msword,.xls,.xlsx',
    },
    templateKey: {
      type: String,
      default: '',
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    importSuccess: (data: object) => true,
  },
  setup(props, { emit }) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const downloadUrl = ref<string>();
    const isUpoad = ref<boolean>(false);
    const importResult = ref<ImportResultKind>({
      successTotal: null,
      failTotal: null,
      fileKey: null,
    });
    const { importExcelConfig, templateKey } = toRefs(props);
    const { onConfim, postData } = importExcelConfig.value;
    const {
      parkOpts, parkOrgIdChange, companyOpts, parkingLotOpts,
    } = useOptions();
    const { modelRef, rulesRef } = importExcelForm();
    const formRef = ref();
    // const acceptType = 'application/msword,.xls,.xlsx';
    const downloadFailResult = async () => {
      const data = await patrolInspectionInessInstance.downloadFailFile(importResult.value.fileKey);
      useDownload(data);
    };
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          // const formData = new FormData();
          // formData.append('companyId', modelRef.companyId as any);
          // formData.append('time', modelRef.time as any);
          // formData.append('parkOrgId', modelRef.parkOrgId as any);
          const formData = {
            companyId: modelRef.companyId,
            time: modelRef.time,
            parkOrgId: modelRef.parkOrgId,
          };

          const res: any = await companyMangementInstance.importEmployeesNew(formData);
          console.log(res, '上传结果-------------');
          importResult.value = {
            successTotal: res.successTotal,
            failTotal: res.failTotal,
            fileKey: res.fileKey,
          };
          console.log(importResult.value, 'importResult.value');
          emit('importSuccess', {});
          if (res.failTotal <= 0) {
            emit('update:show', false);
            message.success(`已成功导入${res.successTotal}条数据`);
          }
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const onChange = async (info:any) => {
      console.log(info, 'info');
    };
    const uploadFilesButton = async () => {
      if (!modelRef.file) {
        message.warning('请先选择上传文件');
        return;
      }
      isUpoad.value = true;
      const formData = new FormData();
      formData.append('file', modelRef.file as any);
      const res: any = await companyMangementInstance.importEmployeesZip(formData);
      isUpoad.value = false;
      modelRef.time = res;
      console.log(res, '上传结果-------------');
    };
    const downloadTemplate = () => {
      console.log('下载模板');
      window.location.href = `${window.location.origin}/smartPark/park-common/file/download?key=${templateKey.value}`;
    };
    const fileList = computed(() => (modelRef.file ? [modelRef.file] : []));
    const handleRemove = async (e: any) => {
      importResult.value.failTotal = 0;
      modelRef.file = null;
      modelRef.time = '';
    };
    const beforeUpload = (file: UploadParams & { name: string }) => {
      console.log(postData, 'postData');
      // const fileType = file?.name.slice(file?.name.lastIndexOf('.')).toLowerCase();
      // if (!['.xls', '.xlsx'].includes(fileType) && postData !== 'addCompany') {
      //   message.info('只能上传.XLS.XLSX格式的文件!');
      //   return false;
      // }
      modelRef.file = file;
      return false;
    };
    const init = async () => {};
    init();
    return {
      isUpoad,
      onChange,
      uploadFilesButton,
      emit,
      modelRef,
      rulesRef,
      formRef,
      // acceptType,
      parkOpts,
      companyOpts,
      parkOrgIdChange,
      confirm,
      fileList,
      handleRemove,
      beforeUpload,
      downloadUrl,
      parkingLotOpts,
      downloadFailResult,
      importResult,
      downloadTemplate,
    };
  },
});

function useOptions() {
  const parkOpts = ref<SelectOptions>([]);
  const companyOpts = ref<SelectOptions>([]);
  const parkingLotOpts = ref<SelectOptions>([]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  // 园区切换
  const parkOrgIdChange = async (value: string) => {
    companyOpts.value = await new Park().listCompanyDictByType(value);
    // parkingLotOpts.value = await carPassInstance.parkingLotDic(value);
    console.log(companyOpts.value);
  };
  return {
    parkOpts,
    parkOrgIdChange,
    parkingLotOpts,
    companyOpts,
  };
}
</script>
<style scoped lang="scss">
.import-excel {
    .cursor {
        cursor: pointer;
    }

    .import-result {
        display: flex;
        align-items: center;

        > div {
            margin-right: 10px;

            span {
                color: var(--ant-primary-color);
            }
        }
    }
}
</style>
