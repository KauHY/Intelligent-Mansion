/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-25 14:51:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-02 16:58:29
 */
/**
 * @description: 编辑新增房屋
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { HouseInfo } from '@/models/house';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';

export const useHouse = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<HouseInfo>({
      id: '',
      name: '', // 楼栋名称
      code: '', // 楼栋编码
      coverArea: null, // 占地面积
      buildArea: null, // 建筑面积
      buildingId: '',
      rent: '',
      floor: null, // 所在楼层
      buildingName: '',
      parkOrgId: '',
      floorType: null, // 楼层属性
      houseStatus: null, // 房屋状态
      houseProperty: null, // 房屋属性
      houseFunction: null, // 房屋功能
      orientation: null, // 朝向
      remark: '',
    });
    const rulesRef = reactive({
      name: [{
        // validator: (rules:unknown, value:string) => {
        //   if (!value) return Promise.reject('请输入楼栋单元名称');
        //   if (value.length > 30) return Promise.reject('最多只能输入30个字符');
        //   return Promise.resolve();
        // },
        pattern: /^.{1,30}$/,
        required: true,
        message: '请输入楼栋单元名称,最多只能输入30个字符',
      }],
      houseFunction: [{ required: true, message: '请选择房屋功能' }],
      coverArea: [{
        pattern: REGS.area,
        message: '请输入套内面积，最多输入9位整数+两位小数',
        required: true,
      }],
      buildArea: [{
        // validator: (rules:unknown, value:string) => {
        //   if (value && !REGS.numberAndFloat_2.test(value)) return Promise.reject('只能输入数字，最多两位小数');
        //   return Promise.resolve();
        // },
        required: true,
        pattern: REGS.area,
        message: '请输入建筑面积，最多输入9位整数+两位小数',
      }],
      houseProperty: [{ required: true, message: '请选择房屋属性' }],
      floor: [{ required: true, message: '请选择所在楼层' }],
      // houseStatus: [{ required: true, message: '请选择房屋属性' }],
    });
    const fill = (targetModelRef:any, baseModelRef:any) => {
      Object.keys(targetModelRef).forEach((key) => {
        if (baseModelRef[key] || baseModelRef[key] === 0) { targetModelRef[key] = baseModelRef[key]; }
      });
      // debugger;
      // targetModelRef.parkOrgId = baseModelRef.value;
      // console.log(targetModelRef, '---targetModelRef');
    };
    const { validateInfos, validate, validateField } = useForm(modelRef, rulesRef);
    return {
      fill,
      modelRef,
      validateInfos,
      validate,
      validateField,
    };
  };
    // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?:any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
