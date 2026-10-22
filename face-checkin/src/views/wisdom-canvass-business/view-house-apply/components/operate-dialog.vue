<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 18:06:22
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="780"
    class="operate-dialog"
    @ok="confirm"
  >
    <div class="view-house-apply-details">
      <div class="item">
        <div class="label">
          招商标题:
        </div>
        <div>{{ defaultData.title }}</div>
      </div>

      <div class="item">
        <div class="label">
          用户姓名:
        </div>
        <div>{{ defaultData.username }}</div>
      </div>

      <div class="item">
        <div class="label">
          手机号码:
        </div>
        <div>{{ defaultData.mobile }}</div>
      </div>

      <div class="item">
        <div class="label">
          企业/个人名称:
        </div>
        <div>{{ defaultData.companyName }}</div>
      </div>

      <div class="item">
        <div class="label">
          申请时间:
        </div>
        <div>{{ defaultData.createTime }}</div>
      </div>

      <div class="item">
        <div class="label">
          意向房源:
        </div>
        <!-- <div>{{ defaultData.title }}</div> -->
        <a-tag
          color="orange"
        >
          <template #icon>
            <v-svg
              class="svg-icon"
              name="home-fill"
            />
          </template>
          {{ defaultData.businessHouseVO?.name }}
        </a-tag>
      </div>

      <div class="item">
        <div class="label">
          选择地点:
        </div>
        <div>{{ defaultData.parkOrgName }}{{ defaultData.buildingName }}</div>
      </div>

      <div class="item">
        <div class="label">
          房屋功能:
        </div>
        <div>{{ options.HOUSE_FUNCTION[defaultData?.houseFunction]?.label || '-' }}</div>
      </div>

      <!-- <div class="item">
        <div class="label">
          用户姓名:
        </div>
        <div>{{ defaultData.title }}</div>
      </div> -->
    </div>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs,
} from 'vue';
// import { message } from 'ant-design-vue';
import {
  ViewHouseApplyInfo,
} from '~models/wisdom-canvass-business';
import { useHashMapDics } from '~composition/useHashMapDic';

export default defineComponent({
  name: 'OperateDialog',
  props: {
    title: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<ViewHouseApplyInfo>,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    orderType: { // 类型 报事|报修
      type: Number as PropType<1|2>,
      default: 1,
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const options = useHashMapDics<['HOUSE_FUNCTION']>(['HOUSE_FUNCTION']);
    const {
      type, visible,
    } = toRefs(props);
    // const operateType = type.value as 'add'|'other'|'follow'|'remind'|'convertToCustomer';
    // const cp = shallowRef();

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      currentVisible.value = false;
    };

    onMounted(() => {
      // fill();
      if (type.value === 'edit') {
        // parkOrgIdChange(modelRef.parkOrgId, false);
      }
      // jobOpts.value =
    });
    return {
      options,
      currentVisible,
      confirm,
    };
  },
});
</script>
<style lang="less" scoped>
.view-house-apply-details {
    .item {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 30px;

        .label {
            width: 160px;
            margin-right: 40px;
            text-align: right;
        }
    }
}
</style>
