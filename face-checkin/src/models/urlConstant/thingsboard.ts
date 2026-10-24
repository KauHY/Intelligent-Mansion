/*
 * @Description: ThingsBoard API routes for frontend-only sensor demo
 */

const THINGSBOARD_URLS = {
  tenantToken: '/iot/tb/getToken',
  authLogin: '/api/auth/login',
  authToken: '/api/auth/token',
  tenantDeviceInfos: '/api/tenant/deviceInfos',
  deviceInfo: (deviceId:string) => `/api/device/info/${deviceId}`,
  telemetryKeys: (entityType:string, entityId:string) => `/api/plugins/telemetry/${entityType}/${entityId}/keys/timeseries`,
  telemetryValues: (entityType:string, entityId:string) => `/api/plugins/telemetry/${entityType}/${entityId}/values/timeseries`,
  websocket: '/api/ws',
};

export default THINGSBOARD_URLS;
