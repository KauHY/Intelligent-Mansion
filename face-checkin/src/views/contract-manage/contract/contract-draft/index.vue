<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-06-09 16:49:32
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <div class="content-section draft-css">
    <div class="contract-draft content-scroll">
      <div class="draft-form">
        <a-button
          @click="goBackPage"
        >
          返回上一级
        </a-button>
        <a-button
          v-if="handleType === 'detail' && state !== '1'"
          class="down-draft"
          @click="downDraft"
        >
          {{ contractType === '1'?'下载协议':'下载合同' }}
        </a-button>
        <a-form
          ref="formRef"
          :model="modelRef"
          :rules="rulesRef"
          :label-col="{ span: 5 }"
        >
          <div class="draft-title">
            基础信息
          </div>
          <div>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  :label="`${fieldPrefix}编号`"
                  name="no"
                >
                  {{ modelRef.no }}
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="所属园区"
                  name="parkOrgId"
                >
                  <v-select
                    v-model:value="modelRef.parkOrgId"
                    placeholder="请选择所属园区"
                    :disabled="!(handleType === 'add' || handleType === 'copy')"
                    @change="parkOrgIdChange"
                  >
                    <a-select-option
                      v-for="opt in parkOpts"
                      :key="opt.value"
                      :value="opt.value"
                    >
                      {{ opt.label }}
                    </a-select-option>
                  </v-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row v-if="handleType === 'extend' || modelRef.attributes === 2">
              <a-col :span="12">
                <a-form-item :label="`原${fieldPrefix}编号`">
                  {{ modelRef.extension && modelRef.extension.originContractNo }}
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  :label="`${fieldPrefix}属性`"
                  name="attributes"
                >
                  {{ attributesName }}
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  :label="`${fieldPrefix}类型`"
                  name="type"
                >
                  {{ contractTypeName }}
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  :label="`${fieldPrefix}名称`"
                  name="name"
                >
                  <a-input
                    v-model:value="modelRef.name"
                    :placeholder="`请输入${fieldPrefix}名称`"
                    :disabled="formDisabled"
                    :maxlength="30"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="有效期"
                  name="startDate"
                  class="date-style"
                >
                  <a-date-picker
                    v-model:value="modelRef.startDate"
                    :disabled="formDisabled"
                    style="width: 100%;"
                    placeholder="开始日期"
                    :allow-clear="false"
                    value-format="YYYY-MM-DD"
                    :disabled-date="disabledstartDate"
                    @change="changeContractDate"
                  />
                  <div class="line-style">
                    —
                  </div>
                  <a-date-picker
                    v-model:value="modelRef.endDate"
                    :disabled="formDisabled"
                    style="width: 100%;"
                    :allow-clear="false"
                    placeholder="结束日期"
                    value-format="YYYY-MM-DD"
                    :disabled-date="disabledendDate"
                    @change="changeContractEndDate"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <!-- 租赁合同管理  装修日期 租期(年) 租金(元) 履约保证金（元） -->
            <div v-if="contractType === '3'">
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="装修日期"
                    name="renovationStartDate"
                  >
                    <a-range-picker
                      v-model:value="decorationDate"
                      :disabled="formDisabled"
                      style="width: 100%;"
                      :mode="['month', 'month']"
                      format="YYYY-MM"
                      value-format="YYYY-MM"
                      @panelChange="handleMonthChange"
                    />
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="租期（年）"
                    name="extension.leaseTermYear"
                  >
                    <a-input-number
                      v-model:value="modelRef.extension.leaseTermYear"
                      :disabled="formDisabled"
                      style="width: 100%;"
                      placeholder="请输入租期"
                    />
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="履约保证金（元）"
                    name="performanceBond"
                  >
                    {{ modelRef.extension.performanceBond }}
                  </a-form-item>
                </a-col>
              </a-row>
            </div>

            <!-- ---------------------------------------------- -->
            <!-- 物业合同管理  租期(年) 物业费(元) -->
            <div v-if="contractType === '4'">
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="租期（年）"
                    name="leaseTermYear"
                  >
                    <a-input-number
                      v-model:value="modelRef.extension.leaseTermYear"
                      :disabled="formDisabled"
                      style="width: 100%;"
                      :min="0"
                      placeholder="请输入租期"
                    />
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="物业费（元）"
                    name="propertyCosts"
                  >
                    {{ modelRef.extension.propertyCosts }}
                  </a-form-item>
                </a-col>
              </a-row>
            </div>
            <!-- ---------------------------------------------- -->
            <a-row>
              <a-col
                v-if="contractType !== '5'"
                :span="12"
              >
                <a-form-item
                  label="总面积（㎡）"
                  name="totalArea"
                >
                  {{ modelRef.totalArea }}
                </a-form-item>
              </a-col>
              <a-col
                v-if="contractType === '3'"
                :span="12"
              >
                <a-form-item
                  label="租金（元）"
                  name="rentAmount"
                >
                  {{ modelRef.extension.rentAmount }}
                </a-form-item>
              </a-col>
            </a-row>
            <!-- ---------------------------------------------- -->
            <!-- 公寓合同管理 租期(天) 租金(元) 客房数(间) 押金(元) -->
            <div v-if="contractType === '5'">
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="租期（天）"
                    name="leaseTermDay"
                  >
                    {{ modelRef.extension.leaseTermDay }}
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="租金（元）"
                    name="rentAmount"
                  >
                    {{ modelRef.extension.rentAmount }}
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="客房数（间）"
                    name="numberOfRooms"
                  >
                    {{ modelRef.extension.numberOfRooms }}
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="押金（元）"
                    name="deposit"
                  >
                    {{ modelRef.extension.deposit }}
                  </a-form-item>
                </a-col>
              </a-row>
            </div>
            <!-- ---------------------------------------------- -->
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="经办人"
                  name="employeeId"
                >
                  <v-select-employee
                    v-model:value="modelRef.employeeId"
                    :default-select-opts="defaultEmployeeOpts"
                    placeholder="请选择经办人"
                    title="选择人员"
                    :company-type="0"
                    :disabled="!modelRef.parkOrgId || formDisabled"
                    :park-org-ids="modelRef.parkOrgId"
                  />
                  <!-- <view v-show="!modelRef.parkOrgId">
                    请先选择所属园区
                  </view> -->
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="办理日期"
                  name="contractedDate"
                >
                  <a-date-picker
                    v-model:value="modelRef.contractedDate"
                    :disabled="formDisabled"
                    style="width: 100%;"
                    value-format="YYYY-MM-DD"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="备注"
                  name="remark"
                >
                  <a-textarea
                    v-model:value="modelRef.remark"
                    :disabled="formDisabled"
                    placeholder="请输入备注"
                    :auto-size="{ minRows: 2, maxRows: 5 }"
                    :maxlength="200"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  :label="`${fieldPrefix}附件`"
                  name="attachmentKey"
                >
                  <v-upload
                    v-model:value="modelRef.attachmentKey"
                    :disabled="formDisabled"
                    :size="20"
                    :multiple="true"
                    :count="10"
                    list-type="text"
                    accept=".pdf,.png,.jpg,.jpeg,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.zip"
                    tip="仅支持以下格式文件，pdf/doc/docx/xls/xlsx/ppt/pptx/png/jpg/jpeg/gif/zip，文件大小不超过20M。"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
          <div class="draft-title">
            甲方信息
          </div>
          <div>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="甲方"
                  name="nameA"
                >
                  <a-input
                    v-model:value="modelRef.nameA"
                    disabled
                  >
                    <template #addonAfter>
                      <span
                        class="cursor"
                        @click="showModal('partyA')"
                      > 选择 </span>
                    </template>
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="统一社会信用代码"
                  name="creditCodeA"
                >
                  <a-input
                    v-model:value="modelRef.creditCodeA"
                    disabled
                    placeholder=" "
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="甲方联系人"
                  name="contactA"
                >
                  <a-input
                    v-model:value="modelRef.contactA"
                    :disabled="formDisabled"
                    :maxlength="99"
                    placeholder="请输入甲方联系人"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="地址"
                  name="addressA"
                >
                  <a-input
                    v-model:value="modelRef.addressA"
                    :disabled="formDisabled"
                    placeholder="请输入地址"
                    :maxlength="99"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="甲方联系电话"
                  name="contactTelA"
                >
                  <a-input
                    v-model:value="modelRef.contactTelA"
                    :disabled="formDisabled"
                    placeholder="请输入甲方联系电话"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
          <div class="draft-title">
            乙方信息
          </div>
          <div>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="乙方"
                  name="nameB"
                >
                  <a-input
                    v-model:value="modelRef.nameB"
                    disabled
                  >
                    <template #addonAfter>
                      <span
                        class="cursor"
                        @click="showModal('partyB')"
                      > 选择 </span>
                    </template>
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="统一社会信用代码"
                  name="creditCodeB"
                >
                  <a-input
                    v-model:value="modelRef.creditCodeB"
                    disabled
                    placeholder=" "
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="乙方联系人"
                  name="contactB"
                >
                  <a-input
                    v-model:value="modelRef.contactB"
                    :disabled="formDisabled"
                    :maxlength="99"
                    placeholder="请输入乙方联系人"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="地址"
                  name="addressB"
                >
                  <a-input
                    v-model:value="modelRef.addressB"
                    :disabled="formDisabled"
                    :maxlength="99"
                    placeholder="请输入地址"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="乙方联系电话"
                  name="contactTelB"
                >
                  <a-input
                    v-model:value="modelRef.contactTelB"
                    :disabled="formDisabled"
                    :maxlength="200"
                    placeholder="请输入乙方联系电话"
                  />
                </a-form-item>
              </a-col>
              <a-col
                v-if="contractType === '1'"
                :span="12"
              >
                <a-form-item
                  label="乙方联系人身份证号码"
                  name="extension.contactIdCardNoB"
                >
                  <a-input
                    v-model:value="modelRef.extension.contactIdCardNoB"
                    :disabled="formDisabled"
                    placeholder="请输入乙方联系人身份证号码"
                  />
                </a-form-item>
              </a-col>
              <a-col
                v-if="contractType === '2'"
                :span="12"
              >
                <a-form-item
                  label="年纳税额（万元）"
                  name="extension.annualTaxTenThousand"
                >
                  <a-input-number
                    v-model:value="modelRef.extension.annualTaxTenThousand"
                    :disabled="formDisabled"
                    style="width: 100%;"
                    :max="999999"
                    :min="0"
                    placeholder="请输入年纳税额"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col
                v-if="contractType === '2'"
                :span="12"
              >
                <a-form-item
                  label="经营范围"
                  name="businessScope"
                >
                  <a-textarea
                    v-model:value="modelRef.extension.businessScope"
                    :disabled="formDisabled"
                    placeholder="请输入经营范围"
                    :auto-size="{ minRows: 2, maxRows: 5 }"
                    :maxlength="200"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
          <div class="draft-title">
            房屋信息
          </div>
          <div
            v-if="contractType === '5'"
            class="house-census"
          >
            <p>
              <span>单间（间）:</span>
              <span>{{ modelRef.extension.numberOfSingleRooms }}</span>
            </p>
            <p>
              <span>标间（间）:</span>
              <span>{{ modelRef.extension.numberOfTwinRooms }}</span>
            </p>
            <p>
              <span>套间（间）:</span>
              <span>{{ modelRef.extension.numberOfSuite }}</span>
            </p>
          </div>
          <div class="draft-house">
            <div class="house-bar">
              <span>关联房屋</span>
              <a-button
                :disabled="formDisabled"
                @click="showHouseModal"
              >
                选择
              </a-button>
            </div>
            <v-table
              :data-source="houseList"
              :columns="columns"
              row-key="id"
              bordered
            >
              <!-- :scroll="{y: 400}" -->
              <template #operate="{ record }">
                <a-button
                  :disabled="formDisabled"
                  type="link"
                  @click="houseDel(record)"
                >
                  删除
                </a-button>
              </template>
            </v-table>
          </div>
          <div v-if="!(contractType === '1' || contractType === '2')">
            <div class="draft-title">
              计费信息
            </div>
            <div>
              <a-collapse v-model:activeKey="activeKey">
                <a-collapse-panel
                  v-for="(planItem, index) in modelRef.contractBillingPlanReqDTOS"
                  :key="String(index + 1)"
                  :header="`计费方案${index + 1}`"
                >
                  <template
                    v-if="index >= 0"
                    #extra
                  >
                    <span
                      v-show="!formDisabled"
                      @click="del($event, index)"
                    > 删除 </span>
                  </template>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item
                        label="计费方案"
                        :name="['contractBillingPlanReqDTOS', index, 'feeBillingSchemeName']"
                        :rules="rulesRef.feeBillingSchemeName"
                      >
                        <a-input
                          v-model:value="planItem.feeBillingSchemeName"
                          disabled
                        >
                          <template #addonAfter>
                            <span
                              class="cursor"
                              @click="showBillSchemeModal(planItem,index)"
                            > 选择 </span>
                          </template>
                        </a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item
                        label="收费项"
                        :name="['contractBillingPlanReqDTOS', index, 'chargeItemName']"
                      >
                        {{ planItem.chargeItemMessage.chargeName || planItem.chargeItemName }}
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item
                        label="应用房源"
                        :name="['contractBillingPlanReqDTOS', index, 'houseIds']"
                        :rules="rulesRef.houseIds"
                      >
                        <v-select
                          v-model:value="planItem.houseIds"
                          :disabled="formDisabled"
                          placeholder="请选择应用房源"
                          mode="multiple"
                          @change="getComputedData(planItem)"
                        >
                          <a-select-option
                            v-for="opt in setHouseDisabled(planItem,index)"
                            :key="opt.value"
                            :disabled="opt.disabled"
                          >
                            {{ opt.label }}
                          </a-select-option>
                        </v-select>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item
                        label="起止日期"
                        :name="['contractBillingPlanReqDTOS', index, 'billingStartDate']"
                        :rules="rulesRef.billingStartDate"
                      >
                        <a-range-picker
                          v-model:value="planItem.billingStartAndEndDate"
                          :disabled="formDisabled || contractDate.length === 0"
                          style="width: 100%;"
                          :allow-clear="false"
                          :placeholder="['开始日期  ', '结束日期']"
                          value-format="YYYY-MM-DD"
                          :get-calendar-container="getCalendarContainer"
                          :disabled-date="disabledDate"
                          @change="dateChange($event, planItem)"
                        />
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <div v-if="planItem.feeBillingSchemeId && planItem.billingStartAndEndDate.length > 0 && planItem.houseIds.length > 0">
                    <!-- 调试时一直显示收费项所有表单 -->
                    <!-- <div> -->
                    <a-row>
                      <a-col :span="12">
                        <a-form-item
                          label="计费周期"
                          :name="['contractBillingPlanReqDTOS', index, 'billingCycle']"
                          :rules="rulesRef.billingCycle"
                        >
                          <v-select
                            v-model:value="planItem.billingCycle"
                            :disabled="formDisabled || contractType === '4' || contractType === '3'"
                            placeholder="请选择计费周期"
                            @change="changeBillingCycle($event, planItem)"
                          >
                            <a-select-option
                              v-for="opt in options?.FEE_BILLING_CYCLE"
                              :key="opt.value"
                              :value="opt.value"
                            >
                              {{ opt.label }}
                            </a-select-option>
                          </v-select>
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="保留位数"
                          :name="['contractBillingPlanReqDTOS', index, 'reservedDigits']"
                          :rules="rulesRef.reservedDigits"
                        >
                          <v-select
                            v-model:value="planItem.reservedDigits"
                            disabled
                            placeholder="请选择保留位数"
                          >
                            <a-select-option
                              v-for="opt in options?.FEE_RESERVED_DIGITS"
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
                          label="进位方式"
                          :name="['contractBillingPlanReqDTOS', index, 'carryMode']"
                          :rules="rulesRef.carryMode"
                        >
                          <v-select
                            v-model:value="planItem.carryMode"
                            placeholder="请选择进位方式"
                            disabled
                          >
                            <a-select-option
                              v-for="opt in options?.FEE_CARRY_MODE"
                              :key="opt.value"
                              :value="opt.value"
                            >
                              {{ opt.label }}
                            </a-select-option>
                          </v-select>
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="公式类型"
                          :name="['contractBillingPlanReqDTOS', index, 'formulaType']"
                          :rules="rulesRef.formulaType"
                        >
                          <v-select
                            v-model:value="planItem.formulaType"
                            :disabled="formDisabled"
                            placeholder="请选择公式类型"
                            @change="getComputedData(planItem)"
                          >
                            <a-select-option
                              v-for="opt in options?.FEE_FORMULA_TYPE"
                              :key="opt.value"
                              :value="opt.value"
                            >
                              {{ opt.label }}
                            </a-select-option>
                          </v-select>
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <!-- <a-row v-if="planItem.formulaType === 1">
                      <a-col :span="12">
                        <a-form-item
                          label="固定金额(不含税)"
                          :name="['contractBillingPlanReqDTOS', index, 'fixedAmountWithoutTax']"
                          :rules="rulesRef.fixedAmountWithoutTax"
                        >
                          <a-input
                            v-model:value="planItem.fixedAmountWithoutTax"
                            :disabled="formDisabled"
                            placeholder="请输入固定金额"
                            suffix="元"
                            @blur="amountChange($event, planItem, 'fixedAmountWithTax')"
                          />
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="固定金额(含税)"
                          :name="['contractBillingPlanReqDTOS', index, 'fixedAmountWithTax']"
                        >
                          <a-input
                            v-model:value="planItem.fixedAmountWithTax"
                            disabled
                            suffix="元"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row> -->
                    <a-row v-if="planItem.formulaType === 2">
                      <a-col :span="12">
                        <a-form-item
                          label="计算标准"
                          :name="['contractBillingPlanReqDTOS', index, 'calculationStandard']"
                          :rules="rulesRef.calculationStandard"
                        >
                          <v-select
                            v-model:value="planItem.calculationStandard"
                            placeholder="请选择计算标准"
                            :disabled="formDisabled"
                            @change="getComputedData(planItem)"
                          >
                            <a-select-option
                              v-for="opt in freightBasisOpts[index]"
                              :key="opt.value"
                              :value="opt.value"
                            >
                              {{ opt.label }}
                            </a-select-option>
                          </v-select>
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="免租期"
                          :name="['contractBillingPlanReqDTOS', index, 'freeRentDay']"
                          :rules="rulesRef.freeRentDay"
                        >
                          <a-input
                            v-model:value="planItem.freeRentDay"
                            :disabled="formDisabled"
                            placeholder="请输入免租期"
                            :suffix="contractType === '4' || contractType === '3'?'月':'天'"
                            @blur="getComputedData(planItem)"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row v-if="planItem.formulaType === 2">
                      <a-col :span="12">
                        <a-form-item
                          label="单价元(不含税)"
                          :name="['contractBillingPlanReqDTOS', index, 'unitPriceWithoutTax']"
                          :rules="rulesRef.unitPriceWithoutTax"
                        >
                          <a-input
                            v-model:value="planItem.unitPriceWithoutTax"
                            :disabled="formDisabled"
                            placeholder="请输入单价元（不含税），不能为零"
                            suffix="元"
                            @blur="amountChange($event, planItem, 'unitPriceWithTax')"
                          />
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="单价元(含税)"
                          :name="['contractBillingPlanReqDTOS', index, 'unitPriceWithTax']"
                        >
                          <a-input
                            v-model:value="planItem.unitPriceWithTax"
                            disabled
                            suffix="元"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row>
                      <a-col :span="12">
                        <a-form-item
                          label="总金额(不含税)"
                          :name="['contractBillingPlanReqDTOS', index, 'amountWithoutTax']"
                          :rules="rulesRef.amountWithoutTax"
                        >
                          <a-input
                            v-model:value="planItem.amountWithoutTax"
                            :disabled="formDisabled || planItem.formulaType===2"
                            placeholder="请输入总金额（不含税），不能为零"
                            suffix="元"
                            @blur="amountChange($event, planItem, 'amountWithTax')"
                          />
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="总金额(含税)"
                          :name="['contractBillingPlanReqDTOS', index, 'amountWithTax']"
                        >
                          <a-input
                            v-model:value="planItem.amountWithTax"
                            disabled
                            suffix="元"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row>
                      <a-col :span="12">
                        <a-form-item
                          label="收费类型"
                          :name="['contractBillingPlanReqDTOS', index, 'chargeType']"
                          :rules="rulesRef.chargeType"
                        >
                          <v-select
                            v-model:value="planItem.chargeType"
                            :disabled="formDisabled"
                            placeholder="请选择收费类型"
                          >
                            <a-select-option
                              v-for="opt in options?.FEE_CHARGE_TYPE"
                              :key="opt.value"
                              :value="opt.value"
                            >
                              {{ opt.label }}
                            </a-select-option>
                          </v-select>
                        </a-form-item>
                      </a-col>
                      <a-col :span="planItem.chargingCycle === 2 ? 8 : 12">
                        <a-form-item
                          label="收费周期"
                          :label-col="{ span: planItem.chargingCycle === 2 ? 8 : 5 }"
                          :name="['contractBillingPlanReqDTOS', index, 'chargingCycle']"
                          :rules="rulesRef.chargingCycle"
                        >
                          <a-radio-group
                            v-model:value="planItem.chargingCycle"
                            :disabled="formDisabled"
                          >
                            <a-radio :value="1">
                              与计费周期一致
                            </a-radio>
                            <!-- <a-radio :value="2">
                              多计费周期
                            </a-radio> -->
                          </a-radio-group>
                        </a-form-item>
                      </a-col>
                      <a-col
                        v-if="planItem.chargingCycle === 2"
                        :span="4"
                      >
                        <a-form-item label="">
                          <a-input
                            v-model:value="planItem.cumulativeCycle"
                            :disabled="formDisabled"
                            prefix="累计"
                            suffix="个计费周期"
                            placeholder="请输入累计计费周期"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row>
                      <a-col :span="12">
                        <a-form-item
                          label="账单生成日期"
                          :name="['contractBillingPlanReqDTOS', index, 'dayOfMonth']"
                          :rules="rulesRef.dayOfMonth"
                          class="cost-flex"
                        >
                          <span
                            v-if="(planItem.billingCycle === 3 || planItem.billingCycle === 4) &&( !['4','3'].includes(contractType))"
                            class="ml-10"
                          >第</span>
                          <v-select
                            v-if="(planItem.billingCycle === 3 || planItem.billingCycle === 4) &&( !['4','3'].includes(contractType))"
                            v-model:value="planItem.monthBy"
                            placeholder="请选择月份"
                            :disabled="formDisabled"
                          >
                            <a-select-option
                              v-for="opt in monthOpts"
                              :key="opt"
                            >
                              {{ opt }}
                            </a-select-option>
                          </v-select>
                          <span
                            v-if="(planItem.billingCycle === 3 || planItem.billingCycle === 4) &&( !['4','3'].includes(contractType))"
                            class="m-10"
                          >月</span>
                          <a-input
                            v-model:value="planItem.dayOfMonth"
                            :disabled="formDisabled"
                            placeholder="请输入账单生成日期"
                            suffix="天"
                            prefix="第"
                            @change="checkVail"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row>
                      <a-col :span="12">
                        <a-form-item
                          label="最晚缴纳日期"
                          :name="['contractBillingPlanReqDTOS', index, 'daysForDelay']"
                          :rules="rulesRef.daysForDelay"
                        >
                          <a-input
                            v-model:value="planItem.daysForDelay"
                            :disabled="formDisabled"
                            prefix="生成日期后第"
                            suffix="天"
                            placeholder="请输入最晚缴纳日期"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row>
                      <a-col :span="12">
                        <a-form-item
                          label="是否收取滞纳金"

                          :name="['contractBillingPlanReqDTOS', index, 'lateCharge']"
                        >
                          <a-radio-group
                            v-model:value="planItem.lateCharge"
                            :disabled="formDisabled"
                            @change="changeLateCharge($event, planItem)"
                          >
                            <a-radio :value="1">
                              是
                            </a-radio>
                            <a-radio :value="0">
                              否
                            </a-radio>
                          </a-radio-group>
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row v-if="planItem.lateCharge===1">
                      <a-col :span="12">
                        <a-form-item
                          label="滞纳率计算规则"
                          :name="['contractBillingPlanReqDTOS', index, 'lateType']"
                        >
                          <a-radio-group
                            v-model:value="planItem.lateType"
                            :disabled="formDisabled"
                            @change="changeRadio($event, planItem)"
                          >
                            <a-radio :value="1">
                              逾期金额*逾期天数*滞纳率
                            </a-radio>
                            <a-radio :value="2">
                              每日滞纳金*逾期天数*建筑面积
                            </a-radio>
                            <a-radio :value="3">
                              每日滞纳金*逾期天数*套内面积
                            </a-radio>
                          </a-radio-group>
                        </a-form-item>
                      </a-col>
                    </a-row>
                    <a-row v-if="planItem.lateCharge===1">
                      <a-col :span="12">
                        <a-form-item
                          v-if="planItem.lateType === 1 || !planItem.lateType"
                          label="滞纳率"
                          :name="['contractBillingPlanReqDTOS', index, 'lateRate']"
                          :rules="rulesRef.lateRate"
                        >
                          <a-input
                            v-model:value="planItem.lateRate"
                            :disabled="formDisabled"
                            placeholder="请输入滞纳率"
                            suffix="%"
                          />
                        </a-form-item>
                        <a-form-item
                          v-if="planItem.lateType === 2 || planItem.lateType === 3"
                          label="每日滞纳金"
                          :name="['contractBillingPlanReqDTOS', index, 'lateDayAmount']"
                          :rules="rulesRef.lateDayAmount"
                        >
                          <a-input
                            v-model:value="planItem.lateDayAmount"
                            suffix="元"
                            :disabled="formDisabled"
                            placeholder="请输入每日滞纳金"
                          />
                        </a-form-item>
                      </a-col>
                      <a-col :span="12">
                        <a-form-item
                          label="豁免天数"
                          :name="['contractBillingPlanReqDTOS', index, 'exemptDays']"
                          :rules="rulesRef.exemptDays"
                        >
                          <a-input
                            v-model:value="planItem.exemptDays"
                            :disabled="formDisabled"
                            style="width: 100%;"
                            placeholder="请输入豁免天数"
                            suffix="天"
                          />
                        </a-form-item>
                      </a-col>
                    </a-row>
                  </div>
                </a-collapse-panel>
              </a-collapse>
              <div
                v-show="!formDisabled"
                class="add-btn"
              >
                <a-button
                  type="link"
                  @click="add"
                >
                  新增计费方案
                </a-button>
              </div>
            </div>
          </div>
          <div v-if="modelRef.state === 9 && handleType === 'verify'">
            <a-row>
              <a-col :span="12">
                <a-form-item label="退约日期">
                  {{ modelRef.withdrawDate }}
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="退约原因">
                  {{ modelRef.withdrawReason }}
                </a-form-item>
              </a-col>
            </a-row>
          </div>
          <div v-if="(modelRef.attributes === 3 && handleType === 'detail') || handleType === 'change'">
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="变更说明"
                  name="extension.changeExplanation"
                >
                  <a-input
                    v-model:value="modelRef.extension.changeExplanation"
                    style="width: 100%;"
                    :disabled="handleType === 'detail'"
                    :maxlength="200"
                    placeholder="请输入变更说明"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
        </a-form>
      </div>
      <ContractVerify
        v-if="handleType === 'verify'"
        :id="modelRef.id"
        ref="verifyRef"
        @goBack="goBack"
      />
    </div>
    <div
      v-show="!(handleType === 'detail')"
      class="draft-foot"
    >
      <a-button
        class="btn-lf"
        type="primary"
        @click="save"
      >
        {{ handleType === 'verify' ? '确认' : '保存' }}
      </a-button>
      <a-button @click="goBack">
        取消
      </a-button>
    </div>
    <SelectParty
      v-if="partyFlag"
      ref="party"
      v-model:show="partyFlag"
      :party-config="partyConfig"
      @getPartyData="getPartyData"
    />
    <SelectHouse
      v-if="houseFlag"
      ref="house"
      v-model:show="houseFlag"
      :house-config="houseConfig"
      @getHouseData="getHouseData"
    />
    <SelectBillScheme
      v-if="billSchemeFlag"
      ref="bill"
      v-model:show="billSchemeFlag"
      :bill-scheme-config="billSchemeConfig"
      @getBillSchemeData="getBillSchemeData"
    />
    <DownFiles
      v-if="downFilesFlag"
      v-model:show="downFilesFlag"
      :party-config="downFilesConfig"
      @getBillSchemeData="getBillSchemeData"
    />
  </div>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, watch, Ref, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter, useRoute } from 'vue-router';
