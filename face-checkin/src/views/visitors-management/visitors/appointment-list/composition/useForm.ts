/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-31 16:34:52
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { RegisterInfo } from '~models/visitors-management';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useRegister = (() => {
  const createInstance = () => {
    const modelRef = reactive<RegisterInfo>({
      invitorName: '', // 被访人姓名
      invitorTel: '', // 被访人电话号码
      visitReason: null, // 来访事由 1:商务交流 2:面试 3:其他
      visitorFaceImg: '', // 访客人脸图url路径
      visitorIdCard: '', // 访客身份证号
      visitorName: '', // 访客名
      visitorTel: '', // 访客电话号码
      accessPolicyId: '',
      companyId: '', // 通行策略ID,无预约ID时不能为空
      id: '',
      registerVerifyType: null, // 核验类型，1:无任何凭证 2:身份证 3:预约凭证
      visitorCount: null, // 访客人数
      visitorRemark: '', // 访客备注
      parkOrgId: '',
      cardNo: '',
      carNum: '',
    });
    const rulesRef = reactive({
      visitorName: [{ required: true, message: '请输入访客姓名' }],
      visitorIdCard: [{
        validator: (rules:unknown, value:string) => {
          // debugger;
          if (!REGS.correctId.test(value)) return Promise.reject('请输入正确的访客身份证号码');
          return Promise.resolve();
        },
        required: true,
      }],
      invitorName: [{ required: true, message: '请输入被访者姓名' }],
      invitorTel: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号码');
          return Promise.resolve();
        },
        required: true,
      }],
      visitorTel: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号码');
          return Promise.resolve();
        },
        required: true,
      }],
      accessPolicyId: [{ required: true, message: '请选择访问区域' }],
      companyId: [{ required: true, message: '请选择企业' }],
      parkOrgId: [{ required: true, message: '请输入园区' }],
    });

    const fill = (defaultModelRef: any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
    };
  };

  // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return () => {
    if (!userFormInstance) {
      userFormInstance = createInstance();
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
