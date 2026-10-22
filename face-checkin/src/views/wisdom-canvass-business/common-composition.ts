/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-28 13:53:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 19:30:28
 */
import {
  ref, shallowRef, watch, Ref, Component,
} from 'vue';

export const useAddHouse = <T extends Object>(cp:Component) => {
  // 新增招商房源
  const houseHashMap = ref<any>({});
  const houseIds = ref<string[]>([]);
  const addHouseInfoCp = shallowRef();
  const houseInfoIsShow = ref(false);
  const showHouseInfo = () => {
    console.log(cp, '---cp');
    addHouseInfoCp.value = cp;
    houseInfoIsShow.value = true;
  };
  watch(houseInfoIsShow, (value:boolean) => {
    if (!value) {
      addHouseInfoCp.value = null;
    }
  });
  const houseInfoChange = (selectKeys:string[], selectData:T) => {
    houseIds.value = selectKeys;
    houseHashMap.value = selectData;
  };
  const delHouseInfo = (id:string) => {
    console.log(id, '----id');
    delete houseHashMap.value[id];
    houseIds.value = Object.keys(houseHashMap.value);
  };
  return {
    houseHashMap: houseHashMap as Ref<T>,
    houseIds,
    houseInfoIsShow,
    addHouseInfoCp,
    houseInfoChange,
    delHouseInfo,
    showHouseInfo,
  };
};
