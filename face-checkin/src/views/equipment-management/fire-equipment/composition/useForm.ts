import { REGS } from '@/constants/validate';
import {
  reactive,
} from 'vue';
import { CustomerInfoKind } from '~models/equipment-management';
import { CustomerTagInfoKind } from '~models/customer-manage';

export const customerForm = () => {
  const modelRef = reactive<CustomerInfoKind>({
    buildingId: null,
    deviceCode: '',
    floor: null,
    id: '',
    installationDate: '',
    installationLocation: '',
    deviceName: '',
    originalNumber: '',
    parkOrgId: '',
    state: true,
    systemType: null,
    ip: null,
    deviceId: null,
    communicationPort: null,
    houseId: null,
    serialNumber: null,
    port: null,
  });
  const rulesRef = reactive({
    deviceName: { required: true, message: '请输入设备名称' },
    parkOrgId: { required: true, message: '请选择所属园区' },
    systemType: { required: true, message: '请选择所属系统' },
    serialNumber: { required: true, message: '请输入设备SN' },
    ip: { required: true, message: '请输入ip地址' },
    port: { required: true, message: '请输入端口号' },
    communicationPort: { required: true, message: '请输入通信端口' },
    deviceId: { required: true, message: '请输入设备ID' },
    floor: { required: true, message: '请选择安装楼层' },
    buildingId: { required: true, message: '请选择安装楼栋' },
  });
  return {
    modelRef,
    rulesRef,
  };
};
