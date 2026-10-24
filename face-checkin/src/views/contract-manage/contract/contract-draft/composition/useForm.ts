/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-04-24 15:48:55
 * @LastEditors: Xucaizhi3
 * @Description:
 */
/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:36:36
 * @LastEditTime: 2022-01-13 10:38:09
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Contract, ContractBillingPlanReqDTOS, VerifyInfo } from '@/models/contract-manage';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';

const creditCodeRule = {
  validator: (rules:unknown, value:string) => {
    if (!REGS.creditCode.test(value)) return Promise.reject('请输入18位统一社会信用代码');
    return Promise.resolve();
  },
  required: true,
};
const telRule = {
  validator: (rules: unknown, value: string) => {
    if (!value) return Promise.resolve();
    if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
    return Promise.resolve();
  },
};
const taxRule = {
  validator: (rules:unknown, value:string) => {
    if (!REGS.money.test(value)) return Promise.reject('请输入金额，9位整数，最多两位小数');
    if (Number(value) === 0) return Promise.reject('请输入总金额（不含税），不能为零');
    return Promise.resolve();
  },
  required: true,
};
const moneyRule = {
  validator: (rules:unknown, value:string) => {
    if (!REGS.money.test(value)) return Promise.reject('请输入金额，9位整数，最多两位小数');
    if (Number(value) === 0) return Promise.reject('请输入单价元（不含税），不能为零');
    return Promise.resolve();
  },
  required: true,
};

