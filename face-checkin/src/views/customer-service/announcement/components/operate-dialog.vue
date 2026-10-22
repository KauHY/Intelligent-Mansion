<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-21 17:15:49
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="720"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      :model="modelRef"
      :label-col="{span:6}"
    >
      <a-form-item
        label="标题"
        v-bind="validateInfos.title"
      >
        <a-input
          v-model:value="modelRef.title"
          :maxlength="100"
          :disabled="type === 'detail'"
          placeholder="请输入标题"
        />
      </a-form-item>
      <a-form-item
        label="类型"
        v-bind="validateInfos.type"
      >
        <v-select
          v-model:value="modelRef.type"
          :disabled="type === 'detail'"
          placeholder="请选择公告类型"
        >
          <a-select-option
            v-for="opt in contentTypeOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          :disabled="type === 'detail'"
          placeholder="请选择所属园区"
          @change="parkOrgIdChange($event,false)"
        >
          <a-select-option
            v-for="opt in parkOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="推送范围"
        v-bind="validateInfos.withoutLimit"
      >
        <a-radio-group
          v-model:value="modelRef.withoutLimit"
          :disabled="type === 'detail'"
          @change="withoutLimitChange"
        >
          <a-radio
            :value="true"
            style="display: block; line-height: 30px;"
          >
            不限
          </a-radio>
          <a-radio
            :value="false"
            style="display: block;"
          >
            限定人员公司
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <div
        v-if="!modelRef.withoutLimit"
      >
        <!-- <a-row :gutter="[24,8]"> -->
        <a-col
          :span="18"
          :offset="6"
        >
          <a-form-item v-bind="validateInfos.companyIds">
            <div
              class="flex"
            >
              <!-- <a-col> -->
              <a-checkbox
                v-model:checked="withoutLimitDetailsConf.isShowSelectCompany"
                :disabled="type === 'detail'"
                style="display: block; line-height: 30px;"
                @change="withoutLimitDetailsConfChange($event,'company')"
              >
                指定公司
              </a-checkbox>
              <!-- </a-col> -->
              <!-- </a-form-item> -->

              <!-- <a-form-item v-bind="validateInfos.employeeIds"> -->
              <div
                v-if="withoutLimitDetailsConf.isShowSelectCompany"
                class="flex-content"
              >
                <v-select
                  v-model:value="modelRef.companyIds"
                  mode="multiple"
                  :disabled="!modelRef.companyIds || type === 'detail'"
                  placeholder="请选择公司"
                >
                  <a-select-option
                    v-for="opt in companyOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </div>
            </div>
          </a-form-item>
        </a-col>

        <a-col
          :span="18"
          :offset="6"
        >
          <a-form-item v-bind="validateInfos.employeeIds">
            <div class="flex">
              <a-checkbox
                v-model:checked="withoutLimitDetailsConf.isShowSelectEmployee"
                style="display: block;"
                @change="withoutLimitDetailsConfChange($event,'employee')"
              >
                指定人员
              </a-checkbox>
              <div
                v-if="withoutLimitDetailsConf.isShowSelectEmployee"
                class="flex-content"
                :span="18"
              >
                <v-select-employee
                  v-model:value="modelRef.employeeIds"
                  :disabled="!modelRef.parkOrgId || type === 'detail'"
                  :default-select-opts="defaultOptions"
                  placeholder="请选择跟进人"
                  title="选择跟进人"
                  :company-type="0"
                  :park-org-ids="modelRef.parkOrgId"
                />
              </div>
            </div>
          </a-form-item>
        </a-col>
        <!-- </a-row> -->
      </div>

      <a-form-item
        label="投放至小程序"
        v-bind="validateInfos.putIntoType"
      >
        <a-checkbox-group
          v-model:value="modelRef.putIntoType"
          :disabled="type === 'detail'"
        >
          <a-checkbox
            v-for="opt in putIntoTypeOpts"
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-checkbox>
        </a-checkbox-group>
      </a-form-item>
      <!-- <a-form-item
        label="移动端是否弹窗提醒"
        v-bind="validateInfos.popuped"
      >
        <a-switch
          v-model:checked="modelRef.popuped"
          :disabled="type === 'detail'"
        />
      </a-form-item> -->
      <a-form-item
        label="公告时间"
        v-bind="validateInfos.effectTimeStart"
      >
        <a-range-picker
          v-model:value="rangeTime"
          :disabled="type === 'detail'"
          show-time
          :placeholder="['开始时间','结束时间']"
          value-format="YYYY-MM-DD HH:mm:ss"
          @change="onRangeChange"
        />
      </a-form-item>
      <a-form-item
        label="摘要"
        v-bind="validateInfos.summary"
      >
        <a-textarea
          v-model:value="modelRef.summary"
          show-count
          :disabled="type === 'detail'"
          :maxlength="100"
          placeholder="请输入摘要"
        />
      </a-form-item>
      <a-form-item
        label="公告内容"
        v-bind="validateInfos.content"
      >
        <v-editor
          v-model:value="modelRef.content"
          :disabled="type === 'detail'"
          placeholder="请输入公告内容"
        />
      </a-form-item>
      <a-form-item
        label="附件"
      >
        <v-upload
          v-model:value="modelRef.files"
          :disabled="type === 'detail'"
          :size="20"
          :count="10"
          accept=".png,.jpg,.jpeg,.gif,.doc,.pdf,.docx,.xls,.xlsx,.ppt,.pptx"
          list-type="text"
          tip="支持pdf/doc/docx/xls/xlsx/ppt/pptx/png/jpg/jpeg/gif格式文件，单个文件大小不超过20M"
          placeholder="请输上传附件"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="detail"?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='detail'"
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, reactive,
} from 'vue';
import { message } from 'ant-design-vue';
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
import * as dayjs from 'dayjs';
import CustomerService, { AnnouncementInfo } from '~models/customer-service';
import { useAddAnnouncement } from '../composition/useForm';
import { companyTypeNames } from '../config';
import useOpts from '../composition/useOpts';

