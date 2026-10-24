/*
 * @Description: Runtime config for the ThingsBoard sensor demo page
 */

import { ThingsBoardSensorDemoConfig } from '@/models/thinksboard';

const config:ThingsBoardSensorDemoConfig = {
  baseUrl: 'http://183.194.113.230:6080',
  apiKey: 'tb_1wSZfbK4jv4Y5wP9GstDFedZBG0RxL5j_owVAaJSXjI5thephEQrMc0bOMZCNeaqR8gZjUhLKHjNIKC9Ac5ElQ',
  device: {
    id: 'eef9f220-37a7-11f1-bc98-4b23962921cd',
    name: '温湿度传感器',
  },
  telemetry: {
    temperatureKey: 'temperature',
    humidityKey: 'humidity',
  },
  snapshot: {
    lookbackMs: 24 * 60 * 60 * 1000,
  },
  realtime: {
    enabled: true,
    pollIntervalMs: 5000,
  },
};

export default config;
