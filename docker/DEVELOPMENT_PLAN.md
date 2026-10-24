# 人脸识别签到模块 — 开发计划表

> 本文档为动态开发计划，根据实际进度持续更新。
> 最后更新：2026-10-21（Docker 全站验证通过）

---

## Phase 1: 基础骨架（已完成）

| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 1.1 | 后端数据模型（person/member/request/attendance 四张表） | ✅ | 含唯一约束 |
| 1.2 | 后端 REST API（verify/save/list/remove/members/attendance） | ✅ | 6 个接口 |
| 1.3 | 后端 Service 骨架 | ✅ | 签到判断顺序完整 |
| 1.4 | 前端签到管理页面（签到记录 + 人员管理） | ✅ | 两个 Tab |
| 1.5 | 前端路由注册（/meeting-manage/face-checkin） | ✅ | |
| 1.6 | Docker Compose 编排（MySQL+Redis+Nacos+Gateway+System+Meeting+Frontend） | ✅ | |
| 1.7 | 串口桥接 api_bridge.py（TCP收图→BMP→POST后端） | ✅ | |
| 1.8 | 网关路由配置（bootstrap-docker.yml） | ✅ | |
| 1.9 | 数据库初始化 SQL（init-sql/01-face-checkin.sql） | ✅ | |
| 1.10 | 幂等控制 (device_id, request_id) 查重 | ✅ | |
| 1.11 | 前端 API 调用修复（request from axios） | ✅ | |

---

## Phase 2: 修复设计风险（进行中）

| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 2.1 | 移除 attendance 表唯一键，改为代码层面查重 | ✅ | 允许记录"无权限""已签到"等事件 |
| 2.2 | matchPerson 接入真实人脸比对 | ✅ | face_match YuNet+SFace 推理服务 + 余弦相似度阈值 0.363 |
| 2.3 | checkTimeWindow 对接 MeetingReserve 时间窗 | ✅ | 查询会议起止时间判断 |
| 2.4 | 幂等重复请求返回原始结果（而非"已处理"） | ✅ | 查询原请求的 result 还原回执 |
| 2.5 | FaceCheckinPersonDTO 添加 personType @NotNull | ✅ | |
| 2.6 | api_bridge.py BMP→JPEG 压缩 | ✅ | 减小 POST 体积 |
| 2.7 | 设备鉴权（verify 接口 token 校验） | ✅ | 后端校验 `face-checkin.device-token`，bridge 传递 `--device-token` |
| 2.8 | 照片存储从 base64→Minio/OSS | 🔲 | DB 存路径引用 |

---

## Phase 3: 核心功能完善

| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 3.1 | LCD 结果回传协议（颜色+文字） | 🔲 | 板端固件解析 JSON 并渲染 |
| 3.2 | 多人检测提示（obj_number > 1 → "请单人签到"） | 🔲 | 依赖板端固件 |
| 3.3 | 迟到统计（对比 checkin_time 与 meeting_time_start） | ✅ | 后端 remark 字段标记迟到 |
| 3.4 | 签到事件区分记录（无权限/已签到等非成功事件也落库） | ✅ | attendance 表无唯一键，status≠1 均记录 |
| 3.5 | 签到记录导出 CSV | ✅ | UTF-8 BOM + 公式转义 |
| 3.6 | 人员审核流程（待审→通过/驳回） | ✅ | 后端 auditPerson + 前端按钮 |
| 3.7 | 会议成员管理界面 | ✅ | 前端选择会议→勾选人员，对接 member/save |
| 3.8 | 设备管理（绑定/心跳/状态） | 🔲 | |
| 3.9 | 会议结束后统计快照 | 🔲 | 冻结名单版本，防止后续变更影响历史 |
| 3.10 | 未签到名单生成 | ✅ | 后端 listAbsent 接口 |
| 3.11 | 前端签到记录显示迟到标记 | ✅ | remark 显示为橙色 tag |
| 3.12 | 人员编辑功能（不只新增） | ✅ | 前端编辑弹窗含 id 字段 |
| 3.13 | 前端签到事件显示（区分无权限/已签到等） | ✅ | status tag 颜色+文字+remark 标签 |
| 3.14 | 会议成员管理界面（前端选择会议→勾选人员） | ✅ | 已并入 3.7 |
| 3.15 | 签到事件显示 | ✅ | 已并入 3.13 |
| 3.16 | face_match 推理服务集成（Python service.py + Java RestTemplate 调用） | ✅ | 端口 8901，YuNet 检测 + SFace 特征 + 余弦相似度 |
| 3.17 | face_match 中文路径兼容（_ascii_safe_path 临时文件复制） | ✅ | 修复 Windows 中文路径导致 OpenCV DNN 加载失败 |
| 3.18 | face_match 人员登记时存储照片作为比对模板 | ✅ | faceVector 字段存储 base64 照片 |
| 3.19 | face_match Docker 容器化 | ✅ | 独立容器 `face-inference`，端口 8901，`/health` 健康检查 |
| 3.20 | Java 推理服务地址容器化适配 | ✅ | `FACE_INFERENCE_URL` 指向 `http://face-inference:8901/match` |

