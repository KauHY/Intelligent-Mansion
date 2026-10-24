<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-01 21:40:02
 * @LastEditors: xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="company-check"
    :visible="true"
    :width="730"
    :title="companyCheckConfig.title"
    @cancel="emit('update:show', false)"
  >
    <div class="com">
      <div class="com-up">
        <div>
          <h3 class="com-title">
            企业信息
          </h3>
          <div>
            <a-row>
              <a-col :span="5">
                企业名称
              </a-col>
              <a-col>{{ checkData.name }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                企业类型
              </a-col>
              <a-col>{{ companyTypeName }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                统一社会信用代码
              </a-col>
              <a-col>{{ checkData.code }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                营业执照电子版
              </a-col>
              <a-col>
                <v-image
                  width="80px"
                  height="80px"
                  :keys="checkData.licenses"
                />
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                行业领域
              </a-col>
              <a-col>{{ industryFieldName }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                企业纳税类型
              </a-col>
              <a-col>{{ companyTaxesTypeName }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                企业税收（万元）
              </a-col>
              <a-col>{{ corporateTax }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                企业规模
              </a-col>
              <a-col>{{ enterpriseSizeName }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                注册地址
              </a-col>
              <a-col>{{ checkData.address }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                附件
              </a-col>
              <a-col>
                <v-upload
                  v-model:value="checkData.atts"
                  list-type="text"
                  accept=".doc,.pdf,.docx,.xls,.xlsx,.ppt,.pptx"
                  disabled
                />
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                备注
              </a-col>
              <a-col>{{ checkData.remark }}</a-col>
            </a-row>
            <a-row v-if="checkData.checkType === 'checkInfo'">
              <a-col :span="5">
                提交时间
              </a-col>
              <a-col>{{ checkData.createTime }}</a-col>
            </a-row>
          </div>
        </div>
        <div v-if="checkData.checkType === 'check'">
          <h3 class="com-title">
            入驻信息
          </h3>
          <div>
            <a-row>
              <a-col :span="5">
                申请入驻园区
              </a-col>
              <a-col>{{ checkData.parkOrgName }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                联系人
              </a-col>
              <a-col>{{ checkData.contactPerson }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                联系电话
              </a-col>
              <a-col>{{ checkData.contactPhone }}</a-col>
            </a-row>
            <a-row>
              <a-col :span="5">
                提交时间
              </a-col>
              <a-col>{{ checkData.applyTime }}</a-col>
            </a-row>
          </div>
        </div>
      </div>
      <div
        v-if="formDisabled"
        class="record-list"
      >
        <a-row>
          <a-col :span="5" />
          <a-col :span="7">
            <a-timeline>
              <a-timeline-item color="#2578ED">
                <template #dot>
                  <v-svg
                    name="clock"
                    class="clock-svg"
                  />
                </template>
                <div class="content-box">
                  <div class="record-item">
                    <p class="label">
                      提交申请
                    </p>
                    <p>{{ checkData.applyTime }}</p>
                  </div>
                </div>
              </a-timeline-item>
              <a-timeline-item color="#2578ED">
                <template #dot>
                  <v-svg
                    name="clock"
                    class="clock-svg"
                  />
                </template>
                <div class="content-box">
                  <div class="record-item">
                    <p class="label">
                      {{ statusName }}
                    </p>
                    <p v-if="checkData.auditStatus !== 1">
                      {{ checkData.auditTime }}
                    </p>
                  </div>
                  <template v-if="[2, 3].includes(checkData.auditStatus)">
                    <div class="record-item des">
                      <p class="label">
                        审核人
                      </p>
                      <p>{{ checkData.auditUserName }}</p>
                    </div>
                    <div class="record-item des">
                      <p class="label">
                        审核意见
                      </p>
                      <p>{{ checkData.auditMsg || '-' }}</p>
                    </div>
                  </template>
                </div>
              </a-timeline-item>
            </a-timeline>
          </a-col>
        </a-row>
      </div>
      <div
        v-if="!formDisabled"
        class="com-down"
      >
        <h3 class="com-title">
          审核信息
        </h3>
        <a-form
          ref="formRef"
          :model="modelRef"
          :rules="rulesRef"
          :label-col="{ span: 5 }"
          :colon="false"
        >
          <a-form-item
            label="审核结果"
            name="auditStatus"
          >
            <a-radio-group v-model:value="modelRef.auditStatus">
              <!-- 状态 1待审核 2通过 3拒绝 4已取消 -->
              <a-radio :value="2">
                通过
              </a-radio>
              <a-radio :value="3">
                拒绝
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item
            label="审核意见"
            name="auditMsg"
          >
            <a-textarea
              v-model:value="modelRef.auditMsg"
              placeholder="请输入审核意见"
              :maxlength="200"
            />
          </a-form-item>
        </a-form>
      </div>
    </div>
    <template #footer>
      <a-button @click="emit('update:show', false)">
        {{ formDisabled ? '关闭' : '取消' }}
      </a-button>
      <a-button
        v-show="!formDisabled"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs, nextTick,
} from 'vue';
import CompanyMangementVo, { CompanyCheckInfo } from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { checkForm } from '../composition/useForm';

export default defineComponent({
  components: {},
  props: {
    companyCheckConfig: {
      type: Object,
      default: () => ({
        title: '',
        type: '',
        checkData: null,
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const formRef = ref();
    const { modelRef, rulesRef } = checkForm();
    const { checkData, type } = toRefs(props.companyCheckConfig);
    const {
      checkType, auditStatus, industryField, companyTaxesType, companyType, enterpriseSize, corporateTax,
    } = checkData.value;
    console.log(checkData.value, 12);
    const formDisabled = type.value === 'detail';
    const options = useHashMapDics<['SYS_COMPANY_AUDIT_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'ENTERPRISE_SIZE', 'COMPANY_TYPE']>(
      ['SYS_COMPANY_AUDIT_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'ENTERPRISE_SIZE', 'COMPANY_TYPE'],
    );
    const apiReq = {
      check: new CompanyMangementVo().companyCheckVerifyApi,
      checkInfo: new CompanyMangementVo().companyCheckInfoVerifyApi,
    };
    const statusName = computed(() => options.SYS_COMPANY_AUDIT_STATUS && options.SYS_COMPANY_AUDIT_STATUS[auditStatus]?.label);
    const industryFieldName = computed(() => options.INDUSTRY_FIELD && options.INDUSTRY_FIELD[industryField]?.label);
    const companyTaxesTypeName = computed(() => options.COMPANY_TAXES_TYPE && options.COMPANY_TAXES_TYPE[companyTaxesType]?.label);
    const enterpriseSizeName = computed(() => options.ENTERPRISE_SIZE && options.ENTERPRISE_SIZE[enterpriseSize]?.label);
    const companyTypeName = computed(() => options.COMPANY_TYPE && options.COMPANY_TYPE[companyType]?.label);

    const confirm = () => {
      formRef.value.validate().then(async () => {
        await apiReq[checkType as 'check' | 'checkInfo']({ ...modelRef, id: checkData.value.id });
        emit('refresh', {});
        emit('update:show', false);
      });
    };
    return {
      emit,
      confirm,
      checkData,
      type,
      modelRef,
      rulesRef,
      formRef,
      formDisabled,
      statusName,
      industryFieldName,
      companyTaxesTypeName,
      enterpriseSizeName,
      companyTypeName,
      corporateTax,
    };
  },
});
</script>
<style scoped lang="scss">
.company-check {
    .com {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        height: 100%;

        .com-up {
            flex: 1;
            max-height: 30vh;
            overflow: auto;
        }
    }

    h3 {
        margin-bottom: 10px;
        font-size: 16px;
        font-weight: 500;
    }

    .ant-row {
        margin-bottom: 24px;
    }

    .ant-col-5 {
        padding-right: 8px;
        text-align: right;
    }

    .record-list {
        padding-top: 20px;
        border-top: 1px solid #d8d8d8;

        .clock-svg {
            width: 30px;
            height: 30px;
            color: var(--ant-primary-color);
        }
    }

    .content-box {
        min-width: 300px;
        max-width: 300px;
        padding-bottom: 30px;
        margin-left: 20px;

        .record-item {
            display: flex;
            align-items: center;
            justify-content: flex-start;

            .label {
                width: 90px;
            }

            &.des {
                .label {
                    color: #ddd;
                }
            }
        }
    }
}
</style>
