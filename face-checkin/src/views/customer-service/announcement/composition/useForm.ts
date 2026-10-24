/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-22 16:53:59
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
// import { TagInfo } from '~models/company-mangement';
import { AnnouncementInfo } from '~models/customer-service';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddAnnouncement = (() => {
  const createInstance = () => {
    // let tempValidateField:any = null;
    const modelRef = reactive<AnnouncementInfo>({
      content: '', // 内容
      type: null, // 类型
      effectTimeEnd: '', // 发布结束时间
      effectTimeStart: '', // 发布开始时间
      files: '',
      putIntoType: [], // 投放至那个小程序
      withoutLimit: true, // 推送范围
      employeeIds: [],
      companyIds: [],
      summary: '',
      id: '',
      parkOrgId: '',
      popuped: false,
      parkOrgIds: [], // 园区id
      parkOrgNames: [], // 园区名称
      title: '', // 标题
      views: 0, // 浏览量
    });
    const rulesRef = reactive({
      title: [{ required: true, message: '请输入标题' }],
      type: [{ required: true, message: '请选择类型' }],
      parkOrgId: [{ required: true, message: '请选择推送范围' }],
      effectTimeStart: [{ required: true, message: '请选择公告时间' }],
      withoutLimit: [{ required: true, message: '请选择推送范围' }],
      summary: [{ required: true, message: '请输入摘要' }],
      content: [{ required: true, message: '请输入公告内容' }],
      putIntoType: [{ required: true, message: '请上选择投放至那个小程序', type: 'array' }],
      companyIds: [{
        validator: () => {
          if (!modelRef.withoutLimit) {
            if (!modelRef.companyIds.length && !modelRef.employeeIds.length) return Promise.reject('请指定公司/人员');
          }
          return Promise.resolve();
        },
        required: !modelRef.withoutLimit,
      }],
      employeeIds: [{
        validator: () => {
          console.log('触发了-------------');
          if (!modelRef.withoutLimit) {
            if (!modelRef.companyIds.length && !modelRef.employeeIds.length) return Promise.reject('请指定公司/人员');
          }
          return Promise.resolve();
        },
        required: !modelRef.withoutLimit,
      }],

    });

    const fill = (defaultModelRef:any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || key === 'withoutLimit') {
          copyModelRef[key] = defaultModelRef[key];
        }
        if (key === 'employeeIds') {
          copyModelRef.employeeIds = defaultModelRef.employeeIds?.map((item:any) => item.value);
        }
      });
      console.log(modelRef, 'modref');
    };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    // tempValidateField = validateField;
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
