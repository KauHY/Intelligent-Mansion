<template>
  <div class="tb-sensor-list content-section">
    <div class="content-scroll">
      <section class="compact-hero">
        <div class="compact-hero__identity">
          <p class="compact-hero__eyebrow">
            THINGSBOARD SENSOR CENTER
          </p>
          <h1>传感器列表</h1>
          <p class="compact-hero__meta">
            当前租户共 {{ filteredDevices.length }} 台设备，支持按设备动态发现遥测项并在抽屉中实时监测。
          </p>
        </div>
        <div class="compact-hero__actions">
          <a-input-search
            v-model:value="pageState.searchKeyword"
            allow-clear
            placeholder="搜索设备名称 / 类型 / 档案"
            class="compact-hero__search"
          />
          <a-button
            :loading="pageState.loading"
            @click="refreshDeviceList"
          >
            刷新列表
          </a-button>
        </div>
      </section>

      <a-alert
        v-if="configWarnings.length"
        class="top-alert"
        type="info"
        show-icon
        message="当前缺少运行配置"
      >
        <template #description>
          <ul class="warning-list">
            <li
              v-for="warning in configWarnings"
              :key="warning"
            >
              {{ warning }}
            </li>
          </ul>
        </template>
      </a-alert>

      <a-alert
        v-else-if="pageState.errorMessage"
        class="top-alert"
        type="warning"
        show-icon
        message="设备列表读取异常"
        :description="pageState.errorMessage"
      />

      <div class="summary-grid">
        <article class="summary-card">
          <span class="summary-card__label">设备总数</span>
          <strong>{{ pageState.totalDevices }}</strong>
        </article>
        <article class="summary-card">
          <span class="summary-card__label">已激活</span>
          <strong>{{ activeDeviceCount }}</strong>
        </article>
        <article class="summary-card">
          <span class="summary-card__label">当前筛选</span>
          <strong>{{ filteredDevices.length }}</strong>
        </article>
      </div>

      <a-card
        class="list-card"
        :bordered="false"
      >
        <template #title>
          设备列表
        </template>
        <template #extra>
          <span class="list-card__extra">点击“监测”打开右侧实时抽屉</span>
        </template>

        <a-table
          :columns="tableColumns"
          :data-source="filteredDevices"
          :loading="pageState.loading"
          :pagination="false"
          row-key="id"
          size="middle"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'name'">
              <div class="device-cell">
                <strong>{{ record.name || '--' }}</strong>
                <span>{{ record.label || record.description || '暂无补充说明' }}</span>
              </div>
            </template>

            <template v-else-if="column.key === 'profile'">
              <div class="device-meta">
                <a-tag color="blue">
                  {{ record.deviceProfileName || '未命名档案' }}
                </a-tag>
                <span>{{ record.type || 'default' }}</span>
              </div>
            </template>

            <template v-else-if="column.key === 'status'">
              <a-tag :color="record.active ? 'success' : 'default'">
                {{ record.active ? '已激活' : '未激活' }}
              </a-tag>
            </template>

            <template v-else-if="column.key === 'createdTime'">
              {{ formatDateTime(record.createdTime || 0) }}
            </template>

            <template v-else-if="column.key === 'action'">
              <a-button
                type="primary"
                size="small"
                @click="openDetailDrawer(record)"
              >
                监测
              </a-button>
            </template>
          </template>
        </a-table>

        <a-empty
          v-if="!pageState.loading && !filteredDevices.length"
          description="当前没有可展示的传感器设备"
        />
      </a-card>
    </div>

    <a-drawer
      :visible="detailState.visible"
      placement="right"
      width="50vw"
      :body-style="{ padding: '14px 14px 24px' }"
      @close="closeDetailDrawer"
    >
      <template #title>
        {{ drawerTitle }}
      </template>

      <div class="drawer-shell">
        <section class="drawer-bar">
          <div class="drawer-bar__identity">
            <p class="drawer-bar__eyebrow">
              DEVICE MONITOR
            </p>
            <div class="drawer-bar__title-row">
              <h2>{{ drawerTitle }}</h2>
              <div class="drawer-bar__actions">
                <a-button
                  :loading="detailState.booting"
                  type="primary"
                  @click="reloadCurrentDevice"
                >
                  重新连接
                </a-button>
                <a-button
                  :loading="detailState.historyRefreshing"
                  @click="refreshCurrentHistory"
                >
                  刷新历史
                </a-button>
              </div>
            </div>
            <p class="drawer-bar__meta">
              最后更新 {{ formatDateTime(detailState.lastUpdateTs) }}
            </p>
          </div>
          <div class="drawer-bar__status">
            <div class="status-pill">
              <span>阶段</span>
              <a-tag :color="detailTag.color">
                {{ detailTag.label }}
              </a-tag>
            </div>
            <div class="status-pill">
              <span>实时</span>
              <a-tag :color="socketTag.color">
                {{ socketTag.label }}
              </a-tag>
            </div>
            <div class="status-pill">
              <span>状态</span>
              <a-tag :color="detailState.device?.active ? 'success' : 'default'">
                {{ detailState.device?.active ? '激活' : '未激活' }}
              </a-tag>
            </div>
            <div class="status-pill">
              <span>设备类型</span>
              <strong>{{ detailState.device?.type || '--' }}</strong>
            </div>
            <div class="status-pill">
              <span>配置</span>
              <strong>{{ detailState.device?.deviceProfileName || '--' }}</strong>
            </div>
            <div class="status-pill">
              <span>遥感数量</span>
              <strong>{{ telemetryDisplayKeys.length }}</strong>
            </div>
          </div>
        </section>

        <a-alert
          v-if="detailState.errorMessage"
          class="top-alert"
          type="warning"
          show-icon
          message="当前设备存在监测异常"
          :description="detailState.errorMessage"
        />

        <a-spin :spinning="detailState.booting">
          <a-card
            class="drawer-card"
            :bordered="false"
          >
            <template #title>
              实时遥测
            </template>
            <div
              v-if="telemetryDisplayKeys.length"
              class="telemetry-grid"
            >
              <article
                v-for="key in telemetryDisplayKeys"
                :key="key"
                class="telemetry-card"
                :class="telemetryToneClass(key)"
              >
                <div class="telemetry-card__head">
                  <div>
                    <p>{{ telemetryLabel(key) }}</p>
                    <span>{{ key }}</span>
                  </div>
                  <a-tag color="blue">
                    {{ telemetryUnit(key) || '值' }}
                  </a-tag>
                </div>
                <div class="telemetry-card__value">
                  {{ formatTelemetryValue(key, detailState.latestTelemetry[key]?.value) }}
                  <small v-if="telemetryUnit(key)">{{ telemetryUnit(key) }}</small>
                </div>
                <p class="telemetry-card__meta">
                  最近更新 {{ formatRelativeTime(detailState.latestTelemetry[key]?.ts || 0) }}
                </p>
              </article>
            </div>
            <a-empty
              v-else
              description="当前设备还没有读取到遥测项"
            />
          </a-card>

          <a-card
            class="drawer-card"
            :bordered="false"
          >
            <template #title>
              历史趋势
            </template>
            <template #extra>
              <span class="history-extra">历史窗口 {{ historyWindowLabel }}</span>
            </template>

            <div
              v-if="chartTelemetryKeys.length"
              class="history-grid"
            >
              <article
                v-for="key in chartTelemetryKeys"
                :key="key"
                class="history-item"
              >
                <div class="history-item__header">
                  <div>
                    <h3>{{ telemetryLabel(key) }}</h3>
                    <span>{{ key }}</span>
                  </div>
                  <a-tag :color="telemetryColor(key)">
                    {{ telemetryUnit(key) || '值' }}
                  </a-tag>
                </div>
                <div class="history-item__chart">
                  <echart :option="chartOptionMap[key]" />
                </div>
              </article>
            </div>
            <a-empty
              v-else
              description="当前设备暂无可绘制的数值型历史数据"
            />
          </a-card>
        </a-spin>
      </div>
    </a-drawer>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  onActivated,
  onBeforeUnmount,
  onDeactivated,
  reactive,
  ref,
} from 'vue';
import dayjs from 'dayjs';
import Echart, { LineECOption } from '~components/echart';
import ThingsBoard, {
  ThingsBoardDeviceInfo,
  ThingsBoardTelemetryMap,
  ThingsBoardTelemetryPoint,
} from '@/models/thinksboard';
import { ensureThingsBoardTenantToken } from '@/models/thinksboard/tenant-token';
import sensorListConfig from './thingsboard-sensor-list/config';

