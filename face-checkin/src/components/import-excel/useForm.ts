/*
 * @Author: Xucaizhi3
 * @Date: 2022-03-04 10:11:11
 * @LastEditTime: 2022-04-13 17:08:16
 * @LastEditors: GUOCHAO82
 * @Description:
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { ImportExcelKind } from './type';

export const importExcelForm = (() => {
  const modelRef = reactive<ImportExcelKind>({
    parkOrgId: '',
    file: '',
    companyId: '',
    parkingLotIds: [],
  });
  const rulesRef = reactive({
    companyId: { required: true, message: '请选择所属企业' },
    parkOrgId: { required: true, message: '请选择所属园区' },
    file: { required: true, message: '请选择文件' },
  });
  return {
    modelRef,
    rulesRef,
  };
});
