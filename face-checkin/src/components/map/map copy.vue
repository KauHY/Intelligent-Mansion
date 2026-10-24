<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-03 15:52:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 14:16:10
-->
<template>
  <v-modal
    v-model:visible="currentShow"
    class="gd-map"
    :width="800"
    title="选择地址"
  >
    <a-input
      id="pickerInput"
      v-model:value="searchAddress"
    />
    <div id="gdMap" />
    <template #footer>
      <a-button
        key="back"
      >
        确认
      </a-button>
      <a-button
        key="submit"
        type="primary"
        @click="close"
      >
        取消
      </a-button>
    </template>
  </v-modal>
</template>
<script  lang='ts' >
import {
  reactive, ref, computed, defineComponent, onMounted, watch,
} from 'vue';
import mapLoad, { PoiPickedEventOptions, MarkerOptions } from '~utils/map';

export default defineComponent({
  props: {
    show: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    'update:show': (paylod:boolean) => true,
  },
  setup(props, { emit }) {
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
    const getLocation = (address:string, AMap:any) => new Promise((resolve, reject) => {
      new AMap.Geocoder().getLocation(address, (status:'complete'|'error'|'no_data', result:any) => {
        console.log(status, result, result?.geocodes[0].location, '---result---');
        if (status === 'complete' && result.info === 'OK') {
          // Object.assign(location, result?.geocodes[0].location);
          const geocodes = result ? result.geocodes : [];
          resolve(geocodes);
        }
        reject(result);
      });
    });
    const initMap = async () => {
      /**
       * @description: 选中某个点时调用
       * @param {*}
       * @return {*}
       * @author: GUOCHAO82
       */
      const selectPoi = (poiPicker:any) => {
        // AMap.MarkerOptions.label
        poiPicker.on('poiPicked', async ({ item }:PoiPickedEventOptions) => {
          const { AMap, map } = mapInstance as NonNullable<typeof mapInstance>;
          map.clearMap(); // 清楚覆盖物

          const { address, district, name } = item;
          let { location } = item;
          searchAddress.value = name;
          const addressFull = `${district}${address.toString()}`;
          console.log(item, '---location');
          if (!location) { // 地址不存在时
            // debugger;
            const addressArray = await getLocation(addressFull, AMap) as any[];
            location = addressArray[0]?.location;
            // console.log(location, '----location');
          }
          const markOptions: MarkerOptions = {
            icon: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png',
            position: [location?.lng, location?.lat],
            offset: new AMap.Pixel(-13, -30),
            animation: 'AMAP_ANIMATION_BOUNCE',
            label: {
              content: addressFull,
              offset: new AMap.Pixel(0, -0),
              direction: 'bottom',
            },
          };
          const marker:AMap.Marker = new AMap.Marker(markOptions);
          map.add(marker);
          map.setCenter([location?.lng, location?.lat]);
          // marker.add()
          // mapInstance?.map.remove(marker); // 清楚标记得点
          // console.log(item);
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
        const poiPicker = new PoiPicker({
          input: 'pickerInput', // 输入框id
          AutocompleteOptions: {
            datatype: 'poi',
          },
        });
        // console.log('xxxxooo', mapInstance?.AMap?.Marker);
        // const marker:AMap.Marker = mapInstance?.AMap.Marker();
        selectPoi(poiPicker);
      });
    };

    const close = () => {

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
      close,
      searchAddress,
    };
  },

});
// const emit = defineEmits<{(event:'update:show', paylod:boolean):void
// }>();
// const a:string = 1;

</script>
<style lang="scss" scoped>
#gdMap {
    width: 100%;
    height: 400px;
}

#pickerInput {
    margin-bottom: 4px;
}
</style>