type DetailStatus = 'idle' | 'loading' | 'ready' | 'error';
type SocketStatus = 'idle' | 'connecting' | 'connected' | 'reconnecting' | 'error' | 'closed';

const TOKEN_REFRESH_LEAD_MS = 60 * 1000;
const HISTORY_KEYS_LIMIT = 240;
const CHART_COLORS = ['#2b89ff', '#19a979', '#6259ff', '#e57b1d', '#1f74c9', '#35a7c8', '#db5b70'];

function sortTelemetryPoints(list:ThingsBoardTelemetryPoint[]) {
  return [...list].sort((a, b) => a.ts - b.ts);
}

function mergeSeries(existing:ThingsBoardTelemetryPoint[], incoming:ThingsBoardTelemetryPoint[]) {
  const mergedMap = new Map<number, ThingsBoardTelemetryPoint>();
  [...existing, ...incoming].forEach((point) => {
    mergedMap.set(point.ts, point);
  });
  return sortTelemetryPoints(Array.from(mergedMap.values())).slice(-HISTORY_KEYS_LIMIT);
}

function pickLatestPoint(list:ThingsBoardTelemetryPoint[]) {
  return [...list].sort((a, b) => b.ts - a.ts)[0] || null;
}

function toNumber(value:string | number | boolean | undefined | null) {
  if (typeof value === 'number') return Number.isFinite(value) ? value : null;
  if (typeof value === 'string' && value.trim() !== '') {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : null;
  }
  return null;
}

