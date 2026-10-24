<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-20 16:23:01
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate"
        >
          导出
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
          <template #headerCell="{title, column}">
            <div
              v-if="column.dataIndex === 'isOverTemp'"
              class="isOverTemp"
            >
              {{ title.props.title }}

              <a-popover
                content="体温大于37.3为异常"
              >
                <v-svg
                  class="icon"
                  name="prompt-fill"
                />
              </a-popover>
            </div>
          </template>
          <template #enterImgUrl="{text}">
            <v-image
              width="80px"
              height="80px"
              :src="text"
            />
          </template>
          <template #faceImageUrl="{text}">
            <v-image
              width="80px"
              height="80px"
              :keys="text"
            />
          </template>
          <template #sceneImageUrl="{text}">
            <v-image
              width="80px"
              height="80px"
              :src="text"
            />
          </template>
          <template #healthCode="{text}">
            <span>{{ options.HEALTH_CODE[text]?.label }}</span>
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
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import PersonPass, { PersonPassRecord } from '~models/person-pass';
import Park from '~models/park';
import CompanyMangement from '~models/company-mangement';
import EquipmentManagement from '~models/equipment-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, carRegisteredOpts,
} from './config';
// import PsersonPass from '@/models/urlConstant/person-pass';

// import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

// type Operatetypes = 'add'|'edit'|'del'
const personPassInstance = new PersonPass();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    // const laneOpts = ref<SelectOptions>([]);
    const tagOpts = ref<SelectOptions>([]);
    const deviceOpts = ref<SelectOptions>([]);
    const options = useHashMapDics<['HEALTH_CODE']>(['HEALTH_CODE']);
    // const options = ref<DictionaryMap<['CAR_TYPE']>>({
    //   CAR_TYPE: [],
    // });

    const parkOrgChang = async (parkOrgId:string) => {
      if (!parkOrgId) {
        tagOpts.value = [];
        deviceOpts.value = [];
        return;
      }
      searchConf.deviceId.value = '';
      searchConf.labelId.value = '';
      tagOpts.value = await new CompanyMangement().listTagDict({ parkOrgId });
      deviceOpts.value = await personPassInstance.listDropDownDevices(parkOrgId, [1, 2]);
    };

    const searchConf = reactive<SearchConf<['userName', 'mobile', 'labelId', 'deviceId', 'parkOrgId', 'times']>>({
      userName: {
        tag: 'input',
        value: '',
        placeholder: '请输入姓名',
      },
      mobile: {
        tag: 'input',
        value: '',
        placeholder: '请输入手机号',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkOpts.value,
        handleChange: parkOrgChang,
      },
      labelId: {
        tag: 'select',
        value: '',
        placeholder: '请选择标签',
        options: () => tagOpts.value,
      },
      deviceId: {
        tag: 'select',
        value: '',
        placeholder: '请选择设备',
        options: () => deviceOpts.value,
      },
      times: {
        tag: 'date-range',
        value: '',
        placeholder: ['通行开始时间', '通行结束时间'],
        endTimeKey: 'endTime',
        startTimeKey: 'startTime',
      },
    });

    // searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
    //   laneOpts.value = await carPassInstance.d;
    // };

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<PersonPassRecord>(personPassInstance.personPassRecordListPage, searchConf, null, personPassInstance.personPassRecordExport);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async () => {
      console.log('导出');
      await download();
    };
    search(true);
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      // options.value = await useHashMapDic<['CAR_TYPE']>(['CAR_TYPE']);

      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      // parkOpts,
      // carRegisteredOpts,
      options,
    };
  },
});
</script>
<style lang='scss' scoped>
div {
    &.isOverTemp {
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 900;

        .icon {
            color: var(--ant-primary-4),;
        }
    }
}
</style>
