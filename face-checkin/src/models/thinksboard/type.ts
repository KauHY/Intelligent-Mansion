/*
 * @Description: ThingsBoard types for sensor demo page
 */

export type ThingsBoardEntityType = 'DEVICE';

export interface ThingsBoardDeviceInfo {
  id:string
  name:string
  label?:string
  type?:string
  deviceProfileName?:string
  customerTitle?:string
  createdTime?:number
  active?:boolean
  description?:string
  customerIsPublic?:boolean
}

export interface ThingsBoardPageData<T> {
  data:T[]
  totalPages:number
  totalElements:number
  hasNext:boolean
}

export interface ThingsBoardTelemetryPoint {
  ts:number
  value:string | number | boolean
}

export type ThingsBoardTelemetryMap = Record<string, ThingsBoardTelemetryPoint[]>;

export interface ThingsBoardLoginResponse {
  token:string
  refreshToken?:string
  expTime?:number
}

export type ThingsBoardRestAuth = string | {
  token?:string
  apiKey?:string
  authorization?:string
};

export interface ThingsBoardTelemetryHistoryOptions {
  auth:ThingsBoardRestAuth
  entityType:ThingsBoardEntityType
  entityId:string
  keys:string[]
  startTs:number
  endTs:number
  limit:number
  orderBy?:'ASC'|'DESC'
  agg?:'NONE'|'AVG'|'MIN'|'MAX'|'SUM'|'COUNT'
}

export interface ThingsBoardTenantDeviceQueryOptions {
  page?:number
  pageSize?:number
  textSearch?:string
  sortProperty?:string
  sortOrder?:'ASC'|'DESC'
}

export interface ThingsBoardWsMessage {
  subscriptionId?:number
  errorCode?:number
  errorMsg?:string | null
  data?:Record<string, unknown>
  latestValues?:Record<string, number>
}

export interface ThingsBoardStreamOptions {
  jwt:string
  deviceId:string
  keys:string[]
  onOpen?:() => void
  onClose?:() => void
  onError?:(error:Error) => void
  onData?:(data:ThingsBoardTelemetryMap, raw:ThingsBoardWsMessage) => void
}

export interface ThingsBoardSensorDemoConfig {
  baseUrl:string
  apiKey:string
  device:{
    id:string
    name:string
  }
  telemetry:{
    temperatureKey:string
    humidityKey:string
  }
  snapshot:{
    lookbackMs:number
  }
  realtime:{
    enabled:boolean
    pollIntervalMs:number
  }
}

export interface ThingsBoardHybridSensorDemoConfig {
  baseUrl:string
  auth:{
    username:string
  }
  device:{
    id:string
    name:string
  }
  telemetry:{
    temperatureKey:string
    humidityKey:string
    pressureKey:string
    extraKeys:string[]
  }
  history:{
    windowMs:number
    limit:number
  }
  realtime:{
    enabled:boolean
    reconnectDelayMs:number
  }
}