function formatFallbackKey(key:string) {
  return key
    .split(/[_-]+/)
    .filter(Boolean)
    .map((segment) => {
      if (segment.toLowerCase() === 'co2') return 'CO2';
      if (segment.toLowerCase() === 'pm25') return 'PM2.5';
      if (segment.toLowerCase() === 'pm10') return 'PM10';
      if (segment.toLowerCase() === 'tvoc') return 'TVOC';
      return segment.charAt(0).toUpperCase() + segment.slice(1);
    })
    .join(' ');
}

function getDigitsByKey(key:string) {
  if (/humidity|temperature/i.test(key)) return 2;
  if (/pressure|altitude/i.test(key)) return 2;
  if (/battery/i.test(key)) return 0;
  return 2;
}

function settlePromise<T>(promise:Promise<T>) {
  return promise.then(
    (value) => ({ status: 'fulfilled' as const, value }),
    (reason) => ({ status: 'rejected' as const, reason }),
  );
}

export default defineComponent({
  name: 'ThingsboardSensorList',
  components: {
    Echart,
  },
  setup() {
    const config = sensorListConfig;
    const client = new ThingsBoard(config.baseUrl);
    const destroyed = ref(false);
    const pageActive = ref(true);
    const socketInstance = ref<WebSocket | null>(null);
    const reconnectTimer = ref<number | null>(null);
    const tokenRefreshTimer = ref<number | null>(null);
    const ignoreNextClose = ref(false);
    const detailSessionId = ref(0);

    const pageState = reactive({
      loading: false,
      errorMessage: '',
      searchKeyword: '',
      devices: [] as ThingsBoardDeviceInfo[],
      totalDevices: 0,
    });

    const detailState = reactive({
      visible: false,
      booting: false,
      historyRefreshing: false,
      errorMessage: '',
      status: 'idle' as DetailStatus,
      socketStatus: 'idle' as SocketStatus,
      realtimeJwt: '',
      realtimeExpTime: 0,
      device: null as ThingsBoardDeviceInfo | null,
      telemetryKeys: [] as string[],
      latestTelemetry: {} as Record<string, ThingsBoardTelemetryPoint | null>,
      historyTelemetry: {} as Record<string, ThingsBoardTelemetryPoint[]>,
      lastUpdateTs: 0,
    });

    const configWarnings = computed(() => {
      const warnings:string[] = [];
      if (!config.auth.username) warnings.push('请先配置租户标识，用于服务端换取 ThingsBoard token。');
      return warnings;
    });

    const tableColumns = [
      {
        title: '设备名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '档案 / 类型',
        key: 'profile',
      },
      {
        title: '状态',
        key: 'status',
        width: 100,
      },
      {
        title: '创建时间',
        dataIndex: 'createdTime',
        key: 'createdTime',
        width: 180,
      },
      {
        title: '操作',
        key: 'action',
        width: 100,
      },
    ];

    const activeDeviceCount = computed(() => pageState.devices.filter(({ active }) => active).length);

    const filteredDevices = computed(() => {
      const keyword = pageState.searchKeyword.trim().toLowerCase();
      if (!keyword) return pageState.devices;

      return pageState.devices.filter((device) => {
        const source = [
          device.name,
          device.label,
          device.type,
          device.deviceProfileName,
          device.description,
        ]
          .filter(Boolean)
          .join(' ')
          .toLowerCase();

        return source.includes(keyword);
      });
    });

    const telemetryKeys = computed(() => {
      const keys = [
        ...detailState.telemetryKeys,
        ...Object.keys(detailState.latestTelemetry),
        ...Object.keys(detailState.historyTelemetry),
      ].filter(Boolean);

      return Array.from(new Set(keys));
    });

    const telemetryDisplayKeys = computed(() => {
      const priorityMap = new Map(config.telemetry.priorityKeys.map((key, index) => [key, index]));

      return [...telemetryKeys.value as any].sort((left, right) => {
        const leftPriority = priorityMap.has(left) ? priorityMap.get(left)! : Number.MAX_SAFE_INTEGER;
        const rightPriority = priorityMap.has(right) ? priorityMap.get(right)! : Number.MAX_SAFE_INTEGER;

        if (leftPriority !== rightPriority) return leftPriority - rightPriority;
        return left.localeCompare(right);
      });
    });

    const chartTelemetryKeys = computed(() => telemetryDisplayKeys.value.filter((key) => (
      (detailState.historyTelemetry[key] || []).some((point) => toNumber(point.value) !== null)
    )));

    const drawerTitle = computed(() => detailState.device?.name || '设备监测');

    const historyWindowLabel = computed(() => {
      const hours = Math.round(config.history.windowMs / (60 * 60 * 1000));
      return `${hours}h`;
    });

    const detailTag = computed(() => {
      const map:Record<DetailStatus, { label:string, color:string }> = {
        idle: { label: '未启动', color: 'default' },
        loading: { label: '读取中', color: 'processing' },
        ready: { label: '已就绪', color: 'success' },
        error: { label: '异常', color: 'error' },
      };

      return map[detailState.status];
    });

    const socketTag = computed(() => {
      const map:Record<SocketStatus, { label:string, color:string }> = {
        idle: { label: '未启动', color: 'default' },
        connecting: { label: '连接中', color: 'processing' },
        connected: { label: '实时中', color: 'success' },
        reconnecting: { label: '重连中', color: 'orange' },
        error: { label: '异常', color: 'error' },
        closed: { label: '已断开', color: 'default' },
      };

      return map[detailState.socketStatus];
    });

    const telemetryLabel = (key:string) => config.telemetry.labelMap[key as keyof typeof config.telemetry.labelMap] || formatFallbackKey(key);

    const telemetryUnit = (key:string) => config.telemetry.unitMap[key as keyof typeof config.telemetry.unitMap] || '';

    const telemetryColor = (key:string) => {
      const colorMap:Record<string, string> = {
        temperature_aht30: 'green',
        temperature_bmp180: 'green',
        temperature: 'green',
        humidity: 'cyan',
        pressure: 'blue',
        altitude: 'purple',
        battery: 'gold',
      };

      return colorMap[key] || 'blue';
    };

    const telemetryToneClass = (key:string) => {
      if (/temperature/i.test(key)) return 'telemetry-card--temperature';
      if (/humidity/i.test(key)) return 'telemetry-card--humidity';
      if (/pressure/i.test(key)) return 'telemetry-card--pressure';
      return 'telemetry-card--default';
    };

    const formatDateTime = (value:number) => {
      if (!value) return '--';
      return dayjs(value).format('YYYY-MM-DD HH:mm:ss');
    };

    const formatRelativeTime = (value:number) => {
      if (!value) return '--';
      const diffMinutes = Math.max(0, dayjs().diff(dayjs(value), 'minute'));
      if (diffMinutes < 1) return '刚刚';
      if (diffMinutes < 60) return `${diffMinutes} 分钟前`;
      const diffHours = Math.floor(diffMinutes / 60);
      if (diffHours < 24) return `${diffHours} 小时前`;
      return `${Math.floor(diffHours / 24)} 天前`;
    };

    const formatTelemetryValue = (key:string, value:string | number | boolean | undefined | null) => {
      if (typeof value === 'boolean') return value ? '是' : '否';
      const numericValue = toNumber(value);
      if (numericValue !== null) {
        return numericValue.toFixed(getDigitsByKey(key));
      }
      if (value === undefined || value === null || value === '') return '--';
      return String(value);
    };

    const clearReconnectTimer = () => {
      if (reconnectTimer.value !== null) {
        window.clearTimeout(reconnectTimer.value);
        reconnectTimer.value = null;
      }
    };

    const clearTokenRefreshTimer = () => {
      if (tokenRefreshTimer.value !== null) {
        window.clearTimeout(tokenRefreshTimer.value);
        tokenRefreshTimer.value = null;
      }
    };

    const closeSocket = () => {
      clearReconnectTimer();
      if (!socketInstance.value) return;

      const currentSocket = socketInstance.value;
      socketInstance.value = null;
      ignoreNextClose.value = true;
      currentSocket.close();
    };

    const resetRealtimeAuth = () => {
      clearTokenRefreshTimer();
      detailState.realtimeJwt = '';
      detailState.realtimeExpTime = 0;
    };

    const resetDetailData = () => {
      detailState.errorMessage = '';
      detailState.status = 'idle';
      detailState.socketStatus = 'idle';
      detailState.telemetryKeys = [];
      detailState.latestTelemetry = {};
      detailState.historyTelemetry = {};
      detailState.lastUpdateTs = 0;
      resetRealtimeAuth();
    };

    const updateLatestTelemetry = (map:ThingsBoardTelemetryMap) => {
      Object.entries(map).forEach(([key, points]) => {
        if (!points.length) return;
        const latestPoint = pickLatestPoint(points);
        detailState.latestTelemetry[key] = latestPoint;
        detailState.lastUpdateTs = Math.max(detailState.lastUpdateTs, latestPoint?.ts || 0);
      });
    };

    const updateHistoryTelemetry = (map:ThingsBoardTelemetryMap) => {
      Object.entries(map).forEach(([key, points]) => {
        detailState.historyTelemetry[key] = mergeSeries(detailState.historyTelemetry[key] || [], points);
      });
    };

    const isCurrentSession = (sessionId:number) => sessionId === detailSessionId.value && detailState.visible;

    const canMaintainRealtime = () => (
      !destroyed.value
      && pageActive.value
      && detailState.visible
      && config.realtime.enabled
      && Boolean(detailState.device?.id)
      && detailState.telemetryKeys.length > 0
    );

    const createHistoryChartOption = (key:string, color:string, list:ThingsBoardTelemetryPoint[]):LineECOption => ({
      animation: false,
      color: [color],
      tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(10, 27, 43, 0.92)',
        borderWidth: 0,
        textStyle: {
          color: '#eef7ff',
        },
        formatter: (params:any) => {
          const rows = Array.isArray(params) ? params : [params];
          const title = dayjs(rows[0]?.value?.[0]).format('MM-DD HH:mm:ss');
          const value = Number(rows[0]?.value?.[1] ?? 0).toFixed(getDigitsByKey(key));
          return `${title}<br/>${telemetryLabel(key)}: ${value}${telemetryUnit(key)}`;
        },
      },
      grid: {
        left: 40,
        right: 18,
        top: 18,
        bottom: 30,
      },
      xAxis: {
        type: 'time',
        axisLine: {
          lineStyle: {
            color: 'rgba(74, 107, 132, 0.22)',
          },
        },
        axisLabel: {
          color: '#6d8498',
        },
        splitLine: {
          show: false,
        },
      },
      yAxis: {
        type: 'value',
        name: telemetryUnit(key),
        nameTextStyle: {
          color: '#6d8498',
        },
        axisLabel: {
          color: '#6d8498',
        },
        splitLine: {
          lineStyle: {
            color: 'rgba(102, 136, 161, 0.12)',
          },
        },
      },
      series: [{
        name: telemetryLabel(key),
        type: 'line',
        smooth: true,
        symbol: 'none',
        lineStyle: {
          width: 3,
        },
        areaStyle: {
          opacity: 0.12,
        },
        data: sortTelemetryPoints(list)
          .map((point) => [point.ts, toNumber(point.value)])
          .filter((point) => point[1] !== null),
      }],
    });

    const chartOptionMap = computed<Record<string, LineECOption>>(() => chartTelemetryKeys.value.reduce<Record<string, LineECOption>>((acc, key, index) => {
      acc[key] = createHistoryChartOption(
        key,
        CHART_COLORS[index % CHART_COLORS.length],
        detailState.historyTelemetry[key] || [],
      );
      return acc;
    }, {}));

    const loadDeviceList = async () => {
      if (configWarnings.value.length) return;

      pageState.loading = true;
      pageState.errorMessage = '';

      try {
        const jwt = await fetchTenantJwt();
        const firstPage = await client.getTenantDevices(jwt, {
          page: 0,
          pageSize: config.list.pageSize,
          sortProperty: 'createdTime',
          sortOrder: 'DESC',
        });

        const extraPageTasks = Array.from(
          { length: Math.max(0, firstPage.totalPages - 1) },
          (_, index) => client.getTenantDevices(jwt, {
            page: index + 1,
            pageSize: config.list.pageSize,
            sortProperty: 'createdTime',
            sortOrder: 'DESC',
          }),
        );

        const extraPages = extraPageTasks.length ? await Promise.all(extraPageTasks) : [];
        const devices = [firstPage, ...extraPages].flatMap((pageData) => pageData.data);

        pageState.devices = devices;
        pageState.totalDevices = firstPage.totalElements || devices.length;
      } catch (error:any) {
        pageState.errorMessage = error.message || '传感器列表读取失败';
      } finally {
        pageState.loading = false;
      }
    };

    const scheduleTokenRefresh = (sessionId:number) => {
      clearTokenRefreshTimer();
      if (!canMaintainRealtime() || !detailState.realtimeJwt || !isCurrentSession(sessionId)) return;

      const expiryTs = detailState.realtimeExpTime;
      if (!expiryTs) return;

      const remainingMs = expiryTs - Date.now();
      if (remainingMs <= 0) return;

      const delayMs = remainingMs > TOKEN_REFRESH_LEAD_MS
        ? remainingMs - TOKEN_REFRESH_LEAD_MS
        : Math.max(remainingMs - 1000, 0);
      tokenRefreshTimer.value = window.setTimeout(() => {
        refreshRealtimeSession(sessionId);
      }, delayMs);
    };

    const updateRealtimeAuth = (sessionId:number, token:string, expTime:number) => {
      if (!isCurrentSession(sessionId)) return;
      detailState.realtimeJwt = token;
      detailState.realtimeExpTime = expTime;
      scheduleTokenRefresh(sessionId);
    };

    const fetchTenantJwt = async (forceRefresh = false) => ensureThingsBoardTenantToken(client, config.auth.username, {
      forceRefresh,
      refreshLeadMs: TOKEN_REFRESH_LEAD_MS,
    });

    const ensureRealtimeJwt = async (sessionId:number, forceRefresh = false) => {
      if (!forceRefresh && detailState.realtimeJwt && detailState.realtimeExpTime) {
        if (detailState.realtimeExpTime > Date.now()) {
          scheduleTokenRefresh(sessionId);
          return detailState.realtimeJwt;
        }
      }

      const result = await fetchTenantJwt(forceRefresh);
      updateRealtimeAuth(sessionId, result.token, result.expTime);
      return result.token;
    };

    const scheduleReconnect = (sessionId:number) => {
      clearReconnectTimer();
      if (!canMaintainRealtime() || !isCurrentSession(sessionId)) return;

      detailState.socketStatus = 'reconnecting';
      reconnectTimer.value = window.setTimeout(() => {
        connectRealtime(sessionId);
      }, config.realtime.reconnectDelayMs);
    };

    const refreshRealtimeSession = async (sessionId:number) => {
      clearTokenRefreshTimer();
      if (!canMaintainRealtime() || !isCurrentSession(sessionId)) return;

      try {
        await ensureRealtimeJwt(sessionId, true);
        if (socketInstance.value) {
          connectRealtime(sessionId);
        }
      } catch (error:any) {
        if (!isCurrentSession(sessionId)) return;
        detailState.socketStatus = 'error';
        detailState.errorMessage = `实时令牌刷新失败：${error.message || 'ThingsBoard 刷新失败'}`;
        scheduleReconnect(sessionId);
      }
    };

    const connectRealtime = async (sessionId:number) => {
      if (!canMaintainRealtime() || !isCurrentSession(sessionId)) {
        detailState.socketStatus = 'closed';
        return;
      }

      closeSocket();
      detailState.socketStatus = 'connecting';

      try {
        const jwt = await ensureRealtimeJwt(sessionId);
        if (!isCurrentSession(sessionId) || !detailState.device?.id) {
          detailState.socketStatus = 'closed';
          return;
        }

        socketInstance.value = client.connectTelemetryStream({
          jwt,
          deviceId: detailState.device.id,
          keys: detailState.telemetryKeys,
          onOpen: () => {
            if (!isCurrentSession(sessionId)) return;
            detailState.socketStatus = 'connected';
            detailState.status = 'ready';
            detailState.errorMessage = '';
          },
          onData: (telemetry) => {
            if (!isCurrentSession(sessionId)) return;
            updateLatestTelemetry(telemetry);
            updateHistoryTelemetry(telemetry);
          },
          onError: (error) => {
            if (!isCurrentSession(sessionId)) return;
            detailState.socketStatus = 'error';
            detailState.errorMessage = `实时订阅失败：${error.message}`;
          },
          onClose: () => {
            if (ignoreNextClose.value) {
              ignoreNextClose.value = false;
              return;
            }
            if (!isCurrentSession(sessionId)) return;
            detailState.socketStatus = 'closed';
            scheduleReconnect(sessionId);
          },
        });
      } catch (error:any) {
        if (!isCurrentSession(sessionId)) return;
        detailState.socketStatus = 'error';
        detailState.errorMessage = `实时连接失败：${error.message || 'ThingsBoard 登录失败'}`;
        scheduleReconnect(sessionId);
      }
    };

    const loadTelemetrySnapshot = async (deviceId:string, keys:string[]) => {
      if (!keys.length) {
        detailState.latestTelemetry = {};
        detailState.historyTelemetry = {};
        return;
      }

      const now = Date.now();
      const startTs = Math.max(0, now - config.history.windowMs);
      const jwt = (await fetchTenantJwt()).token;
      const [latestResult, historyResult] = await Promise.all([
        settlePromise(client.getLatestTelemetry(jwt, 'DEVICE', deviceId, keys, config.history.windowMs)),
        settlePromise(client.getHistoricalTelemetry({
          auth: jwt,
          entityType: 'DEVICE',
          entityId: deviceId,
          keys,
          startTs,
          endTs: now,
          limit: config.history.limit,
          orderBy: 'ASC',
        })),
      ]);

      detailState.latestTelemetry = {};
      detailState.historyTelemetry = {};
      detailState.lastUpdateTs = 0;

      if (latestResult.status === 'fulfilled') {
        updateLatestTelemetry(latestResult.value);
      }

      if (historyResult.status === 'fulfilled') {
        updateHistoryTelemetry(historyResult.value);
      }

      if (latestResult.status === 'rejected' && historyResult.status === 'rejected') {
        throw latestResult.reason || historyResult.reason;
      }
    };

    const bootstrapDeviceDetail = async (device:ThingsBoardDeviceInfo) => {
      const sessionId = detailSessionId.value + 1;
      detailSessionId.value = sessionId;
      closeSocket();
      detailState.booting = true;
      detailState.historyRefreshing = false;
      detailState.visible = true;
      detailState.device = device;
      resetDetailData();
      detailState.status = 'loading';

      try {
        const jwt = (await fetchTenantJwt()).token;
        const resolvedDevice = await client.getDeviceById(jwt, device.id);
        if (!isCurrentSession(sessionId)) return;

        detailState.device = {
          ...device,
          ...resolvedDevice,
        };

        const keys = await client.getTelemetryKeys(jwt, 'DEVICE', device.id);
        if (!isCurrentSession(sessionId)) return;

        detailState.telemetryKeys = Array.from(new Set(keys));
        await loadTelemetrySnapshot(device.id, detailState.telemetryKeys);
        if (!isCurrentSession(sessionId)) return;

        detailState.status = 'ready';
        detailState.socketStatus = config.realtime.enabled && detailState.telemetryKeys.length ? 'connecting' : 'closed';

        if (config.realtime.enabled && detailState.telemetryKeys.length) {
          connectRealtime(sessionId);
        }
      } catch (error:any) {
        if (!isCurrentSession(sessionId)) return;
        detailState.status = 'error';
        detailState.socketStatus = 'error';
        detailState.errorMessage = error.message || '设备详情读取失败';
      } finally {
        if (isCurrentSession(sessionId)) {
          detailState.booting = false;
        }
      }
    };

    const refreshCurrentHistory = async () => {
      if (!detailState.device?.id) return;

      const sessionId = detailSessionId.value;
      detailState.historyRefreshing = true;
      detailState.errorMessage = '';

      try {
        await loadTelemetrySnapshot(detailState.device.id, detailState.telemetryKeys);
        if (!isCurrentSession(sessionId)) return;
        detailState.status = 'ready';
      } catch (error:any) {
        if (!isCurrentSession(sessionId)) return;
        detailState.errorMessage = error.message || '历史数据刷新失败';
        detailState.status = 'error';
      } finally {
        if (isCurrentSession(sessionId)) {
          detailState.historyRefreshing = false;
        }
      }
    };

    const openDetailDrawer = (device:ThingsBoardDeviceInfo) => {
      bootstrapDeviceDetail(device);
    };

    const closeDetailDrawer = () => {
      detailSessionId.value += 1;
      detailState.visible = false;
      detailState.booting = false;
      detailState.historyRefreshing = false;
      detailState.socketStatus = 'closed';
      closeSocket();
      resetRealtimeAuth();
    };

    const reloadCurrentDevice = () => {
      if (!detailState.device) return;
      bootstrapDeviceDetail(detailState.device);
    };

    const refreshDeviceList = async () => {
      await loadDeviceList();
    };

    loadDeviceList();

    onActivated(() => {
      pageActive.value = true;
      if (detailState.visible && detailState.device?.id && detailState.telemetryKeys.length && detailState.socketStatus !== 'connected') {
        connectRealtime(detailSessionId.value);
      }
    });

    onDeactivated(() => {
      pageActive.value = false;
      clearTokenRefreshTimer();
      closeSocket();
    });

    onBeforeUnmount(() => {
      destroyed.value = true;
      clearTokenRefreshTimer();
      closeSocket();
    });

    return {
      chartOptionMap,
      chartTelemetryKeys,
      configWarnings,
      detailState,
      detailTag,
      drawerTitle,
      filteredDevices,
      formatDateTime,
      formatRelativeTime,
      formatTelemetryValue,
      historyWindowLabel,
      openDetailDrawer,
      pageState,
      refreshCurrentHistory,
      refreshDeviceList,
      reloadCurrentDevice,
      socketTag,
      tableColumns,
      telemetryColor,
      telemetryDisplayKeys,
      telemetryLabel,
      telemetryToneClass,
      telemetryUnit,
      closeDetailDrawer,
      activeDeviceCount,
    };
  },
});
</script>

