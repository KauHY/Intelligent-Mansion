<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-14 14:35:49
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 10:36:44
-->

# 智慧园区运营平台开发蓝图

这份文档的目标不是简单介绍目录，而是帮助新同学尽快形成对项目的整体认知，知道：

- 这个项目是做什么的
- 项目如何启动、构建、联调
- 核心运行骨架是什么
- 页面、路由、菜单、权限、接口是怎么串起来的
- 新增一个业务模块时应该改哪些地方
- 哪些地方最容易踩坑

如果你是第一次接手这个仓库，建议按本文从上到下阅读一遍，再去看具体业务代码。

---

## 1. 项目定位

这是一个以园区运营管理为核心的前端中后台项目，覆盖运营端与企业端两个入口。项目承载的业务范围很广，核心领域包括：

- 园区管理
- 企业入驻管理
- 合同管理
- 招商管理
- 收费管理
- 客户服务
- 会议室管理
- 访客管理
- 人行 / 车行管理
- 设备管理
- 巡检管理
- 智慧能源
- 智慧消防
- 数据大屏与经营分析

从代码结构看，它已经不是单一业务系统，而是一个“多业务域聚合平台”。

---

## 2. 技术栈总览

### 2.1 基础技术

- Vue 3
- TypeScript
- Webpack 5
- Vue Router 4
- Pinia
- Axios
- Ant Design Vue 3

### 2.2 业务与可视化相关依赖

- ECharts
- Three.js
- 高德地图 JS API
- TinyMCE 富文本

### 2.3 工程化相关

- ESLint
- Stylelint
- Husky
- Commitlint
- `@gc082/dog-cli`

### 2.4 运行环境

- Node 版本要求：`14.1.0`

来源：`package.json`

---

## 3. 启动与构建

### 3.1 安装依赖

```bash
npm i
```

### 3.2 常用命令

```bash
npm run dev
npm run dev:proxy
npm run dev:mock
npm run build
npm run build:sit
npm run lint:eslint
npm run lint:stylelint
npm run ts
```

### 3.3 命令说明

- `npm run dev`
  通过 `dog-cli start` 启动，本地最简入口，但具体代理和环境注入由外部工具接管。

- `npm run dev:proxy`
  直接走 Webpack Dev Server，并显式指定 `ENV_MODE=dev`。如果你希望更可控、更贴近构建配置，建议优先使用这条命令。

- `npm run dev:mock`
  用于本地 mock，但当前脚本写的是 `mock=true`，而构建侧读取的是 `process.env.MOCK`。如果 mock 没生效，先排查这里。

- `npm run build`
  生产打包，`ENV_MODE=prod`。

- `npm run build:sit`
  SIT 环境打包。

- `npm run ts`
  执行 `tsc --noEmit` 做类型检查。

### 3.4 环境配置来源

项目没有使用 `.env` 体系，而是通过 `config/index.js` 提供编译配置：

- `dev`
- `sit`
- `prod`

关键配置项有：

- `publicPath`
- `projectName`
- `serverBaseUrl`

### 3.5 代理配置

开发代理定义在：

- `config/proxyTable.js`

当前 `/smartPark` 指向的候选环境包括：

- 测试环境：`http://183.194.113.230:8099`
- 本地环境：`http://127.0.0.1:8082`

Webpack Dev Server 会通过 `assemblyProxyTable` 组装代理配置。

---

## 4. 运行时骨架

## 4.1 应用入口

入口文件是 `src/main.ts`，主要做几件事：

- 创建 Vue 应用
- 注册 Ant Design Vue
- 注册 Pinia
- 注册 Router
- 注册全局组件
- 注册全局指令
- 注册全局方法
- 初始化页面进度条
- 引入全局样式和 Antd 微调样式

换句话说，`main.ts` 本身很薄，真正的业务动态性都在路由、菜单、请求、布局里。

## 4.2 根组件

`src/App.vue` 负责：

- 使用 `a-config-provider` 设置 Ant Design Vue 中文环境
- 设置 `getPopupContainer`
- 初始化主题色

这意味着全局主题能力是项目底层能力，不建议各页面自己绕开这套机制单独改风格。

## 4.3 布局层

核心布局是：

- 左侧菜单
- 顶部导航
- 面包屑
- 内容区
- `keep-alive` 页面缓存

对应文件：

