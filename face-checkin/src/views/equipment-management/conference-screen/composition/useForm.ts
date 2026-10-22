import {
  onUnmounted, reactive,
} from 'vue';
import { EquipmentInfo, Extension } from '~models/equipment-management';

export const customerForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<EquipmentInfo>({
      id: '',
      deviceType: 7, // 设备类型名
      deviceId: '', // 设备id
      deviceName: '', // 设备名称
      ipAddress: '', // ip地址
      parkOrgId: '',
      companyId: '',
      remark: '',
      location: '',
      extension: {
        location: '', // 设备位
        companyId: '',
        // ------- 道闸 ----------
        parkingLotId: '', // 关联停车场
        carLane: '', // 车道
        gatewayType: null, // 出入口
      },
    });
    const rulesRef = reactive({
      deviceName: { required: true, message: '请输入设备名称' },
      deviceId: { required: true, message: '请输入设备ID' },
      parkOrgId: { required: true, message: '请选择园区' },
    });
    const fill = (defaultModelRef:any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    return {
      fill,
      modelRef,
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
