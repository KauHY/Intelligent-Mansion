/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 16:57:49
 */
import Smarter from '~models/urlConstant/smarter-energy';
import request from '~utils/axios';
import {
  AddParams,
  changeParams,
  AnalysisItem,
  lConsumptionAnalysisParams,
  RoleListItem,
  SearchParams,
  setAddParams,
  updataStatusParams,
  modifyMeterReadingParams,
  warningRecordPageParams,
  RecordPageParams,
} from './type';

export default class SmarterMeter {
  // 分页查询
  async meterListPageApi(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: Smarter.listMeter,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  async listMeterConsumptionPage(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: Smarter.listMeterConsumptionPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    const arr: any[] = [];
    // let houseObj = {};
    list.forEach((listItem: any, index:number) => {
      listItem.consumption.forEach((item:any, ind:number) => {
        // houseObj = {
        //   rowSpan: ind,
        //   rowSpanLength: ind === 0 ? item.consumption.length : 0,
        //   companyName: item.companyName,
        //   contractType: item.contractType,
        //   houseNames: item.houseNames,
        //   parkOrgName: item.parkOrgName,
        //   settledMonth: item.settledMonth,
        //   type: item.type,
        //   id: item.id,
        //   consumption: {
        //     deviceType: key.deviceType,
        //     totalAmount: key.totalAmount,
        //   },
        // };
        // arr.push(houseObj);

        arr.push({
          ...listItem,
          ...item,
          rowSpanLength: !ind ? listItem.consumption.length : 0,
          _id: `${listItem.id}_${ind}`,
        });
      });
    });
    console.log(arr, 'list111');
    return {
      list: <RoleListItem[]>arr,
      total: <number>total,
    };
  }

  async findByIdlistMeterConsumption(id:string) {
    const { data } = await request({
      url: `${Smarter.findById}${id}`,
      method: 'GET',
    });

    const findObj = (list: any[], deviceType: any, categoryType: any, name:string) => {
      const newArr: any[] = [];
      const arryNew: any[] = [];
      let consumptionTotal = 0;
      let lastReadingTotal = 0;
      let thisReadingTotal = 0;
      if (list.length > 0) {
        list.forEach((item) => {
          if (item.deviceType === deviceType && item.categoryType === categoryType) {
            consumptionTotal += item.consumption;
            lastReadingTotal += item.lastReading;
            thisReadingTotal += item.thisReading;
            newArr.push({
              categoryName: name,
              ...item,

            });
          }
        });
        newArr.push({
          categoryName: name,
          categoryType: 1,
          consumption: consumptionTotal,
          deviceName: '合计',
          deviceType: 5,
          lastReading: lastReadingTotal,
          thisReading: thisReadingTotal,
        });
        newArr.forEach((item: any, index: number) => {
          arryNew.push({ ...item, rowSpan: index, rowSpanLength: index === 0 ? newArr.length : 0 });
        });
      }
      return arryNew;
    };
    const roomAmountList = findObj(data.consumptionDetail, 6, 1, '室内用电');
    const publicAmountList = findObj(data.consumptionDetail, 6, 2, '公共用电');
    const roomAmountWaterList = findObj(data.consumptionDetail, 5, 1, '室内用水');
    const publicAmountWaterList = findObj(data.consumptionDetail, 5, 2, '公共用水');
    const allArr = [...roomAmountList, ...publicAmountList, ...roomAmountWaterList, ...publicAmountWaterList];
    // console.log(allArr, 'allArr');
    data.consumptionDetail = allArr;
    return data;
  }

  async modifyShareConsumption(searchData:updataStatusParams) {
    await request({
      url: `${Smarter.modifyShareConsumption}`,
      method: 'POST',
      data: searchData,
    });
  }

  // 分页查询
  async findListMeterById(id:string) {
    const { data } = await request({
      url: `${Smarter.findListMeterById}/${id}`,
      method: 'GET',
    });
    return data;
  }

  /**
   * @description: 新增电表
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async createMeters(searchData:AddParams) {
    const { data } = await request({
      url: Smarter.createMeter,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 换表
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async exchangeMeter(searchData:changeParams) {
    const { data } = await request({
      url: Smarter.exchangeMeter,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<setAddParams>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 删除电表
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async removeMeters(id:string) {
    await request({
      url: `${Smarter.removeMeter}/${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 修改电表状态
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async updateMeterStatus(searchData:updataStatusParams) {
    await request({
      url: `${Smarter.updateMeterStatus}`,
      method: 'POST',
      data: searchData,
    });
  }

  /**
   * @description: 修改电表
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async modifyMeter(searchData:AddParams) {
    const { data } = await request({
      url: Smarter.modifyMeter,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 抄表记录
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async listMeterReadingRecordPage(searchData:RoleListItem) {
    const { data } = await request({
      url: Smarter.listMeterReadingRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  // 抄表记录-读数导出
  async downloadMeterReading(searchData: any) {
    const { data, headers } = await request({
      url: Smarter.downloadMeterReading,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  /**
   * @description: 删除抄表
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async removeMeterReadingMeters(id:string) {
    await request({
      url: `${Smarter.removeMeterReading}/${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 修正读数
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async modifyMeterReading(searchData:modifyMeterReadingParams) {
    const { data } = await request({
      url: Smarter.modifyMeterReading,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  // 分页查询
  async findRecordById(id:string) {
    const { data } = await request({
      url: `${Smarter.findRecordById}/${id}`,
      method: 'GET',
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 用量记录
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async listMeterConsumptionRecordPage(searchData:RecordPageParams) {
    const { data } = await request({
      url: Smarter.listMeterConsumptionRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RecordPageParams>>list,
      total: <number>total,
    };
  }

  // 用量分析-分页查询
  async listMeterWarningRecordPage(searchData:warningRecordPageParams) {
    const { data } = await request({
      url: Smarter.listMeterWarningRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<warningRecordPageParams>>list,
      total: <number>total,
    };
  }

  // 能耗分析-分页查询
  async listMeterConsumptionAnalysis(searchData:lConsumptionAnalysisParams) {
    const { data } = await request<AnalysisItem[]>({
      url: Smarter.listMeterConsumptionAnalysis,
      method: 'POST',
      data: searchData,
    });
    const consumptions: number[] = [];
    const times: string[] = [];
    data?.forEach(({ consumption, time }) => {
      consumptions.push(consumption);
      times.push(time);
    });
    // if (data.length > 0) {
    //   data.forEach((file: any) => {
    //     consumptionArr.push(file.consumption);
    //     timeArr.push(file.time);
    //   });
    // }
    // const finallData = {
    //   consumption: consumptionArr,
    //   time: timeArr,
    // };
    // console.log(finallData, 'finallData');
    return {
      consumptions,
      times,
    };
  }

  // 导出仪表
  async listMeterdownload(searchData: any) {
    const { data, headers } = await request({
      url: Smarter.listMeterdownload,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 导入仪表
  async listMeterimportPoint(searchData: any, id:string) {
    const { data } = await request({
      url: `${Smarter.listMeterimportPoint}${id}`,
      data: searchData,
    });
    return data;
  }

  async selectMeterName(id:string) {
    const { data } = await request({
      url: `${Smarter.selectMeterName}?parkOrgId=${id}`,
      method: 'get',
    });
    return data;
  }

  // 用量分析导出
  async listanalysisdownload(searchData: any) {
    const { data, headers } = await request({
      url: Smarter.listanalysisdownload,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 抄表记录-导入读数
  async importMeterReadingRecord(searchData: any) {
    const { data } = await request({
      url: Smarter.importMeterReadingRecord,
      data: searchData,
    });
    return data;
  }

  // 能耗-导出
  async downloadConsumptionRecord(searchData: any) {
    const { data, headers } = await request({
      url: Smarter.downloadConsumptionRecord,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 企业或房屋水电用量-报表导出
  async downloadMeterConsumption(searchData: any) {
    const { data, headers } = await request({
      url: Smarter.downloadMeterConsumption,
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
