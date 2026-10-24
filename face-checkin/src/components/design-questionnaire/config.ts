/* eslint-disable global-require */
/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 16:22:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 10:46:30
 */
export const componentList = {
  radio: {
    tag: 'radio',
    name: '单选题',
    icon: require('assets/images/design-questionnaire/radio_icon.png'),
  },
  checkbox: {
    tag: 'checkbox',
    name: '多选题',
    icon: require('assets/images/design-questionnaire/checkbox_icon.png'),

  },
  input: {
    tag: 'input',
    name: '单行文本',
    icon: require('assets/images/design-questionnaire/input_icon.png'),

  },
  textarea: {
    tag: 'textarea',
    name: '多行文本',
    icon: require('assets/images/design-questionnaire/textarea_icon.png'),

  },
  upload: {
    tag: 'upload',
    name: '上传附件',
    icon: require('assets/images/design-questionnaire/upload_icon.png'),
  },
};

export const defaultConfigValue = { // 配置默认值
  radio: () => ({
    id: '',
    name: '请输入标题',
    elementType: 'radio',
    value: '',
    constraintCondition: { required: true },
    elementContent: ['选项1'],
  }),
  checkbox: () => ({
    id: '',
    name: '请输入标题',
    value: '',
    elementType: 'checkbox',
    constraintCondition: { required: true, minlength: null },
    elementContent: ['选项1'],
  }),
  textarea: () => ({
    id: '',
    name: '请输入标题',
    value: '',
    elementType: 'textarea',
    constraintCondition: { required: true },
    elementContent: '',
  }),
  input: () => ({
    id: '',
    name: '请输入标题',
    value: '',
    elementType: 'input',
    constraintCondition: { required: true },
    elementContent: '',
  }),
  upload: () => ({
    id: '',
    name: '请上传附件？',
    value: '',
    elementType: 'upload',
    constraintCondition: { required: true },
    elementContent: '',
  }),
};