<style lang="less" scoped>
.tb-sensor-list {
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    height: calc(100% - 32px);
    min-height: 0;
    padding: 14px 12px 18px;
    overflow: hidden;
    color: #17354f;

    :deep(.content-scroll) {
        flex: 1;
        min-height: 0;
        padding-bottom: 20px;
        overflow-x: hidden;
        overflow-y: auto;
    }

    :deep(.ant-drawer-content) {
        background: linear-gradient(180deg, #f4f9ff 0%, #edf5fc 100%);
    }

    :deep(.ant-drawer-header) {
        background: #f4f9ff;
        border-bottom: 1px solid rgba(157, 201, 244, 0.28);
    }

    :deep(.ant-drawer-title) {
        color: #17354f;
    }

    .top-alert {
        margin-bottom: 12px;
        border-radius: 16px;
    }

    .warning-list {
        padding-left: 18px;
        margin: 6px 0 0;
    }
}

.compact-hero,
.drawer-bar {
    display: flex;
    flex-wrap: wrap;
    gap: 14px;
    align-items: center;
    justify-content: space-between;
    padding: 16px 18px;
    margin-bottom: 12px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(244, 249, 255, 0.96) 100%);
    border: 1px solid rgba(157, 201, 244, 0.2);
    border-radius: 22px;
    box-shadow:
        inset 0 1px 0 rgba(255, 255, 255, 0.82),
        0 14px 30px rgba(12, 42, 64, 0.08);
}

