/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-28 15:25:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-29 12:18:53
 */
import { Ref } from 'vue';
import { Form } from 'ant-design-vue';

const { useForm } = Form;
export type UserFormInstance = ReturnType<typeof useForm>

export interface Props {
    [key: string]: any;
}
export default <T>(modelRef:T, rulesRef?:Props) => {
  const {
    initialModel,
    validateInfos,
    resetFields,
    validate,
    validateField,
    mergeValidateInfo,
    clearValidate,
  } = useForm(modelRef as any, rulesRef);
  return {
    modelRef,
    rulesRef,
    initialModel,
    validateInfos,
    resetFields,
    validate,
    validateField,
    mergeValidateInfo,
    clearValidate,
  };
};
