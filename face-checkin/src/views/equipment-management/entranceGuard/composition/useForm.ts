/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-09 17:32:15
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import useForm from '~utils/useForm';
import { EntranceGuardInfo } from '~models/entranceGuard-management';

export const perfectEntranceGuard = (() => {
  const createInstance = () => {
    const modelRef = reactive<EntranceGuardInfo>({
      id: '',
      deviceType: null,
      deviceName: '',
      deviceId: '',
      ipAddress: '',
      location: '',
      parkOrgId: '',
      buildingId: '',
      floor: '',
      houseId: '',
      remark: '',
      lockChanelList: [],
    });

    const rulesRef = reactive({
      deviceName: [{ required: true, message: '请输入设备名称' }],
      deviceType: [{ required: true, message: '请选择设备类型' }],
      remark: [{ required: false, message: '超出限制字符长度', max: 200 }],
    });

    const fill = (defaultModelRef?: any) => {
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
