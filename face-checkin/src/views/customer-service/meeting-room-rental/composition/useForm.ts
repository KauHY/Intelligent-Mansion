/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-03-16 19:23:32
 * @LastEditors: xucaizhi3
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
import { MeetingRentalInfoKind } from '~models/meeting-rental';

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

export const rentalForm = () => {
  const modelRef = reactive<MeetingRentalInfoKind>({
    id: '',
    buildingId: '',
    companyIds: [], // 关联公司id 逗号分割
    employeeIds: [], // 关联员工id 逗号分割
    houseId: '',
    meetingName: '',
    parkOrgId: '',
    telephone: '', // 咨询电话
    capacity: '', // 容纳人数
    meetingEquipment: [], // 1摄像机、2远程会议设备、3投影仪、4电子白板
    coverImage: '', // 封面图片（单张）
    detailImage: '', // 详情图片（最多五张）
    remark: '', // 会议室介绍
    reserveInstruct: '', // 预定须知
    charge: 1, // 是否收费 1是 0否
    rentPrice: null, // 租赁价格
    goodsId: '', // 物资id 逗号分割
    usePermission: 0, // 0不限 1指定公司/人员
    timeArr: [1, 2], // 收费设置
    timeReqDTOList: [], // 时间
    material: '', // 收费设置
    meetingRentDTO: {}, // 收费设置
  });
  const rulesRef = reactive({
    meetingName: { required: true, message: '请输入会议室名称' },
    remark: { required: true, message: '请输入会议室介绍' },
    reserveInstruct: { required: true, message: '请输入预定须知' },
    parkOrgId: { required: true, message: '请选择所属园区' },
    timeArr: { required: true, message: '请选择可预定时段' },
    charge: { required: true, message: '请选择收费设置' },
    buildingId: { required: true, message: '请选择所属楼栋单元' },
    houseId: { required: true, message: '请选择所属楼层房号' },
    coverImage: { required: true, message: '请选择封面图片' },
    detailImage: { required: true, message: '请选择详情图片' },
    capacity: { required: true, message: '请输入容纳人数' },
    usePermission: { required: true, message: '请选择使用权限设置' },
    rentPrice: { required: true, message: '请输入租赁价格(元/半天)' },
    telephone: {
      required: true,
      validator: (rules: unknown, value: string) => {
        if (!REGS.notNegativeNumber.test(value)) return Promise.reject('请输入正确的电话号码');
        return Promise.resolve();
      },
    },
  });
  return {
    modelRef,
    rulesRef,
  };
};

export const materialForm = () => {
  const modelRef = reactive<any>({
    parkOrgId: '',
    goodsName: '',
    goodsType: '',
    unitPrice: '',
    unit: '',
    goodsImage: '',
    remark: '',
    id: '',
  });
  const rulesRef = reactive({
    parkOrgId: { required: true, message: '请选择所属园区' },
    goodsName: { required: true, message: '请输入物资名称' },
    goodsType: { required: true, message: '请选择物资类型' },
    unit: { required: true, message: '请输入计量单位' },
    unitPrice: [{
      validator: (rules:unknown, value:string) => {
        if (!REGS.area.test(value)) return Promise.reject('请输入价格，最多输入9位整数+两位小数');
        return Promise.resolve();
      },
      required: true,
    }],
    goodsImage: { required: true, message: '请选择上传图片' },
    remark: { required: true, message: '请输入说明' },
  });
  return {
    modelRef,
    rulesRef,
  };
};

export const orderForm = () => {
  const modelRef = reactive<any>({
    id: '',
    reviewAmount: '',
    reviewMsg: '',
    reviewStatus: null, // 审核状态 2通过 3拒绝

  });
  const rulesRef = reactive({
    // reviewStatus: { required: true, message: '请选择审核结果' },
    reviewStatus: {
      required: true,
      validator: (rules:unknown, value:number) => {
        if (![2, 3].includes(value)) return Promise.reject('请选择审核结果');
        return Promise.resolve();
      },
    },
    reviewAmount: { required: true, message: '请输入实际订单金额(元)' },
    reviewMsg: { required: true, message: '请输入审核意见(元)' },
  });
  return {
    modelRef,
    rulesRef,
  };
};
