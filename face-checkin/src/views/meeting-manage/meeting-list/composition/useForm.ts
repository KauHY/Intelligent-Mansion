/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-06-21 15:31:54
 * @LastEditors: GUOCHAO82
 * @Description:
 */
/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:36:36
 * @LastEditTime: 2022-01-13 10:38:09
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Meeting } from '@/models/meeting-manage';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';

export const meetingForm = (() => {
  const modelRef = reactive<Meeting>({
    screenDeviceId: '',
    buildingId: '',
    capacity: null,
    needReview: null,
    floorId: '',
    houseId: '',
    id: '',
    meetingEquipment: [],
    meetingName: '',
    meetingType: null,
    openTimeEnd: '',
    openTimeStart: '',
    parkOrgId: '',
    reviewerListName: '',
    permissionList: [],
    reviewerList: [],
    relationType: null,
    remark: '',
    companyIds: [], // 关联公司id 逗号分割
    employeeIds: [], // 关联员工id 逗号分割
  });
  const rulesRef = reactive({
    parkOrgId: { required: true, message: '请选择所属园区' },
    buildingId: { required: true, message: '请选择所属楼栋单元' },
    floorId: { required: true, message: '请选择所在楼层' },
    houseId: { required: true, message: '请选择楼层房号' },
    meetingName: { required: true, message: '请输入会议室名称' },
    meetingType: { required: true, message: '请选择会议室类型' },
    capacity: { required: true, message: '请输入容纳人数' },
    // companyIds: { required: true, message: '请选择使用权限设置', type: 'array' },
    reviewerList: { required: true, message: '请选择审核人员' },
    needReview: { required: true, message: '请选择是否需要审核' },
    openTimeStart: { required: true, message: '请选择开始时间' },
    openTimeEnd: { required: true, message: '请选择结束时间' },
    // openTimeStart: [{
    //   validator: (rules:unknown, value:string) => {
    //     if ((!value || !modelRef.openTimeEnd)) return Promise.reject('请选择开放时间');
    //     return Promise.resolve();
    //   },
    //   required: true,
    // }],
  });
  return {
    modelRef,
    rulesRef,
  };
});