---

## Phase 4: 安全与隐私

| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 4.1 | 密码加盐哈希存储 | 🔲 | 现有 park-system 已有 |
| 4.2 | 终端独立凭据 + 设备与会议绑定校验 | 🔄 | 已实现统一设备令牌；后续需拆分为逐设备凭据和会议绑定 |
| 4.3 | 外部访客邀请链接（不可预测令牌 + 时效 + 范围绑定） | 🔲 | |
| 4.4 | 上传限制（大小/格式/次数） | 🔲 | |
| 4.5 | 服务端解码校验并重新编码保存，移除元数据 | 🔲 | |
| 4.6 | 照片不公共暴露，审核通过前不参与识别 | 🔲 | |
| 4.7 | 日志不记录完整凭据/原始人脸照片/特征向量 | 🔲 | |
| 4.8 | 导出 CSV 防公式注入 | 🔲 | |
| 4.9 | 保存期限配置 + 自动清理任务 | 🔲 | |

---

## Phase 5: 集成与验收

| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 5.1 | Docker Compose 全栈部署验证 | ✅ | MySQL/Redis/Nacos/Gateway/System/Meeting/Inference/Frontend 全部启动并通过 HTTP 检查 |
| 5.2 | 板端固件编译（XiUOS + YOLOv2 + LCD + TCP） | 🔲 | |
| 5.3 | 端到端联调（板→桥→后端→前端） | 🔲 | |
| 5.4 | 性能测试（3秒闭环目标） | 🔲 | |
| 5.5 | 测试记录（正常/重复/无权限/未识别/断网） | 🔲 | |
| 5.6 | 文档完善（部署说明/接口说明/测试记录） | 🔲 | |

## 2026-10-21 Docker 全站验证记录

| 验证项 | 结果 | 说明 |
|---|---|---|
| Compose 构建 | ✅ | 五个业务镜像均构建成功 |
| 基础设施健康 | ✅ | MySQL、Redis、Nacos、face-inference 健康检查通过 |
| 后端启动 | ✅ | Gateway/System/Meeting 分别完成 Netty/Tomcat 启动 |
| 前端静态服务 | ✅ | `http://localhost:9001/` 返回 200 |
| 直连 meeting API | ✅ | 人员登记与列表接口通过，数据落库 |
| 网关白名单 API | ✅ | `verify` 通过 Gateway 返回“未识别”闭环状态 |
| 板端联调 | 🔲 | 需要连接 K210 后再执行 |

> 宿主机端口冲突已规避：MySQL 映射 3307，Redis 6380，Nacos 8850/9850，前端 9001；容器内部端口不变。

## 新增待办

| # | 任务 | 状态 | 说明 |
|---|------|------|------|
| 5.7 | park-common 调度依赖治理 | 🔲 | 各服务启动后有 `park-common` 负载均衡告警，非致命但需要明确是否部署或关闭调度 |
| 5.8 | 前端登录链路验证 | 🔲 | 当前只验证了网关白名单接口，还需要带 token 验证受保护页面和 CRUD |
| 5.9 | face_match 真实图片回归 | 🔲 | 使用两人正面照片验证阈值、拒识率与误识率 |

---

## 状态图例

- ✅ 已完成
- 🔄 进行中
- 🔲 未开始
- ❌ 已取消