- `src/views/layout/layout.vue`
- `src/views/layout/components/slide-menu/slide-menu.vue`
- `src/views/layout/components/navbar/navbar.vue`
- `src/views/layout/components/nav-record/nav-record.vue`

布局层负责的关键能力：

- 侧栏折叠与响应式宽度
- 动态菜单展示
- 当前路由面包屑记录
- 页面缓存容器
- 顶部用户操作与修改密码
- 主题切换

## 4.4 状态管理

Pinia 很轻，主要分两个 store：

### `src/store/user.ts`

负责：

- `routerInitComplete`
- `userInfo`
- `baseMenuList`
- `menuList`
- `routerRecordList`
- `openkeys`

### `src/store/common.ts`

负责：

- `cachePageNames`
- `collapsed`

也就是说，这个项目的状态管理没有做成大而全的全局仓库，而是围绕“用户态”和“布局态”做了最小化抽象。

---

## 5. 路由、菜单、权限是怎么串起来的

这是这个项目最关键的一条主线。

## 5.1 路由分两类

### 公共路由

启动时先注册公共路由，包括：

- 登录页
- 企业登录页
- 重置密码页
- 企业注册成功页
- 会议预定概览页
- demo 页

定义在：

- `src/routers/routes/common.ts`

### 动态业务路由

业务路由文件都放在：

- `src/routers/routes/*.ts`

系统会通过 `require.context` 自动加载这些路由文件，再根据当前用户的菜单权限做过滤后注入。

## 5.2 动态注入流程

核心逻辑在：

- `src/routers/index.ts`
- `src/composition/user.ts`
- `src/models/meun/index.ts`

流程如下：

1. 应用启动时只挂载公共路由
2. 进入业务页前，如果 `routerInitComplete` 为 `false`
3. 调用 `refreshRouter()`
4. `refreshRouter()` 内部调用 `useMenus(addRouter, getUserMenuList)`
5. 通过接口拉取当前用户菜单列表
6. 构造菜单树并保存到 store
7. 根据菜单里的 `routeName` 过滤业务路由
8. 将符合权限的业务路由挂到根布局 `/` 下
9. 标记 `routerInitComplete = true`
10. 再次跳转到原目标页

## 5.3 这里最关键的约束

后端菜单返回的 `routeName` 必须和前端路由的 `name` 一致。

如果不一致，就会出现这些典型问题：

- 左侧菜单看得到但点不开
- 手输地址刷新后进不去
- 页面代码写了，但动态路由没被注入

所以新增页面时，`route name` 不是随便起的，它通常要和后端菜单配置联动。

## 5.4 面包屑与菜单展开

项目会根据当前路由，在用户菜单树里追溯父级，生成：

- `routerRecordList`
- `openKeys`

这样顶部面包屑和左侧菜单展开状态就能自动同步。

---

## 6. 请求层与登录态

## 6.1 请求入口

统一请求入口在：

- `src/utils/axios/index.ts`
- `src/utils/axios/server.ts`
- `src/utils/axios/axios.conf.ts`

## 6.2 这层做了什么

- 统一设置 `baseURL`
- GET 请求自动走 `params`
- 非 GET 自动走 `data`
- 支持基于 `sessionStorage` 的简单缓存 `cacheKey`
- 请求前自动显示 loading
- 自动注入 `Authorization`
- 未登录或 token 失效时自动跳转登录
- 自动统一错误通知

## 6.3 返回码约定

响应拦截器里约定：

- `code === 10000` 或 `code === 0` 视为成功
- `code === 40009` 视为登录失效

## 6.4 登录态存储

不要直接手写 `localStorage`，项目统一使用：

- `window.$localStorage`

它的特点：

- 对象代理访问
- 自动序列化 / 反序列化
- 根据当前 URL 选择不同的存储命名空间

两个主要命名空间：

- `WISDOM-PARK`
- `WISDOM-PARK-COMPANY`

这也是运营端和企业端能共仓运行的重要原因之一。

---

## 7. 构建配置与工程约定

## 7.1 构建工具

项目不是 Vite，而是手写的 Webpack 5 配置，主要文件：

- `build/webpack.base.conf.js`
- `build/webpack.dev.conf.js`
- `build/webpack.prod.conf.js`

## 7.2 别名

核心路径别名如下：

