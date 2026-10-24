# ThingsBoard 传感器测试页实现说明

## 本次实现目标

在现有项目中新增一个前端直连 ThingsBoard 的测试路由页面，用于展示温度和湿度传感器的实时数据，并为后续设备接入预留可替换配置。

## 实现步骤

### 1. 研究现有项目结构

- 查看路由结构，确认项目使用“公共路由 + 登录后动态注入业务路由”的模式。
- 查看现有 axios 封装，确认它绑定的是园区后端接口，不适合直接复用到 ThingsBoard。
- 决定把测试页放到公共路由中，避免受后端菜单权限影响。

### 2. 研究 ThingsBoard 接口

- 确认 Swagger 可访问，接口文档来自 `http://183.194.113.230:6080/v3/api-docs`
- 确认 API key 可用于 REST 读取，认证头格式：
  - `X-Authorization: ApiKey <apiKey>`
- 确认 REST 读取遥测所需接口：
  - `GET /api/device/info/{deviceId}`
  - `GET /api/tenant/deviceInfos`
  - `GET /api/plugins/telemetry/{entityType}/{entityId}/keys/timeseries`
  - `GET /api/plugins/telemetry/{entityType}/{entityId}/values/timeseries`
- 确认 WebSocket 实时能力走：
  - `ws://host/api/ws`
- 实测结论：
  - `JWT` 可以成功完成 WebSocket 认证并拿到 telemetry
  - `API key` 可以用于 REST，但不能完成 WebSocket 订阅，握手后会被服务端关闭

### 3. 设计前端接入方案

- ThingsBoard 单独封装一个前端客户端，不与现有园区系统接口耦合。
- 首次进入页面时执行：
  1. 使用 API key 校验设备读取权限
  2. 根据 `deviceId` 或 `deviceName` 解析目标设备
  3. 拉取最新温湿度快照
  4. 通过 REST 轮询刷新最新 telemetry
- 页面需要具备这些状态：
  - 未配置
  - 解析设备中
  - 读取快照中
  - 启动轮询中
  - 已连接
  - 异常

### 4. 编写配置文件

- 新增独立配置文件，用于后续替换：
  - ThingsBoard 地址
  - 租户 ID
  - API key
  - 设备 ID / 名称
  - 温度与湿度 telemetry key
  - 是否启用轮询刷新
  - 轮询间隔

### 5. 实现测试页 UI

- 新增一个测试页，展示：
  - 当前连接阶段
  - 实时刷新状态
  - 设备定位方式
  - 最后更新时间
  - 温度值
  - 湿度值
  - 当前配置摘要
  - 设备详情
  - telemetry key 探测结果
- 增加“重新连接”和“刷新快照”两个按钮，方便联调。

### 6. 接入公共路由

- 新增公共测试路由：
  - `/#/tb-sensor-demo`
- 这样页面可以独立访问，不依赖业务菜单。

### 7. 验证与收尾

- 已实测 API key 可以读取目标设备详情、telemetry key 和最新温湿度值。
- 已实测 WebSocket 只有 JWT 能完成订阅，API key 不能直接用于 WebSocket 实时链路。
- 页面因此改为 API key + REST 轮询模式。
- 已对本次变更涉及的前端文件执行 ESLint 检查并通过。

## 本次改动的文件

### 新增文件

- [src/models/urlConstant/thingsboard.ts](e:/work/wofu-code/wisdom-park-master/src/models/urlConstant/thingsboard.ts)
  - ThingsBoard 接口地址常量

- [src/models/thingsboard/index.ts](e:/work/wofu-code/wisdom-park-master/src/models/thingsboard/index.ts)
  - ThingsBoard API key REST 客户端

- [src/models/thingsboard/type.ts](e:/work/wofu-code/wisdom-park-master/src/models/thingsboard/type.ts)
  - ThingsBoard 相关类型定义

- [src/views/demo/thingsboard-sensor-demo.vue](e:/work/wofu-code/wisdom-park-master/src/views/demo/thingsboard-sensor-demo.vue)
  - 温湿度传感器测试页面

- [src/views/demo/thingsboard-sensor-demo/config.ts](e:/work/wofu-code/wisdom-park-master/src/views/demo/thingsboard-sensor-demo/config.ts)
  - 页面运行配置

- [docs/thingsboard-sensor-demo-todo.md](e:/work/wofu-code/wisdom-park-master/docs/thingsboard-sensor-demo-todo.md)
  - 用户后续待办清单

- [docs/thingsboard-sensor-demo-implementation.md](e:/work/wofu-code/wisdom-park-master/docs/thingsboard-sensor-demo-implementation.md)
  - 本文档，实现步骤和改动说明

- [docs/task-tracking/.active-module](e:/work/wofu-code/wisdom-park-master/docs/task-tracking/.active-module)
  - 当前任务跟踪模块标记

- [docs/task-tracking/thingsboard-sensor-demo/task_plan.md](e:/work/wofu-code/wisdom-park-master/docs/task-tracking/thingsboard-sensor-demo/task_plan.md)
  - 本次任务计划

- [docs/task-tracking/thingsboard-sensor-demo/findings.md](e:/work/wofu-code/wisdom-park-master/docs/task-tracking/thingsboard-sensor-demo/findings.md)
  - 研究结论与接口发现

- [docs/task-tracking/thingsboard-sensor-demo/progress.md](e:/work/wofu-code/wisdom-park-master/docs/task-tracking/thingsboard-sensor-demo/progress.md)
  - 过程记录

### 修改文件

- [src/routers/routes/common.ts](e:/work/wofu-code/wisdom-park-master/src/routers/routes/common.ts)
  - 增加公共测试路由 `thingsboardSensorDemo`

## 当前访问入口

- 页面地址：`/#/tb-sensor-demo`

## 你接下来主要要做的事

- 在 [src/views/demo/thingsboard-sensor-demo/config.ts](e:/work/wofu-code/wisdom-park-master/src/views/demo/thingsboard-sensor-demo/config.ts) 中替换 API key、设备和 telemetry key。
- 在 ThingsBoard 中创建或确认目标温湿度设备。
- 使用具备目标设备读取权限的 API key 联调。
