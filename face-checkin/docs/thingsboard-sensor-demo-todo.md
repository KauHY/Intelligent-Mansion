# ThingsBoard Sensor Demo 待完成清单

这个文档用于接手联调和最终上线前替换配置。

## 1. 必须补齐的认证信息

- 提供目标租户可访问设备数据的 ThingsBoard `API key`。
- 前端测试页当前只支持 `API key` 模式，不再依赖租户账号密码。

## 2. 必须补齐的设备信息

- 设备 `deviceId`
- 或设备名称 `deviceName`
- 设备真实 telemetry key
  常见示例：`temperature`、`humidity`

## 3. 需要在前端替换的配置

请修改：

- `src/views/demo/thingsboard-sensor-demo/config.ts`

重点检查这些字段：

- `apiKey`
- `device.id` 或 `device.name`
- `telemetry.temperatureKey`
- `telemetry.humidityKey`
- `realtime.enabled`
- `realtime.pollIntervalMs`

## 3.1 页面访问入口

- 路由地址：`/#/tb-sensor-demo`

## 4. 联调时需要确认

- ThingsBoard 已经存在该温湿度设备
- 该 API key 对目标设备有读取权限
- 浏览器访问当前项目页面时，对 `http://183.194.113.230:6080` 没有跨域限制
- 设备确实已经持续上报温度和湿度 telemetry
- 当前这台 ThingsBoard 服务端的 WebSocket 订阅需要 JWT，不能把 API key 直接拿去做 WebSocket 实时订阅

## 5. 如果页面没有数据，优先排查

- API key 是否有效且未禁用
- `deviceId` / `deviceName` 是否正确
- telemetry key 是否与 ThingsBoard 中真实 key 完全一致
- 设备最近是否有上报数据
- 浏览器控制台是否存在 CORS 错误
- 轮询间隔是否过长，导致误以为页面未刷新
