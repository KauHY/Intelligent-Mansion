/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-16 10:20:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-16 18:20:34
 */
enum StatementAnalysis {
    paymentTrend = '/park-contract/statement/feeStatistic/paymentTrend', // 缴费趋势
    incomeDistribution = '/park-contract/statement/feeStatistic/incomeDistribution', // 收入分析
    overdueTrend = '/park-contract/statement/feeStatistic/overdueTrend', // 逾期趋势
    lateCustomers = '/park-contract/statement/feeStatistic/lateCustomers', // 逾期客户top5
    topStatistic = '/park-contract/statement/feeStatistic/topStatistic', // 收费统计-顶部统计
}
export default StatementAnalysis;
