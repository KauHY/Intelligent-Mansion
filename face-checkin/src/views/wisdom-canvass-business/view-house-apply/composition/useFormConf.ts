/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 17:04:11
 */

/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
// import {
//   onMounted, onUnmounted, reactive, computed,
// } from 'vue';
// import { REGS } from '@/constants/validate';
// import { HousingInfo, BaseHousingInfo } from '~models/wisdom-canvass-business';
// import useForm from '~utils/useForm';

// export const useAddHouseApply = (() => {
//   const createInstance = () => {
//     const modelRef = reactive<BaseHousingInfo>({
//       id: '',
//       stationNum: null, // 容纳工位
//       visitTime: [], // 看房时间
//       chargeType: null, // 押付方式
//       freeRentMonth: null, // 免租期
//       canDivided: false, // 能否分割
//       unitRent: '', // 单位租金
//       earliestRentTime: '', // 最早租期
//       minRentMonth: null, // minRentMonth
//     });
//     const rulesRef = reactive({
//       stationNum: [{
//         validator: (rules:unknown, value:string) => {
//           if (value && !REGS.notNegativeNumber.test(value)) return Promise.reject('请输入容纳工位，只能输入整数');
//           return Promise.resolve();
//         },
//       }],
//       unitRent: [{ required: true, message: '请输入单位租金' }],
//       freeRentMonth: [{
//         validator: (rules:unknown, value:string) => {
//           if (value && !REGS.numberAndFloat_2.test(value)) return Promise.reject('请输入免租期，只能数字，最多两位小数');
//           return Promise.resolve();
//         },
//       }],
//       minRentMonth: [{
//         validator: (rules:unknown, value:string) => {
//           if (value && !REGS.numberAndFloat_2.test(value)) return Promise.reject('请输入最段租期，只能数字，最多两位小数');
//           return Promise.resolve();
//         },
//       }],
//     });
//     const fill = (defaultModelRef:any) => {
//       // debugger;
//       if (!defaultModelRef && !modelRef) return;
//       const copyModelRef = modelRef as any;
//       Object.entries(copyModelRef).forEach(([key]) => {
//         if (defaultModelRef[key] || defaultModelRef[key] === 0) {
//           copyModelRef[key] = defaultModelRef[key];
//         }
//         // copyModelRef.houseIds = Object.keys(defaultModelRef.businessHouseVO);
//       });
//       // copyModelRef.
//     };

//     const { validateInfos, validate } = useForm(modelRef, rulesRef);
//     return {
//       modelRef,
//       fill,
//       validateInfos,
//       validate,
//       rulesRef,
//     };
//   };
//     // 固定写法
//   let userFormInstance:ReturnType<typeof createInstance>|null;
//   return () => {
//     if (!userFormInstance) {
//       userFormInstance = createInstance();
//       onUnmounted(() => {
//         userFormInstance = null; // 卸载时回收
//       });
//     }
//     return { ...userFormInstance };
//   };
// })();
