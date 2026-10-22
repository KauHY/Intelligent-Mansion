/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-03-02 16:29:00
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
import { REGS } from '@/constants/validate';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { number } from 'mathjs';
import { CustomerInfoKind } from '~models/equipment-management';
import { CustomerTagInfoKind } from '~models/customer-manage';

const telRule = {
  validator: (rules: unknown, value: string) => {
    if (!value) return Promise.resolve();
    if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
    return Promise.resolve();
  },
};
const emailRule = {
  validator: (rules: unknown, value: string) => {
    if (!value) return Promise.resolve();
    if (!REGS.email.test(value)) return Promise.reject('请输入正确的邮箱');
    return Promise.resolve();
  },
};

export const customerForm = () => {
  const modelRef = reactive<CustomerInfoKind>({
    buildingId: null,
    deviceCode: '',
    floor: null,
    id: '',
    installationDate: '',
    installationLocation: '',
    name: '',
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
    name: { required: true, message: '请输入设备名称' },
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

export const customerTagForm = () => {
  const modelRef = reactive<CustomerTagInfoKind>({
    name: '',
    parkOrgId: '',
    id: '',
  });
  const rulesRef = reactive({
    name: { required: true, message: '请输入标签名称' },
    parkOrgId: { required: true, message: '请选择园区' },
  });
  return {
    modelRef,
    rulesRef,
  };
};
