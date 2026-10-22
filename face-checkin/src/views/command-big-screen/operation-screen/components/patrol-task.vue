<!--
 * @Description:巡检任务
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-02 09:26:50
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 17:43:40
-->
<template>
  <div class="patrol-task">
    <slot name="title" />
    <div class="title">
      <div>
        <img
          src="~assets/images/operation-screen/patrol-task_title_icon1.png"
          alt=""
        >
        待执行：
        <span>{{ exceptionalTaskInfo[1] }}</span>
      </div>
      <div>
        <img
          src="~assets/images/operation-screen/patrol-task_title_icon2.png"
          alt=""
        >
        巡检中：
        <span>{{ exceptionalTaskInfo[2] }}</span>
      </div>
      <div>
        <img
          src="~assets/images/operation-screen/patrol-task_title_icon3.png"
          alt=""
        >
        已结束：
        <span>{{ exceptionalTaskInfo[3] }}</span>
      </div>
    </div>
    <div class="table">
      <v-table
        :columns="columns"
        :pagination="false"
        size="small"
        :scroll="{y:'10vh'}"
        :data-source="exceptionalTaskInfo.list||tableData"
      >
        <template #emptyText>
          <span />
        </template>
      </v-table>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import { ExceptionalTaskInfo } from '~models/command-big-screen';

export default defineComponent({
  props: {
    exceptionalTaskInfo: {
      type: Object as PropType<ExceptionalTaskInfo>,
      default: () => ({}),
    },
  },
  setup() {
    const columns = [
      {
        title: '巡检任务',
        dataIndex: 'taskName',
      },
      {
        title: '巡检点',
        dataIndex: 'pointName',
      },
      {
        title: '完成时间',
        dataIndex: 'updateTime',
      },
    ];
    const tableData = ref([
      {
        pointName: '巡检点名称',
        taskId: 1,
        taskName: '巡检任务',
        updateTime: '完成时间',
      },
      {
        pointName: '巡检点名称',
        taskId: 2,
        taskName: '巡检任务',
        updateTime: '完成时间',
      },
      {
        pointName: '巡检点名称',
        taskId: 3,
        taskName: '巡检任务',
        updateTime: '完成时间',
      },
      {
        pointName: '巡检点名称',
        taskId: 3,
        taskName: '巡检任务',
        updateTime: '完成时间',
      },
      {
        pointName: '巡检点名称',
        taskId: 3,
        taskName: '巡检任务',
        updateTime: '完成时间',
      },
    ]);
    return {
      columns,
      tableData,
    };
  },
});
</script>
<style lang='scss' scoped>
.patrol-task {
    position: relative;
    height: h(282);
    padding: 0 15px h(15);
    margin-bottom: h(20);
    overflow: hidden;
    background: url('~assets/images/operation-screen/patrol-task_bg.png') no-repeat;
    background-size: 100% 100%;

    >.title {
        display: flex;
        align-items: center;
        height: h(40);
        padding-left: 10px;
        font-size: 14px;
        color: #6c6e8e;
        white-space: nowrap;

        img {
            width: 28px;
            height: 28px;
            margin-right: 6px;
        }

        >div {
            display: flex;
            align-items: center;
            margin-right: 48px;

            >span {
                color: #fff;
            }
        }
    }

    >.table {
        height: h(147);
        padding: 0 10px;
        margin-top: h(40);
        overflow: hidden;
        background: url('~assets/images/operation-screen/patrol-task_table_bg.gif') no-repeat;
        background-size: 100% 100%;

        :deep(.ant-table) {
            line-height: h(28);
            background: transparent;

            .ant-table-thead > tr > th,
            .ant-table-tbody > tr > td {
                background: transparent;
                border: none;
            }

            .ant-table-tbody > tr.ant-table-placeholder:hover > td,
            .table-striped td {
                background: transparent;
            }

            .ant-table-thead > tr > th,
            tr,
            td {
                padding: 1px;
                color: #6c6e8e;
            }

            .ant-table-cell-scrollbar {
                box-shadow: 0 1px 0 1px transparent;
            }

            .ant-table-body::-webkit-scrollbar-track {
                background: transparent;
            }
        }

        :deep(.ant-table-wrapper) {
            height: 100%;
        }
    }
}

</style>
