/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-27 19:18:30
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-27 19:30:38
 */
import { ref } from 'vue';
import Building from '~models/building';
import House from '~models/house';
import Park from '~models/park';

export const useParkInfoOpts = () => {
  const buildingOpts = ref<SelectOptions>([]);
  const houseOpts = ref<SelectOptions>([]);
  const parkOpts = ref<SelectOptions>([]);
  const getParkOpts = async () => {
    parkOpts.value = await new Park().searchParkListName();
  };
  /**
   * @description: 根据楼栋id查询房屋
   * @param {string} buildingId
   * @return {*}
   * @author: GUOCHAO82
   */
  const getHouseOpts = async (buildingId:string) => {
    houseOpts.value = await new House().searchHouseOpts(buildingId);
  };
  const getBuildingOpts = async (parkOrgId:string) => {
    buildingOpts.value = await new Building().listBuildingDict(parkOrgId);
  };
  return {
    buildingOpts,
    houseOpts,
    parkOpts,
    getParkOpts,
    getHouseOpts,
    getBuildingOpts,
  };
};