export const contractForm = ((contractType: any) => {
  const firstParty = () => reactive({
    nameA: '', // 甲方名称
    companyIdA: '', // 甲方公司id
    creditCodeA: '', // 甲方统一社会信用代码
    contactA: '', // 甲方联系人
    addressA: '', // 甲方地址
    contactTelA: '', // 甲方联系电话
  });
  const secondParty = () => reactive({
    nameB: '', // 乙方企业名称
    customerIdB: '', // 乙方公司id
    creditCodeB: '', // 乙方统一社会信用代码
    contactB: '', // 乙方联系人
    addressB: '', // 乙方地址
    contactTelB: '', // 乙方联系电话
  });
  const modelRef = reactive<Contract>({
    attachmentKey: '', // 附件id(com_disk_file主键)
    attributes: null, // 合同属性：1-新签合同；2-续签合同；3-变更合同；4-退约合同；5-历史合同
    contractBillingPlanReqDTOS: [], // 计费方案
    contractedDate: '', // 签约日期
    employeeId: '', // 经办人id，多个时,分隔
    employeeName: '', // 经办人id，多个时,分隔
    employeeDicts: [], // 经办人id，多个时,分隔
    endDate: '', // 合同结束日期
    // extension: '', // 扩展字段json字符串(原合同编号:originContractNo、原合同id:originContractId、变更说明:changeExplanation、变更类型:changeType)
    houseMessages: [], // 关联房屋
    id: '', //
    name: '', // 合同名称
    no: '', // 合同编号
    parkOrgId: '', // 园区Id
    processNo: '', // 当前流程节点
    remark: '', // 备注
    startDate: '', // 合同开始日期
    state: null, // 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期
    type: null, // 合同类型
    withdrawDate: '',
    withdrawReason: '',
    extension: {
      changeExplanation: '',
      performanceBond: null,
      originContractId: '',
      originContractNo: '',
      leaseTermYear: null,
      leaseTermDay: null,
      deposit: null,
      annualTaxTenThousand: null,
      businessScope: '',
      numberOfSingleRooms: 0,
      numberOfTwinRooms: 0,
      numberOfSuite: 0,
      numberOfRooms: 0,
      renovationStartDate: '',
      renovationEndDate: '',
    },
    ...firstParty(),
    ...secondParty(),
  });
  const rulesRef = reactive({
    // no: {
    //   validator: (rules: unknown, value: string) => {
    //     const reg = /^HT-ZL-[2-9]\d{3}(?:1[0-2]|[1-9])-\d{6}$/;
    //     if (!value) return Promise.reject('请输入合同编号');
    //     if (!reg.test(value)) return Promise.reject('请输入正确合同编号,合同—租赁—年月—6位随机数字');
    //     return Promise.resolve();
    //   },
    //   required: true,
    // },
    parkOrgId: { required: true, message: '请选择所属园区' },
    employeeId: { required: true, message: '请选择经办人' },
    startDate: { required: contractType !== '2', message: '请选择有效期' },
    contractedDate: { required: true, message: '请选择有效期' },
    nameA: { required: true, message: '请选择甲方' },
    nameB: { required: true, message: '请选择乙方' },
    creditCodeA: [creditCodeRule],
    creditCodeB: [creditCodeRule],
    contactTelA: [telRule],
    contactTelB: [telRule],
    feeBillingSchemeName: { required: true, message: '请选择计费方案' },
    billingCycle: { required: true, message: '请选择计费周期' },
    reservedDigits: { required: true, message: '请选择保留位数' },
    carryMode: { required: true, message: '请选择进位方式' },
    formulaType: { required: true, message: '请选择公式类型' },
    calculationStandard: { required: true, message: '请选择计算标准' },
    // unitPriceWithTax: [moneyRule],
    unitPriceWithoutTax: [moneyRule],
    chargeType: { required: true, message: '请选择收费类型' },
    chargingCycle: { required: true, message: '请选择收费周期' },
    houseIds: { required: true, message: '请选择应用房源' },
    amountWithoutTax: [taxRule],
    // fixedAmountWithoutTax: [taxRule],
    billingStartDate: { required: true, message: '请输选择起止日期' },
    dayOfMonth: [{
      validator: (rules:unknown, value:any) => {
        if (['3', '4'].includes(contractType)) {
          if (!value) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多3位');
          const pattern = /^([1-9]\d{0,2})$/;
          if (value && !pattern.test(value)) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多3位');
          if (value > 365) return Promise.reject('最大输入365天');
        } else {
          if (!value) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多2位');
          const pattern = /^([1-9]\d{0,1})$/;
          if (value && !pattern.test(value)) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多2位');
          if (value > 28) return Promise.reject('最大输入28天');
        }
        return Promise.resolve();
      },
      required: true,
    }],
    exemptDays: [{
      pattern: /^(\d{0,4})$/,
      message: '请输入豁免天数，只能输入非负的整数，最多4位',
    }],
    daysForDelay: [{
      required: true,
      pattern: /^([1-9]\d{0,3})$/,
      message: '请输入最晚缴纳日期，只能输入大于0的整数，最多4位',
    }],
    freeRentDay: [{
      pattern: /^(\d{0,4})$/,
      message: '请输入免租期，只能输入非负的整数，最多4位',
    }],
    lateRate: [{
      pattern: /^(([1-9]{1}\d{0,1})|(0{0}))(\.\d{0,2})?$|^0?\.\d{0,2}$/,
      message: '请输入滞纳率，最多输入两位整数，小数点后最多两位',
      required: true,
    }],
    lateDayAmount: [{
      pattern: /^[1-9]+\d*(\.\d{0,2})?$|^0?\.\d{0,2}$/,
      message: '请输入每日滞纳金，最多输入9位整数+两位小数',
      required: true,
    }],
    'extension.changeExplanation': [{
      validator: (rules:unknown, value:string) => {
        if (!modelRef.extension.changeExplanation) return Promise.reject('请输入变更说明');
        return Promise.resolve();
      },
      required: true,
      message: '请输入变更说明',
    }],
    'extension.annualTaxTenThousand': [{
      validator: (rules:unknown, value:string) => {
        const annualTaxTenThousand = modelRef.extension.annualTaxTenThousand;
        if (!annualTaxTenThousand) {
          if (annualTaxTenThousand === 0) {
            return Promise.reject('请输入大于0的数字');
          }
          return Promise.resolve();
        }
        if (annualTaxTenThousand < 0) return Promise.reject('请输入大于0的数字');
        return Promise.resolve();
      },
    }],
    'extension.contactIdCardNoB': [{
      validator: (rules:unknown, value:string) => {
        const contactIdCardNoB = modelRef.extension.contactIdCardNoB;
        if (!contactIdCardNoB) return Promise.resolve();
        if (!REGS.idCard.test(contactIdCardNoB)) return Promise.reject('请输入正确的身份证号码');
        return Promise.resolve();
      },
      required: false,
    }],
    'extension.leaseTermYear': [{
      validator: (rules:unknown, value:string) => {
        const leaseTermYear = modelRef.extension.leaseTermYear;
        if (!leaseTermYear) {
          if (leaseTermYear === 0) {
            return Promise.reject('请输入大于0的数字');
          }
          return Promise.resolve();
        }
        if (leaseTermYear < 0) return Promise.reject('请输入大于0的数字');
        return Promise.resolve();
      },
    }],
    // cumulativeCycle: [{
    //   validator: (rules:unknown, value:string) => {
    //     if (modelRef.chargingCycle === 2 && !REGS.mounth.test(value)) return Promise.reject('请输入累计计费周期，只能输入1-12的整数');
    //     return Promise.resolve();
    //   },
    //   required: modelRef.chargingCycle === 2,
    // }],
  });
  return {
    modelRef,
    rulesRef,
    firstParty,
    secondParty,
  };
});

export const verifyForm = () => {
  const modelRef = reactive<VerifyInfo>({
    id: '',
    auditOpinion: '',
    auditResult: '',

  });
  const rulesRef = reactive({
    auditResult: { required: true, message: '请选择审核结果' },
    // auditOpinion: { required: true, message: '请填写审核意见' },
  });
  return {
    modelRef,
    rulesRef,
  };
};
