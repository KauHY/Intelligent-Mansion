/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-22 16:52:26
 */
import { onUnmounted, reactive } from 'vue';
import useForm from '~utils/useForm';
// import { TagInfo } from '~models/company-mangement';
import { AnnouncementInfo, NoticeInfo } from '~models/customer-service';

/**
 * @description: 新增咨讯
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddNotice = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<NoticeInfo>({
      content: '', // 内容
      contentType: 0, // 类型
      effectTimeEnd: '', // 发布结束时间
      effectTimeStart: '', // 发布开始时间
      id: '',
      // popuped: false,
      parkOrgIds: [], // 园区id
      parkOrgNames: [], // 园区名称
      picture: '', // 图片
      title: '', // 标题
      views: 0, // 浏览量
      putIntoType: null,
      type: null,
    });
    const rulesRef = reactive({
      title: [{ required: true, message: '请输入标题' }],
      contentType: [{ required: true, message: '请选择类型' }],
      putIntoType: [{ required: true, message: '请选择投放类型' }],
      parkOrgIds: [{ required: true, message: '请选择推送范围' }],
      effectTimeStart: [{ required: true, message: '请选择生效时间' }],
      type: [{ required: true, message: '请选择类型' }],
      picture: [{ required: true, message: '请上传封面图片' }],
      content: [{ required: true, message: '请输入资讯内容' }],
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
