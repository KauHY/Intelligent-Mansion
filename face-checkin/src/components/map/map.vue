<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-03 15:52:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-09 11:09:12
-->
<template>
  <v-modal
    v-model:visible="currentShow"
    class="gd-map"
    :width="900"
    title="选择地址"
  >
    <div class="search-box">
      <a-input
        id="pickerInput"
        v-model:value="searchAddress"
      />
      <a-button @click="search(searchAddress)">
        搜索
      </a-button>
    </div>
    <div class="content-box">
      <div class="map-box">
        <div id="gdMap" />
      </div>
      <div id="search-result" />
    </div>
    <template #footer>
      <a-button
        type="primary"
        @click="confirm"
      >
        确认
      </a-button>
      <a-button
        @click="currentShow=false"
      >
        取消
      </a-button>
    </template>
  </v-modal>
</template>
<script  lang='ts' >
import { message } from 'ant-design-vue';
import {
  reactive, ref, computed, defineComponent, onMounted, watch,
} from 'vue';
import mapLoad from '~utils/map';
import { AddressInfo } from './type';

// eslint-disable-next-line no-underscore-dangle
window._AMapSecurityConfig = {
  securityJsCode: '6a7628cd584bdfa5b1e05d36c5c86208',
};

export default defineComponent({
  props: {
    show: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    'update:show': (paylod:boolean) => true,
    change: (addressInfo:AddressInfo) => addressInfo instanceof Object && Object.keys(addressInfo).length,
  },
  setup(props, { emit }) {
    // 弹框关闭隐藏
    const currentShow = computed({
      set(value:boolean) {
        emit('update:show', value);
      },
      get() {
        return props.show;
      },
    });
    const searchAddress = ref();
    let mapInstance:{AMap:any, map:AMap.Map}|null = null;
    let poiPicker:any = null;
    // let search:(name:string):void = ref((name) => {});
    // const getLocation = (address:string, AMap:any) => new Promise((resolve, reject) => {
    //   new AMap.Geocoder().getLocation(address, (status:'complete'|'error'|'no_data', result:any) => {
    //     console.log(status, result, result?.geocodes[0].location, '---result---');
    //     if (status === 'complete' && result.info === 'OK') {
    //       // Object.assign(location, result?.geocodes[0].location);
    //       const geocodes = result ? result.geocodes : [];
    //       resolve(geocodes);
    //     }
    //     reject(result);
    //   });
    // });
    const currentCheckAddressInfo = ref<AddressInfo|null>(null);
    const initMap = async () => {
      /**
       * @description: 选中某个点时调用
       * @param {*}
       * @return {*}
       * @author: GUOCHAO82
       */
      const selectPoi = (poiPicker:any) => {
        // AMap.MarkerOptions.label
        poiPicker.on('poiPicked', async (poiResult:any) => {
          const { AMap, map } = mapInstance as NonNullable<typeof mapInstance>;
          poiPicker.hideSearchResults();
          const { source } = poiResult;
          // console.log(poiResult, '-----poiResult');
          const poi = poiResult.item;
          if (source !== 'search') {
            // suggest来源的，同样调用搜索
            poiPicker.searchByKeyword(poi.name);
            return;
          }
          currentCheckAddressInfo.value = poi;
          // console.log(poi, '----poi');
        });
      };
      /**
       * @description: 初始化地图
       * @param {*}
       * @return {*}
       * @author: GUOCHAO82
       */
      mapInstance = await mapLoad({
        container: 'gdMap',
        plugins: ['AMap.Geocoder'],
        mapOptions: {
          zoom: 18,
        },
      });
      /**
       * @description: 初始化poi选点
       * @param {*}
       * @return {*}
       * @author: GUOCHAO82
       */
      AMapUI.loadUI(['misc/PoiPicker'], (PoiPicker:any) => {
        poiPicker = new PoiPicker({
          input: 'pickerInput', // 输入框id
          placeSearchOptions: {
            map: mapInstance?.map,
            pageSize: 10,
          },
          searchResultsContainer: 'search-result',
        });
        selectPoi(poiPicker);
      });
    };

    /**
     * @description: 根据关键词搜索地址
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const search = (name:string) => {
      // debugger;
      if (!name) {
        message.warning('请输入关键字');
        return;
      }
      poiPicker.searchByKeyword(name);
    };
    const confirm = () => {
      if (!currentCheckAddressInfo.value) {
        message.warning('请先选择一个地点后再点击确认！');
        return;
      }
      emit('change', currentCheckAddressInfo.value as NonNullable<AddressInfo>);
      currentShow.value = false;
    };
    watch(currentShow, () => {
      if (!mapInstance) {
        initMap();
      }
    });
    // onMounted(() => {
    //   initMap();
    // });
    return {
      currentShow,
      searchAddress,
      search,
      confirm,
    };
  },

});

</script>
<style lang="scss" scoped>
.content-box {
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 650px;
    overflow: hidden;

    .map-box {
        flex: 1;

        #gdMap {
            width: 100%;
            height: 100%;
        }
    }

    #search-result {
        min-width: 240px;
        max-width: 200px;
        height: 100%;
        overflow: auto;
    }
}

.search-box {
    display: flex;
    gap: 10px;
    align-items: center;
    justify-content: flex-start;
    padding: 12px;
    margin-bottom: 12px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.78) 0%, rgba(244, 249, 255, 0.9) 100%);
    border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
    border-radius: 18px;
    box-shadow:
        inset 0 1px 0 rgba(255, 255, 255, 0.84),
        0 12px 24px rgba(8, 24, 40, 0.05);
    backdrop-filter: blur(14px);

    #pickerInput {
        width: 500px;
        margin-bottom: 0;
    }

    button {
        margin-left: 0;
    }
}
</style>
