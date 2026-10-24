/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-19 15:46:41
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 15:20:39
 */
import {
  TimePicker, message, FormItem, TimeRangePicker,
} from 'ant-design-vue';
import * as dayjs from 'dayjs';
import './range-time.scss';

import {
  h, ref, computed, SetupContext,
} from 'vue';

interface Props {
 startTime:string,
 endTime:string,
}

const defaultProps = {
  valueFormat: 'HH:mm',
  format: 'HH:mm',
  placeholder: ['开始时间', '结束时间'],
  defaultStartOpenValue: '00:00',
  defaultEndOpenValue: '23:59',
};

const VRangeTime = (props:Props, context:SetupContext) => {
  const { startTime, endTime } = props;
  const margeProps = {
    ...defaultProps,
    ...props,
  } as any;

  const times = computed({
    set(value:string[]) {
      const [tempStartTime, tempEndTime] = value || [];
      context.emit('update:startTime', tempStartTime);
      context.emit('update:endTime', tempEndTime);
      context.emit('change');
    },
    get() {
      return startTime && endTime ? [startTime, endTime] : [];
    },
  });
  const CreateTimeRange = () => h(TimeRangePicker, {
    ...margeProps,
    value: times.value,
    'onUpdate:value': (value:string[]) => { times.value = value; },
  });

  return h('span', {
    class: 'v-range-time',
  },
  [
    CreateTimeRange(),
  ]);
};

export default VRangeTime;