.compact-hero__identity,
.drawer-bar__identity {
    min-width: 0;
}

.drawer-bar__identity {
    flex: 1 1 100%;
}

.drawer-bar__title-row {
    display: flex;
    flex-wrap: nowrap;
    gap: 14px;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    min-width: 0;
}

.compact-hero__eyebrow,
.drawer-bar__eyebrow {
    margin-bottom: 4px;
    font-size: 11px;
    font-weight: 700;
    color: #7f95aa;
    letter-spacing: 0.18em;
}

.compact-hero__identity h1,
.drawer-bar__identity h2 {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    line-height: 1.15;
    color: #17354f;
}

.compact-hero__meta,
.drawer-bar__meta {
    margin: 6px 0 0;
    font-size: 13px;
    color: #728ca0;
}

.compact-hero__actions,
.drawer-bar__actions,
.drawer-bar__status {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    align-items: center;
}

.drawer-bar__actions {
    flex-shrink: 0;
    flex-wrap: nowrap;
}

.compact-hero__search {
    width: 260px;
}

.summary-grid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin-bottom: 12px;
}

.summary-card,
.list-card,
.drawer-card,
.history-item {
    overflow: hidden;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.97) 0%, rgba(245, 250, 255, 0.95) 100%);
    border: 1px solid rgba(157, 201, 244, 0.2);
    border-radius: 24px;
    box-shadow:
        inset 0 1px 0 rgba(255, 255, 255, 0.82),
        0 18px 40px rgba(12, 42, 64, 0.08);
}

