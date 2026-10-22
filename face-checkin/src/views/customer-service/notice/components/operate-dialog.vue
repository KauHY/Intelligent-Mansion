<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-29 15:54:51
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
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
          placeholder="请选择类型"
        >
          <a-select-option
            v-for="opt in typeOptions['INFOMATION_TYPE']"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="投放至小程序"
        v-bind="validateInfos.putIntoType"
      >
        <a-checkbox-group
          v-model:value="modelRef.putIntoType"
          :disabled="type === 'detail'"
        >
          <a-checkbox :value="1">
            C端
          </a-checkbox>
          <a-checkbox :value="2">
            运营端
          </a-checkbox>
        </a-checkbox-group>
      </a-form-item>
      <a-form-item
        label="推送范围"
        v-bind="validateInfos.parkOrgIds"
      >
        <v-select
          v-model:value="modelRef.parkOrgIds"
          mode="multiple"
          filter-option
          :disabled="type === 'detail'"
          placeholder="请选择推送范围"
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
        label="生效时间"
        v-bind="validateInfos.effectTimeStart"
      >
        <a-date-picker
          v-model:value="modelRef.effectTimeStart"
          show-time
          :disabled="type === 'detail'"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>
      <a-form-item
        label="封面图片"
        v-bind="validateInfos.picture"
      >
        <!-- <a-input v-model:value="modelRef.picture" /> -->
        <v-upload
          v-model:value="modelRef.picture"
          :disabled="type === 'detail'"
          accept=".jpg,.bmp,.png,.gif"
          tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸360*270PX"
        />
      </a-form-item>

      <!-- <a-form-item
        label="摘要"
        v-bind="validateInfos.summary"
      >
        <a-input
          v-model:value="modelRef.summary"
          placeholder="请输入摘要"
        />
      </a-form-item> -->
      <a-form-item
        label="资讯内容"
        v-bind="validateInfos.content"
      >
        <!-- <a-radio-group
          v-model:value="modelRef.contentType"
          @change="relationChange"
        >
          <a-radio
            :value="0"
            :style="radioStyle"
          >
            内容
          </a-radio>
          <a-radio
            :value="1"
            :style="radioStyle"
          >
            超链接
          </a-radio>
        </a-radio-group> -->
        <v-editor
          v-model:value="modelRef.content"
          :disabled="type === 'detail'"
          placeholder="请输入摘要"
        />
        <!-- <a-input
          v-if="modelRef.contentType===1"
          v-model:value="modelRef.content"
          placeholder="请输入资讯链接"
        /> -->
        <!-- <div v-if="modelRef.contentType===1">
          请联系开发者将跳转地址添加至信任列表
        </div> -->
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
import CustomerService, { NoticeInfo } from '~models/customer-service';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { useSearchListPage } from '~composition/common';
import { useAddNotice } from '../composition/useForm';
import {
  companyTypeNames, contentTypeOpts, putIntoTypeOpts,
} from '../config';
import useOpts from '../composition/useOpts';

const customerServiceInstance = new CustomerService();
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
    // parkOpts: {
    //   type: Array as PropType<SelectOptions>,
    //   default: () => ([]),
    // },

  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const radioStyle = reactive({
      display: 'block',
      height: '30px',
      lineHeight: '30px',
    });
    const typeOptions = ref<DictionaryMap<['INFOMATION_TYPE']>>({
      INFOMATION_TYPE: [],
    });
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
    } = useAddNotice(defaultData.value);

    const {
      parkOpts,
    } = useOpts(modelRef);

    // 公告时间
    const rangeTime = computed({
      set(value:string[]) {
        const [effectTimeStart, effectTimeEnd] = value;
        if (!effectTimeStart || !effectTimeEnd) return;
        modelRef.effectTimeEnd = effectTimeEnd;
        modelRef.effectTimeStart = effectTimeStart;
      },
      get() {
        const { effectTimeStart, effectTimeEnd } = modelRef;
        return effectTimeStart && effectTimeEnd ? [effectTimeStart, effectTimeEnd] : [];
      },
    });
    // 移动端是否弹窗展示
    // const popuped = computed({
    //   set(value) {
    //     modelRef.popuped = value ? 1 : 0;
    //   },
    //   get() {
    //     return modelRef.popuped === 1;
    //   },
    // });
    const companyTypeName = ref<string>();
    const selectCompany = (companyOpt:SelectOptions[0]) => {
      console.log((companyTypeNames as any), [companyOpt.type]);
      companyTypeName.value = (companyTypeNames as any)[companyOpt.type].label;
    };
    const relationChange = () => {
      modelRef.content = '';
    };
    // const contentCp = props.type === 'resetPwd' ? resetPwd : addUser;
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
          await customerServiceInstance.noticeAdd(modelRef);
        } else {
          await customerServiceInstance.noticeEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    const init = async () => {
      typeOptions.value = await useHashMapDic<['INFOMATION_TYPE', ]>(['INFOMATION_TYPE']);
    };
    onMounted(() => {
      init();
      fill();
    });
    return {
      // disabledCheckTime,
      // disabledCheckoutDate,
      relationChange,
      radioStyle,
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      parkOpts,
      contentTypeOpts,
      putIntoTypeOpts,
      rangeTime,
      typeOptions,
    };
  },
});
</script>
