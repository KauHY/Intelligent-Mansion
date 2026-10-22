<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-13 14:44:51
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    class="operate-dialog"
    :title="title"
    :width="820"
    @ok="confirm"
  >
    <a-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      :label-col="{span:5}"
    >
      <a-form-item
        label="中文名称"
        name="name"
      >
        <a-input
          v-model:value="formData.name"
          placeholder="请输入菜单名称"
        />
      </a-form-item><a-form-item
        v-if="formData.type===0"
        name="sort"
        label="排序"
      >
        <a-input-number
          v-model:value="formData.sort"
          placeholder="请输入排序序号"
          :precision="1"
          :min="0"
          :formatter="formatSort"
        /><br><span>同一层级菜单，数字越小越靠前</span>
      </a-form-item><a-form-item
        v-if="formData.type===0"
        label="图标"
      >
        <a-select
          v-model:value="formData.icon"
          allow-clear
          show-search
          placeholder="请选择图标"
        >
          <a-select-option
            v-for="opt in iconConf"
            :key="opt.icon_id"
            :value="opt.font_class"
          >
            <v-svg :name="opt.font_class" />{{ opt.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <!-- <a-form-item
        v-if="[0,2].includes(formData.type)"
        label="是否展示"
      >
        <a-switch v-model:checked="statusBoolen" />
      </a-form-item> -->
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import { rootMenu } from '@/constants/config';
import Menu from '@/models/meun';
import iconConf from '@/constants/font.class.config';
import {
  reactive, ref, defineComponent, PropType, computed, toRefs, onMounted,
} from 'vue';
import { message } from 'ant-design-vue';
import { menuTypeName } from '../config';

const menuInstance = new Menu();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    menuList: {
      type: Array as PropType<Array<MenuItem>>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    // form表单
    const formRef = ref();
    const formData = reactive<MenuItem>({
      name: null,
      id: '',
      apiUrl: '',
      icon: '',
      sort: 0,
      parentId: '0',
      route: '',
      routeName: '',
      type: 0,
      status: 1,
    });
    const rules = {
      name: {
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('菜单名称不能为空');
          if (value.length > 30) return Promise.reject('最多只能输入30个字符');
          return Promise.resolve();
        },
        required: true,
      },
      sort: { max: 1000000000, message: '最多只能输入10个字符', type: 'number' },
    };
    const statusBoolen = computed({ // 展示隐藏boolean转换
      set(status) {
        formData.status = status ? 1 : 0;
      },
      get() {
        return formData.status === 1;
      },
    });
    // rootMenu.children = toRefs(props.menuList);
    const allMenu = [{ ...rootMenu, children: props.menuList }];
    // console.log(allMenu, '----allMenu---');
    const formatSort = (value:string) => (parseInt(value, 10) || 0);
    const fillData = () => {
      Object.entries(formData).forEach(([key, value]) => {
        if (props.defaultData[key] || props.defaultData[key] === 0) {
          formData[key] = props.defaultData[key];
        }
      });
      console.log(formData, '-----formData');
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      formRef.value.validate().then(async () => {
        const { type } = props;
        if (type === 'add') {
          await menuInstance.add(formData);
          message.success('新增成功');
        } else {
          await menuInstance.edit(formData);
          message.success('编辑成功');
        }

        currentVisible.value = false;
        emit('operate:success');
      }).catch(() => {
        message.warning('操作失败');
      });
    };
    onMounted(() => {
      fillData();
    });
    return {
      currentVisible,
      formatSort,
      formRef,
      formData,
      rules,
      // menuTypeName,
      confirm,
      iconConf,
      // allMenu,
      statusBoolen,
    };
  },
});
</script>
