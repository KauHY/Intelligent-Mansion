/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 14:51:55
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-16 18:27:24
 */
import URLS from '~models/urlConstant/statement-analysis';
import request from '~utils/axios';
import {
  SearchPaymentTrend,
  PaymentTrendItem,
  IncomeNnalysis,
  OverdueTrendItem,
  LateCustomerItem,
  TopStatistic,
} from './type';

export default class {
  async paymentTrend(params:SearchPaymentTrend) {
    const { data } = await request<PaymentTrendItem[]>({
      url: URLS.paymentTrend,
      data: params,
    });
    return data;
  }

  async incomeDistribution(params:SearchPaymentTrend) {
    const { data } = await request<IncomeNnalysis>({
      url: URLS.incomeDistribution,
      data: params,
    });
    return data;
  }

  async overdueTrend(params:SearchPaymentTrend) {
    const { data } = await request<OverdueTrendItem[]>({
      url: URLS.overdueTrend,
      data: params,
    });
    return data;
  }

  async lateCustomers(params:SearchPaymentTrend) {
    const { data } = await request<LateCustomerItem[]>({
      url: URLS.lateCustomers,
      data: params,
    });
    return data;
  }

  async topStatistic(params:SearchPaymentTrend) {
    const { data } = await request<TopStatistic>({
      url: URLS.topStatistic,
      data: params,
    });
    return data;
  }
}
export * from './type';