- `@` -> `src`
- `~utils` -> `src/utils`
- `~views` -> `src/views`
- `~components` -> `src/components`
- `~composition` -> `src/composition`
- `~models` -> `src/models`

## 7.3 样式体系

全局样式入口：

- `src/assets/css/common.scss`
- `src/assets/css/antd-custom/index.scss`

说明：

- `common.scss` 负责全局 reset、主题 CSS 变量、整体背景氛围、内容区基础样式
- `antd-custom` 是对 Ant Design Vue 的二次微调

## 7.4 TypeScript 配置

`tsconfig.json` 的特点：

- 开启 `strict`
- `allowJs: true`
- `noEmit: true`
- 使用路径别名

项目里还有很多全局类型声明，集中在：

- `src/typings/global.d.ts`
- `src/typings/module.d.ts`
- `src/typings/CustomProperties.d.ts`

---

## 8. 目录结构怎么理解

推荐把目录分成 8 层来理解：

### 8.1 `src/views`

页面层，按业务域拆分，而不是按技术维度拆分。

一级目录基本就代表一个业务域，例如：

- `park-management`
- `company-mangement`
- `contract-manage`
- `wisdom-canvass-business`
- `collect-fee`
- `customer-service`
- `visitors-management`
- `equipment-management`
- `smarter-energy`
- `command-big-screen`

### 8.2 `src/routers/routes`

路由配置层，每个业务域一个路由文件。

### 8.3 `src/models`

接口模型层，是本项目非常重要的一层。

常见结构为：

- `index.ts`：接口调用 class
- `type.d.ts`：类型定义

### 8.4 `src/models/urlConstant`

接口地址常量层，按业务域拆分，不把 URL 写死在页面里。

### 8.5 `src/components`

组件层，分为：

- 通用业务组件
- 全局组件 `global-components`

### 8.6 `src/composition`

组合式逻辑复用层，放跨页面复用的 hook。

### 8.7 `src/store`

Pinia store。

### 8.8 `src/utils`

工具层，包括：

- axios 封装
- localStorage 代理
- 通用工具
- 地图
- threejs

---

## 9. 业务模块版图

下面按业务域快速建立认知，方便你后面找代码。

## 9.1 系统与基础管理

- 系统管理
- 用户管理
- 组织架构
- 角色管理
- 菜单管理
- 企业端登录地址生成

对应：

- `src/views/system-management`

## 9.2 园区与房源底盘

- 园区列表
- 园区详情
- 房屋管理
- 楼栋 / 楼层 / 房源等相关模型

对应：

- `src/views/park-management`
- `src/models/park`
- `src/models/building`
- `src/models/floor`
- `src/models/house`

## 9.3 企业入驻与企业管理

- 园区企业
- 企业审核
- 企业绑定 / 解绑房屋
- 入住记录 / 续租记录
- 员工管理 / 标签 / 岗位

对应：

- `src/views/company-mangement`
- `src/views/entry-record`

## 9.4 合同管理

- 入园协议
- 招引合同
- 租赁合同
- 物业合同
- 公寓合同
- 合同模板
- 模板字段库

对应：

- `src/views/contract-manage`

## 9.5 招商管理

- 房源招商信息
- 商机管理
- 看房申请
- 客户管理

对应：

- `src/views/wisdom-canvass-business`

## 9.6 收费管理

这是当前最庞大的业务域之一。

包括：

- 计费设置
- 收费项管理
- 计费方案
- 租赁费用管理
- 物业费用管理
- 公寓费用管理
- 企业水电费管理
- 公寓水电费管理
- 账单、交易明细、押金、滞纳金、离场结算

对应：

- `src/views/collect-fee`

## 9.7 客户服务

- 服务反馈
- 报修服务
- 公告管理
- 资讯管理
- 活动管理
- 物品放行
- 广告管理
- 会议室租赁 / 会议室管理
- 订单管理

对应：

- `src/views/customer-service`

## 9.8 访客 / 通行 / 设备 / 安防

- 人行管理
- 车行管理
- 访客管理
- 设备管理
- 巡检管理
- 智慧消防

对应：

- `src/views/person-pass`
- `src/views/car-pass`
- `src/views/visitors-management`
- `src/views/equipment-management`
- `src/views/patrol-inspection`
- `src/views/smarter-fire`

## 9.9 能源、消防与大屏