import * as dayjs from 'dayjs';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import { HouseInfo } from '~models/house';
import { CompanyInfo } from '~models/company-mangement';
import { CustomerManagementInfo } from '~models/wisdom-canvass-business';
import CollectFee, { BillingScheme, Calculation } from '~models/collect-fee';
import ContractVo, {
  Contract, ContractBillingPlanReqDTOS, ContractType, HouseMessages,
} from '~models/contract-manage';
import SelectParty from './components/SelectParty.vue';
import SelectHouse from './components/SelectHouse.vue';
import SelectBillScheme from './components/SelectBillScheme.vue';
import DownFiles from './components/downFiles.vue';
import ContractVerify from './components/ContractVerify.vue';
import { contractForm } from './composition/useForm';
import {
  getContractColumns, mathAdd, mathSubtract, mathMultiply, mathDivide,
} from './config';
import { getFieldPrefix, contractConfig } from '../contract-list/config';
import {
  PartyStr, PartyConfig, HouseConfig, BillSchemeConfig,
} from './type';

export default defineComponent({
  components: {
    DownFiles,
    SelectParty,
    SelectHouse,
    SelectBillScheme,
    ContractVerify,
  },
  props: {
    id: {
      type: String,
      default: '',
    },
    state: {
      type: String,
      default: '',
    },
    handleType: {
      type: String,
      default: '',
    },
    contractType: {
      type: String,
      default: '',
    },
    changeType: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    // changeType: 变更类型 主体变更-1 租期调整-2 资产变更-3 计费变更-4
    const { contractType, changeType, handleType } = toRefs(props);
    const {
      modelRef, rulesRef, firstParty, secondParty,
    } = contractForm(contractType.value);
    const fieldPrefix = getFieldPrefix(Number(contractType.value) as ContractType);
    const contractTypeName = contractConfig[Number(contractType.value) as ContractType]?.name;
    const noIds = ref<string[]>([]);
    const houseIdsOpts = ref<string[]>([]);
    const houseList = ref<HouseMessages[]>([]);
    const {
      options, getCalendarContainer, disabledDate, groupBy, disabledstartDate, disabledendDate,
    } = independentModules(modelRef, fieldPrefix, props.contractType);
    const router = useRouter();
    const ContractVoReq = new ContractVo();
    const CollectFeeReq = new CollectFee();
    const partyFlag = ref<boolean>(false);
    const houseFlag = ref<boolean>(false);
    const billSchemeFlag = ref<boolean>(false);
    const currentIndex = ref<number>(0);
    const downFilesFlag = ref<boolean>(false);
    const downFilesConfig = ref<any>({});
    const billingCycleOpts = ref<SelectOptions>([]);
    const activeKey = ref(['1']);
    const partyConfig = ref<PartyConfig>({
      partyStr: 'partyA',
      title: '',
      parkOrgId: '',
    });
    const monthOpts = ref<Number>(12);
    const attributesName = ref<string>('');
    const houseConfig = ref<HouseConfig>({});
    const billSchemeConfig = ref<BillSchemeConfig>({});
    const billSchemeItem = ref<any>({});
    const defaultEmployeeOpts = ref<SelectOptions>([]);
    const freightBasisOpts = ref<any>([]);
    const parkOpts = ref<SelectOptions>([]);
    const columns = getContractColumns(options);
    const formDisabled = handleType.value === 'detail' || handleType.value === 'verify';
    const getContractDetail = async () => {
      if (!props.id) return;
      try {
        const data = await ContractVoReq.contractFindByIdApi(props.id);
        const copy = modelRef as any;
        Object.entries(modelRef).forEach(([key]) => {
          if (key === 'contractBillingPlanReqDTOS' && !(contractType.value === '1' || contractType.value === '2')) {
            copy[key] = data.contractBillingPlanDTOS?.map((item: ContractBillingPlanReqDTOS) => ({
              ...item,
              billingStartAndEndDate: [item.billingStartDate, item.billingEndDate],
            })) || [];
          } else {
            copy[key] = data[key];
          }
        });
        if (!modelRef.extension) {
          modelRef.extension = {};
        }
        if (modelRef.extension) {
          if (modelRef.extension.renovationStartDate) {
            decorationDate.value = [modelRef.extension.renovationStartDate, modelRef.extension.renovationEndDate];
          }
        }
        if (modelRef.employeeDicts) {
          defaultEmployeeOpts.value = modelRef.employeeDicts;
          console.log(defaultEmployeeOpts.value, 'defaultEmployeeOpts.value');
        }
        // 复制和续约重新生成合同编号
        if (props.handleType === 'copy' || props.handleType === 'extend') {
          getNoByType();
        }

        if (props.handleType === 'copy') {
          houseList.value = [];
          modelRef.contractBillingPlanReqDTOS.forEach((item) => {
            item.houseIds = [];
          });
          attributesName.value = getAttributesName(1 as number);
        } else {
          // 从列表选中的数据回显没 houseId ，接口返回的数据没 id
          houseList.value = data.houseMessages.map((item: HouseMessages) => ({ ...item, id: item.houseId })); // 添加 id
          attributesName.value = getAttributesName(modelRef.attributes as number);
        }
        getUnavailableHouseIds();
        if (props.handleType === 'extend') {
          modelRef.extension.originContractNo = modelRef.no;
          const diffstartTime = dayjs(modelRef.startDate);
          const diffendTime = dayjs(modelRef.endDate);
          const diffTime = diffendTime.diff(diffstartTime, 'day');
          modelRef.startDate = dayjs(modelRef.endDate).add(1, 'day').format('YYYY-MM-DD');
          modelRef.endDate = dayjs(modelRef.startDate).add(diffTime, 'day').format('YYYY-MM-DD');
          modelRef.no = '';
          // modelRef.startDate = '';
          // modelRef.endDate = '';
          // houseList.value = [];
          // modelRef.contractBillingPlanReqDTOS = [];
        }
        // console.log(props, data, modelRef, 'modelRef详情');
      } catch (error) {
        console.log(error);
      }
    };

    // 处理计费起止时间
    const dateChange = (dates: string[], bindHouseItem: ContractBillingPlanReqDTOS) => {
      const [billingStartDate, billingEndDate] = dates;
      bindHouseItem.billingStartDate = billingStartDate;
      bindHouseItem.billingEndDate = billingEndDate;
      console.log(bindHouseItem, 'dateChange');
      getComputedData(bindHouseItem);
    };
    const checkVail = () => {
      formRef.value.validate();
    };
    const changeBillingCycle = (dates: number, bindHouseItem: ContractBillingPlanReqDTOS) => {
      bindHouseItem.dayOfMonth = null;
      bindHouseItem.monthBy = null;
      if (dates === 4) {
        monthOpts.value = 12;
      } else if (dates === 3) {
        monthOpts.value = 3;
      }
    };
    const changeRadio = (dates: number, bindHouseItem: ContractBillingPlanReqDTOS) => {
      bindHouseItem.lateDayAmount = null;
      bindHouseItem.lateRate = null;
    };
    const changeLateCharge = (dates: number, bindHouseItem: ContractBillingPlanReqDTOS) => {
      // bindHouseItem.lateDayAmount = null;
      // bindHouseItem.lateRate = null;
      // bindHouseItem.exemptDays = null;
    };
    const calculatChange = (e: any, bindHouseItem: any) => {
      // e： 1 建筑面积*月单价，2 套内面积*月单价
      console.log(e, bindHouseItem);
    };
    const formulaTypeChange = async (e: any, bindHouseItem: ContractBillingPlanReqDTOS) => {};
    const getComputedData = async (bindHouseItem: ContractBillingPlanReqDTOS) => {
      // 1 固定金额，2 计算类型 公式类型才开始调接口计算总金额
      // console.log(bindHouseItem, '---modref');
      if (!bindHouseItem.feeBillingSchemeName) {
        bindHouseItem.houseIds = [];
        message.warning('请先选择计费方案');
        return {};
      }
      if (
        !bindHouseItem
        || bindHouseItem.houseIds?.length <= 0
        || bindHouseItem.billingStartAndEndDate?.length <= 0
        || bindHouseItem.formulaType !== 2
      ) {
        return {};
      }
      const {
        billingStartDate,
        billingEndDate,
        calculationStandard,
        carryMode,
        reservedDigits,
        unitPriceWithoutTax,
        freeRentDay = 0,
      } = bindHouseItem;
      const { feeType } = bindHouseItem.chargeItemMessage;
      if (props.contractType === '4' || props.contractType === '3') {
        const diffMonth = dayjs(billingEndDate).diff(billingStartDate, 'month');
        if (Number(freeRentDay) > diffMonth) {
          message.warning('免租期不能大于租赁期！');
          bindHouseItem.freeRentDay = null;
        }
      } else {
        const diffMonth = dayjs(billingEndDate).diff(billingStartDate, 'day');
        if (Number(freeRentDay) > diffMonth) {
          message.warning('免租期天数不能大于租赁天数！');
          bindHouseItem.freeRentDay = null;
        }
        const [startDate, endDate] = contractDate.value;
        // 计算各个计费方案免租期之和
        const price = modelRef.contractBillingPlanReqDTOS.reduce((sum, e) => sum + Number(e.freeRentDay || 0), 0);
        modelRef.extension.leaseTermDay = Number(dayjs(endDate).diff(startDate, 'day') + 1) - price;
      }

      // 租赁合同：计算标准 1:建筑面积*月单价 2:套内面积*月单价 3:用量*单价 4:天单价
      const buildingAreas = houseList.value
        .filter((item) => bindHouseItem.houseIds.indexOf(item.houseId) !== -1)
        .map((item) => item.buildArea || 0);
      const coverAreas = houseList.value
        .filter((item) => bindHouseItem.houseIds.indexOf(item.houseId) !== -1)
        .map((item) => item.coverArea || 0);
      if (calculationStandard === 1 && buildingAreas.length <= 0) return {};
      if (calculationStandard === 2 && coverAreas.length <= 0) return {};
      console.log(houseList, '计算');
      const data = await CollectFeeReq.calculationApi({
        billingStartDate,
        billingEndDate,
        calculationStandard,
        carryMode,
        reservedDigits,
        unitPrice: unitPriceWithoutTax,
        buildingAreas,
        numberOfRooms: bindHouseItem.houseIds.length,
        coverAreas,
        freeRentDay,
        feeType,
      });
      if (data < 0) {
        message.warning('经计算总金额会为负数，请重新输入免租期');
        return {};
      }
      bindHouseItem.amountWithoutTax = data;
      bindHouseItem.amountWithTax = Number(mathAdd(data, mathDivide(mathMultiply(bindHouseItem.taxRate, data), 100)).toFixed(2));
      return {
        amountWithoutTax: data,
        amountWithTax: bindHouseItem.amountWithTax,
      };
    };
    const showModal = (type: PartyStr) => {
      if (!modelRef.parkOrgId) {
        message.warning('请选择所属园区');
        return;
      }
      partyConfig.value = {
        title: type === 'partyA' ? '选择甲方' : '选择乙方',
        partyStr: type,
        parkOrgId: modelRef.parkOrgId,
      };
      partyFlag.value = true;
    };
    const getPartyData = (data: CustomerManagementInfo | CompanyInfo) => {
      console.log(partyConfig.value.partyStr, data);
      const str = partyConfig.value.partyStr === 'partyA' ? 'A' : 'B';
      const companyStr = partyConfig.value.partyStr === 'partyA' ? 'companyIdA' : 'customerIdB';
      Object.assign(modelRef, {
        [`contact${str}`]: data.contacts,
        [`contactTel${str}`]: data.tel,
        [companyStr]: data.id,
        [`creditCode${str}`]: data.code,
        [`address${str}`]: data.address,
        [`name${str}`]: data.name,
      });
      formRef.value.validate();
    };
    // 获取已被关联的房屋
    const getUnavailableHouseIds = async () => {
      noIds.value = await ContractVoReq.contractUnavailableHouseApi({
        startDate: modelRef.startDate,
        endDate: modelRef.endDate,
        type: Number(contractType.value),
        parkOrgId: modelRef.parkOrgId,
      });
      console.log(noIds, 'houseIdsData');
    };
    const showHouseModal = async () => {
      if (!modelRef.parkOrgId) {
        message.warning('请选择所属园区');
        return;
      }
      if (contractType.value !== '2' && !modelRef.startDate) {
        message.warning(`请选择${fieldPrefix}时间`);
        return;
      }
      houseConfig.value = {
        title: '关联房屋',
        parkId: modelRef.parkOrgId,
        unavailableHouseIds: noIds.value,
        houseStatusSet: [1, 3],
        checkedKeys: houseList.value.map((item) => item.houseId),
      };
      console.log(houseConfig.value);
      houseFlag.value = true;
    };

    const houseDel = async (record: HouseMessages) => {
      houseList.value = houseList.value.filter((item) => item.houseId !== record.houseId);
      const ids = houseList.value.map((item) => item.houseId);
      noIds.value = noIds.value.filter((item) => item !== record.houseId);
      if (contractType.value === '1' || contractType.value === '2') return;
      const copy = modelRef.contractBillingPlanReqDTOS;
      modelRef.contractBillingPlanReqDTOS = copy.map((item) => ({
        ...item,
        houseIds: item.houseIds.filter((id) => ids.indexOf(id) !== -1),
      }));
      // 删除房子重新计算总金额
      modelRef.contractBillingPlanReqDTOS.forEach(async (item) => {
        getComputedData(item);
      });
      console.log(ids, 'houseDel');
    };

    const setHouseDisabled = (contractBillingPlanItem: ContractBillingPlanReqDTOS, index:number) => {
      const { chargeItemMessage: { id } } = contractBillingPlanItem;
      let allHouseIds:string[] = [];
      modelRef.contractBillingPlanReqDTOS?.forEach(({ houseIds, chargeItemMessage }, inx) => {
        if (chargeItemMessage.id === id && index !== inx) {
          allHouseIds = [...allHouseIds, ...houseIds];
        }
      });

      const arr = houseList.value.map((item) => ({
        label: `${item.buildingName}-${item.houseName || item.name}`,
        value: item.houseId,
        disabled: allHouseIds.includes(item.houseId),
        // 一个收费项只能出现一次相同的房屋，否则房屋不能选
        // disabled: currentHouseIds.indexOf(item.houseId) !== -1,
      }));
      return arr;
    };
    const showBillSchemeModal = (data: ContractBillingPlanReqDTOS, index:number) => {
      currentIndex.value = index;
      if (!modelRef.parkOrgId) {
        message.warning('请选择所属园区');
        return;
      }
      if (!houseList.value.length) {
        message.warning('请选择关联房屋');
        return;
      }
      billSchemeItem.value = data;
      billSchemeConfig.value = {
        title: '选择计费方案',
        parkOrgId: modelRef.parkOrgId,
        type: contractType.value,
      };
      billSchemeFlag.value = true;
    };
    const parkOrgIdChange = () => {
      houseList.value = [];
      modelRef.contractBillingPlanReqDTOS = [];
      if (contractType.value === '5' && contractDate.value.length > 0) {
        const [startDate, endDate] = contractDate.value;
        modelRef.extension.leaseTermDay = dayjs(endDate).diff(startDate, 'day') + 1;
      }
      modelRef.employeeId = '';
      defaultEmployeeOpts.value = [];
      Object.assign(modelRef, firstParty(), secondParty());
      getUnavailableHouseIds();
      // add();
    };
    const getBillSchemeData = (billData: any) => {
      console.log(billData, 'getBillSchemeData', `${billData.chargeItemDTO?.taxRate}税率`);
      const taxRate = billData.chargeItemDTO?.taxRate;
      const feeNumer = Number(
        mathAdd(billData.amountWithoutTax, mathDivide(mathMultiply(taxRate, billData.amountWithoutTax), 100)).toFixed(2),
      );
      const unitFeeNumer = Number(
        mathAdd(billData.unitPriceWithoutTax, mathDivide(mathMultiply(taxRate, billData.unitPriceWithoutTax), 100)).toFixed(2),
      );
      const copy = billSchemeItem.value;
      copy.chargeItemMessage = billData.chargeItemDTO;
      copy.feeBillingSchemeName = billData.schemeName;
      copy.feeBillingSchemeId = billData.id;
      copy.billingCycle = billData.billingCycle;
      copy.formulaType = billData.formulaType;
      copy.chargeType = billData.chargeType;
      copy.chargingCycle = billData.chargingCycle;
      copy.dayOfMonth = billData.dayOfMonth;
      copy.daysForDelay = billData.daysForDelay;
      copy.exemptDays = billData.exemptDays;
      copy.lateRate = billData.lateRate;
      copy.lateType = billData.lateType;
      copy.lateCharge = billData.lateCharge;
      copy.lateDayAmount = billData.lateDayAmount;
      copy.monthBy = billData.monthBy;
      copy.amountWithoutTax = billData.amountWithoutTax; // 总金额不含税
      copy.amountWithTax = feeNumer; // 总金额含税
      copy.fixedAmountWithoutTax = billData.amountWithoutTax; // 固定不含税
      copy.fixedAmountWithTax = feeNumer; // 固定含税
      copy.unitPriceWithTax = unitFeeNumer;
      copy.unitPriceWithoutTax = billData.unitPriceWithoutTax;
      copy.taxRate = taxRate;
      copy.chargeItemName = billData.chargeItemDTO?.chargeName;
      copy.calculationStandard = billData.calculationStandard; // 计算标准 固定 1
      // copy.reservedDigits = billData.reservedDigits; // 保留位数 固定 2
      // copy.carryMode = billData.carryMode; // 进位方式 固定 4
      getComputedData(copy);
      formRef.value.validate();
      getCalculationListByFeeType(billData.chargeItemDTO.id, currentIndex.value);
    };
    const del = (event: MouseEvent, index: number) => {
      event.stopPropagation();
      modelRef.contractBillingPlanReqDTOS.splice(index, 1);
    };
    // 处理合同起止时间
    const contractDate = computed({
      set(value: string[]) {
        const [startDate, endDate] = value;
        if (!startDate || !endDate) return;
        modelRef.endDate = endDate;
        modelRef.startDate = startDate;
        getUnavailableHouseIds();
      },
      get() {
        const { startDate, endDate } = modelRef;
        return startDate && endDate ? [startDate, endDate] : [];
      },
    });
    watch(contractDate, (value) => {
      const [startDate, endDate] = value;
      if (value.length > 0) {
        modelRef.extension.leaseTermDay = dayjs(endDate).diff(startDate, 'day') + 1;
      }
      modelRef.extension.leaseTermYear = Number(mathDivide(dayjs(endDate).diff(startDate, 'day'), 365).toFixed(2));
    });
    const feeFunc = (arr: []) => arr?.reduce((cur: any, pre: any) => mathAdd(cur, pre.amountWithTax), 0) || 0;
    watch(
      () => modelRef.contractBillingPlanReqDTOS,
      (value) => {
        console.log(value, 'modelRef.contractBillingPlanReqDTOS');
        if (!value) return;
        if (value.length > 0) {
          if (props.handleType !== 'add') {
            value.forEach((item, index) => {
              if (item.chargeItemMessage.id) {
                getCalculationListByFeeType(item.chargeItemMessage.id, index);
              }
            });
          }
          if (props.handleType === 'edit') {
            value.forEach((item) => {
              if (item.billingCycle === 3) {
                monthOpts.value = 3;
              } else if (item.billingCycle === 4) {
                monthOpts.value = 12;
              }
            });
          }
          if (props.contractType === '4' || props.contractType === '3') {
            value.forEach((item) => {
              if (item.chargeItemMessage.feeType && (item.chargeItemMessage.feeType === 1 || item.chargeItemMessage.feeType === 4)) {
                item.billingCycle = 4;
              } else {
                item.billingCycle = 1;
              }
            });
          }
        }

        const feeArr = value.map((item) => ({ ...item, feeType: item.chargeItemMessage.feeType }));
        //  1: 房屋租金 7：履约保证金  4：物业费
        // 17 18 19 公寓租金 20 21 22 公寓押金
        const chargeInfo = groupBy(feeArr, 'feeType');
        switch (contractType.value) {
          case '3':
            modelRef.extension.rentAmount = feeFunc(chargeInfo[1]);
            modelRef.extension.performanceBond = feeFunc(chargeInfo[7]);
            break;
          case '4':
            modelRef.extension.propertyCosts = feeFunc(chargeInfo[4]);
            break;
          case '5':
            modelRef.extension.rentAmount = [feeFunc(chargeInfo[17]), feeFunc(chargeInfo[18]), feeFunc(chargeInfo[19])].reduce(
              (cur, pre) => mathAdd(cur, pre),
              0,
            );
            modelRef.extension.deposit = [feeFunc(chargeInfo[20]), feeFunc(chargeInfo[21]), feeFunc(chargeInfo[22])].reduce(
              (cur, pre) => mathAdd(cur, pre),
              0,
            );
            break;
          default:
            break;
        }
        console.log(chargeInfo, modelRef, '押金计算');
      },
      { immediate: true, deep: true },
    );
    // 处理装修起止时间
    const decorationDate = ref<any>();
    const handleMonthChange = (val: string[]) => {
      decorationDate.value = val;
    };
    watch(
      decorationDate,
      (value) => {
        if (!value) return;
        if (value.length > 0) {
          const [renovationStartDate, renovationEndDate] = value;
          if (!renovationStartDate || !renovationEndDate) return;
          modelRef.extension.renovationEndDate = dayjs(renovationEndDate).format('YYYY-MM');
          modelRef.extension.renovationStartDate = dayjs(renovationStartDate).format('YYYY-MM');
        } else {
          modelRef.extension.renovationEndDate = '';
          modelRef.extension.renovationStartDate = '';
        }
      },
      { immediate: true },
    );
    const goBack = () => {
      router.push({ name: contractConfig[Number(contractType.value) as ContractType].path });
    };
    const formRef = ref();
    const verifyRef = ref(); // 记得 return 出去
    const save = async () => {
      console.log(formRef.value, 'formRef.value');
      if (props.handleType === 'verify') {
        verifyRef.value.confirm();
        return;
      }
      console.log(modelRef, '提交参数');
      formRef.value
        .validate()
        .then(async () => {
          if (['3', '4', '5'].includes(contractType.value)) {
            if (modelRef.contractBillingPlanReqDTOS && modelRef.contractBillingPlanReqDTOS.length > 0) {
              const allHousesArr: string[] = [];
              modelRef.contractBillingPlanReqDTOS.forEach((item) => {
                allHousesArr.push(...item.houseIds);
              });
              const newArr = Array.from(new Set(allHousesArr));
              const c = newArr.length === houseIdsOpts.value.length && newArr.sort().toString() === houseIdsOpts.value.sort().toString();
              if (!c) {
                Modal.confirm({
                  content: '有房源未关联计费方案，若不关联，将不会产生相应费用',
                  onOk: async () => {
                    saveAllMethod();
                  },
                });
              } else {
                saveAllMethod();
              }
            } else {
              saveAllMethod();
            }
          } else {
            saveAllMethod();
          }
        })
        .catch(() => {
          // message.warning('请将必选项填写完整');
        });
    };
    const saveAllMethod = async () => {
      try {
        const apiReq = {
          add: ContractVoReq.contractCreateApi,
          copy: ContractVoReq.contractCreateApi,
          edit: ContractVoReq.contractModifyApi,
          extend: ContractVoReq.contractExtendApi,
          change: ContractVoReq.contractChangeApi,
        };
        const handleType = props.handleType as 'add' | 'edit' | 'copy' | 'extend' | 'change';
        if (contractType.value === '1' || contractType.value === '2') {
          modelRef.contractBillingPlanReqDTOS = [];
        }
        modelRef.attributes = props.handleType === 'copy' ? 1 : modelRef.attributes;
        await apiReq[handleType]({
          ...modelRef,
          parkOrgName: parkOpts.value.find((item) => item.value === modelRef.parkOrgId)?.label || '',
          state: props.handleType === 'copy' || props.handleType === 'add' ? 1 : modelRef.state,
          employeeId: modelRef.employeeId.toString(),
          houseMessages: houseList.value.map((item) => ({
            buildArea: item.buildArea || null,
            coverArea: item.buildArea || null,
            buildingName: item.buildingName,
            floor: item.floor,
            houseFunction: item.houseFunction,
            houseId: item.houseId,
            houseName: item.name || item.houseName,
            id: item.houseId,
            totalFloor: item.totalFloor || null,
          })),
          type: Number(contractType.value), // 合同类型
        });
        message.success('操作成功');
        goBack();
      } catch (error) {
        console.log(error);
      }
    };
    const amountChange = (
      e: any,
      bindHouseItem: ContractBillingPlanReqDTOS,
      key: 'fixedAmountWithTax' | 'amountWithTax' | 'unitPriceWithTax',
    ) => {
      console.log(bindHouseItem.taxRate, e.target.value);
      if (!e.target.value) return;
      bindHouseItem[key] = Number(mathAdd(e.target.value, mathDivide(mathMultiply(bindHouseItem.taxRate, e.target.value), 100)).toFixed(2));
      if (key === 'unitPriceWithTax') {
        getComputedData(bindHouseItem);
      }
    };
    const getNoByType = async () => {
      const data = await ContractVoReq.contractGetNoByTypeApi(contractType.value);
      modelRef.no = data;
    };
    // 处理有效期-起始日期
    const changeContractDate = async (value: any) => {
      if (props.contractType === '4') {
        const a = dayjs(value).add(1, 'year').format('YYYY-MM-DD');
        const endDate = dayjs(a).subtract(1, 'day').format('YYYY-MM-DD');
        contractDate.value = [value, endDate];
      }
      // 公寓合同
      if (props.contractType === '5') {
        modelRef.endDate = '';
        getUnavailableHouseIds();
      }
      if (modelRef.contractBillingPlanReqDTOS && modelRef.contractBillingPlanReqDTOS.length > 0) {
        modelRef.contractBillingPlanReqDTOS.forEach((item) => {
          console.log(contractDate.value);
          item.billingStartAndEndDate = contractDate.value;
          item.billingStartDate = contractDate.value[0];
          item.billingEndDate = contractDate.value[1];
        });
      }
    };
    // 处理有效期-终止日期
    const changeContractEndDate = async (value: any) => {
      if (props.contractType === '4') {
        const { startDate } = modelRef;
        const startYear = Number(dayjs(startDate).format('YYYY'));
        const endYear = Number(dayjs(value).format('YYYY'));
        const a = dayjs(startDate).add(endYear - startYear, 'year').format('YYYY-MM-DD');
        const b = dayjs(a).subtract(1, 'day').format('YYYY-MM-DD');
        if (value !== b) {
          contractDate.value = [startDate, b];
        }
      }
      if (props.contractType === '5') {
        getUnavailableHouseIds();
      }
      if (modelRef.contractBillingPlanReqDTOS && modelRef.contractBillingPlanReqDTOS.length > 0) {
        modelRef.contractBillingPlanReqDTOS.forEach((item) => {
          item.billingStartAndEndDate = contractDate.value;
          item.billingStartDate = contractDate.value[0];
          item.billingEndDate = contractDate.value[1];
        });
      }
    };
    const downDraft = () => {
      downFilesFlag.value = true;
      downFilesConfig.value = {
        title: contractType.value === '1' ? '下载协议文件' : '下载合同文件',
        id: props.id,
        type: contractType.value,
        parkOrgId: modelRef.parkOrgId,
      };
    };
    const goBackPage = () => {
      window.$localStorage.detail = true;
      goBack();
    };
    const panelChangeDate = (isShow:Boolean) => {
      if (isShow) {
        setTimeout(() => {
          const dateDom = document.querySelector('.ant-picker') as HTMLElement;
          console.log(dateDom, 'dateDom');
          dateDom.addEventListener('DOMCharacterDataModified', () => {
            const year = document.querySelector('.ant-calendar-year-select') as HTMLElement;
            // const a=year.innerText.replace('年', '');
            console.log(year, 'a');
          });
        }, 0);
      }
    };
    const getAttributesName = (attributes: number) => {
      if (contractType.value === '1') {
        return options?.PROTOCOL_ATTRIBUTES[attributes].label || '';
      }
      return options?.CONTRACT_ATTRIBUTES[attributes].label || '';
    };
    const add = () => {
      modelRef.contractBillingPlanReqDTOS.push({
        billingStartAndEndDate: [modelRef.startDate, modelRef.endDate],
        taxRate: 0, // 税率
        amountWithTax: null, // 金额元(含税)
        amountWithoutTax: null, // 金额元(不含税)
        billingCycle: null, // 计费周期
        billingEndDate: modelRef.endDate, // 计费结束日期
        billingStartDate: modelRef.startDate, // 计费开始日期
        calculationStandard: null, // 计算标准
        carryMode: 4, // 进位方式
        chargeType: null, // 收费类型
        chargeItemName: null, // 收费类型名称
        chargingCycle: null, // 收费周期
        cumulativeCycle: null, // 累计x个计费周期
        dayOfMonth: null, // 账单生成日期
        daysForDelay: null, // 最晚缴纳日期
        exemptDays: null, // 豁免天数
        lateType: null, // 滞纳率计算规则
        lateCharge: 1, // 是否收取滞纳金
        lateDayAmount: null, // 每日滞纳金
        monthBy: null, // 账单生成日期-按月
        feeBillingSchemeId: '', // 收费方案Id
        feeBillingSchemeName: '', // 收费方案Id
        formulaType: null, // 公式类型
        houseIds: [], // 应用房源ids
        lateRate: null, // 滞纳率
        reservedDigits: 2, // 保留位数
        unitPriceWithTax: null, // 单价元(含税)
        unitPriceWithoutTax: null, // 单价元(不含税)
        fixedAmountWithTax: null, // 固定金额元（含税）
        fixedAmountWithoutTax: null, // 固定金额元（不含税）
        houseIdsNames: '',
        freeRentDay: null,
        chargeItemMessage: {},
      });
      if (modelRef.contractBillingPlanReqDTOS.length > 0 && props.handleType === 'add' && houseList.value && houseList.value.length > 0) {
        if (modelRef.contractBillingPlanReqDTOS[0].houseIds.length === 0) {
          modelRef.contractBillingPlanReqDTOS[0].houseIds = houseIdsOpts.value;
        }
      }
    };
    const init = async () => {
      if (props.handleType === 'add') {
        getNoByType();
        modelRef.attributes = 1;
        const currentMonth = dayjs(new Date()).format('YYYY-MM-DD');
        decorationDate.value = [currentMonth, currentMonth];
        attributesName.value = getAttributesName(modelRef.attributes);
        if (props.contractType === '4' || props.contractType === '3') {
          const a = dayjs().add(1, 'year').format('YYYY-MM-DD');
          const endDate = dayjs(a).subtract(1, 'day').format('YYYY-MM-DD');
          contractDate.value = [currentMonth, endDate];
        }
      }
      modelRef.contractedDate = dayjs(new Date()).format('YYYY-MM-DD'); // 到时候根据合同状态赋值
      console.log(modelRef.contractedDate, 'modelRef.contractedDate');
      getContractDetail();
      parkOpts.value = await new Park().searchParkListName();
    };
    watch(
      houseList,
      (value) => {
        modelRef.totalArea = value.reduce((cur, pre) => mathAdd(cur, pre.buildArea), 0);
        // houseFunction: 2,3,4 单间,标间,套间
        modelRef.extension.numberOfRooms = value.filter((item) => [2, 3, 4].indexOf(Number(item.houseFunction)) !== -1).length;
        const roomInfo = groupBy(value, 'houseFunction');
        modelRef.extension.numberOfSingleRooms = roomInfo[2]?.length || 0;
        modelRef.extension.numberOfTwinRooms = roomInfo[3]?.length || 0;
        modelRef.extension.numberOfSuite = roomInfo[4]?.length || 0;
        if (value.length > 0) {
          const tempAry = value.map((item: { id: any; }) => item.id);
          houseIdsOpts.value = tempAry;
        }
        // console.log(houseIdsOpts.value, 'houseIdsOpts.value');
        // console.log(modelRef.contractBillingPlanReqDTOS, 'modelRef.contractBillingPlanReqDTOS');
      },
      { immediate: true },
    );
    const getHouseData = (data: HouseMessages[]) => {
      houseList.value = houseList.value.concat(data).map((item) => ({ ...item, houseId: item.id })); // 添加houseId
      const tempAry = houseList.value.map((item: { id: any; }) => item.id);
      noIds.value = [...new Set(noIds.value.concat(houseList.value.map((item) => item.id)))];
      if (modelRef.contractBillingPlanReqDTOS && modelRef.contractBillingPlanReqDTOS.length > 0 && props.handleType === 'add') {
        modelRef.contractBillingPlanReqDTOS[0].houseIds = tempAry;
      }
    };
    const getCalculationListByFeeType = async (id:string, index:number) => {
      const data = await CollectFeeReq.getCalculationListByFeeType(id);
      // const data = await ContractVoReq.calculationListByFeeTypeApi(contractType.value);
      freightBasisOpts.value[index] = data;
      // console.log(freightBasisOpts.value, 'freightBasisOpts.value');
    };
    onMounted(() => {
      init();
    });
    // console.log(modelRef, rulesRef);
    return {
      goBackPage,
      currentIndex,
      downFilesFlag,
      downFilesConfig,
      downDraft,
      changeContractEndDate,
      panelChangeDate,
      disabledstartDate,
      disabledendDate,
      houseIdsOpts,
      billingCycleOpts,
      changeLateCharge,
      changeContractDate,
      checkVail,
      changeRadio,
      changeBillingCycle,
      monthOpts,
      goBack,
      save,
      add,
      del,
      dateChange,
      calculatChange,
      formulaTypeChange,
      modelRef,
      rulesRef,
      formRef,
      verifyRef,
      columns,
      options,
      parkOpts,
      houseList,
      contractDate,
      activeKey,
      partyFlag,
      showModal,
      getPartyData,
      partyConfig,
      showHouseModal,
      getHouseData,
      houseConfig,
      houseFlag,
      houseDel,
      getBillSchemeData,
      billSchemeFlag,
      billSchemeConfig,
      showBillSchemeModal,
      setHouseDisabled,
      parkOrgIdChange,
      amountChange,
      getComputedData,
      formDisabled,
      defaultEmployeeOpts,
      fieldPrefix,
      contractTypeName,
      decorationDate,
      handleMonthChange,
      attributesName,
      getCalendarContainer,
      disabledDate,
      freightBasisOpts,
    };
  },
});
function independentModules(modelRef: Contract, fieldPrefix:string, contractType:string) {
  const options = useHashMapDics([
    'FEE_BILLING_CYCLE',
    'FEE_RESERVED_DIGITS',
    'FEE_CARRY_MODE',
    'FEE_FORMULA_TYPE',
    'FEE_CALCULATION_STANDARD',
    'FEE_CHARGING_CYCLE',
    'CONTRACT_ATTRIBUTES',
    'CONTRACT_CHANGE_TYPE',
    'CONTRACT_STATE',
    'FEE_CHARGE_TYPE',
    'CONTRACT_TYPE',
    'PROTOCOL_ATTRIBUTES',
    'HOUSE_FUNCTION',
  ]);
  const getCalendarContainer = (triggerNode: HTMLElement) => document.body;
  const disabledDate = (time: any) => {
    const { startDate, endDate } = modelRef;
    if (!time) return false;
    const times = dayjs(time).format('YYYY-MM-DD');
    return times < startDate || times > endDate;
  };
  const disabledstartDate = (time: any) => {
    // if (!time) return false;
    // const times = dayjs(time).format('YYYY-MM-DD');
    // const startDate = dayjs(new Date()).format('YYYY-MM-DD');
    // return times < startDate;
  };
  const disabledendDate = (time: any) => {
    const { startDate } = modelRef;
    if (!time) return false;
    const times = dayjs(time).format('YYYY-MM-DD');
    if (contractType === '4') {
      if (!time) return false;
      const times = dayjs(time).format('YYYY-MM-DD');
      const a = dayjs(startDate).add(1, 'year').format('YYYY-MM-DD');
      const b = dayjs(a).subtract(1, 'day').format('YYYY-MM-DD');
      return times < b;
    }
    return times <= startDate;
  };
  const groupBy = (objectArray: any[], property: string) => objectArray.reduce((acc, obj) => {
    const key = obj[property];
    if (!acc[key]) {
      acc[key] = [];
    }
    acc[key].push(obj);
    return acc;
  }, {});
  return {
    disabledendDate,
    disabledstartDate,
    options,
    getCalendarContainer,
    disabledDate,
    groupBy,
  };
}

