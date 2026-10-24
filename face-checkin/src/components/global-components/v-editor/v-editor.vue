<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-11 14:06:22
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-08 16:17:19
-->
<template>
  <div class="v-editor">
    <VueEditor
      v-model:modelValue="currentValue"
      :inline="inline"
      :init="initConf"
      :toolbar="toolbar"
      :output-format="outputFormat"
      :disabled="disabled"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, onMounted, toRefs, computed, watch,
} from 'vue';
import tinymce, { RawEditorSettings, Editor } from 'tinymce/tinymce';
import 'tinymce/themes/silver';

import VueEditor from '@tinymce/tinymce-vue';
import { useUpload } from '~components/global-components/v-upload/useUpload';
import { format } from 'path';
import { message, Form } from 'ant-design-vue';

// eslint-disable-next-line max-len
const plugins = ['advlist anchor autolink autosave code codesample  directionality fullscreen hr image  insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace  tabfocus table template  textpattern visualblocks visualchars wordcount'];

console.log(compileConf.publicPath, '---compileConf.publicPath');
export default defineComponent({
  name: 'VEditor',
  components: { VueEditor },
  props: {
    value: {
      type: String,
      default: '',
    },
    id: {
      type: String,
      default: `v-editor_${Date.now()}`,
    },
    outputFormat: { // value值输出格式
      type: String as PropType<'html' | 'text'>,
      default: 'html',
    },
    inline: { // 使用使用内联模式
      type: Boolean,
      default: false,
    },
    tagName: { // 使用标签名称
      type: String,
      default: 'div',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    toolbar: {
      type: [String, Array],
      default: '',
    },
    // 插入图片自适应
    insertPictureAdaptation: {
      type: Boolean,
      default: false,
    },
    length: {
      type: [Number],
      default: 1000,
    },
    // modelEvents: string[] | string;
    // plugins: string[] | string;
    // modelValue: string;
    // disabled: boolean;
    // tinymceScriptSrc: string;
  },
  emits: {
    'update:value': (paylod:string) => true,
    change: (paylod:string) => true,
  },
  setup(prop, { emit }) {
    // const value = ref();
    const { value, length } = toRefs(prop);
    const { onFieldChange } = Form.useInjectFormItemContext();
    let editorInstance:Editor;
    const currentValue = computed({
      set(val:string) {
        if (length.value) {
          const text = editorInstance.getContent({ format: 'text' });
          const len = length.value as number;
          if (text.length > len) {
            editorInstance.setContent(value.value);
            message.warning('输入长度超过限制，自动截断');
            val = value.value;
          }
        }
        emit('update:value', val);
        emit('change', val);
        onFieldChange();
      },
      get() {
        return value.value;
      },
    });

    const upLoad = (cb:(...arg:any)=>void, value:string, meta:string) => {
      const input = document.createElement('input');
      // console.log(cb, value, meta, input);
      input.setAttribute('type', 'file');
      input.setAttribute('accept', 'image/*');
      input.onchange = async () => {
        const file = (input as any).files[0];

        const { key, fullUrl } = await useUpload().uploadFile({ file });
        const initObj = { title: file.name, src: fullUrl };
        let Obj = {};
        if (prop.insertPictureAdaptation) {
          // obj.width = '100%';
          Obj = { ...initObj, width: '100%' };
        }
        cb(fullUrl, Obj);
      };

      input.click();
    };
    const initConf:RawEditorSettings = {
      base_url: `${compileConf.publicPath}static/tinymce`,
      language: 'zh_CN',
      plugins,
      fullscreen_native: true,
      toolbar_mode: 'floating',
      toolbar: 'fullscreen',
      convert_urls: false,
      automatic_uploads: true,
      image_title: true,
      file_picker_types: 'image',
      file_picker_callback: upLoad as any,
      setup: (e:Editor) => {
        editorInstance = e;
      },
    };

    onMounted(() => {
      tinymce.init({});
    });
    return {
      currentValue,
      initConf,
    };
  },
});
</script>
