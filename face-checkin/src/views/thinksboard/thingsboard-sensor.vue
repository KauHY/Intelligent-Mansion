<template>
  <div class="tb-sensor-demo content-section">
    <div class="content-scroll">
      <section class="hero-panel">
        <div class="hero-copy">
          <p class="eyebrow">
            THINGSBOARD SENSOR STREAM
          </p>
          <h1>温湿度实时监测测试页</h1>
          <p class="hero-text">
            这个页面直接从前端使用 API key 连接 ThingsBoard，通过 REST 读取设备和 telemetry，并按固定间隔轮询最新数据。
          </p>
        </div>
        <div class="hero-actions">
          <a-button
            type="primary"
            :loading="booting"
            @click="bootstrap"
          >
            重新连接
          </a-button>
          <a-button
            :loading="snapshotRefreshing"
            @click="refreshSnapshot"
          >
            刷新快照
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

      <div class="dashboard-grid">
        <a-card
          class="status-card"
          :bordered="false"
        >
          <template #title>
            连接状态
          </template>
          <div class="status-stack">
            <div class="status-row">
              <span>阶段</span>
              <a-tag :color="phaseTag.color">
                {{ phaseTag.label }}
              </a-tag>
            </div>
            <div class="status-row">
              <span>实时刷新</span>
              <a-tag :color="realtimeTag.color">
                {{ realtimeTag.label }}
              </a-tag>
            </div>
            <div class="status-row">
              <span>设备定位</span>
              <span class="status-value">{{ deviceSourceLabel }}</span>
            </div>
            <div class="status-row">
              <span>最后更新</span>
              <span class="status-value">{{ formatDateTime(lastUpdateTs) }}</span>
            </div>
          </div>

          <div
            v-if="errorMessage"
            class="error-box"
          >
            {{ errorMessage }}
          </div>
        </a-card>

        <a-card
          class="config-card"
          :bordered="false"
        >
          <template #title>
            当前配置摘要
          </template>
          <a-descriptions
            :column="1"
            size="small"
          >
            <a-descriptions-item label="ThingsBoard 地址">
              {{ config.baseUrl }}
            </a-descriptions-item>
            <a-descriptions-item label="认证方式">
              API key
            </a-descriptions-item>
            <a-descriptions-item label="API key">
              {{ maskedApiKey }}
            </a-descriptions-item>
            <a-descriptions-item label="目标设备">
              {{ config.device.id || config.device.name || '未配置' }}
            </a-descriptions-item>
            <a-descriptions-item label="实时方式">
              {{ config.realtime.enabled ? `REST 轮询 / ${config.realtime.pollIntervalMs}ms` : '仅手动刷新' }}
            </a-descriptions-item>
            <a-descriptions-item label="温度 key">
              {{ config.telemetry.temperatureKey }}
            </a-descriptions-item>
            <a-descriptions-item label="湿度 key">
              {{ config.telemetry.humidityKey }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>

        <a-card
          v-for="metric in metricCards"
          :key="metric.key"
          class="metric-card"
          :bordered="false"
        >
          <div class="metric-card__top">
            <span>{{ metric.label }}</span>
            <a-tag :color="metric.tagColor">
              {{ metric.key }}
            </a-tag>
          </div>
          <div class="metric-card__value">
            {{ formatMetricValue(metric.key) }}
            <small>{{ metric.unit }}</small>
          </div>
          <p class="metric-card__meta">
            数据时间：{{ formatMetricTime(metric.key) }}
          </p>
        </a-card>
      </div>

      <div class="detail-grid">
        <a-card
          class="detail-card"
          :bordered="false"
        >
          <template #title>
            设备详情
          </template>
          <a-spin :spinning="booting">
            <a-descriptions
              v-if="resolvedDevice"
              :column="1"
              size="small"
            >
              <a-descriptions-item label="设备 ID">
                {{ resolvedDevice.id }}
              </a-descriptions-item>
              <a-descriptions-item label="设备名称">
                {{ resolvedDevice.name || '--' }}
              </a-descriptions-item>
              <a-descriptions-item label="标签">
                {{ resolvedDevice.label || '--' }}
              </a-descriptions-item>
              <a-descriptions-item label="类型">
                {{ resolvedDevice.type || '--' }}
              </a-descriptions-item>
              <a-descriptions-item label="设备档案">
                {{ resolvedDevice.deviceProfileName || '--' }}
              </a-descriptions-item>
              <a-descriptions-item label="客户">
                {{ resolvedDevice.customerTitle || '--' }}
              </a-descriptions-item>
            </a-descriptions>
            <a-empty
              v-else
              description="还没有解析到设备，请先补充 deviceId 或 deviceName。"
            />
          </a-spin>
        </a-card>

        <a-card
          class="detail-card"
          :bordered="false"
        >
          <template #title>
            Telemetry Key 探测
          </template>
          <div
            v-if="detectedTelemetryKeys.length"
            class="key-list"
          >
            <a-tag
              v-for="key in detectedTelemetryKeys"
              :key="key"
              color="blue"
            >
              {{ key }}
            </a-tag>
          </div>
          <a-empty
            v-else
            description="当前还没有读取到 telemetry key。设备未创建、API key 无权限或设备暂无上报时都可能出现。"
          />
        </a-card>
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
import ThingsBoard, {
  ThingsBoardDeviceInfo,
  ThingsBoardTelemetryMap,
  ThingsBoardTelemetryPoint,
} from '@/models/thinksboard';
import sensorDemoConfig from './thingsboard-sensor/config';

type PhaseStatus =
  | 'idle'
  | 'awaiting-config'
  | 'resolving-device'
  | 'loading-snapshot'
  | 'starting-polling'
  | 'ready'
  | 'error';

type RealtimeStatus = 'idle' | 'polling' | 'paused' | 'error';

function settlePromise<T>(promise:Promise<T>) {
  return promise.then(
    (value) => ({ status: 'fulfilled' as const, value }),
    (reason) => ({ status: 'rejected' as const, reason }),
  );
}

export default defineComponent({
  name: 'ThingsboardSensorDemo',
  setup() {
    const config = sensorDemoConfig;
    const client = new ThingsBoard(config.baseUrl);
    const pollingTimer = ref<number | null>(null);
    const destroyed = ref(false);
    const pageActive = ref(true);
    const pollInFlight = ref(false);

    const state = reactive({
      phase: 'idle' as PhaseStatus,
      realtimeStatus: 'idle' as RealtimeStatus,
      booting: false,
      snapshotRefreshing: false,
      errorMessage: '',
      resolvedDevice: null as ThingsBoardDeviceInfo | null,
      availableTelemetryKeys: [] as string[],
      latestTelemetry: {} as Record<string, ThingsBoardTelemetryPoint | null>,
      lastUpdateTs: 0,
    });

    const telemetryKeys = computed(() => {
      const keys = [
        config.telemetry.temperatureKey,
        config.telemetry.humidityKey,
      ].filter(Boolean);

      return Array.from(new Set(keys));
    });

    const detectedTelemetryKeys = computed(() => {
      const keys = [
        ...state.availableTelemetryKeys,
        ...Object.keys(state.latestTelemetry),
      ].filter(Boolean);

      return Array.from(new Set(keys));
    });

    const configWarnings = computed(() => {
      const warnings:string[] = [];

      if (!config.apiKey || config.apiKey === 'replace-with-api-key') {
        warnings.push('请在配置文件中填写真实可用的 ThingsBoard API key。');
      }
      if (!config.device.id && !config.device.name) {
        warnings.push('请至少填写 device.id 或 device.name 中的一个。');
      }
      if (!config.telemetry.temperatureKey || !config.telemetry.humidityKey) {
        warnings.push('请确认温度/湿度 telemetry key。');
      }

      return warnings;
    });

    const phaseTag = computed(() => {
      const map:Record<PhaseStatus, { label:string, color:string }> = {
        idle: { label: '未启动', color: 'default' },
        'awaiting-config': { label: '待补配置', color: 'gold' },
        'resolving-device': { label: '解析设备', color: 'processing' },
        'loading-snapshot': { label: '读取快照', color: 'processing' },
        'starting-polling': { label: '启动轮询', color: 'cyan' },
        ready: { label: '已连接', color: 'success' },
        error: { label: '异常', color: 'error' },
      };
      return map[state.phase];
    });

    const realtimeTag = computed(() => {
      const map:Record<RealtimeStatus, { label:string, color:string }> = {
        idle: { label: '未启动', color: 'default' },
        polling: { label: '轮询中', color: 'success' },
        paused: { label: '已暂停', color: 'default' },
        error: { label: '刷新异常', color: 'error' },
      };
      return map[state.realtimeStatus];
    });

    const metricCards = computed(() => ([
      {
        key: config.telemetry.temperatureKey,
        label: '温度',
        unit: '°C',
        tagColor: 'volcano',
      },
      {
        key: config.telemetry.humidityKey,
        label: '湿度',
        unit: '%',
        tagColor: 'geekblue',
      },
    ]));

    const deviceSourceLabel = computed(() => {
      if (config.device.id) return '按 deviceId 直连';
      if (config.device.name) return '按 deviceName 搜索';
      return '尚未配置';
    });

    const maskedApiKey = computed(() => {
      const apiKey = config.apiKey?.trim();
      if (!apiKey) return '未填写';
      if (apiKey.length <= 16) return `${apiKey.slice(0, 4)}****${apiKey.slice(-4)}`;
      return `${apiKey.slice(0, 8)}...${apiKey.slice(-8)}`;
    });

    const clearPollingTimer = () => {
      if (pollingTimer.value !== null) {
        window.clearTimeout(pollingTimer.value);
        pollingTimer.value = null;
      }
    };

    const upsertTelemetry = (map:ThingsBoardTelemetryMap) => {
      Object.entries(map).forEach(([key, list]) => {
        if (!list?.length) return;
        const latestPoint = [...list].sort((a, b) => b.ts - a.ts)[0];
        state.latestTelemetry[key] = latestPoint;
        state.lastUpdateTs = Math.max(state.lastUpdateTs, latestPoint.ts || 0);
      });
    };

    const formatDateTime = (value:number) => {
      if (!value) return '--';
      return dayjs(value).format('YYYY-MM-DD HH:mm:ss');
    };

    const formatMetricValue = (key:string) => {
      const point = state.latestTelemetry[key];
      if (!point) return '--';
      return point.value;
    };

    const formatMetricTime = (key:string) => {
      const point = state.latestTelemetry[key];
      if (!point?.ts) return '--';
      return formatDateTime(point.ts);
    };

    const refreshSnapshot = async ({ silent = false } = {}) => {
      if (!state.resolvedDevice) return;

      if (!silent) {
        state.snapshotRefreshing = true;
      }
      try {
        const telemetry = await client.getLatestTelemetry(
          config.apiKey,
          'DEVICE',
          state.resolvedDevice.id,
          telemetryKeys.value,
          config.snapshot.lookbackMs,
        );
        upsertTelemetry(telemetry);
        if (config.realtime.enabled) {
          state.realtimeStatus = 'polling';
        }
        state.errorMessage = '';
      } catch (error:any) {
        state.errorMessage = error.message;
        if (config.realtime.enabled) {
          state.realtimeStatus = 'error';
        }
      } finally {
        if (!silent) {
          state.snapshotRefreshing = false;
          if (config.realtime.enabled) {
            scheduleNextPoll();
          }
        }
      }
    };

    const scheduleNextPoll = () => {
      clearPollingTimer();
      if (
        destroyed.value
        || !pageActive.value
        || !config.realtime.enabled
        || !state.resolvedDevice?.id
        || !telemetryKeys.value.length
      ) {
        return;
      }

      pollingTimer.value = window.setTimeout(async () => {
        if (destroyed.value || !pageActive.value || pollInFlight.value) {
          scheduleNextPoll();
          return;
        }

        pollInFlight.value = true;
        try {
          await refreshSnapshot({ silent: true });
        } finally {
          pollInFlight.value = false;
          scheduleNextPoll();
        }
      }, config.realtime.pollIntervalMs);
    };

    const startRealtimePolling = () => {
      clearPollingTimer();
      if (!config.realtime.enabled || !state.resolvedDevice?.id || !telemetryKeys.value.length) {
        state.realtimeStatus = 'paused';
        return;
      }

      state.phase = 'starting-polling';
      state.realtimeStatus = 'polling';
      scheduleNextPoll();
    };

    const resolveDevice = async () => {
      if (config.device.id) {
        return client.getDeviceById(config.apiKey, config.device.id);
      }
      if (config.device.name) {
        return client.searchDeviceByName(config.apiKey, config.device.name);
      }
      return null;
    };

    const bootstrap = async () => {
      clearPollingTimer();
      state.errorMessage = '';
      state.booting = true;
      state.availableTelemetryKeys = [];
      state.latestTelemetry = {};
      state.lastUpdateTs = 0;
      state.realtimeStatus = 'idle';
      state.resolvedDevice = null;

      if (configWarnings.value.length) {
        state.phase = 'awaiting-config';
        state.booting = false;
        return;
      }

      try {
        state.phase = 'resolving-device';
        state.resolvedDevice = await resolveDevice();
        if (!state.resolvedDevice?.id) {
          throw new Error('没有找到目标设备。请补充正确的 deviceId 或 deviceName。');
        }

        state.phase = 'loading-snapshot';
        const [keysResult] = await Promise.all([
          settlePromise(client.getTelemetryKeys(config.apiKey, 'DEVICE', state.resolvedDevice.id)),
          settlePromise(refreshSnapshot()),
        ]);

        if (keysResult.status === 'fulfilled') {
          state.availableTelemetryKeys = keysResult.value;
        } else {
          state.availableTelemetryKeys = Object.keys(state.latestTelemetry);
        }

        if (config.realtime.enabled) {
          startRealtimePolling();
          state.phase = 'ready';
        } else {
          state.phase = 'ready';
          state.realtimeStatus = 'paused';
        }
      } catch (error:any) {
        state.phase = 'error';
        state.realtimeStatus = 'error';
        state.errorMessage = error.message || 'ThingsBoard 连接失败';
      } finally {
        state.booting = false;
      }
    };

    bootstrap();

    onActivated(() => {
      pageActive.value = true;
      if (config.realtime.enabled && state.resolvedDevice?.id) {
        startRealtimePolling();
      }
    });

    onDeactivated(() => {
      pageActive.value = false;
      clearPollingTimer();
    });

    onBeforeUnmount(() => {
      destroyed.value = true;
      clearPollingTimer();
    });

    return {
      config,
      configWarnings,
      phaseTag,
      realtimeTag,
      maskedApiKey,
      metricCards,
      detectedTelemetryKeys,
      deviceSourceLabel,
      formatDateTime,
      formatMetricValue,
      formatMetricTime,
      bootstrap,
      refreshSnapshot,
      ...toRefs(state),
    };
  },
});
</script>

<style lang="less" scoped>
.tb-sensor-demo {
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    height: calc(100% - 32px);
    min-height: 0;
    padding: 20px 16px 28px;
    overflow: hidden;
    color: #10324d;

    :deep(.content-scroll) {
        flex: 1;
        min-height: 0;
        padding-bottom: 32px;
        overflow-x: hidden;
        overflow-y: auto;
    }

    .top-alert {
        margin-bottom: 18px;
        border-radius: 18px;
    }

    .warning-list {
        padding-left: 18px;
        margin: 8px 0 0;
    }
}

.hero-panel {
    display: flex;
    gap: 24px;
    align-items: flex-start;
    justify-content: space-between;
    padding: 28px 30px;
    margin-bottom: 20px;
    background:
        radial-gradient(circle at top left, rgba(37, 149, 255, 0.24), transparent 34%),
        linear-gradient(135deg, rgba(12, 34, 54, 0.96) 0%, rgba(9, 60, 84, 0.92) 56%, rgba(13, 120, 140, 0.88) 100%);
    border: 1px solid rgba(111, 192, 255, 0.22);
    border-radius: 28px;
    box-shadow: 0 24px 56px rgba(9, 37, 55, 0.18);

    .hero-copy {
        max-width: 720px;
    }

    .eyebrow {
        margin-bottom: 12px;
        font-size: 12px;
        font-weight: 700;
        color: rgba(208, 238, 255, 0.72);
        letter-spacing: 0.2em;
    }

    h1 {
        margin: 0 0 12px;
        font-size: 34px;
        font-weight: 700;
        color: #f4fbff;
        letter-spacing: 0.04em;
    }

    .hero-text {
        max-width: 620px;
        margin: 0;
        font-size: 15px;
        line-height: 1.8;
        color: rgba(226, 242, 255, 0.86);
    }

    .hero-actions {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
        justify-content: flex-end;
    }
}

.dashboard-grid {
    display: grid;
    grid-template-columns: 1.15fr 1.15fr repeat(2, minmax(220px, 1fr));
    gap: 18px;
    margin-bottom: 18px;
}

.detail-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 18px;
    padding-bottom: 4px;
}

