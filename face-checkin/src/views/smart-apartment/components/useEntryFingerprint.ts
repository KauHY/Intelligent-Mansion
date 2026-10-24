/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-08-01 17:05:25
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-03 10:26:56
 */

import { ref, onBeforeMount, onBeforeUnmount } from 'vue';
import { message, notification } from 'ant-design-vue';
import SmarterApartment from '~models/smarter-apartment';

const msgConfig = { // 消息类型
  heartBeat: '{"id": 1, "session": 0, "method": "common.heartbeat","info": {} }', // 心跳
  fingefOpen: '{"id": 1, "session": 0, "method": "finger.open","info": {"devType":2} }',
  fingerOne: '{"id": 1, "session": 0, "method": "finger.getSample","info": {"bufID":0} }',
  fingerTwo: '{"id": 1, "session": 0, "method": "finger.getSample","info": {"bufID":1} }',
  fingerThree: '{"id": 1, "session": 0, "method": "finger.getSample","info": {"bufID":2} }',
  fingerEnroll: '{"id": 1, "session": 0, "method": "finger.enrollSample","info": {"bufCount":3} }',
};

const getAsync = async <T>(fun:Promise<T>) => {
  try {
    const res = await fun;
    return [res, null];
  } catch (error) {
    return [null, error];
  }
};
const sleep = () => new Promise((resolve, reject) => {
  setTimeout(() => {
    resolve(true);
  }, 1000);
});
type CurrentStatus = 'process'|'await'|'error'|'finish'
export function useEntryFingerprint(employeeId:string) {
  const step = ref(-1);
  const currentStatus = ref<CurrentStatus>('process');
  let ws:WebSocket;
  const operate = async () => {
    if (!step.value) {
      step.value = 1;
    }
  };

  const next = (type:CurrentStatus) => {
    step.value += 1;
    currentStatus.value = type;
  };
  const resetEntry = () => {
    step.value = -1;
    currentStatus.value = 'process';
  };
  const startEntry = async () => { // 开始采集
    const heartBeatRes = await heartBeat();
    if (!heartBeatRes) {
      notification.error({
        message: 'socket连接失败，请确认是否安装插件或者指纹采集设备是否正确连接！！',
        duration: 20,
      });
      return;
    }
    try {
      await send('fingefOpen');
      // 第一次采样
      await send('fingerOne');
      next('finish');

      // 第二次采样
      await send('fingerTwo');
      next('finish');
      // 第三次采样和合成
      await send('fingerThree');
      const { data } = await send<{data:{sampleBuf:string}}>('fingerEnroll');
      if (!data?.sampleBuf) {
        next('error');
        return;
      }
      await new SmarterApartment().fingerprint({
        employeeId,
        fingers: [{
          finger: data.sampleBuf,
          fingerIndex: 1,
        }],
      });
      next('finish');
      console.log(employeeId, '----maskClosable');
      console.log(data, '--指纹结果');
    } catch (error) {
      console.log(error, 'error');
      next('error');
    }
  };

  const openSocket = () => new Promise((resolve, reject) => {
    let reconnection = 0;
    const open = () => {
      ws = new WebSocket('ws://localhost:12000');
      ws.onopen = (evt) => {
        reconnection = 0;
        console.log('连接成功-->', evt);
        resolve(evt);
      };
      ws.onclose = (evt) => {
        reject(evt);
        console.error('Connection closed.');
      };
      ws.onerror = (evt) => {
        console.log('打开次数---', reconnection);
        if (reconnection > 5) {
          reject(`socket连接失败，重连次数${reconnection}`);
          reconnection = 0;
          return;
        }
        reconnection += 1;
        window.open('DHLocalServer://');
        open();
      };
    };
    open();
  });

  const send = async <T>(messageType: keyof typeof msgConfig) => new Promise<T>((resolve, reject) => {
    // if (ws.readyState === ws.CLOSED) {
    //   console.log(`【${messageType}】响应报错：`, '连接断开');
    //   reject('连接断开');
    // }
    ws.send(msgConfig[messageType]);
    ws.onmessage = (res) => {
      try {
        const data = JSON.parse(res.data);
        const { code } = data;
        console.log(`收到【${messageType}】响应：`, data);
        if (code === 0 || code === undefined) {
          resolve(data);
          return;
        }
        reject(data);
      } catch (error) {
        reject(error);
      }
    };
    ws.onerror = (err) => {
      console.log(`【${messageType}】响应失败：`, err);
      reject(err);
    };
  });

  // 心跳检测

  const heartBeat = async () => {
    if (!ws || ws.readyState !== ws.OPEN) { // socket断开，重新连接
      await getAsync(openSocket()); // 连接socket
    }
    // 发送心跳
    const [heartBeat] = await getAsync(send('heartBeat'));
    if (heartBeat) { return true; }
    return false; // socket连接失败
  };

  const QuitApp = 	() => {
    let msg = '';
    msg = '{"id": 1, "session": 0, "method": "common.quitApp","info": {} }';

    console.log(`Connection send: ${msg}`);
    ws.send(msg);
  };
  let heartBeatTimeId:NodeJS.Timer;
  onBeforeMount(() => {
    heartBeatTimeId = setInterval(() => {
      if (!ws) return;
      heartBeat();
    }, 1000 * 60);
  });
  onBeforeUnmount(() => {
    clearInterval(heartBeatTimeId as any);
  });
  return {
    operate,
    step,
    startEntry,
    currentStatus,
    resetEntry,
    QuitApp,
  };
}
