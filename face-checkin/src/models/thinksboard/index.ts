/*
 * @Description: ThingsBoard REST client for the sensor demo page
 */

import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import request from '~utils/axios';
import URLS from '~models/urlConstant/thingsboard';
import {
  ThingsBoardDeviceInfo,
  ThingsBoardEntityType,
  ThingsBoardLoginResponse,
  ThingsBoardPageData,
  ThingsBoardRestAuth,
  ThingsBoardStreamOptions,
  ThingsBoardTelemetryMap,
  ThingsBoardTelemetryHistoryOptions,
  ThingsBoardTelemetryPoint,
  ThingsBoardTenantDeviceQueryOptions,
  ThingsBoardWsMessage,
} from './type';

function normalizeBaseUrl(baseUrl:string) {
  return baseUrl.replace(/\/$/, '');
}

function normalizeApiKey(apiKey:string) {
  const trimmedKey = apiKey.trim();
  return trimmedKey.startsWith('ApiKey ')
    ? trimmedKey
    : `ApiKey ${trimmedKey}`;
}

function normalizeJwt(jwt:string) {
  return jwt.trim().replace(/^Bearer\s+/i, '');
}

function normalizeRestAuthorization(auth:ThingsBoardRestAuth): any {
  if (typeof auth === 'string') {
    const trimmedAuth = auth.trim();
    if (!trimmedAuth) {
      throw new Error('ThingsBoard authorization 不能为空');
    }

    if (/^ApiKey\s+/i.test(trimmedAuth) || trimmedAuth.startsWith('tb_')) {
      return normalizeApiKey(trimmedAuth);
    }

    return `Bearer ${normalizeJwt(trimmedAuth)}`;
  }

  if (auth.authorization?.trim()) {
    return normalizeRestAuthorization(auth.authorization);
  }

  if (auth.apiKey?.trim()) {
    return normalizeApiKey(auth.apiKey);
  }

  if (auth.token?.trim()) {
    return `Bearer ${normalizeJwt(auth.token)}`;
  }

  throw new Error('ThingsBoard authorization 不能为空');
}

function createAuthHeaders(auth:ThingsBoardRestAuth) {
  return {
    'X-Authorization': normalizeRestAuthorization(auth),
  };
}