.status-card,
.config-card,
.metric-card,
.detail-card {
    overflow: hidden;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(244, 250, 255, 0.94) 100%);
    border: 1px solid rgba(134, 191, 255, 0.22);
    border-radius: 24px;
    box-shadow: 0 18px 42px rgba(12, 42, 64, 0.08);

    :deep(.ant-card-head) {
        min-height: 54px;
        border-bottom: none;
    }

    :deep(.ant-card-head-title) {
        padding: 18px 0 0;
        font-size: 16px;
        font-weight: 700;
        color: #12344f;
    }

    :deep(.ant-card-body) {
        padding: 6px 24px 24px;
    }
}

.status-stack {
    display: flex;
    flex-direction: column;
    gap: 14px;
}

.status-row {
    display: flex;
    gap: 16px;
    align-items: center;
    justify-content: space-between;
    padding-bottom: 12px;
    font-size: 14px;
    border-bottom: 1px solid rgba(16, 50, 77, 0.08);

    &:last-child {
        padding-bottom: 0;
        border-bottom: none;
    }

    .status-value {
        font-weight: 600;
        color: #0d517a;
        text-align: right;
    }
}

.error-box {
    padding: 14px 16px;
    margin-top: 18px;
    font-size: 13px;
    line-height: 1.7;
    color: #7f1d1d;
    background: rgba(254, 226, 226, 0.72);
    border: 1px solid rgba(248, 113, 113, 0.26);
    border-radius: 16px;
}

.metric-card {
    position: relative;

    &::after {
        position: absolute;
        top: -36px;
        right: -18px;
        width: 120px;
        height: 120px;
        content: '';
        background: radial-gradient(circle, rgba(37, 149, 255, 0.16) 0%, transparent 70%);
    }

    .metric-card__top {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20px;
        font-size: 14px;
        color: #45657f;
    }

    .metric-card__value {
        position: relative;
        z-index: 1;
        display: flex;
        gap: 8px;
        align-items: baseline;
        font-size: 42px;
        font-weight: 700;
        line-height: 1;
        color: #0a3551;

        small {
            font-size: 14px;
            font-weight: 600;
            color: #4f7ea5;
        }
    }

    .metric-card__meta {
        margin: 18px 0 0;
        font-size: 13px;
        color: #6d88a0;
    }
}

.key-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

@media (max-width: 1400px) {
    .dashboard-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .detail-grid {
        grid-template-columns: 1fr;
    }
}
</style>
