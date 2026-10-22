/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 14:44:04
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-07 13:42:10
 */
import { CreateAppFunction, DirectiveBinding, Directive } from 'vue';

import { message } from 'ant-design-vue';

const autoFocus = {
  mounted(el:HTMLElement) {
    el.focus();
  },
};

// 只允许输入大写英文以及数字
function Alphabet(el:any) {
  const input = el.querySelector('#valueInput');
  input.onkeyup = function onkeyup(e:any) {
    input.value = input.value.replace(/[^A-F0-9]/g, '');
  };
  input.onblur = function onblur(e:any) {
    input.value = input.value.replace(/[^A-F0-9]/g, '');
  };
}

function getCopy() {
  function hanldleClick(e:MouseEvent) { // 点击事件
    let copyValue:string = '';
    const el = this as HTMLElement;
    copyValue = el.dataset.value || el?.innerText || '';
    copy(copyValue);
  }
  async function copy(copyValue:string) {
    const oldCopy = () => {
      const input = document.createElement('input');
      input.value = copyValue;
      document.body.appendChild(input);
      input.select();
      document.execCommand('copy');
      document.body.removeChild(input);
    };
    try {
      const { state } = await navigator.permissions.query({ name: 'clipboard-write' as any });
      if (state === 'granted') {
        await navigator.clipboard.writeText(copyValue);
      } else {
        oldCopy();
      }
    } catch (error) {
      oldCopy();
      console.log(error, 'error  ');
    }
    message.success(`复制成功：【${copyValue}】`);
  }

  const copyDirecive:Directive = {
    mounted(el:HTMLElement, { value }:DirectiveBinding) {
      el.dataset.value = value;
      el.addEventListener('click', hanldleClick);
    },
    updated(el:HTMLElement, { value }:DirectiveBinding) { // 更新绑定值
      el.dataset.value = value;
    },
    unmounted(el:HTMLElement) {
      el.removeEventListener('click', hanldleClick);
    },
  };
  return copyDirecive;
}

export const directives = (app:ReturnType<CreateAppFunction<Element>>) => {
  app.directive('focus', autoFocus);
  app.directive('copy', getCopy());
  app.directive('Alphabet', Alphabet);
};
