<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-27 17:59:08
 * @LastEditors: your name
 * @LastEditTime: 2022-03-18 19:47:59
-->
<template>
  <div class="details">
    <div class="item">
      <div class="label">
        被访人姓名:
      </div>
      <div>{{ details.invitorName }}</div>
    </div>
    <div class="item">
      <div class="label">
        被访人电话:
      </div>
      <div>{{ details.invitorTel }}</div>
    </div>
    <div class="item">
      <div class="label">
        访客姓名:
      </div>
      <div>{{ details.visitorName }}</div>
    </div>
    <div class="item">
      <div class="label">
        访客身份证号:
      </div>
      <div>{{ details.visitorIdCard }}</div>
    </div>
    <div class="item">
      <div class="label">
        访客手机号:
      </div>
      <div>{{ details.visitorTel }}</div>
    </div>
    <div class="item">
      <div class="label">
        来访时间:
      </div>
      <div>{{ details.visitDate }}</div>
    </div>
    <div class="item">
      <div class="label">
        来访人数:
      </div>
      <div>{{ details.visitorCount }}</div>
    </div>
    <div class="item">
      <div class="label">
        来访事由:
      </div>
      <div>{{ options['VISIT_REASON'][details.visitReason]?.label||'-' }}</div>
    </div>
    <div class="item">
      <div class="label">
        人脸照片:
      </div>
      <div><v-image :keys="details.visitorFaceImg" /></div>
    </div>
    <div class="item">
      <div class="label">
        访客车牌号:
      </div>
      <div>{{ details.carNum||'-' }}</div>
    </div>
    <div
      v-if="visitorSource===1"
      class="item"
    >
      <div class="label">
        审核状态:
      </div>
      <div v-if="[4,5].includes(details.processStatus)">
        审核通过
      </div>
      <div v-else>
        {{ options['SUBSCRIBE_PROCESS_STATUS'][details.processStatus]?.label||'-' }}
      </div>
    </div>
    <div class="item">
      <div class="label">
        访客类型:
      </div>
      <div>{{ details.visitorTypeName }}</div>
    </div>
    <div class="item">
      <div class="label">
        访问区域:
      </div>
      <div>{{ details.accessPolicyName }}</div>
    </div>
    <div class="item">
      <div class="label">
        备注:
      </div>
      <div>{{ details.auditRemark }}</div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed, toRefs,
} from 'vue';
import { RegisterInfo } from '~models/visitors-management';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

export default defineComponent({
  name: '',
  components: {},
  props: {
    details: {
      type: Object as PropType<RegisterInfo>,
      default: () => ({}),
    },
    visitReasonOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { details } = toRefs(props);
    const visitorSource = computed(() => details.value.visitorSource);
    const options = ref<DictionaryMap<['SUBSCRIBE_PROCESS_STATUS', 'VISIT_REASON']>>({
      SUBSCRIBE_PROCESS_STATUS: [],
      VISIT_REASON: [],
    });
    const init = async () => {
      options.value = await useHashMapDic<['SUBSCRIBE_PROCESS_STATUS', 'VISIT_REASON']>(['SUBSCRIBE_PROCESS_STATUS', 'VISIT_REASON']);
    };
    init();
    return {
      visitorSource,
      options,
    };
  },
});
</script>
<style lang="scss" scoped>
.details {
    .item {
        display: flex;
        justify-content: flex-start;
        padding-bottom: 10px;
        margin-bottom: 16px;
        border-bottom: 1px solid #f0f0f0;

        .label {
            flex: 0 0 200px;
            color: #999;
        }

        >div {
            flex: 1;
        }
    }
}
</style>
