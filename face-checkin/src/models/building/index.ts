/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-11 18:24:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 18:50:52
 */
import URLS from '@/models/urlConstant/building';
import request from '~utils/axios';
import { SearchParams, FloorListItem, FloorInfo } from './type';

export default class Floor {
//   async searchListPage<T>(searchData:SearchParams) {
//     const { data } = await request({
//       url: URLS.searchFloorList,
//       method: 'POST',
//       data: searchData,
//     });
//     const { list, total } = data;
//     return {
//       list: <Array<ParkListItem>>list,
//       total: <number>total,
//     };
//   }
  async listBuildingDict(parkOrgId:string, hiddenLoading = false) {
    const { data } = await request({
      url: `${URLS.listBuildingDict}${parkOrgId}`,
      method: 'get',
      hiddenLoading,
    });
    return <SelectOptions>data;
  }

  async findBuildingById(id:string) {
    const { data } = await request({
      url: `${URLS.findBuildingById}${id}`,
      method: 'get',
    });
    return <FloorInfo>data;
  }

  async add(data:FloorInfo) {
    await request({
      url: URLS.save,
      data,
    });
  }

  async del(id:string) {
    await request({
      url: `${URLS.del}${id}`,
      method: 'delete',
    });
  }

  /**
   *
   * @param id string 楼层id
   * @returns totalFloor number 房屋总数
   */
  async getBuildingTotalFloor(id:string) {
    const { data: totalFloor } = await request({
      url: URLS.getBuildingTotalFloor,
      method: 'get',
      data: {
        buildingId: id,
      },
    });
    return Number(totalFloor) || 0;
  }

  async importBuildings(searchData: any) {
    const { data } = await request({
      url: `${URLS.importBuildings}`,
      data: searchData,
    });
    return data;
  }

  async importHouses(searchData: any) {
    const { data } = await request({
      url: `${URLS.importHouses}`,
      data: searchData,
    });
    return data;
  }

  async exportHouses(searchData:any) {
    const { data, headers } = await request({
      url: URLS.exportHouses,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
}
export * from './type';
