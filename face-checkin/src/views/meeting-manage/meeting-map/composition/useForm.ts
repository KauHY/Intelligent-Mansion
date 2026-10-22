/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-01-26 14:03:01
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
import { MeetingMap } from '@/models/meeting-manage';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';

export const meetingMapForm = (() => {
  const modelRef = reactive<MeetingMap>({
    id: '',
    parkOrgId: '',
    buildingId: '',
    floorId: '',
    meetingPlaneFigure: '',
  });
  const rulesRef = reactive({
    parkOrgId: { required: true, message: '请选择所属园区' },
    buildingId: { required: true, message: '请选择所属楼栋单元' },
    floorId: { required: true, message: '请选择所在楼层' },
    meetingPlaneFigure: { required: true, message: '请上传会议室分布图' },
  });
  return {
    modelRef,
    rulesRef,
  };
});