function normalizeWebSocketBaseUrl(baseUrl:string) {
  if (baseUrl.startsWith('https://')) {
    return baseUrl.replace(/^https:\/\//, 'wss://');
  }
  if (baseUrl.startsWith('http://')) {
    return baseUrl.replace(/^http:\/\//, 'ws://');
  }
  return baseUrl;
}

function normalizeTelemetryPoints(raw:unknown): ThingsBoardTelemetryPoint[] {
  if (!raw) return [];

  if (Array.isArray(raw)) {
    return raw.reduce<ThingsBoardTelemetryPoint[]>((list, item) => {
      if (Array.isArray(item) && item.length >= 2) {
        list.push({
          ts: Number(item[0]),
          value: item[1] as string | number | boolean,
        });
        return list;
      }

      if (typeof item === 'object' && item !== null && 'ts' in item && 'value' in item) {
        list.push({
          ts: Number((item as any).ts),
          value: (item as any).value,
        });
      }
      return list;
    }, []);
  }

  if (typeof raw === 'object' && raw !== null && 'ts' in raw && 'value' in raw) {
    return [{
      ts: Number((raw as any).ts),
      value: (raw as any).value,
    }];
  }

  return [];
}

function normalizeTelemetryMap(raw:unknown): ThingsBoardTelemetryMap {
  if (!raw || typeof raw !== 'object') return {};

  return Object.entries(raw as Record<string, unknown>).reduce<ThingsBoardTelemetryMap>((acc, [key, value]) => {
    const points = normalizeTelemetryPoints(value);
    if (points.length) {
      acc[key] = points;
    }
    return acc;
  }, {});
}

function normalizeDeviceInfo(raw:any): ThingsBoardDeviceInfo {
  return {
    id: raw?.id?.id || raw?.id || '',
    name: raw?.name || '',
    label: raw?.label || '',
    type: raw?.type || '',
    deviceProfileName: raw?.deviceProfileName || '',
    customerTitle: raw?.customerTitle || '',
    createdTime: Number(raw?.createdTime || 0),
    active: Boolean(raw?.active),
    description: raw?.additionalInfo?.description || '',
    customerIsPublic: Boolean(raw?.customerIsPublic),
  };
}

function normalizePageData<T>(raw:any, mapper:(item:any) => T): ThingsBoardPageData<T> {
  const data = Array.isArray(raw?.data)
    ? raw.data.map((item:any) => mapper(item))
    : [];

  return {
    data,
    totalPages: Number(raw?.totalPages || 0),
    totalElements: Number(raw?.totalElements || data.length),
    hasNext: Boolean(raw?.hasNext),
  };
}

function normalizeLoginResponse(payload: any): ThingsBoardLoginResponse {
  const source = payload.data as Record<string, any>;
  return {
    token: source.token || '',
    refreshToken: source.refreshToken || '',
    expTime: Number(source.expTime || 0),
  };
}

export default class ThingsBoard {
  private readonly baseUrl:string;

  private readonly http:AxiosInstance;

  constructor(baseUrl:string) {
    this.baseUrl = normalizeBaseUrl(baseUrl);
    this.http = axios.create({
      baseURL: this.baseUrl,
      timeout: 15000,
    });
  }

  getWebSocketUrl() {
    return `${normalizeWebSocketBaseUrl(this.baseUrl)}${URLS.websocket}`;
  }

  async getTenantToken(tenant:string) {
    const { data } = await request<unknown>({
      url: URLS.tenantToken,
      method: 'post',
      data: {
        tenant: tenant.trim(),
      },
      hiddenLoading: true,
    });

    return normalizeLoginResponse(data);
  }

  async login(username:string, password:string) {
    const { data } = await this.request<ThingsBoardLoginResponse>({
      url: URLS.authLogin,
      method: 'post',
      data: {
        username: username.trim(),
        password,
      },
    });

    if (!data?.token) {
      throw new Error('ThingsBoard 登录成功，但没有返回 JWT token');
    }

    return data;
  }

  async refreshToken(refreshToken:string) {
    const { data } = await this.request<ThingsBoardLoginResponse>({
      url: URLS.authToken,
      method: 'post',
      data: {
        refreshToken,
      },
    });

    if (!data?.token) {
      throw new Error('ThingsBoard 刷新成功，但没有返回 JWT token');
    }

    return data;
  }

  async getDeviceById(auth:ThingsBoardRestAuth, deviceId:string) {
    const { data } = await this.request<any>({
      url: URLS.deviceInfo(deviceId),
      method: 'get',
      headers: createAuthHeaders(auth),
    });

    return normalizeDeviceInfo(data);
  }

  async searchDeviceByName(auth:ThingsBoardRestAuth, deviceName:string) {
    const { data } = await this.request<any>({
      url: URLS.tenantDeviceInfos,
      method: 'get',
      headers: createAuthHeaders(auth),
      params: {
        pageSize: 20,
        page: 0,
        textSearch: deviceName,
        sortProperty: 'name',
        sortOrder: 'ASC',
      },
    });

    const devices: any[] = Array.isArray(data?.data)
      ? data.data.map((item:any) => normalizeDeviceInfo(item))
      : [];

    return devices.find(({ name }) => name === deviceName) || devices[0] || null;
  }

  async getTenantDevices(auth:ThingsBoardRestAuth, options:ThingsBoardTenantDeviceQueryOptions = {}) {
    const { data } = await this.request<any>({
      url: URLS.tenantDeviceInfos,
      method: 'get',
      headers: createAuthHeaders(auth),
      params: {
        pageSize: options.pageSize ?? 50,
        page: options.page ?? 0,
        textSearch: options.textSearch || undefined,
        sortProperty: options.sortProperty || 'createdTime',
        sortOrder: options.sortOrder || 'DESC',
      },
    });

    return normalizePageData(data, normalizeDeviceInfo);
  }

  async getTelemetryKeys(auth:ThingsBoardRestAuth, entityType:ThingsBoardEntityType, entityId:string) {
    const { data } = await this.request<any>({
      url: URLS.telemetryKeys(entityType, entityId),
      method: 'get',
      headers: createAuthHeaders(auth),
    });

    return Array.isArray(data) ? data : [];
  }

  async getLatestTelemetry(
    auth:ThingsBoardRestAuth,
    entityType:ThingsBoardEntityType,
    entityId:string,
    keys:string[],
    lookbackMs:number,
  ) {
    const now = Date.now();
    const { data } = await this.request<any>({
      url: URLS.telemetryValues(entityType, entityId),
      method: 'get',
      headers: createAuthHeaders(auth),
      params: {
        keys: keys.join(','),
        startTs: Math.max(0, now - lookbackMs),
        endTs: now,
        limit: 1,
        agg: 'NONE',
        orderBy: 'DESC',
        useStrictDataTypes: true,
      },
    });

    return normalizeTelemetryMap(data);
  }

  async getHistoricalTelemetry({
    auth,
    entityType,
    entityId,
    keys,
    startTs,
    endTs,
    limit,
    orderBy = 'ASC',
    agg = 'NONE',
  }: ThingsBoardTelemetryHistoryOptions) {
    const { data } = await this.request<any>({
      url: URLS.telemetryValues(entityType, entityId),
      method: 'get',
      headers: createAuthHeaders(auth),
      params: {
        keys: keys.join(','),
        startTs,
        endTs,
        limit,
        agg,
        orderBy,
        useStrictDataTypes: true,
      },
    });

    return normalizeTelemetryMap(data);
  }

  connectTelemetryStream({
    jwt,
    deviceId,
    keys,
    onOpen,
    onClose,
    onError,
    onData,
  }: ThingsBoardStreamOptions) {
    const socket = new WebSocket(this.getWebSocketUrl());

    socket.onopen = () => {
      socket.send(JSON.stringify({
        authCmd: {
          cmdId: 0,
          token: normalizeJwt(jwt),
        },
        cmds: [{
          entityType: 'DEVICE',
          entityId: deviceId,
          scope: 'LATEST_TELEMETRY',
          cmdId: 10,
          type: 'TIMESERIES',
          keys: keys.join(','),
        }],
      }));
      onOpen?.();
    };

    socket.onmessage = (event:MessageEvent<string>) => {
      try {
        const payload = JSON.parse(event.data) as ThingsBoardWsMessage | ThingsBoardWsMessage[];
        const messages = Array.isArray(payload) ? payload : [payload];

        messages.forEach((message) => {
          if (message.errorCode && message.errorCode !== 0) {
            onError?.(new Error(message.errorMsg || `ThingsBoard WebSocket error ${message.errorCode}`));
            return;
          }

          const mapped = normalizeTelemetryMap(message.data);
          if (Object.keys(mapped).length) {
            onData?.(mapped, message);
          }
        });
      } catch (error) {
        onError?.(new Error('ThingsBoard WebSocket message parse failed'));
      }
    };

    socket.onerror = () => {
      onError?.(new Error('ThingsBoard WebSocket connection failed'));
    };

    socket.onclose = () => {
      onClose?.();
    };

    return socket;
  }

  private async request<T=any>(config:AxiosRequestConfig) {
    try {
      return await this.http.request<T>(config);
    } catch (error:any) {
      const message = error?.response?.data?.message || error?.message || 'ThingsBoard request failed';
      throw new Error(message);
    }
  }
}

export * from './type';
