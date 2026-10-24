<template>
  <div class="add-user">
    <div class="billing-scheme-operate-dialog">
      <a-form
        :label-col="{span:6}"
        :width="980"
        :rules="rulesRef"
      >
        <a-card
          title="设备管理"
          :bordered="false"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="所属园区"
              >
                <span>{{ nodeName }}</span>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="设备名称"
                v-bind="validateInfos.name"
              >
                <a-input
                  v-model:value="modelRef.name"
                  :maxlength="30"
                  :disabled="formDisabled"
                  placeholder="请输入设备名称"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="设备分类"
                v-bind="validateInfos.categoryId"
              >
                <a-tree-select
                  v-model:value="modelRef.categoryId"
                  :disabled="formDisabled"
                  style="width: 100%;"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="deviceTypeOpts"
                  placeholder="请选择设备分类"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="安装楼栋"
                v-bind="validateInfos.buildingId"
              >
                <v-select
                  v-model:value="modelRef.buildingId"
                  :disabled="formDisabled"
                  placeholder="请选择安装楼栋"
                  @change="
                    (e) => {
                      buildChange(e, modelRef.buildingId)
                    }
                  "
                >
                  <a-select-option
                    v-for="opt in buildingOpts"
                    :key="opt.value"
                    :value="opt.value"
                    @click="buildClick(opt)"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="安装楼层"
                v-bind="validateInfos.floor"
              >
                <v-select
                  v-model:value="modelRef.floor"
                  :disabled="formDisabled"
                  placeholder="请选择安装楼层"
                >
                  <a-select-option
                    v-for="opt in floorIdOpts"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="详细位置"
                v-bind="validateInfos.installationLocation"
              >
                <a-input
                  v-model:value="modelRef.installationLocation"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入详细位置"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="设备管理员"
                v-bind="validateInfos.adminId"
              >
                <v-select-employee
                  v-model:value="modelRef.adminId"
                  :disabled="formDisabled"
                  :default-select-opts="defaultOptions"
                  placeholder="请选择设备管理员"
                  title="选择跟进人"
                  mode="single"
                  :company-type="0"
                  :park-org-ids="modelRef.parkOrgId"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="设备品牌"
                v-bind="validateInfos.brand"
              >
                <a-input
                  v-model:value="modelRef.brand"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入设备品牌"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="备注"
                v-bind="validateInfos.remark"
              >
                <a-textarea
                  v-model:value="modelRef.remark"
                  :disabled="formDisabled"
                  :maxlength="200"
                  placeholder="请输入备注"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item
                label="设备图片"
                v-bind="validateInfos.pic"
              >
                <v-upload
                  v-model:value="modelRef.pic"
                  :disabled="formDisabled"
                  :count="10"
                  :multiple="true"
                  accept=".jpg,.bmp,.png,.gif"
                  :size="10"
                  tip="仅支持以下格式文件，jpg、bmp、png、gif，文件大小不超过10M。"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="主要参数"
          :bordered="false"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="规格型号"
                :maxlength="30"
                v-bind="validateInfos.specifications"
              >
                <a-input
                  v-model:value="modelRef.specifications"
                  :disabled="formDisabled"
                  placeholder="请输入规格型号"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="安装日期"
                v-bind="validateInfos.installationDate"
              >
                <a-date-picker
                  v-model:value="modelRef.installationDate"
                  :disabled="formDisabled"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="原厂编号"
                v-bind="validateInfos.originalNumber"
              >
                <a-input
                  v-model:value="modelRef.originalNumber"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入原厂编号"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="生产厂家"
                v-bind="validateInfos.manufacturer"
              >
                <a-input
                  v-model:value="modelRef.manufacturer"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入生产厂家"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="维保单位"
                v-bind="validateInfos.manufacturerUnit"
              >
                <a-input
                  v-model:value="modelRef.manufacturerUnit"
                  :maxlength="30"
                  :disabled="formDisabled"
                  placeholder="请输入维保单位"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="维保电话"
                v-bind="validateInfos.manufacturerMobile"
              >
                <a-input
                  v-model:value="modelRef.manufacturerMobile"
                  :maxlength="30"
                  :disabled="formDisabled"
                  placeholder="请输入维保电话"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent, toRefs,
} from 'vue';
//
import { useUserStore } from '@/store/user';
import Role from '~models/role';
import { useRemoteFilterSelect } from '~composition/common';
import Building from '~models/building';
import EquipmentApi from '~models/equipment-management';
import { useAddUserForm } from '../config';

