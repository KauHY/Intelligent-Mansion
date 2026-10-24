<template>
  <div
    class="jobs-mangement content-section"
    style="overflow-y: scroll;"
  >
    <div v-if="currentStep===2">
      <a-form
        ref="formRef"
        :model="modelRef"
        :label-col="{ span: 5 }"
      >
        <a-form-item
          label="企业名称"
        >
          <span>{{ modelRef.name||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="企业类型"
        >
          <span>{{ options.COMPANY_TYPE[modelRef.companyType]?.label||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="统一社会信用代码"
        >
          <span>{{ modelRef.code||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="营业执照电子版"
        >
          <v-upload
            v-model:value="modelRef.licenses"
            :disabled="true"
            accept=".jpg,.bmp,.png,.gif"
            :size="5"
          />
        </a-form-item>

        <a-form-item
          label="行业领域"
        >
          <span>{{ options.INDUSTRY_FIELD[modelRef.industryField]?.label||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="企业纳税类型"
        >
          <span>{{ options.COMPANY_TAXES_TYPE[modelRef.companyTaxesType]?.label||'-' }}</span>
        </a-form-item>
        <a-form-item
          label="企业税收（万元）"
        >
          <span>{{ modelRef.corporateTax||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="企业规模"
        >
          <span>{{ options.ENTERPRISE_SIZE[modelRef.enterpriseSize]?.label||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="注册地址"
        >
          <span>{{ modelRef.address||'-' }}</span>
        </a-form-item>

        <a-form-item
          label="附件"
        >
          <v-upload
            v-model:value="modelRef.atts"
            :disabled="true"
            accept=".doc,.pdf,.docx,.xls,.xlsx,.ppt,.pptx"
            list-type="text"
          />
        </a-form-item>

        <a-form-item label="备注">
          {{ modelRef.remark }}
        </a-form-item>
      </a-form>
      <div>
        <a-button
          v-if="[4].includes(currentStep)"
          type="primary"
          style="margin-left: 220px;"
          @click="manipulate('add')"
        >
          完善信息
        </a-button>
        <a-button
          v-if="currentStep===2"
          type="primary"
          style="margin-left: 220px;"
          @click="manipulate('edit')"
        >
          完善信息
        </a-button>
      </div>
    </div>
    <div class="add-other">
      <div
        v-if="currentStep===1"
        class="shen-he"
      >
        <img
          class="logo-icon"
          src="~assets/images/status.png"
          alt=""
        >
        <div class="cancel-box">
          <div class="message-status">
            信息审核中
          </div>
          <div
            class="button-status"
            @click="manipulate('del')"
          >
            取消申请
          </div>
        </div>
      </div>
      <div>
        <a-empty
          v-if="currentStep===4 || currentStep===3"
          image="https://gw.alipayobjects.com/mdn/miniapp_social/afts/img/A*pevERLJC9v0AAAAAAAAAAABjAQAAAQ/original"
          :image-style="{
            height: '60px',
          }"
        >
          <template #description>
            <span v-if="currentStep===4">
              使用平台服务需完善企业信息
            </span>
          </template>
          <a-button
            v-if="currentStep===4"
            type="primary"
            @click="manipulate('add')"
          >
            完善信息
          </a-button>
          <a-button
            v-if="currentStep===3"
            type="primary"
            @click="manipulate('edit')"
          >
            信息审核不通过，重新提交
          </a-button>
        </a-empty>
      </div>
    </div>
    <InformationEditAdd
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess"
    />
  </div>
</template>
<script lang="ts">
import {
  ref, onMounted, defineComponent,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import CustomerVo from '~models/company';
import InformationEditAdd from './components/operate-dialog.vue';
import { companyForm } from './composition/useForm';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}
type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import'
const CustomerVoReq = new CustomerVo();
export default defineComponent({
  components: { InformationEditAdd },
  setup() {
    const inforConfig = ref<any>();
    const currentStep = ref(4);
    const { modelRef, fill } = companyForm();
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const options = useHashMapDics<['INDUSTRY_FIELD', 'COMPANY_TYPE', 'ENTERPRISE_SIZE', 'COMPANY_TAXES_TYPE']>(['INDUSTRY_FIELD', 'COMPANY_TYPE', 'ENTERPRISE_SIZE', 'COMPANY_TAXES_TYPE']);

    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'add':
          customerConfig.value = {
            title: '企业信息',
            type,
          };
          customerFlag.value = true;
          break;
        case 'edit':
          data = await CustomerVoReq.auditInfos();
          customerConfig.value = {
            title: '重新提交',
            type,
            customerData: data,
          };

          customerFlag.value = true;
          break;
        case 'del':
          Modal.confirm({
            content: '确定取消企业信息的修改申请？',
            onOk: async () => {
              await CustomerVoReq.cancelInfor(inforConfig.value);
              message.success('操作成功');
              getLastAudit();
            },
          });
          break;

        default:
          break;
      }
    };
    const getLastAudit = async () => {
      const data = await CustomerVoReq.auditInfos();
      currentStep.value = data.auditStatus;
      inforConfig.value = data.id;
    };
    const getParkOpts = async () => {
      const data = await CustomerVoReq.getDetail();
      if (data) {
        fill(data);
      } else {
        getLastAudit();
      }
    };
    const operateSuccess = () => {
      customerFlag.value = false;
      getParkOpts();
      getLastAudit();
    };
    const init = async () => {
      getParkOpts();
      getLastAudit();
    };
    onMounted(() => {
      init();
    });
    return {
      operateSuccess,
      options,
      modelRef,
      currentStep,
      manipulate,
      customerFlag,
      customerConfig,
      inforConfig,
    };
  },
});
</script>
<style lang="scss" scoped>
.add-other {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;

    .shen-he {
        display: flex;

        .cancel-box {
            margin-left: 20px;
            line-height: 36px;

            .message-status {
                font-size: 16px;
                color: #1f2e4d;
            }

            .button-status {
                font-size: 12px;
                color: var(--ant-primary-color);
                cursor: pointer;
            }
        }
    }
}
</style>
