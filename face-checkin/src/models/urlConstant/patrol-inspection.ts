/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 13:46:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 15:38:41
 */
enum PatrolPlace {
    patrolPlaceListPage = '/park-inspection/inspection/inspection-point/listPointPage', // 巡检点分页查询
    patrolPlaceEdit= '/park-inspection/inspection/inspection-point/modify', // 巡检点编辑
    patrolPlaceAdd = '/park-inspection/inspection/inspection-point/create', // 巡检点新增
    patrolPlaceDel='/park-inspection/inspection/inspection-point/remove/', // 巡检点删除
    patrolPlaceExport = '/park-inspection/inspection/inspection-point/export', // 巡检点导出
    patrolPlaceImportPoint='/park-inspection/inspection/inspection-point/importPoint', // 巡检点导入

    // 巡检线路
    patrolLineListPage = '/park-inspection/inspection/inspection-circuit/listCircuitPage', // 巡检线路分页查询
    patrolLineAdd = '/park-inspection/inspection/inspection-circuit/create', // 巡检点分页查询
    patrolLineDel = '/park-inspection/inspection/inspection-circuit/remove/', // 巡检点分页查询
    patrolLineEdit = '/park-inspection/inspection/inspection-circuit/modify', // 巡检线路编辑
    patrolLineFindById = '/park-inspection/inspection/inspection-circuit/findById/', // 根据id查询巡检线路详情
    patrolLineLinkPlace = '/park-inspection/inspection/inspection-circuit/circuitRelatePoints', // 巡检路线关联巡检点
    patrolLineLinkOpts = '/park-inspection/inspection/inspection-circuit/selectCircuits', // 巡检路线下拉数据

    // 巡检计划
    patrolPlanListPage = '/park-inspection/inspection/inspection-plan/listPlanPage', // 巡检计划分页查询
    patrolPlanAdd = '/park-inspection/inspection/inspection-plan/create', // 巡检计划新增
    patrolPlanDel = '/park-inspection/inspection/inspection-plan/remove/', // 巡检计划删除
    patrolPlanFindById = '/park-inspection/inspection/inspection-plan/findById/', // 巡检计划根据id查询
    patrolPlanEdit = '/park-inspection/inspection/inspection-plan/modify', // 巡检计划编辑
    patrolPlanLinkEmployees = '/park-inspection/inspection/inspection-plan/planRelateEmployees', // 巡检计划关联巡检人

    // 巡检任务
    patrolTaskListPage = '/park-inspection/inspection/inspection-task/listTaskPage', // 巡检任务分页查询
    patrolTaskRotateFindById = '/park-inspection/inspection/inspection-task/findTaskById/', // 巡检任务根据id查询
    patrolTaskRotate = '/park-inspection/inspection/inspection-task/rotateTask', // 巡检任务根据流转

  }

export default PatrolPlace;