.summary-card {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 16px 18px;

    strong {
        font-size: 30px;
        line-height: 1;
        color: #17354f;
    }
}

.summary-card__label {
    font-size: 13px;
    color: #728ca0;
}

.list-card,
.drawer-card {
    :deep(.ant-card-head) {
        min-height: 48px;
        border-bottom: none;
    }

    :deep(.ant-card-head-title) {
        padding-top: 14px;
        font-size: 16px;
        font-weight: 700;
        color: #17354f;
    }

    :deep(.ant-card-extra) {
        padding-top: 13px;
    }

    :deep(.ant-card-body) {
        padding: 0 14px 14px;
    }
}

.list-card__extra,
.history-extra {
    font-size: 13px;
    color: #7f95aa;
}

.device-cell {
    display: flex;
    flex-direction: column;
    gap: 6px;

    strong {
        color: #17354f;
    }

    span {
        font-size: 13px;
        color: #728ca0;
    }
}

.device-meta {
    display: flex;
    flex-direction: column;
    gap: 6px;

    span {
        font-size: 13px;
        color: #728ca0;
    }
}

.drawer-shell {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.status-pill {
    display: flex;
    gap: 10px;
    align-items: center;
    padding: 10px 12px;
    color: #17354f;
    background: rgba(237, 245, 252, 0.9);
    border: 1px solid rgba(194, 230, 255, 0.3);
    border-radius: 14px;

    span {
        font-size: 12px;
        color: #68829a;
    }

    strong {
        font-size: 13px;
        font-weight: 700;
        color: #17354f;
    }
}

.drawer-card + .drawer-card {
    margin-top: 12px;
}

.telemetry-grid,
.history-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 12px;
}

