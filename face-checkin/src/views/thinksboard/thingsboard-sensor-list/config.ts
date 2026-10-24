/*
 * @Description: Runtime config for the ThingsBoard sensor list page
 */

const config = {
  baseUrl: 'http://183.194.113.230:6080',
  auth: {
    username: '837669836@qq.com',
  },
  list: {
    pageSize: 100,
  },
  history: {
    windowMs: 12 * 60 * 60 * 1000,
    limit: 240,
  },
  realtime: {
    enabled: true,
    reconnectDelayMs: 3000,
  },
  telemetry: {
    priorityKeys: [
      'temperature_aht30',
      'temperature_bmp180',
      'temperature',
      'humidity',
      'pressure',
      'altitude',
      'pm25',
      'pm10',
      'co2',
      'tvoc',
      'voc',
      'battery',
    ],
    labelMap: {
      temperature_aht30: '温度 AHT30',
      temperature_bmp180: '温度 BMP180',
      temperature: '温度',
      humidity: '湿度',
      pressure: '气压',
      altitude: '海拔',
      pm25: 'PM2.5',
      pm10: 'PM10',
      co2: '二氧化碳',
      tvoc: '总挥发性有机物',
      voc: '挥发性有机物',
      battery: '电量',
    },
    unitMap: {
      temperature_aht30: '°C',
      temperature_bmp180: '°C',
      temperature: '°C',
      humidity: '%',
      pressure: 'hPa',
      altitude: 'm',
      pm25: 'μg/m³',
      pm10: 'μg/m³',
      co2: 'ppm',
      tvoc: 'ppb',
      voc: 'ppb',
      battery: '%',
    },
  },
} as const;

export default config;
