<template>
  <div class="tb-sensor-hybrid-demo content-section">
    <div class="content-scroll">
      <section class="compact-bar">
        <div class="compact-bar__identity">
          <p class="compact-bar__eyebrow">
            THINGSBOARD HYBRID SENSOR STREAM
          </p>
          <h1>{{ deviceTitle }}</h1>
          <p class="compact-bar__meta">
            最后更新 {{ formatDateTime(lastUpdateTs) }}
          </p>
        </div>
        <div class="compact-bar__status">
          <div class="status-pill">
            <span>阶段</span>
            <a-tag :color="phaseTag.color">
              {{ phaseTag.label }}
            </a-tag>
          </div>
          <div class="status-pill">
            <span>REST</span>
            <a-tag :color="restTag.color">
              {{ restTag.label }}
            </a-tag>
          </div>
          <div class="status-pill">
            <span>实时</span>
            <a-tag :color="socketTag.color">
              {{ socketTag.label }}
            </a-tag>
          </div>
        </div>
        <div class="compact-bar__actions">
          <a-button
            type="primary"
            :loading="booting"
            @click="bootstrap"
          >
            重新连接
          </a-button>
          <a-button
            :loading="historyRefreshing"
            @click="refreshHistory"
          >
            刷新历史
          </a-button>
        </div>
      </section>

      <a-alert
        v-if="configWarnings.length"
        class="top-alert"
        type="info"
        show-icon
        message="当前还缺少运行配置"
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
        v-if="errorMessage"
        class="top-alert"
        type="warning"
        show-icon
        message="当前存在链路异常"
        :description="errorMessage"
      />

      <div class="dashboard-shell">
        <section class="overview-grid">
          <article class="metric-card metric-card--primary metric-card--temperature">
            <div class="metric-card__copy">
              <div class="metric-card__head">
                <div class="metric-icon metric-icon--temperature">
                  <span>T</span>
                </div>
                <div>
                  <p class="metric-card__eyebrow">
                    Primary Temperature
                  </p>
                  <h3>温度 AHT30</h3>
                </div>
              </div>
              <p class="metric-card__meta">
                最近更新 {{ formatRelativeTime(primaryMetricTime(config.telemetry.temperatureKey)) }}
              </p>
            </div>
            <div class="metric-card__value metric-card__value--primary">
              {{ formatMetricValue(config.telemetry.temperatureKey, 1) }}
              <small>°C</small>
            </div>
          </article>

          <article class="metric-card metric-card--compact metric-card--humidity">
            <div class="metric-card__copy">
              <div class="metric-card__head">
                <div class="metric-icon metric-icon--humidity">
                  <span>H</span>
                </div>
                <div>
                  <p class="metric-card__eyebrow">
                    Relative Humidity
                  </p>
                  <h3>湿度</h3>
                </div>
              </div>
              <p class="metric-card__meta">
                最近更新 {{ formatRelativeTime(primaryMetricTime(config.telemetry.humidityKey)) }}
              </p>
            </div>
            <div class="metric-card__value metric-card__value--compact">
              {{ formatMetricValue(config.telemetry.humidityKey, 1) }}
              <small>%</small>
            </div>
          </article>

          <article class="metric-card metric-card--compact metric-card--pressure">
            <div class="metric-card__copy">
              <div class="metric-card__head">
                <div class="metric-icon metric-icon--pressure">
                  <span>P</span>
                </div>
                <div>
                  <p class="metric-card__eyebrow">
                    Air Pressure
                  </p>
                  <h3>气压</h3>
                </div>
              </div>
              <p class="metric-card__meta">
                最近更新 {{ formatRelativeTime(primaryMetricTime(config.telemetry.pressureKey)) }}
              </p>
            </div>
            <div class="metric-card__value metric-card__value--compact metric-card__value--pressure">
              {{ formatMetricValue(config.telemetry.pressureKey, 2) }}
              <small>hPa</small>
            </div>
          </article>

          <article class="aux-card aux-card--compact">
            <header class="aux-card__header">
              <div>
                <p class="aux-card__eyebrow">
                  Secondary Telemetry
                </p>
                <h3>辅助指标</h3>
              </div>
              <span class="aux-card__window">历史窗口 {{ historyWindowLabel }}</span>
            </header>
            <div class="aux-grid">
              <div class="aux-item">
                <span class="aux-item__label">BMP180 温度</span>
                <strong>{{ formatMetricValue('temperature_bmp180', 1) }} °C</strong>
              </div>
              <div class="aux-item">
                <span class="aux-item__label">海拔</span>
                <strong>{{ formatMetricValue('altitude', 2) }} m</strong>
              </div>
            </div>
          </article>
        </section>

        <section class="chart-grid">
          <a-card
            class="chart-card"
            :bordered="false"
          >
            <template #title>
              气候趋势
            </template>
            <template #extra>
              <span class="chart-extra">温度 AHT30 / 湿度</span>
            </template>
            <div class="chart-card__body">
              <echart :option="climateChartOption" />
            </div>
          </a-card>

          <a-card
            class="chart-card"
            :bordered="false"
          >
            <template #title>
              气压序列
            </template>
            <template #extra>
              <span class="chart-extra">{{ socketStatus === 'connected' ? '实时追加中' : '历史快照' }}</span>
            </template>
            <div class="chart-card__body">
              <echart :option="pressureChartOption" />
            </div>
          </a-card>
        </section>
      </div>
    </div>
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
  toRefs,
} from 'vue';
import dayjs from 'dayjs';
import Echart, { LineECOption } from '~components/echart';
import ThingsBoard, {
  ThingsBoardDeviceInfo,
  ThingsBoardTelemetryMap,
  ThingsBoardTelemetryPoint,
} from '@/models/thinksboard';
import { ensureThingsBoardTenantToken } from '@/models/thinksboard/tenant-token';
import sensorHybridConfig from './thingsboard-sensor-hybrid/config';