.telemetry-card {
    padding: 14px 16px;
    background: rgba(237, 245, 252, 0.82);
    border: 1px solid rgba(157, 201, 244, 0.18);
    border-radius: 20px;
}

.telemetry-card--temperature {
    background: linear-gradient(180deg, rgba(234, 248, 239, 0.95) 0%, rgba(245, 252, 247, 0.98) 100%);
}

.telemetry-card--humidity {
    background: linear-gradient(180deg, rgba(232, 246, 252, 0.95) 0%, rgba(245, 250, 255, 0.98) 100%);
}

.telemetry-card--pressure {
    background: linear-gradient(180deg, rgba(239, 238, 255, 0.95) 0%, rgba(247, 247, 255, 0.98) 100%);
}

.telemetry-card__head {
    display: flex;
    gap: 12px;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 14px;

    p {
        margin: 0 0 4px;
        font-size: 15px;
        font-weight: 700;
        color: #17354f;
    }

    span {
        font-size: 12px;
        color: #7f95aa;
    }
}

.telemetry-card__value {
    font-size: 34px;
    font-weight: 700;
    line-height: 1;
    color: #17354f;
    letter-spacing: -0.03em;

    small {
        margin-left: 6px;
        font-size: 15px;
        font-weight: 600;
        color: #5d7f97;
        letter-spacing: 0;
    }
}

.telemetry-card__meta {
    margin: 12px 0 0;
    font-size: 13px;
    color: #8199ad;
}

.history-item {
    padding: 14px 14px 10px;
}

.history-item__header {
    display: flex;
    gap: 12px;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 8px;

    h3 {
        margin: 0 0 4px;
        font-size: 16px;
        font-weight: 700;
        color: #17354f;
    }

    span {
        font-size: 12px;
        color: #7f95aa;
    }
}

.history-item__chart {
    height: 190px;
}

@media screen and (max-width: 1360px) {
    .summary-grid,
    .telemetry-grid,
    .history-grid {
        grid-template-columns: 1fr;
    }
}

@media screen and (max-width: 980px) {
    .compact-hero,
    .drawer-bar {
        align-items: flex-start;
    }

    .compact-hero__actions,
    .drawer-bar__actions {
        width: 100%;
    }

    .compact-hero__search {
        width: 100%;
    }
}
</style>