console.log(dayjs('00:000:00', 'HH:mm:ss'), 'xxxxx');
const customerServiceInstance = new CustomerService();
// const contentTypeOpts
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    putIntoTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    contentTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible, defaultData } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useAddAnnouncement();

    // 指定人员/公司是否展示
    // const withoutLimitDetailsConf = reactive({
    //   isShowSelectCompany: false,
    //   isShowSelectEmployee: false,
    // });

    const {
      parkOpts,
      companyOpts,
      parkOrgIdChange: baseParkOrgIdChange,
    } = useOpts(modelRef);

    const onRangeChange = (date: [], dateString: []) => {
      // rangeTime.value = [];
    };

    // 公告时间
    const rangeTime = computed({
      set(value:string[]) {
        if (!value) value = [];
        const [effectTimeStart, effectTimeEnd] = value;
        // if (!effectTimeStart || !effectTimeEnd) return;
        modelRef.effectTimeEnd = effectTimeEnd || '';
        modelRef.effectTimeStart = effectTimeStart || '';
      },
      get() {
        const { effectTimeStart, effectTimeEnd } = modelRef;
        return effectTimeStart && effectTimeEnd ? [effectTimeStart, effectTimeEnd] : [];
      },
    });

    const withoutLimitChange = ({ target }:{target:{value:number}}) => {
      const { value } = target;
      if (value === 1) {
        modelRef.companyIds = [];
        modelRef.employeeIds = [];
      }
      console.log(value, 'e');
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      const params = { ...modelRef };

      validate().then(async () => {
        if (type.value === 'add') {
          await customerServiceInstance.announcementAdd(modelRef);
        } else {
          await customerServiceInstance.announcementEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    // 园区切换
    const parkOrgIdChange = async (parkOrgId:string, isInit = false) => {
      await baseParkOrgIdChange(parkOrgId);
      if (!isInit) {
        modelRef.companyIds = [];
        modelRef.employeeIds = [];
        defaultOptions.value = [];
      }
    };

    const { withoutLimitDetailsConf, defaultOptions } = useInit({
      type: type.value,
      id: defaultData.value.id,
      fill,
      parkOrgIdChange,
      modelRef,
    });
    const withoutLimitDetailsConfChange = (e:any, changeType:string) => {
      // console.log(e, '---e');
      if (e.target.checked) return;

      if (changeType === 'company') {
        modelRef.companyIds = [];
      }
      if (changeType === 'employee') {
        modelRef.employeeIds = [];
      }
    };
    // onMounted(() => {
    //   if (type.value === 'edit') {
    //     fill();
    //   }
    // });
    return {
      onRangeChange,
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      parkOpts,
      // contentTypeOpts,
      rangeTime,
      withoutLimitDetailsConf,
      // dayFormat,
      companyOpts,
      parkOrgIdChange,
      defaultOptions,
      dayjs,
      withoutLimitDetailsConfChange,
      withoutLimitChange,
    };
  },
});
function useInit({
  type, id, fill, parkOrgIdChange, modelRef,
}:{
  type: string,
  id:string,
  fill:(data:any)=>void,
  parkOrgIdChange:(e:string, isInit:boolean)=>void,
  modelRef:AnnouncementInfo
}) {
  // 指定人员/公司是否展示
  const withoutLimitDetailsConf = reactive({
    isShowSelectCompany: false,
    isShowSelectEmployee: false,
  });

  const defaultOptions = ref<SelectOptions>([]);

  const init = async () => {
    const details = await customerServiceInstance.announcementFindById(id);
    const { companyIds, employeeIds } = details;
    withoutLimitDetailsConf.isShowSelectCompany = !!(Array.isArray(companyIds) && companyIds.length);
    withoutLimitDetailsConf.isShowSelectEmployee = !!(Array.isArray(employeeIds) && employeeIds.length);
    fill(details);
    parkOrgIdChange(details.parkOrgId, true);
    if (withoutLimitDetailsConf.isShowSelectEmployee) {
      defaultOptions.value = details.employeeIds as unknown as SelectOptions;
    }
  };

  if (type === 'edit' || type === 'detail') {
    init();
  }
  return {
    withoutLimitDetailsConf,
    defaultOptions,
  };
}
</script>
<style lang="scss" scoped>
.flex {
    display: flex;
    align-items: center;
    justify-content: flex-start;

    :deep(.ant-checkbox-wrapper) {
        display: flex !important;
        width: 160px;
    }

    .flex-content {
        flex: 1;
    }
}

</style>
