/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-10 17:10:08
 */
import URLS from '@/models/urlConstant/wisdom-canvass-business';
import request from '~utils/axios';
import {
  CustomerManagementInfoParams,
  HousingBusinessSearchParams,
  HousingSelectBusinessSearchParams,
  HousingSelectBusinessInfo,
  HousingBusinessInfo,
  CustomerManagementInfo,
  SelectHouseInfo,
  HouseInfoSearchParams, // 招商房屋分页查询
  HousingInfo,
  BaseHousingInfo,
  BusinessInfoSearchParams,
  BusinessInfo,
  OperateBussInfo,
  HouseIntentionSearchParams,
  HouseIntentionInfo,
  BusinessInfoFollow,
  BusinessInRemind,
  BusinessInconvertToCustomer,
  FollowRecord,
  FollowRecordSearchParams,
  StageProcess,
  ViewHouseApplySearchParams,
  ViewHouseApplyInfo,
} from './type';

export default class BusinessInvitation {
  // 招商客户管理
  async customerManagementListpage<T>(searchData:CustomerManagementInfoParams) {
    const { data } = await request({
      url: URLS.customerManagementListpage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CustomerManagementInfo>>list,
      total: <number>total,
    };
  }

  // 招商-导出
  async housingBusinessExport(searchData: any) {
    const { data, headers } = await request({
      url: `${URLS.housingBusinessExport}`,
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
   * @description: 房源信息分页查询
   * @param {HousingBusinessSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async housingBusinessListPage<T>(searchData:HousingBusinessSearchParams) {
    const { data } = await request({
      url: URLS.housingBusinessListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<HousingBusinessInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 查询招商房屋不去重复（用于房源招商）
   * @param {HousingSelectBusinessSearchParams} searchData
   * @param {boolean} init
   * @return {*}
   * @author: GUOCHAO82
   */
  async selectListHouseInfoPage(searchData:HousingSelectBusinessSearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.selectListHouseInfoPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<HousingSelectBusinessInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 更新房源招商状态
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async housingUpdateStatus(data:{id:string, status:number}) {
    await request({
      url: URLS.housingUpdateStatus,
      data,
    });
  }

  /**
   * @description: 根据园区id查询招商楼栋
   * @param {string} parkOrgId
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessBuildingDict(parkOrgId:string) {
    const { data } = await request({
      url: `${URLS.businessBuildingDict}${parkOrgId}`,
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
   * @description: 根据招商楼栋查询招商房屋
   * @param {string} parkOrgId
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessHouseInfoDict(parkOrgId:string) {
    const { data } = await request({
      url: `${URLS.businessHouseInfoDict}${parkOrgId}`,
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
   * @description: 新增招商信息
   * @param {HousingBusinessInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async housingBusinessAdd(data:HousingBusinessInfo) {
    // data.deadline = data.deadline ? `${data.deadline} 00:00:00` : '';
    // debugger;
    await request({
      url: URLS.housingBusinessAdd,
      data,
    });
  }

  /**
 * @description: 更新招商信息
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */

  async housingBusinessEdit(data:HousingBusinessInfo) {
    await request({
      url: URLS.housingBusinessEdit,
      data,
    });
  }

  /**
   * @description:删除招商信息
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async housingBusinessDel(id:string) {
    await request({
      url: `${URLS.housingBusinessDel}${id}`,
      method: 'delete',
    });
  }

  // 根据id查询招商信息
  async housingBusinessById(id:string) {
    const { data } = await request({
      url: `${URLS.housingBusinessById}${id}`,
      method: 'get',
    });
    const businessHouseVO:SelectHouseInfo = {};
    if (data.businessHouseVO) {
      data.businessHouseVO.forEach((item:SelectHouseInfo[string]) => {
        const { houseInfoId } = item;
        if (houseInfoId) {
          businessHouseVO[houseInfoId] = item;
        }
      });
    }
    data.businessHouseVO = businessHouseVO;
    return data as HousingBusinessInfo;
  }

  /**
   * @description: 招商房屋分页查询
   * @param {HouseInfoSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async houseInfoListPage<T>(searchData:HouseInfoSearchParams) {
    const { data } = await request({
      url: URLS.houseInfoListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<HousingInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description:删除招商房屋
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async houseDel(id:string) {
    await request({
      url: `${URLS.houseDel}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description: 意向房源分页查询
   * @param {HouseIntentionSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async houseIntentionListPage<T>(searchData:HouseIntentionSearchParams) {
    const { data } = await request({
      url: URLS.houseIntentionListPage,
      method: 'POST',
      data: searchData,
    });
    const { total, list } = data;
    // const list = data.list.map((item:any) => {
    //   const { name, buildingName, houseId } = item;
    //   return {
    //     name,
    //     buildingName,
    //     houseId,
    //   };
    // });
    return {
      list: <Array<HouseIntentionInfo>>list,
      total: <number>total,
    };
  }

  // 根据id查询招商信息
  async housefindById(id:string) {
    const { data } = await request({
      url: `${URLS.housefindById}${id}`,
      method: 'get',
    });
    // data.visitTime = data.visitTime ? data.visitTime.split(';') : [];
    const { visitTime } = data as HousingInfo;
    if (visitTime) {
      data.visitStartTime = visitTime.split(',')[0];
      data.visitEndTime = visitTime.split(',')[1];
    }
    return data as HousingInfo;
  }

  /**
   * @description: 招商房屋编辑
   * @param {HouseInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async houseModify(data:BaseHousingInfo) {
    const { visitEndTime, visitStartTime } = data;
    const params = {
      ...data,
      visitTime: visitEndTime && visitStartTime ? `${visitStartTime},${visitEndTime}` : '',
    };
    await request({
      url: URLS.houseModify,
      data: params,
    });
  }

  async addBusinessHouse(data:BaseHousingInfo) {
    const { visitEndTime, visitStartTime } = data;
    const params = {
      ...data,
      visitTime: visitEndTime && visitStartTime ? `${visitStartTime},${visitEndTime}` : '',
    };
    await request({
      url: URLS.addBusinessHouse,
      data: params,
    });
  }

  /**
   * @description: 商机分页查询
   * @param {BusinessInfoSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoListPage<T>(searchData:BusinessInfoSearchParams) {
    const { data } = await request({
      url: `${URLS.businessInfoListPage}${searchData.type}`,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<BusinessInfo>>list,
      total: <number>total,
    };
  }

  // 全部商机-导出
  async allBusinessInfoExport(searchData: any) {
    const { data, headers } = await request({
      url: `${URLS.businessInfoExport}0`,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 公共商机-导出
  async commonBusinessInfoExport(searchData: any) {
    const { data, headers } = await request({
      url: `${URLS.businessInfoExport}1`,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 我的商机-导出
  async myBusinessInfoExport(searchData: any) {
    const { data, headers } = await request({
      url: `${URLS.businessInfoExport}2`,
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
   * @description: 新增招商房屋
   * @param {BusinessInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoAdd(data:BusinessInfo) {
    await request({
      url: URLS.businessInfoAdd,
      data,
    });
  }

  /**
   * @description: 商机编辑
   * @param {BusinessInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoEdit(data:BusinessInfo) {
    await request({
      url: URLS.businessInfoEdit,
      data,
    });
  }

  async businessInfoOperate(data:OperateBussInfo) {
    await request({
      url: URLS.businessInfoOperate,
      data,
    });
  }

  /**
   * @description: 删除商机
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoDel(id:string) {
    await request({
      url: `${URLS.businessInfoDel}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description: 商机跟进
   * @param {BusinessInfoFollow} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoFollow(data:BusinessInfoFollow) {
    await request({
      url: URLS.businessInfoFollow,
      data,
    });
  }

  /**
   * @description: 提醒
   * @param {BusinessInRemind} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoRemind(data:BusinessInRemind) {
    await request({
      url: URLS.businessInfoRemind,
      data,
    });
  }

  /**
   * @description: 转成客户
   * @param {BusinessInconvertToCustomer} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoConvertToCustomer(data:BusinessInconvertToCustomer) {
    await request({
      url: URLS.businessInfoConvertToCustomer,
      data,
    });
  }

  /**
   * @description: 根据id查询商机详情
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async businessInfoFindById(id:string) {
    const { data } = await request({
      url: `${URLS.businessInfoFindById}${id}`,
      method: 'get',
    });
    if (data.businessHouseVO) {
      const houseHashMap:SelectHouseInfo = {};
      data.businessHouseVO.forEach((item:any) => {
        const {
          buildingName, name, houseId, buildArea,
        } = item;
        houseHashMap[houseId] = {
          name,
          houseId,
          buildArea,
        };
      });
      data.businessHouseVO = houseHashMap;
    }
    return data as BusinessInfo;
  }

  /**
   * @description: 分页查询跟进记录
   * @param {FollowRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async followRecordFindById<T>(searchData:FollowRecordSearchParams) {
    const { data } = await request({
      url: `${URLS.followRecordFindById}${searchData.id}`,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<FollowRecord>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 根据id查询阶段流程
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async stageProcessById(id:string) {
    const { data } = await request({
      url: `${URLS.stageProcessById}${id}`,
      method: 'get',
    });
    return data as Array<StageProcess>;
  }

  // 看房申请分页查询
  async houseApplyForListPage<T>(searchData:ViewHouseApplySearchParams) {
    const { data } = await request({
      url: URLS.houseApplyForListPage,
      method: 'POST',
      data: searchData,
    });
    const { total, list } = data;
    return {
      list: <Array<ViewHouseApplyInfo>>list,
      total: <number>total,
    };
  }

  // 看房申请受理
  async houseApplyForAccept(id:string) {
    await request({
      url: `${URLS.houseApplyForAccept}?id=${id}`,
    });
  }
}
export * from './type';