function typeOf(contractType: string): any {
  throw new Error('Function not implemented.');
}
</script>
<style scoped lang="scss">
.down-draft {
    margin-bottom: 10px;
    margin-left: 10px;
}

:deep(.cost-flex .ant-form-item-control-input-content) {
    display: flex;
    align-items: center;
}

:deep(.date-style .ant-form-item-control-input-content) {
    display: flex;
}

.line-style {
    margin: 0 5px;
}

.ml-10 {
    margin-right: 10px;
}

.m-10 {
    margin: 0 10px;
}

.draft-css {
    padding: 50px 0;

    .draft-foot {
        text-align: center;

        .btn-lf {
            margin-right: 20px;
        }
    }
}

.cursor {
    cursor: pointer;
}

.contract-draft {
    padding: 0 20px;

    .draft-title {
        margin-bottom: 20px;
        font-size: 16px;
        font-weight: bold;
    }

    .draft-house {
        margin-bottom: 20px;

        .house-bar {
            margin-bottom: 20px;

            > span {
                &::before {
                    display: inline-block;
                    margin-right: 4px;
                    font-family: SimSun, sans-serif;
                    font-size: 14px;
                    line-height: 1;
                    color: #ff4d4f;
                    content: '*';
                }

                margin-right: 20px;
            }
        }
    }

    .add-btn {
        text-align: right;
    }

    .draft-form {
        position: relative;
    }

    .house-census {
        display: flex;
        margin-bottom: 10px;

        p {
            flex: 1;
        }
    }
}
</style>