- 电表管理
- 水表管理
- 抄表记录
- 用量记录
- 异常告警
- 能耗分析
- 指挥大屏

对应：

- `src/views/smarter-energy`
- `src/views/command-big-screen`

## 9.10 企业端

企业端和运营端共仓，典型路径会带：

- `/wisdom-park-company/...`

包括：

- 企业登录
- 企业供应服务
- 企业采购服务
- 企业费用账单

---

## 10. 标准页面开发范式

这是你最该熟悉的部分。

项目里大量页面都遵循同一种模式：

### 模式：搜索区 + 表格 + 弹窗操作

典型代表：

- `src/views/system-management/user/user.vue`
- `src/views/wisdom-canvass-business/customer-manage/index.vue`

## 10.1 搜索区

统一用：

- `search-box`

搜索项配置一般是一个响应式对象：

```ts
const searchConf = reactive({
  username: {
    tag: 'input',
    value: '',
    placeholder: '请输入用户名',
  },
  status: {
    tag: 'select',
    value: '',
    placeholder: '请选择状态',
    options: statusOpt,
  },
});
```

支持的常见字段类型：

- `input`
- `select`
- `date-range`
- `month-picker`
- `date`

定义见：

- `src/components/search-box/type.d.ts`

## 10.2 列表查询

统一复用：

- `useSearchListPage`

这个 hook 帮你处理：

- 分页对象
- 搜索触发
- 翻页
- 下载导出
- 弹窗配置

常见写法：

```ts
const {
  pagination,
  pageChange,
  tableList,
  search,
  operateDialogConf,
} = useSearchListPage(modelInstance.searchListPage, searchConf, operateDialog);
```

## 10.3 表格

统一用：

- `v-table`

它是在 Ant Design Vue Table 上做的一层包装，做了这些增强：

- 兼容旧 `slots` 写法
- 默认列省略
- 默认居中
- 默认固定布局
- 斑马纹
- 自定义分页模式

## 10.4 弹窗

统一大量使用：

- `v-modal`

这是对 Antd Modal 的轻封装，默认：

- 禁止点遮罩关闭
- 禁止键盘 ESC 关闭
- 设置了滚动区域高度

## 10.5 表单

项目常见写法是：

- 页面 `config.ts` 里定义规则和表单初始化逻辑
- 通过 `useForm` 做 Antd Form 封装

相关文件：

- `src/utils/useForm.ts`

## 10.6 字典

字典统一用：

- `useHashMapDic`
- `useHashMapDics`

优点：

- 自动缓存到 `sessionStorage`
- 支持多个字典并发获取
- 页面里直接拿 `label`

示例：

```ts
const options = useHashMapDics<['GOODS_RELEASE_STATUS']>(['GOODS_RELEASE_STATUS']);
```

## 10.7 导入导出

导入统一复用：

- `src/components/import-excel/index.vue`

它内置了：

- 园区选择
- 企业选择
- 停车场选择
- 文件上传
- 模板下载
- 导入失败记录下载

所以新业务如果要做 Excel 导入，优先复用这套。

---

## 11. 接口层开发规范

如果你要新增一个业务接口，建议按现有模式走。

## 11.1 推荐结构

以用户模块为例：

- `src/models/user/index.ts`
- `src/models/user/type.d.ts`
- `src/models/urlConstant/user.ts`

## 11.2 角色分工

### `urlConstant/*.ts`

负责：

- 维护接口地址
- 保证 URL 集中管理

### `models/<domain>/index.ts`

负责：

- 业务接口调用
- 出入参转换
- 基础业务逻辑封装

### `models/<domain>/type.d.ts`

负责：

- 类型定义

## 11.3 不建议的写法

- 不建议在页面里直接手写 axios
- 不建议把接口 URL 硬编码在 `.vue` 文件里
- 不建议页面里手动做大量响应码分支

这些能力项目底层大多已经托管掉了。

---

## 12. 新增一个页面 / 模块的标准落点

如果你要新增一个标准业务页面，通常至少会改这几层：

### 12.1 新增页面文件

放到对应业务目录，例如：

- `src/views/system-management/xxx`
- `src/views/collect-fee/xxx`

### 12.2 新增路由

在对应的路由文件中增加一条路由，例如：

- `src/routers/routes/system-management.ts`

必须保证：

- `name` 唯一
- `component` 指向正确页面
- `meta.title` 正确

