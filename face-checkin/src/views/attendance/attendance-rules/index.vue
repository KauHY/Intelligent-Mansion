<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-09 15:56:43
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box>
      <template #searchOperate>
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp; -->
        <a-button
          type="primary"
          @click="operate('add',undefined)"
        >
          新增
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #ruleName="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >
              {{ record.ruleName }}
            </span>
          </template>
          <template #status="{record}">
            <a-switch
              checked-children="开"
              un-checked-children="关"
              :checked="record.status===1"
              @change="operate('statusChange',record, $event)"
            />
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;&nbsp;
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :menu-list="tableList"
      :park-opts="parkOpts"
      :visitor-client-type-opts="options['COMPANY_BIND_CLIENT_TYPE']"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { i } from 'mathjs';
import Attendance, { AttendanceRules } from '~models/attendance';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'statusChange'|'detail'
const attendanceInstance = new Attendance();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    // const searchConf = reactive<SearchConf<['parkingLotName', 'parkOrgId']>>({
    //   parkingLotName: {
    //     tag: 'input',
    //     value: '',
    //     placeholder: '请输入停车场名称',
    //   },
    //   parkOrgId: {
    //     tag: 'select',
    //     value: '',
    //     placeholder: '请选择园区',
    //     options: () => parkOpts.value,
    //   },
    // });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<AttendanceRules>(attendanceInstance.attendanceListPage, {}, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record:AttendanceRules|undefined, e?:boolean) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.ruleName}】吗？`,
          onOk: async () => {
            await attendanceInstance.attendanceDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'statusChange') {
        const tempRecord = record as AttendanceRules;
        const tempStatus = e ? 1 : 0;
        await attendanceInstance.modifyStatus({
          id: tempRecord.id as string,
          enable: tempStatus,
        });
        tempRecord.status = tempStatus;
        message.success('操作成功');
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as AttendanceRules;
      //   break;
      // }
      // }
    };
    search(true);

    const options = ref<DictionaryMap<['COMPANY_BIND_CLIENT_TYPE']>>({
      COMPANY_BIND_CLIENT_TYPE: [],
    });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['COMPANY_BIND_CLIENT_TYPE']>(['COMPANY_BIND_CLIENT_TYPE']);
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns,
      // searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      options,
    };
  },
});
</script>
