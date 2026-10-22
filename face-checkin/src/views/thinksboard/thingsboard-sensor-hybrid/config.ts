/*
 * @Description: Runtime config for the ThingsBoard hybrid sensor demo page
 */

import { ThingsBoardHybridSensorDemoConfig } from '@/models/thinksboard';

const config:ThingsBoardHybridSensorDemoConfig = {
  baseUrl: 'http://183.194.113.230:6080',
  auth: {
    username: '837669836@qq.com',
  },
  device: {
    id: 'ec331080-37d4-11f1-bc98-4b23962921cd',
    name: '温湿度与气压',
  },
  telemetry: {
    temperatureKey: 'temperature_aht30',
    humidityKey: 'humidity',
    pressureKey: 'pressure',
    extraKeys: ['temperature_bmp180', 'altitude'],
  },
  history: {
    windowMs: 24 * 60 * 60 * 1000,
    limit: 180,
  },
  realtime: {
    enabled: true,
    reconnectDelayMs: 3000,
  },
};

export default config;