### 12.3 对接菜单权限

如果这是一个受权限控制的页面，需要让后端菜单返回的 `routeName` 与你的路由 `name` 一致。

### 12.4 新增接口模型

如果是新业务能力，补充：

- `src/models/<domain>/index.ts`
- `src/models/<domain>/type.d.ts`
- `src/models/urlConstant/<domain>.ts`

### 12.5 新增页面配置

如果页面是列表页，建议拆：

- `config.ts`
- `components/`
- `composition/`

### 12.6 如需字典

优先确认是否已有现成字典 key，复用 `useHashMapDics`。

---

## 13. 全局能力清单

下面这些能力已经是现成的，开发时优先复用。

## 13.1 全局组件

全局组件会被自动注册，常用有：

- `VModal`
- `VTable`
- `VSelect`
- `VUpload`
- `VImage`
- `VField`
- `VButton`
- `VEditor`
- `VSelectEmployee`
- `VSelectHouse`
- `VRangeTime`

## 13.2 全局指令

### `v-copy`

点击复制指定值，或者复制节点文本。

### `v-focus`

页面打开时自动聚焦。

### `v-Alphabet`

只允许输入大写英文和数字。

## 13.3 全局方法

### `$sort`

按字段排序对象数组或对象值数组，默认按 `sort` 排序。

## 13.4 页面缓存

可通过：

- `useCachePage`

维护 `keep-alive` 缓存名单。

## 13.5 主题切换

项目支持主题色切换，并通过 CSS 变量统一影响：

- 主色
- 表头色
- hover 背景
- 面板氛围
- 侧栏风格

---

## 14. 特殊能力模块

## 14.1 ECharts

组件：

- `src/components/echart/echart.vue`

适合做图表展示页和大屏图表。

## 14.2 地图

工具：

- `src/utils/map/index.ts`

当前封装基于高德地图。

## 14.3 Three.js

工具：

- `src/utils/threejs/index.ts`

当前项目已接入模型加载、OrbitControls、描边高亮、CSS3D 标签等能力，主要用于更强交互的空间可视化场景。

## 14.4 富文本

相关组件：

- `src/components/global-components/v-editor`

---

## 15. 开发时最容易踩坑的地方

## 15.1 动态路由不是静态生效

新增路由后，如果没配置对应菜单权限，页面可能还是进不去。

## 15.2 命名里存在历史拼写问题

项目里有一些历史命名，例如：

- `meun`
- `mangement`
- `ectricity`

这些名字虽然不规范，但已经深入现有引用链路。不要为了“看起来正确”就贸然全局改名，否则容易牵出大量路径、类型、导入变更。

## 15.3 企业端与运营端共仓

开发登录、退出、存储、跳转时，要分清当前是：

- 运营端
- 企业端

因为它们在：

- 登录路径
- 本地存储命名空间
- 部分接口

上都不一样。

## 15.4 mock 可能不稳定

`dev:mock` 是否生效，需要结合 `process.env.MOCK` 进一步验证，别默认它一定可用。

## 15.5 测试体系较弱

当前 `npm run test` 只是占位命令，不具备真实测试能力。这个项目当前更依赖：

- eslint
- stylelint
- TypeScript 检查
- 本地手工联调
- 后端接口联调

## 15.6 页面写完后要验证刷新场景

因为项目使用动态路由，所以你不能只验证“从菜单点击能进去”，还要验证：

- 刷新页面后能否恢复
- 直接访问 hash 路径能否进入

---

## 16. 建议的新同学上手路径

如果你刚接手，建议按下面顺序看代码。

### 第一步：先跑起来

- 看 `package.json`
- 看 `config/index.js`
- 看 `build/webpack.*`

### 第二步：理解入口和布局

- `src/main.ts`
- `src/App.vue`
- `src/views/layout/layout.vue`

### 第三步：理解动态权限链路

- `src/routers/index.ts`
- `src/composition/user.ts`
- `src/models/meun/index.ts`

### 第四步：理解一个最标准的 CRUD 页面

建议先看：

- `src/views/system-management/user/user.vue`
- `src/views/system-management/user/config.ts`
- `src/views/system-management/user/components/operate-dialog.vue`

### 第五步：看一个复杂业务页面

建议看：

- `src/views/wisdom-canvass-business/customer-manage/index.vue`
- `src/views/collect-fee/billing-settings/charging-items/components/list-table.vue`

