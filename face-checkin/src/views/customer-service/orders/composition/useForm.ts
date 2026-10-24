/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: xucaizhi3
 * @LastEditTime: 2022-03-22 21:47:16
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { OrderInfo, AssignUser, OrderCancelParams } from '~models/customer-service';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddOrder = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<OrderInfo>({
      createByName: '', // 发布人
      createTime: '', // 报事报修创建时间
      dataSources: 1, // 数据来源 1后台 2移动端
      id: '',
      orderAddress: '', // 报事报修详细地址
      orderContent: '', //  报事报修内容
      orderNo: '', // 报事报修工单编号
      orderOwnerMobile: '', // 报事报修人电话
      orderOwnerName: '', // 报事报修反馈人
      orderState: 1, // 处理状态：1待派单,2待处理,8已完成,9已撤单
      orderType: null, // 报事报修类型：1-报事投诉、2-报修服务
      orderTypeItem: null, // 报事投诉类型：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务 报修服务类型：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备 99-其他
      orderUrl: '', // 图片uri，多个时逗号分隔
      parkOrgName: '', // 园区名
      parkOrgId: '',
    });
    const rulesRef = reactive({
      orderOwnerName: [{ required: true, message: '请输入反馈人姓名' }],
      orderOwnerMobile: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号');
          return Promise.resolve();
        },
        required: true,
      }],
      orderTypeItem: [{ required: true, message: '请选择类型' }],
      parkOrgId: [{ required: true, message: '请选择关联园区' }],
      orderAddress: [{ required: true, message: '请输入详细地址' }],
      // orderUrl: [{ required: true, message: '请上传照片' }],
      orderContent: [{ required: true, message: '请输入内容' }],
    });

    const fill = () => {
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
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();

/**
 * @description: 派单
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useSendOrder = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<AssignUser>({
      companyId: '', // 物业公司id
      handleUserId: '', // 处理人id
      askFinishTime: '', // 要求完成时间
      orderNo: '',
      orderId: '',
    });
    const rulesRef = reactive({
      companyId: [{ required: true, message: '请选择物业公司' }],
      handleUserId: [{ required: true, message: '请选则处理人' }],
      askFinishTime: [{ required: true, message: '请选择完成时间' }],
    });

    const fill = () => {
      modelRef.orderNo = defaultModelRef.orderNo;
      modelRef.orderId = defaultModelRef.id;
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
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();

/**
 * @description: 撤销订单
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useCancelOrder = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<OrderCancelParams>({
      cancelReason: '',
      orderId: '',
    });
    const rulesRef = reactive({
      cancelReason: [{ required: true, message: '请选择撤单原因' }],
    });

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      modelRef.orderId = defaultModelRef.id;
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
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