export default defineComponent({
  name: '',
  props: {
    dialogData: {
      type: Object,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const { dialogData, type } = toRefs(props);
    const nodeName = dialogData.value.parkOrgName;
    const nodeValue = dialogData.value.parkOrgId;
    const userStore = useUserStore();
    const currentUserOrgid = userStore.userInfo.orgId;
    const defaultOptions = ref<SelectOptions>([]);
    const buildingOpts = ref<SelectOptions>([]);
    const floorIdOpts = ref<SelectOptions>([]);
    const deviceTypeOpts = ref<any>([]);
    // 角色列表下拉框
    const {
      loading,
      remoteSearch,
      list: roleOpts,
    } = useRemoteFilterSelect<any>(new Role().getRolesListByOrgId, {
      optionsValueKey: 'id',
      disableAutoSearch: true,
    });

    const {
      modelRef, validateInfos, rulesRef, fill,
    } = useAddUserForm();
    modelRef.parkOrgId = nodeValue;
    if (type.value === 'add') {
      modelRef.categoryId = dialogData.value.curentId;
    }
    const formDisabled = type.value === 'detail';
    const init = async () => {
      getTypeOpts();
      parkChange(modelRef.parkOrgId);
      if (type.value === 'edit' || type.value === 'copy' || type.value === 'detail') {
        fill(dialogData.value);
        if (modelRef.adminId) {
          defaultOptions.value = [{
            label: modelRef.adminName,
            value: modelRef.adminId,
          }];
        }
      }
    };
    const getTypeOpts = async () => {
      const data = await new EquipmentApi().listMultistageCategoryTreemultistage({
        categoryType: 'TZ',
        parkOrgId: nodeValue,
      });
      const getColumns = data.children.map((item: any) => ({
        ...item, disabled: item.hierarchy === 2,
      }));
      deviceTypeOpts.value = getColumns;
      console.log(data.children, 'deviceTypeOpts');
    };
    const parkChange = async (value:string) => {
      if (!value) {
        buildingOpts.value = [];
        floorIdOpts.value = [];
        modelRef.floor = '';
        modelRef.buildingId = '';
      } else {
        buildingOpts.value = await new Building().listBuildingDict(value);
        console.log(buildingOpts, 'buildingOpts');
        const floorNum = buildingOpts.value.find((item:any) => modelRef.buildingId === item.value)?.extraInfo?.totalFloor || 0;
        buildClick(floorNum);
      }
    };
    const buildClick = (opt: any) => {
      let totalFloor = null;
      if (typeof opt === 'object') {
        totalFloor = opt.extraInfo.totalFloor;
      } else {
        totalFloor = opt;
      }
      // 根据楼栋获取楼陈
      floorIdOpts.value = Array.from(new Array(totalFloor)).map((item, index) => ({
        label: `${index + 1}F`,
        value: `${index + 1}`,
      }));
      console.log(totalFloor, opt, 'totalFloor', floorIdOpts);
    };
    // 楼栋切换时
    const buildChange = async (value: string, buildingId: string) => {
      floorIdOpts.value = [];
      modelRef.floor = '';
    };
    init();
    return {
      formDisabled,
      nodeName,
      nodeValue,
      rulesRef,
      modelRef,
      validateInfos,
      loading,
      remoteSearch,
      roleOpts,
      currentUserOrgid,
      parkChange,
      defaultOptions,
      buildingOpts,
      floorIdOpts,
      deviceTypeOpts,
      buildClick,
      buildChange,
    };
  },
});
</script>