### 第六步：再按你负责的业务域深入

这时候你再去看具体业务代码，效率会高很多。

---

## 17. vscode 配置

建议安装插件：

- `stylelint`
- `eslint`

---

## 18. 代码提交规范

提交规范遵从如下规则：

| 类型 | 描述 |
| - | - |
| bug | 此项特别针对 bug 号，用于向测试反馈 bug 列表的 bug 修改情况 |
| feat | 新功能（feature） |
| fix | 修补 bug |
| docs | 文档（documentation） |
| style | 格式（不影响代码运行的变动） |
| refactor | 重构（即不是新增功能，也不是修改 bug 的代码变动） |
| chore | 构建过程或辅助工具的变动 |
| revert | 撤销之前的 commit |
| merge | 合并分支，例如：`merge（前端页面）： feature-xxxx修改线程地址` |

推荐使用：

```bash
npm run commit
```

注意：

- 提交信息必须符合 commitlint 规则
- 提交前会自动执行 ESLint 和 Stylelint
- 校验不通过时无法提交

对应配置：

- `.husky/pre-commit`
- `.husky/commit-msg`
- `commitlint.config.js`

---

## 19. 图标

项目引入阿里图标库：

- [点击预览](https://at.alicdn.com/t/project/2592784/9ab09e27-65f2-4606-b914-315c7ac7e5c8.html?spm=a313x.7781069.1998910419.65)

---

## 20. TypeScript 补充

内置常见条件类型：

| 方法名 | 用途 |
| - | - |
| `Exclude<T, U>` | 用于从类型 `T` 中去除不在 `U` 类型中的成员 |
| `Extract<T, U>` | 用于从类型 `T` 中取出可分配给 `U` 类型的成员 |
| `NonNullable<T>` | 用于从类型 `T` 中去除 `undefined` 和 `null` 类型 |
| `ReturnType<T>` | 获取函数类型的返回类型 |
| `InstanceType<T>` | 获取构造函数的实例类型 |
| `Partial<T>` | 将 `T` 中所有属性转换为可选属性 |

参考：

- [ts高级用法](https://www.jb51.net/article/194411.htm)

---

## 21. 全局指令示例

### 21.1 `v-copy`

点击复制指定值，或者指定区域文本。

不传值时复制绑定节点内部所有文本：

```html
<div class="demo">
  <a-button v-copy>
    123132<span>asdfasdfasdfasfd</span>
  </a-button>
</div>
```

复制结果：

```txt
123132asdfasdfasdfasfd
```

传值时复制绑定值：

```html
<div class="demo">
  <a-button v-copy="text">
    <span>asdfasdfasdfasfd</span>
  </a-button>
</div>

<script>
export default {
  setup() {
    return {
      text: '皮皮猪',
    };
  },
};
</script>
```

复制结果：

```txt
皮皮猪
```

### 21.2 `v-focus`

打开页面时自动聚焦输入框：

```html
<a-input
  v-model:value="currentValue"
  v-focus
  style="width: auto;"
  placeholder="请输入内容"
  @blur="isFocus = false"
/>
```

---

## 22. Node 内存溢出处理

```bash
export NODE_OPTIONS=--max_old_space_size=8192
```

---

## 23. 岗位 code

| 岗位 | code |
| - | - |
| 管理员 | 1 |
| 维修 | 2 |
| 客服 | 3 |
| 保洁 | 4 |
| 保安 | 5 |
| 绿化 | 6 |
| 财务 | 7 |
| 行政 | 8 |
| 经理 | 9 |
| 招商人员 | 10 |
| 巡检人员 | 11 |
| 物品放行审核员 | 12 |

---

## 24. 常用枚举字典

以下是项目里常见的一部分字典 key：

| key | 描述 |
| - | - |
| `JOBCODE` | 岗位 |
| `ORDERTYPE-2` | 投诉类型 |
| `ORDERTYPE-2` | 报修类型 |
| `CANCELORDERREASON` | 撤单原因 |
| `INFOMATION_TYPE` | 资讯具体类型 |
| `NOTICE_TYPE` | 公告类型 |
| `FLOOR_TYPE` | 楼层属性 |
| `HOUSE_STATUS` | 房屋状态 |
| `HOUSE_PROPERTY` | 房屋属性 |
| `HOUSE_FUNCTION` | 房屋功能 |
| `HOUSE_ORIENTATION` | 房屋朝向 |
| `THREAD_SOURCE` | 线索来源 |
| `BUSINESS_CHANCE_STAGE` | 商机阶段 |
| `BUSINESS_CHANCE_SOURCE` | 商机来源 |
| `BUSINESS_FOLLOW_TYPE` | 跟进类型 |
| `CHANCE_RECORD_TYPE` | 商机记录类型 |
| `BUSINESS_HOUSE_AREA` | 面积 |
| `HIGHLIGHT` | 亮点 |
| `VISIT_REASON` | 来访事由 |
| `VISIT_SOURCE_TYPE` | 访客来源 |
| `SUBSCRIBE_PROCESS_STATUS` | 访客预约流程状态 |
| `INVITE_PROCESS_STATUS` | 访客邀请流程状态 |
| `VISIT_PROCESS_NODE` | 访客流程节点 |
| `COMPANY_BIND_CLIENT_TYPE` | 访客公司配置连接类型 |
| `GROUP_FEE_TYPE` | 费用项类型 |
| `FEE_RESERVED_DIGITS` | 保留位数 |
| `FEE_CARRY_MODE` | 进位方式 |
| `FEE_FORMULA_TYPE` | 公式类型 |
| `CONTRACT_ATTRIBUTES` | 合同属性 |
| `CONTRACT_CHANGE_TYPE` | 合同变更类型 |
| `CONTRACT_OPERATE` | 合同操作 |
| `CONTRACT_STATE` | 合同状态 |
| `CONTRACT_TYPE` | 合同类型 |
| `FEE_TYPE` | 费用项类型 |
| `FEE_BILLING_CYCLE` | 计费周期 |
| `FEE_CALCULATION_STANDARD` | 计算标准 |
| `FEE_CHARGE_TYPE` | 收费类型 |
| `FEE_CHARGING_CYCLE` | 收费周期 |
| `Week_Day` | 星期枚举 |
| `ACTIVITY_STATUS` | 活动状态 |
| `ATTENDANCE_RESULT_TYPE` | 考勤结果 |
| `PUNCH_TYPE` | 打卡方式 |
| `COMPANY_TYPE` | 客户类型 / 公司类型 |
| `CUSTOMERINFO_STATUS` | 客户状态 |
| `INDUSTRY_FIELD` | 行业领域 |
| `COMPANY_TAXES_TYPE` | 企业纳税类型 |
| `COMPANY_MANAGEMENT_STATUS` | 企业经营状态 |
| `WARNING_EVENT_CODE` | 告警事件 |
| `METER_TYPE_READING_METHOD` | 抄表方式 |
| `METER_TYPE` | 表类型 |
| `METER_STATUS` | 仪表状态 |
| `METER_EFFECT_PROPERTY` | 表计属性 |
| `ELECTRICITY_METER_EFFECT_TYPE` | 表计类别 |
| `PUT_INTO_TYPE` | 投放至类型 |
| `QUESTIONNAIRE_STATUS` | 问卷状态 |
| `FORM_CYCLE` | 信息统计表单收集周期 |
| `ADD_DATA_SOURCE` | 新增数据来源 |
| `SYS_COMPANY_AUDIT_STATUS` | 企业 / 入驻审核状态 |
| `ENTERPRISE_SIZE` | 企业规模 |
| `SERVICE_GROUPING` | 企业服务分组 |
| `HEALTH_CODE` | 健康码 |
| `RENT_CASH_STATUS` | 租赁费用押金状态 |
| `RENT_LATE_STATUS` | 租赁费用滞纳金状态 |
| `RENT_DATA_SOURCE` | 租赁费用数据来源 |
| `RENT_BILL_STATUS` | 账单状态 |
| `RENT_TRADE_TYPE` | 交易明细类型 |
| `RENT_FEE_PAY_TYPE` | 支付方式 |
| `RENT_FEE_OUT_STATUS` | 租赁费用出账状态 |

示例：

```vue
<script>
import { useHashMapDics } from '~composition/useHashMapDic';

export default defineComponent({
  setup() {
    const options = useHashMapDics<['GOODS_RELEASE_STATUS']>(['GOODS_RELEASE_STATUS']);
    return {
      options,
    };
  },
});
</script>
```