type PhaseStatus = 'idle' | 'awaiting-config' | 'loading-device' | 'loading-history' | 'connecting-realtime' | 'ready' | 'error';
type RestStatus = 'idle' | 'loading' | 'ready' | 'error';
type SocketStatus = 'idle' | 'connecting' | 'connected' | 'reconnecting' | 'error' | 'closed';

const HISTORY_KEYS_LIMIT = 240;
const TOKEN_REFRESH_LEAD_MS = 60 * 1000;

function settlePromise<T>(promise:Promise<T>) {
  return promise.then(
    (value) => ({ status: 'fulfilled' as const, value }),
    (reason) => ({ status: 'rejected' as const, reason }),
  );
}

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

function normalizeMetricValue(value:string | number | boolean | undefined | null, digits:number) {
  if (typeof value === 'number') {
    return value.toFixed(digits);
  }
  if (value === undefined || value === null || value === '') return '--';
  return String(value);
}

function createMetricSeries(series:ThingsBoardTelemetryPoint[]) {
  return sortTelemetryPoints(series).map((point) => [point.ts, Number(point.value)]);
}

export default defineComponent({
  name: 'ThingsboardSensorHybridDemo',
  components: {
    Echart,
  },
  setup() {
    const config = sensorHybridConfig;
    const client = new ThingsBoard(config.baseUrl);
    const socketInstance = ref<WebSocket | null>(null);
    const reconnectTimer = ref<number | null>(null);
    const tokenRefreshTimer = ref<number | null>(null);
    const destroyed = ref(false);
    const pageActive = ref(true);
    const ignoreNextClose = ref(false);

    const state = reactive({
      phase: 'idle' as PhaseStatus,
      restStatus: 'idle' as RestStatus,
      socketStatus: 'idle' as SocketStatus,
      booting: false,
      historyRefreshing: false,
      errorMessage: '',
      realtimeJwt: '',
      realtimeExpTime: 0,
      resolvedDevice: null as ThingsBoardDeviceInfo | null,
      latestTelemetry: {} as Record<string, ThingsBoardTelemetryPoint | null>,
      historyTelemetry: {} as Record<string, ThingsBoardTelemetryPoint[]>,
      lastUpdateTs: 0,
    });

    const snapshotKeys = computed(() => {
      const keys = [
        config.telemetry.temperatureKey,
        config.telemetry.humidityKey,
        config.telemetry.pressureKey,
        ...config.telemetry.extraKeys,
      ].filter(Boolean);

      return Array.from(new Set(keys));
    });

    const chartKeys = computed(() => [
      config.telemetry.temperatureKey,
      config.telemetry.humidityKey,
      config.telemetry.pressureKey,
    ]);

    const configWarnings = computed(() => {
      const warnings:string[] = [];

      if (!config.auth.username) warnings.push('请填写用于服务端获取 ThingsBoard token 的租户标识。');
      if (!config.device.id && !config.device.name) warnings.push('请至少填写 device.id 或 device.name。');
      if (!config.telemetry.temperatureKey || !config.telemetry.humidityKey || !config.telemetry.pressureKey) {
        warnings.push('请确认温度、湿度、气压 telemetry key。');
      }

      return warnings;
    });

    const phaseTag = computed(() => {
      const map:Record<PhaseStatus, { label:string, color:string }> = {
        idle: { label: '未启动', color: 'default' },
        'awaiting-config': { label: '待补配置', color: 'gold' },
        'loading-device': { label: '读取设备', color: 'processing' },
        'loading-history': { label: '拉取历史', color: 'processing' },
        'connecting-realtime': { label: '建立实时', color: 'cyan' },
        ready: { label: '已连接', color: 'success' },
        error: { label: '异常', color: 'error' },
      };

      return map[state.phase];
    });

    const restTag = computed(() => {
      const map:Record<RestStatus, { label:string, color:string }> = {
        idle: { label: '未启动', color: 'default' },
        loading: { label: '读取中', color: 'processing' },
        ready: { label: '可用', color: 'success' },
        error: { label: '失败', color: 'error' },
      };

      return map[state.restStatus];
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

      return map[state.socketStatus];
    });

    const historyWindowLabel = computed(() => {
      const hours = Math.round(config.history.windowMs / (60 * 60 * 1000));
      return `${hours}h`;
    });

    const deviceTitle = computed(() => state.resolvedDevice?.name || config.device.name || '温湿度与气压');

    const climateChartOption = computed<LineECOption>(() => ({
      animation: false,
      color: ['#1a8eff', '#16b57f'],
      legend: {
        top: 0,
        left: 0,
        itemGap: 18,
        icon: 'circle',
        textStyle: {
          color: '#35556f',
          fontSize: 13,
        },
      },
      tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(10, 27, 43, 0.92)',
        borderWidth: 0,
        textStyle: {
          color: '#eef7ff',
        },
        formatter: (params:any) => {
          const list = Array.isArray(params) ? params : [params];
          const title = dayjs(list[0]?.value?.[0]).format('MM-DD HH:mm:ss');
          const lines = list.map((item:any) => `${item.marker}${item.seriesName}: ${Number(item.value?.[1]).toFixed(item.seriesName === '湿度' ? 1 : 1)}${item.seriesName === '湿度' ? '%' : '°C'}`);
          return [title, ...lines].join('<br/>');
        },
      },
      grid: {
        left: 44,
        right: 48,
        top: 56,
        bottom: 34,
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
      yAxis: [
        {
          type: 'value',
          name: '°C',
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
        {
          type: 'value',
          name: '%',
          nameTextStyle: {
            color: '#6d8498',
          },
          axisLabel: {
            color: '#6d8498',
          },
          splitLine: {
            show: false,
          },
        },
      ],
      series: [
        {
          name: '温度',
          type: 'line',
          smooth: true,
          symbol: 'none',
          lineStyle: {
            width: 3,
          },
          areaStyle: {
            opacity: 0.16,
          },
          data: createMetricSeries(state.historyTelemetry[config.telemetry.temperatureKey] || []),
        },
        {
          name: '湿度',
          type: 'line',
          yAxisIndex: 1,
          smooth: true,
          symbol: 'none',
          lineStyle: {
            width: 3,
          },
          areaStyle: {
            opacity: 0.12,
          },
          data: createMetricSeries(state.historyTelemetry[config.telemetry.humidityKey] || []),
        },
      ],
    }));

    const pressureChartOption = computed<LineECOption>(() => ({
      animation: false,
      color: ['#7158ff'],
      legend: {
        top: 0,
        left: 0,
        icon: 'circle',
        textStyle: {
          color: '#35556f',
          fontSize: 13,
        },
      },
      tooltip: {
        trigger: 'axis',
        backgroundColor: 'rgba(10, 27, 43, 0.92)',
        borderWidth: 0,
        textStyle: {
          color: '#eef7ff',
        },
        formatter: (params:any) => {
          const list = Array.isArray(params) ? params : [params];
          const title = dayjs(list[0]?.value?.[0]).format('MM-DD HH:mm:ss');
          const lines = list.map((item:any) => `${item.marker}${item.seriesName}: ${Number(item.value?.[1]).toFixed(2)} hPa`);
          return [title, ...lines].join('<br/>');
        },
      },
      grid: {
        left: 44,
        right: 24,
        top: 56,
        bottom: 34,
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
        name: 'hPa',
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
      series: [
        {
          name: '气压',
          type: 'line',
          smooth: true,
          symbol: 'none',
          lineStyle: {
            width: 3,
          },
          areaStyle: {
            opacity: 0.12,
          },
          data: createMetricSeries(state.historyTelemetry[config.telemetry.pressureKey] || []),
        },
      ],
    }));

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

    const updateLatestTelemetry = (map:ThingsBoardTelemetryMap) => {
      Object.entries(map).forEach(([key, points]) => {
        if (!points.length) return;
        const latestPoint = pickLatestPoint(points);
        state.latestTelemetry[key] = latestPoint;
        state.lastUpdateTs = Math.max(state.lastUpdateTs, latestPoint?.ts || 0);
      });
    };

    const updateHistoryTelemetry = (map:ThingsBoardTelemetryMap) => {
      Object.entries(map).forEach(([key, points]) => {
        state.historyTelemetry[key] = mergeSeries(state.historyTelemetry[key] || [], points);
      });
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

    const primaryMetricTime = (key:string) => state.latestTelemetry[key]?.ts || 0;

    const formatMetricValue = (key:string, digits:number) => normalizeMetricValue(state.latestTelemetry[key]?.value, digits);

    const resolveDevice = async () => {
      const jwt = await fetchTenantJwt();
      if (config.device.id) {
        return client.getDeviceById(jwt, config.device.id);
      }
      if (config.device.name) {
        return client.searchDeviceByName(jwt, config.device.name);
      }
      return null;
    };

    const loadHistorySnapshot = async () => {
      if (!state.resolvedDevice?.id) return;

      state.restStatus = 'loading';
      state.phase = 'loading-history';
      const now = Date.now();
      const startTs = Math.max(0, now - config.history.windowMs);
      const jwt = await fetchTenantJwt();

      const [latestResult, historyResult] = await Promise.all([
        settlePromise(client.getLatestTelemetry(jwt, 'DEVICE', state.resolvedDevice.id, snapshotKeys.value, config.history.windowMs)),
        settlePromise(client.getHistoricalTelemetry({
          auth: jwt,
          entityType: 'DEVICE',
          entityId: state.resolvedDevice.id,
          keys: chartKeys.value,
          startTs,
          endTs: now,
          limit: config.history.limit,
          orderBy: 'ASC',
        })),
      ]);

      if (latestResult.status === 'fulfilled') {
        state.latestTelemetry = {};
        updateLatestTelemetry(latestResult.value);
      }

      if (historyResult.status === 'fulfilled') {
        state.historyTelemetry = {};
        updateHistoryTelemetry(historyResult.value);
      }

      if (latestResult.status === 'rejected' && historyResult.status === 'rejected') {
        throw latestResult.reason || historyResult.reason;
      }

      state.restStatus = 'ready';
    };

    const scheduleTokenRefresh = () => {
      clearTokenRefreshTimer();
      if (!pageActive.value || !config.realtime.enabled || !state.realtimeJwt) return;

      const expiryTs = state.realtimeExpTime;
      if (!expiryTs) return;

      const remainingMs = expiryTs - Date.now();
      if (remainingMs <= 0) return;

      const delayMs = remainingMs > TOKEN_REFRESH_LEAD_MS
        ? remainingMs - TOKEN_REFRESH_LEAD_MS
        : Math.max(remainingMs - 1000, 0);
      tokenRefreshTimer.value = window.setTimeout(() => {
        refreshRealtimeSession();
      }, delayMs);
    };

    const updateRealtimeAuth = (token:string, expTime:number) => {
      state.realtimeJwt = token;
      state.realtimeExpTime = expTime;
      scheduleTokenRefresh();
    };

    const fetchTenantJwt = async (forceRefresh = false) => {
      const result = await ensureThingsBoardTenantToken(client, config.auth.username, {
        forceRefresh,
        refreshLeadMs: TOKEN_REFRESH_LEAD_MS,
      });
      return result;
    };

    const ensureRealtimeJwt = async (forceRefresh = false) => {
      if (!forceRefresh && state.realtimeJwt && state.realtimeExpTime) {
        if (state.realtimeExpTime > Date.now()) {
          scheduleTokenRefresh();
          return state.realtimeJwt;
        }
      }

      const result = await fetchTenantJwt(forceRefresh);
      updateRealtimeAuth(result.token, result.expTime);
      return result.token;
    };

    const refreshRealtimeSession = async () => {
      clearTokenRefreshTimer();
      if (destroyed.value || !pageActive.value || !config.realtime.enabled || !state.resolvedDevice?.id) return;

      try {
        await ensureRealtimeJwt(true);
        if (socketInstance.value) {
          connectRealtime();
        }
      } catch (error:any) {
        state.socketStatus = 'error';
        state.phase = state.restStatus === 'ready' ? 'ready' : 'error';
        state.errorMessage = `JWT 自动续期失败：${error.message || 'ThingsBoard 刷新失败'}`;
        if (!destroyed.value && pageActive.value && config.realtime.enabled) {
          scheduleReconnect();
        }
      }
    };

    const scheduleReconnect = () => {
      clearReconnectTimer();
      if (destroyed.value || !pageActive.value || !config.realtime.enabled || !state.resolvedDevice?.id) return;

      state.socketStatus = 'reconnecting';
      reconnectTimer.value = window.setTimeout(() => {
        connectRealtime();
      }, config.realtime.reconnectDelayMs);
    };

    const connectRealtime = async () => {
      if (!config.realtime.enabled || !state.resolvedDevice?.id || !pageActive.value) {
        state.socketStatus = 'closed';
        return;
      }

      closeSocket();
      state.phase = 'connecting-realtime';
      state.socketStatus = 'connecting';
      state.errorMessage = '';

      try {
        const jwt = await ensureRealtimeJwt();
        if (destroyed.value || !pageActive.value || !state.resolvedDevice?.id) {
          state.socketStatus = 'closed';
          return;
        }

        socketInstance.value = client.connectTelemetryStream({
          jwt,
          deviceId: state.resolvedDevice.id,
          keys: chartKeys.value,
          onOpen: () => {
            state.socketStatus = 'connected';
            state.phase = 'ready';
            state.errorMessage = '';
          },
          onData: (telemetry) => {
            updateLatestTelemetry(telemetry);
            updateHistoryTelemetry(telemetry);
          },
          onError: (error) => {
            state.socketStatus = 'error';
            state.phase = state.restStatus === 'ready' ? 'ready' : 'error';
            state.errorMessage = `实时订阅失败：${error.message}`;
          },
          onClose: () => {
            if (ignoreNextClose.value) {
              ignoreNextClose.value = false;
              return;
            }

            state.socketStatus = 'closed';
            state.phase = state.restStatus === 'ready' ? 'ready' : state.phase;
            if (!destroyed.value && pageActive.value && config.realtime.enabled) {
              scheduleReconnect();
            }
          },
        });
      } catch (error:any) {
        state.socketStatus = 'error';
        state.phase = state.restStatus === 'ready' ? 'ready' : 'error';
        state.errorMessage = `实时登录失败：${error.message || 'ThingsBoard 登录失败'}`;
        if (!destroyed.value && pageActive.value && config.realtime.enabled) {
          scheduleReconnect();
        }
      }
    };

    const refreshHistory = async () => {
      if (!state.resolvedDevice?.id) return;

      state.historyRefreshing = true;
      try {
        await loadHistorySnapshot();
        state.phase = state.socketStatus === 'connected' || !config.realtime.enabled ? 'ready' : 'connecting-realtime';
      } catch (error:any) {
        state.restStatus = 'error';
        state.errorMessage = error.message || '历史数据读取失败';
      } finally {
        state.historyRefreshing = false;
      }
    };

    const bootstrap = async () => {
      closeSocket();
      state.booting = true;
      state.phase = 'idle';
      state.restStatus = 'idle';
      state.socketStatus = 'idle';
      state.errorMessage = '';
      clearTokenRefreshTimer();
      state.realtimeJwt = '';
      state.realtimeExpTime = 0;
      state.latestTelemetry = {};
      state.historyTelemetry = {};
      state.lastUpdateTs = 0;
      state.resolvedDevice = null;

      if (configWarnings.value.length) {
        state.phase = 'awaiting-config';
        state.booting = false;
        return;
      }

      try {
        state.phase = 'loading-device';
        state.restStatus = 'loading';
        state.resolvedDevice = await resolveDevice();
        if (!state.resolvedDevice?.id) {
          throw new Error('没有找到目标设备。请确认 deviceId 或 deviceName。');
        }

        await loadHistorySnapshot();

        if (config.realtime.enabled) {
          connectRealtime();
        } else {
          state.phase = 'ready';
          state.socketStatus = 'closed';
        }
      } catch (error:any) {
        state.phase = 'error';
        state.restStatus = 'error';
        state.socketStatus = 'error';
        state.errorMessage = error.message || 'ThingsBoard 混合链路初始化失败';
      } finally {
        state.booting = false;
      }
    };

    bootstrap();

    onActivated(() => {
      pageActive.value = true;
      if (config.realtime.enabled && state.resolvedDevice?.id && state.socketStatus !== 'connected') {
        connectRealtime();
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
      config,
      configWarnings,
      climateChartOption,
      deviceTitle,
      formatDateTime,
      formatMetricValue,
      formatRelativeTime,
      historyWindowLabel,
      phaseTag,
      pressureChartOption,
      primaryMetricTime,
      refreshHistory,
      restTag,
      socketTag,
      bootstrap,
      ...toRefs(state),
    };
  },
});
</script>

<style lang="less" scoped>
.tb-sensor-hybrid-demo {
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

    .top-alert {
        margin-bottom: 12px;
        border-radius: 16px;
    }

    .warning-list {
        padding-left: 18px;
        margin: 6px 0 0;
    }
}

.compact-bar {
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

.compact-bar__identity {
    min-width: 0;
}

.compact-bar__eyebrow {
    margin-bottom: 4px;
    font-size: 11px;
    font-weight: 700;
    color: #7f95aa;
    letter-spacing: 0.18em;
}

.compact-bar__identity h1 {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    line-height: 1.15;
    color: #17354f;
}

.compact-bar__meta {
    margin: 6px 0 0;
    font-size: 13px;
    color: #728ca0;
}

.compact-bar__status {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    align-items: center;
}

.compact-bar__actions {
    display: flex;
    gap: 10px;
    align-items: center;
    margin-left: auto;
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
}

.dashboard-shell {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.overview-grid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
}

.metric-card,
.aux-card,
.chart-card {
    overflow: hidden;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.97) 0%, rgba(245, 250, 255, 0.95) 100%);
    border: 1px solid rgba(157, 201, 244, 0.2);
    border-radius: 26px;
    box-shadow:
        inset 0 1px 0 rgba(255, 255, 255, 0.82),
        0 18px 40px rgba(12, 42, 64, 0.08);
}

.metric-card {
    display: flex;
    gap: 20px;
    justify-content: space-between;
    padding: 16px 18px;
}

.metric-card--primary {
    flex-direction: column;
    justify-content: space-between;
    min-height: 132px;
}

.metric-card--compact {
    flex-direction: column;
    justify-content: space-between;
    min-height: 132px;
}

.metric-card__head {
    display: flex;
    gap: 12px;
    align-items: center;
}

.metric-card__copy {
    min-width: 0;
}

.metric-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 46px;
    height: 46px;
    font-size: 18px;
    font-weight: 700;
    color: #fff;
    border-radius: 16px;
    box-shadow: 0 10px 22px rgba(30, 86, 130, 0.16);
}

.metric-icon--temperature {
    background: linear-gradient(135deg, #2e8dff 0%, #20c7ff 100%);
}

.metric-icon--humidity {
    background: linear-gradient(135deg, #0fa67f 0%, #3bd89e 100%);
}

.metric-icon--pressure {
    background: linear-gradient(135deg, #6554ff 0%, #8e67ff 100%);
}

.metric-card__eyebrow,
.aux-card__eyebrow {
    margin-bottom: 4px;
    font-size: 12px;
    font-weight: 700;
    color: #7f95aa;
    text-transform: uppercase;
    letter-spacing: 0.08em;
}

.metric-card h3,
.aux-card h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
    color: #17354f;
}

.metric-card__value {
    font-size: 54px;
    font-weight: 700;
    line-height: 1;
    color: #179c65;
    letter-spacing: -0.04em;
    white-space: nowrap;

    small {
        margin-left: 8px;
        font-size: 22px;
        font-weight: 600;
        color: #5d7f97;
        letter-spacing: 0;
    }
}

.metric-card__value--primary {
    font-size: 42px;

    small {
        font-size: 18px;
    }
}

.metric-card__value--compact {
    font-size: 42px;

    small {
        font-size: 18px;
    }
}

.metric-card__value--pressure {
    color: #4a56d8;
}

.metric-card__meta {
    margin-top: 10px;
    font-size: 13px;
    color: #8199ad;
}

.metric-card--temperature .metric-card__value {
    color: #23a65d;
}

.metric-card--humidity .metric-card__value {
    color: #17a16d;
}

.metric-card--pressure .metric-card__value {
    color: #2d58b9;
}

.aux-card {
    grid-column: 1 / -1;
    padding: 14px 16px 16px;
}

.aux-card--compact {
    border-radius: 22px;
}

.aux-card__header {
    display: flex;
    gap: 12px;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 10px;
}

.aux-card__window {
    padding: 8px 12px;
    font-size: 12px;
    font-weight: 600;
    color: #56738b;
    background: #edf5fc;
    border-radius: 999px;
}

.aux-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 12px;
}

.aux-item {
    padding: 12px 14px;
    background: rgba(237, 245, 252, 0.8);
    border: 1px solid rgba(157, 201, 244, 0.18);
    border-radius: 18px;

    strong {
        display: block;
        margin-top: 6px;
        font-size: 18px;
        font-weight: 700;
        color: #17354f;
    }
}

.aux-item__label {
    font-size: 13px;
    color: #728ca0;
}

.chart-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 12px;
}

.chart-card {
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

.chart-extra {
    font-size: 13px;
    color: #7f95aa;
}

.chart-card__body {
    height: 206px;
}

@media screen and (max-width: 1640px) {
    .aux-card {
        grid-column: 1 / -1;
    }

    .chart-grid {
        grid-template-columns: 1fr;
    }
}

@media screen and (max-width: 1200px) {
    .compact-bar {
        align-items: flex-start;
    }

    .compact-bar__actions {
        width: 100%;
        margin-left: 0;
    }

    .overview-grid,
    .aux-grid {
        grid-template-columns: 1fr;
    }

    .metric-card,
    .metric-card--primary {
        flex-direction: column;
        align-items: flex-start;
    }

    .metric-card__value,
    .metric-card__value--primary {
        font-size: 48px;
    }

    .chart-card__body {
        height: 224px;
    }
}
</style>
