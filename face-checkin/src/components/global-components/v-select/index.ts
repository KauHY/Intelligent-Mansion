/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-27 13:58:27
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 18:12:50
 */
import { Select } from 'ant-design-vue';
import {
  h, defineComponent, ref, computed, SetupContext,
} from 'vue';

interface Props {
    placeholder:string,
    allowClear:boolean,
    optionFilterProp:'label'
    showSearch:boolean, // 是否可以搜索
    'container-body':boolean, // 是否以body作为容器
    value:any,
    'filter-option':boolean| ((value:string, options:{key:string})=>boolean) // 对结果进行搜索时，注意将key设置位lable值
}
const getFilterOption = (props:Props) => { // 添加默认过滤方法
  if (props['filter-option'] === undefined) return true;
  return (value:string, option:{key:string}) => option.key.includes(value);
};

const VSelect = (props:Props, context:SetupContext) => {
  // debugger;
  const defaultProps = {
    placeholder: '请选择',
    allowClear: true,
    optionFilterProp: 'label',
    showSearch: props['filter-option'] !== undefined,
  };
  //   mod
  const margeProps = {
    ...defaultProps,
    ...props,
    value: props.value === '' ? undefined : props.value,
    filterOption: getFilterOption(props),
    getPopupContainer: props['container-body'] !== undefined ? () => document.body : undefined,
  };
  // console.log(margeProps, '---props.value');
  return h(Select, { ...margeProps as any }, context.slots);
};
console.log(VSelect.name, '----VSelect');
export default VSelect;
