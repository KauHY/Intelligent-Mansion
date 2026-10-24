/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-03 19:51:20
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { AddUserParams } from '~models/equipment-management';
import useForm from '~utils/useForm';

export const floorDialogTitleNames = {
  add: '新增楼栋',
  edit: '编辑楼栋',
};

export const houseDialogTitleNames = {
  add: '新增一级分类',
  edit: '编辑一级分类',
  addTwice: '新增二级分类',
  editTwice: '编辑二级分类',
  addEqui: '新增设备',
  editEqui: '新增设备',
  del: '',
  copy: '复制设备',
  detail: '设备详情',
};

/**
 * @description: 新增用户
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddUserForm = (() => {
  const createInstance = () => {
    const modelRef = reactive<AddUserParams>({
      adminName: '',
      adminId: '',
      brand: '',
      buildingId: '',
      categoryId: '',
      deviceCode: '',
      floor: '',
      id: '',
      installationDate: '',
      installationLocation: '',
      manufacturer: '',
      manufacturerMobile: '',
      manufacturerUnit: '',
      name: '',
      originalNumber: '',
      parkOrgId: '',
      pic: '',
      item: '',
      remark: '',
      scrapDate: '',
      specifications: '',
      state: true,
    });
    const rulesRef = reactive({
      name: [{ required: true, message: '请输入设备名称' }],
      categoryId: [{ required: true, message: '请选择设备分类' }],
      buildingId: [{ required: true, message: '请选择安装楼栋' }],
      floor: [{ required: true, message: '请选择安装楼层' }],
    });

    const fill = (defaultModelRef:any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        console.log(key, '------------');
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
      console.log(copyModelRef, 999999);
    };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
    };
  };

  // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance();
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();

export const columns = [{
  title: '设备名称',
  dataIndex: 'name',
  slots: { customRender: 'name' },
}, {
  title: '设备编码',
  dataIndex: 'deviceCode',
}, {
  title: '安装楼栋',
  dataIndex: 'buildingName',
}, {
  title: '安装楼层',
  dataIndex: 'floor',
  customRender: ({ record }:{record:any}) => `${record.floor}F`,
}, {
  title: '详细位置',
  dataIndex: 'installationLocation',
}, {
  title: '设备分类',
  dataIndex: 'categoryName',
}, {
  title: '状态',
  dataIndex: 'state',
  slots: { customRender: 'state' },
}, {
  title: '创建时间',
  dataIndex: 'createTime',
}, {
  title: '报废日期',
  dataIndex: 'scrapDate',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 300,
  slots: { customRender: 'operate' },
}];
