/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-07 17:40:42
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 09:47:44
 */
import {
  PropType, ref, toRefs, h, defineComponent, Slot, SetupContext, computed,

} from 'vue';
import { Table, Pagination } from 'ant-design-vue';
// import Table from '@surely-vue/table';
import { Column } from './type';
import './v-table.scss';

interface CoustomColumns extends Column{
  hidden:boolean | ((...ary:any)=>boolean) // 表格对应的列是否展示
}

// 兼容2.x colum配置slots
const getSlots = (slots: SetupContext['slots']) => {
  const slotKeys = Object.keys(slots);

  // if (slotKeys.includes('bodyCell') || slotKeys.includes('headerCell')) {
  //   return slots;
  // }
  let { bodyCell } = slots;
  const { headerCell, emptyText } = slots;
  if (!slotKeys.length) return {};
  if (!bodyCell) {
    bodyCell = ((data:{text:string, record:any, index:number, column:Column}) => {
      const { column } = data;
      // let slot!:Slot;
      const key = column.key || column.dataIndex;
      // debugger;
      const slot = slots[key] || ((...arg:any) => {});
      return slot(data);
    }) as Slot;
  }
  return {
    bodyCell: bodyCell as unknown as Slot,
    headerCell,
    emptyText,
  };
};

export default defineComponent({
  name: 'VTable',
  props: {
    columns: {
      type: Array as PropType<CoustomColumns[]>,
      default: () => ([]),
    },
    customPagination: { // 是否使用talbe自带的分页组件
      type: Boolean,
      default: false,
    },
  },
  setup(props, { slots, attrs }) {
    const { columns, customPagination } = toRefs(props);
    const getTitle = (title:string) => h('span', { // 缩略时增加提示
      title,
      class: 'v-table-title',
    }, title);
    const isAdaptiveScrollTable = computed(() => {
      const scroll = attrs.scroll as Record<string, unknown> | undefined;
      return typeof scroll?.y === 'string' && scroll.y.trim().endsWith('vh');
    });

    const getColumns = () => {
      const currentColumns = ref<CoustomColumns[]>([]);
      columns.value.forEach((item) => {
        const hidden = typeof item.hidden === 'function' ? item.hidden() : item.hidden;
        // 剔除slots属性
        delete item.slots;
        if (!hidden) {
          currentColumns.value.push({
            ...item,
            ellipsis: item.ellipsis === undefined ? true : item.ellipsis,
            align: item.align ?? 'center',
            title: getTitle(item.title) as unknown as string,
            resizable: true,
            width: item.width || 130,
          });
        }
      });
      return currentColumns;
    };
    const transformCellText = ({ text }:{text:string[]|string}) => {
      if ((Array.isArray(text) && !text.length) || !text) return '-';

      return text;
    };
    const tableProps = {
      columns: getColumns().value,
      pagination: customPagination.value ? false : attrs.pagination,
      transformCellText,
      customHeaderRow: (column:any[], index:number) => { column.forEach((item) => { item.ellipsis = false; }); },
      rowClassName: (record:unknown, index:number) => (index % 2 === 1 ? 'table-striped' : null),
      onResizeColumn: (w:number, col:any) => {
        console.log(w, '---w');
        col.width = w;
      },
      tableLayout: 'fixed',
    };
    const getCustomPagination = () => {
      if (!customPagination.value) return null;
      return h('div',
        {
          class: 'pagination-box',
        },
        h(Pagination, {
          ...attrs.pagination as Object,
          onChange: (current:number, pageSize:number) => {
            if (typeof attrs.onChange === 'function')attrs.onChange({ current, pageSize });
          },
        }));
    };

    return () => h('div',
      {
        class: {
          'v-table-box': true,
          'v-table-box--adaptive': isAdaptiveScrollTable.value,
        },
      },
      [
        h(Table,
          {
            class: 'v-table',
            ...attrs,
            ...tableProps as any,
          },
          getSlots(slots)),
        getCustomPagination(),
      ]);
  },
  // mounted() {
  //   (this.$el?.nextSibling.querySelector('.surely-table').children[2] as any).innerText = '';
  //   (this.$el?.nextSibling.querySelector('.surely-table-body').children[1] as any).innerText = '';
  // },
});
