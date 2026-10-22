/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-19 15:46:41
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-19 16:58:48
 */
import { RangePicker } from 'ant-design-vue';
import {
  h, ref, computed, SetupContext,
} from 'vue';

interface Props {
 startTime:string,
 endTime:string,
}

const defaultProps = {
  valueFormat: 'YYYY-MM-DD',
  format: 'YYYY-MM-DD',
  placeholder: ['开始时间', '结束时间'],
};

const VRangePicker = (props:Props, context:SetupContext) => {
  const { startTime, endTime } = props;
  const margeProps = {
    ...defaultProps,
    ...props,
    value: startTime && endTime ? [startTime, endTime] : [],
    'onUpdate:value': (value:string[]) => {
      context.emit('update:startTime', value[0] || '');
      context.emit('update:endTime', value[1] || '');
    },
  } as any;
  return h(RangePicker, { ...margeProps });
};

export default VRangePicker;
