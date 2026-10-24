<template>
  <div class="face-checkin-page">
    <a-card
      :title="pageTitle"
      :class="{ 'face-checkin-single': !!currentMode }"
    >
      <a-tabs
        :active-key="activeTab"
        :class="{ 'single-mode-tabs': !!currentMode }"
      >
        <a-tab-pane
          v-if="!currentMode || currentMode === 'attendance'"
          key="attendance"
          tab="签到记录"
        >
          <div class="toolbar">
            <a-select
              v-model:value="meetingReserveId"
              placeholder="选择会议"
              style="width: 300px; margin-right: 12px;"
              :options="meetingOptions"
            />
            <a-button
              type="primary"
              @click="loadAttendance"
            >
              查询
            </a-button>
            <a-button
              style="margin-left: 12px;"
              :disabled="!meetingReserveId"
              @click="exportCsv"
            >
              导出CSV
            </a-button>
          </div>
          <a-table
            row-key="id"
            :columns="attendanceColumns"
            :data-source="attendanceList"
            :loading="attendanceLoading"
            :pagination="false"
          >
            <template #participantCategory="{ record }">
              <a-tag :color="participantCategoryColor(record.participantCategory)">
                {{ participantCategoryText(record.participantCategory) }}
              </a-tag>
            </template>
            <template #status="{ record }">
              <a-tag :color="statusColor(record.status)">
                {{ statusText(record.status) }}
              </a-tag>
              <a-tag
                v-if="record.remark"
                color="orange"
              >
                {{ record.remark }}
              </a-tag>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane
          v-if="!currentMode || currentMode === 'requests'"
          key="requests"
          tab="上传记录"
        >
          <a-table
            row-key="id"
            :columns="requestColumns"
            :data-source="requestList"
            :loading="requestLoading"
            :pagination="false"
          >
            <template #requestStatus="{ record }">
              <a-tag :color="record.status === 1 ? 'green' : 'orange'">
                {{ record.status === 1 ? '已接收' : '未处理' }}
              </a-tag>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane
          v-if="!currentMode || currentMode === 'persons'"
          key="persons"
          tab="人员管理"
        >
          <div class="toolbar">
            <a-input-search
              v-model:value="personKeyword"
              placeholder="搜索姓名"
              style="width: 300px; margin-right: 12px;"
              @search="loadPersons"
            />
            <a-button
              type="primary"
              @click="showPersonModal"
            >
              新增人员
            </a-button>
          </div>
          <a-table
            row-key="id"
            :columns="personColumns"
            :data-source="personList"
            :loading="personLoading"
            :pagination="personPagination"
            @change="personPageChange"
          >
            <template #personType="{ record }">
              {{ record.personType === 1 ? '工作人员' : '访客' }}
            </template>
            <template #status="{ record }">
              <a-tag :color="record.status === 1 ? 'green' : 'red'">
                {{ record.status === 1 ? '启用' : '禁用' }}
              </a-tag>
            </template>
            <template #auditStatus="{ record }">
              <a-tag :color="record.auditStatus === 1 ? 'green' : record.auditStatus === 2 ? 'red' : 'orange'">
                {{ record.auditStatus === 1 ? '审核通过' : record.auditStatus === 2 ? '审核拒绝' : '待审核' }}
              </a-tag>
            </template>
            <template #operate="{ record }">
              <a-button
                v-if="record.auditStatus === 0"
                type="link"
                @click="auditPerson(record.id, 1)"
              >
                通过
              </a-button>
              <a-button
                v-if="record.auditStatus === 0"
                type="link"
                @click="auditPerson(record.id, 2)"
              >
                拒绝
              </a-button>
              <a-button
                type="link"
                @click="editPerson(record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="removePerson(record.id)"
              >
                删除
              </a-button>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane
          v-if="!currentMode || currentMode === 'meetings'"
          key="meetings"
          tab="会议管理"
        >
          <div class="toolbar">
            <a-input-search
              v-model:value="meetingKeyword"
              placeholder="搜索会议主题"
              style="width: 260px; margin-right: 12px;"
              allow-clear
            />
            <a-select
              v-model:value="meetingStatusFilter"
              style="width: 150px; margin-right: 12px;"
              :options="meetingStatusOptions"
            />
            <a-button
              type="primary"
              @click="showMeetingModal"
            >
              新增会议
            </a-button>
            <a-button
              style="margin-left: 12px;"
              @click="loadMeetings"
            >
              刷新
            </a-button>
          </div>
          <a-table
            row-key="id"
            :columns="meetingColumns"
            :data-source="filteredMeetingList"
            :loading="meetingLoading"
            :pagination="false"
          >
            <template #meetingStatus="{ record }">
              <a-tag :color="meetingStatusColor(record)">
                {{ meetingStatusText(record) }}
              </a-tag>
            </template>
            <template #operate="{ record }">
              <a-button
                type="link"
                @click="openDetail(record)"
              >
                详情
              </a-button>
              <a-button
                type="link"
                @click="removeMeeting(record.id)"
              >
                删除
              </a-button>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <a-modal
      v-model:visible="personModalVisible"
      :title="personForm.id ? '编辑人员' : '新增人员'"
      @ok="savePerson"
    >
      <a-form layout="vertical">
        <a-form-item
          label="姓名"
          required
        >
          <a-input v-model:value="personForm.personName" />
        </a-form-item>
        <a-form-item label="工号">
          <a-input v-model:value="personForm.employeeNo" />
        </a-form-item>
        <a-form-item label="手机号">
          <a-input v-model:value="personForm.phone" />
        </a-form-item>
        <a-form-item
          label="人员类型"
          required
        >
          <a-select
            v-model:value="personForm.personType"
            :options="[{ value: 1, label: '工作人员' }, { value: 2, label: '访客' }]"
          />
        </a-form-item>
        <a-form-item label="人脸照片">
          <input
            type="file"
            accept="image/*"
            @change="onPhotoChange"
          >
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
      v-model:visible="meetingModalVisible"
      :title="meetingForm.id ? '编辑会议' : '新增会议'"
      @ok="saveMeeting"
    >
      <a-form layout="vertical">
        <a-form-item
          label="会议主题"
          required
        >
          <a-input
            v-model:value="meetingForm.meetingSubject"
            :maxlength="30"
            placeholder="最多30字"
          />
        </a-form-item>
        <a-form-item
          label="开始时间"
          required
        >
          <a-date-picker
            v-model:value="meetingForm.meetingTimeStart"
            :show-time="{ format: 'HH:mm' }"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="持续时间"
          required
        >
          <a-select
            v-model:value="meetingForm.durationMinutes"
            :options="durationOptions"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="考勤开始时间"
          required
        >
          <a-date-picker
            v-model:value="meetingForm.attendanceTimeStart"
            :show-time="{ format: 'HH:mm' }"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="考勤截止时间"
          required
        >
          <a-date-picker
            v-model:value="meetingForm.attendanceTimeEnd"
            :show-time="{ format: 'HH:mm' }"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="与会人员"
          required
        >
          <a-select
            v-model:value="meetingForm.personIds"
            mode="multiple"
            :options="personOptions"
            placeholder="选择人员"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea
            v-model:value="meetingForm.remark"
            :maxlength="255"
            placeholder="最多255字"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script lang="ts">
import {
  computed, defineComponent, onMounted, onUnmounted, reactive, ref,
} from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import request from '~utils/axios';
import URLS from '@/models/urlConstant/meeting-manage';

const attendanceColumns = [
  { title: '人员姓名', dataIndex: 'personName', key: 'personName' },
  {
    title: '识别分类', key: 'participantCategory', slots: { customRender: 'participantCategory' }, width: 110,
  },
  { title: '签到时间', dataIndex: 'checkinTime', key: 'checkinTime' },
  { title: '状态', key: 'status', slots: { customRender: 'status' } },
  { title: '备注', dataIndex: 'remark', key: 'remark' },
];
const requestColumns = [
  { title: '请求ID', dataIndex: 'requestId', key: 'requestId' },
  { title: '设备ID', dataIndex: 'deviceId', key: 'deviceId' },
  { title: '会议ID', dataIndex: 'meetingReserveId', key: 'meetingReserveId' },
  { title: '接收时间', dataIndex: 'acceptTime', key: 'acceptTime' },
  { title: '状态', key: 'requestStatus', slots: { customRender: 'requestStatus' } },
  { title: '结果', dataIndex: 'result', key: 'result' },
  { title: '人员ID', dataIndex: 'personId', key: 'personId' },
  { title: '人员姓名', dataIndex: 'personName', key: 'personName' },
];
const personColumns = [
  { title: '姓名', dataIndex: 'personName', key: 'personName' },
  { title: '工号', dataIndex: 'employeeNo', key: 'employeeNo' },
  { title: '手机号', dataIndex: 'phone', key: 'phone' },
  { title: '类型', key: 'personType', slots: { customRender: 'personType' } },
  { title: '状态', key: 'status', slots: { customRender: 'status' } },
  { title: '审核', key: 'auditStatus', slots: { customRender: 'auditStatus' } },
  { title: '操作', key: 'operate', slots: { customRender: 'operate' } },
];
const meetingColumns = [
  { title: '会议主题', dataIndex: 'meetingSubject', key: 'meetingSubject' },
  {
    title: '状态', key: 'meetingStatus', slots: { customRender: 'meetingStatus' }, width: 110,
  },
  { title: '开始时间', dataIndex: 'meetingTimeStart', key: 'meetingTimeStart' },
  { title: '结束时间', dataIndex: 'meetingTimeEnd', key: 'meetingTimeEnd' },
  {
    title: '考勤开始', dataIndex: 'attendanceStartTime', key: 'attendanceStartTime', width: 170,
  },
  {
    title: '考勤截止', dataIndex: 'attendanceEndTime', key: 'attendanceEndTime', width: 170,
  },
  { title: '与会人数', dataIndex: 'meetingPeopleNum', key: 'meetingPeopleNum' },
  { title: '备注', dataIndex: 'remark', key: 'remark' },
  { title: '操作', key: 'operate', slots: { customRender: 'operate' } },
];
const PREFIX = 'park-meeting/face-checkin';

const participantCategoryText = (value: any) => ({ 1: '与会人员', 2: '非与会人员', 3: '非记录人员' }[Number(value)] || '未知');
const participantCategoryColor = (value: any) => ({ 1: 'green', 2: 'orange', 3: 'red' }[Number(value)] || 'default');

export default defineComponent({
  name: 'FaceCheckin',
  props: { mode: { type: String, default: '' } },
  setup(props) {
    const router = useRouter();
    const currentMode = computed(() => props.mode);
    const activeTab = computed(() => currentMode.value || 'attendance');
    const pageTitle = computed(() => ({ attendance: '签到记录', requests: '上传记录', persons: '人员管理' }[currentMode.value] || '人脸识别签到管理'));
    const meetingReserveId = ref('');
    const meetingOptions = ref<Array<{ value: string; label: string }>>([]);
    const attendanceList = ref<Array<Record<string, unknown>>>([]);
    const attendanceLoading = ref(false);
    const requestList = ref<Array<Record<string, unknown>>>([]);
    const requestLoading = ref(false);
    const personList = ref<Array<Record<string, unknown>>>([]);
    const personLoading = ref(false);
    const personKeyword = ref('');
    const personPagination = reactive({ current: 1, pageSize: 10, total: 0 });
    const personModalVisible = ref(false);
    const personForm = reactive({
      id: '', personName: '', employeeNo: '', phone: '', personType: 1, photoBase64: '',
    });
    const meetingList = ref<Array<Record<string, unknown>>>([]);
    const meetingLoading = ref(false);
    const meetingKeyword = ref('');
    const meetingStatusFilter = ref('all');
    const meetingStatusOptions = [
      { value: 'all', label: '全部状态' },
      { value: 'notStarted', label: '未开始' },
      { value: 'inProgress', label: '进行中' },
      { value: 'completed', label: '已完成' },
    ];
    const meetingModalVisible = ref(false);
    const meetingForm = reactive({
      id: '', meetingSubject: '', meetingTimeStart: '', durationMinutes: 60, attendanceTimeStart: '', attendanceTimeEnd: '', personIds: [] as string[], remark: '',
    });
    const durationOptions = [
      { value: 15, label: '15分钟' },
      { value: 30, label: '30分钟' },
      { value: 45, label: '45分钟' },
      { value: 60, label: '1小时' },
      { value: 90, label: '1.5小时' },
      { value: 120, label: '2小时' },
      { value: 180, label: '3小时' },
    ];
    const personOptions = ref<Array<{ value: string; label: string }>>([]);
    const deviceMeetingId = ref('');
    const deviceControlLoading = ref(false);
    const deviceStatus = reactive({ running: false, pid: null as number | null, exitCode: null as number | null });

    const statusText = (status: number) => {
      const statusNames: Record<number, string> = {
        1: '签到成功', 2: '重复签到', 3: '无权限', 4: '未识别', 5: '签到未开始或已结束', 7: '处理失败',
      };
      return statusNames[status] || '未知状态';
    };
    const statusColor = (status: number) => {
      const statusColors: Record<number, string> = {
        1: 'green', 2: 'yellow', 3: 'red', 4: 'gray', 5: 'blue', 7: 'red',
      };
      return statusColors[status] || 'default';
    };
    const loadMeetings = async () => {
      try {
        const { data } = await request({ url: `${PREFIX}/meeting/list`, method: 'GET' });
        const list = data || [];
        meetingOptions.value = list.map((item: any) => ({ value: item.id, label: item.meetingName || item.meetingSubject || item.id }));
        meetingList.value = list;
      } catch {
        meetingOptions.value = [];
        meetingList.value = [];
      }
    };
    const parseMeetingTime = (value: unknown) => {
      const text = String(value || '').replace(' ', 'T');
      const time = new Date(text).getTime();
      return Number.isFinite(time) ? time : 0;
    };
    const meetingStatus = (record: Record<string, unknown>) => {
      const now = Date.now();
      const start = parseMeetingTime(record.meetingTimeStart);
      const end = parseMeetingTime(record.meetingTimeEnd);
      if (start && now < start) return 'notStarted';
      if (end && now > end) return 'completed';
      return 'inProgress';
    };
    const meetingStatusText = (record: Record<string, unknown>) => {
      const names: Record<string, string> = { notStarted: '未开始', inProgress: '进行中', completed: '已完成' };
      return names[meetingStatus(record)] || '未知';
    };
    const meetingStatusColor = (record: Record<string, unknown>) => {
      const colors: Record<string, string> = { notStarted: 'blue', inProgress: 'green', completed: 'gray' };
      return colors[meetingStatus(record)] || 'default';
    };
    const filteredMeetingList = computed(() => {
      const keyword = meetingKeyword.value.trim().toLowerCase();
      return meetingList.value.filter((record) => {
        const subject = String(record.meetingSubject || '').toLowerCase();
        return (!keyword || subject.includes(keyword))
          && (meetingStatusFilter.value === 'all' || meetingStatusFilter.value === meetingStatus(record));
      });
    });
    const loadAttendance = async () => {
      if (!meetingReserveId.value) return;
      attendanceLoading.value = true;
      try {
        // hiddenLoading：轮询请求不打全屏遮罩，避免 10s 一次的 loading 闪烁
        const { data } = await request({
          url: `${PREFIX}/attendance/list`,
          method: 'GET',
          params: { meetingReserveId: meetingReserveId.value },
          hiddenLoading: true,
        });
        attendanceList.value = data || [];
      } catch {
        attendanceList.value = [];
      } finally {
        attendanceLoading.value = false;
      }
    };
    const loadRequests = async () => {
      requestLoading.value = true;
      try {
        const { data } = await request({
          url: `${PREFIX}/request/list`,
          method: 'GET',
          params: { meetingReserveId: meetingReserveId.value || undefined },
          hiddenLoading: true,
        });
        requestList.value = data || [];
      } catch {
        requestList.value = [];
      } finally {
        requestLoading.value = false;
      }
    };
    const loadPersons = async () => {
      personLoading.value = true;
      try {
        const { data } = await request({ url: `${PREFIX}/person/list`, method: 'GET', params: { pageNum: personPagination.current, pageSize: personPagination.pageSize, keyword: personKeyword.value || undefined } });
        const page = data;
        personList.value = page?.list || [];
        personPagination.total = page?.total || 0;
      } catch {
        personList.value = [];
      } finally {
        personLoading.value = false;
      }
    };
    const loadPersonOptions = async () => {
      try {
        const { data } = await request({ url: `${PREFIX}/person/list`, method: 'GET', params: { pageNum: 1, pageSize: 100 } });
        personOptions.value = (data?.list || [])
          .filter((item: any) => item.status === 1 && item.auditStatus === 1)
          .map((item: any) => ({ value: item.id, label: item.personName || item.id }));
      } catch {
        personOptions.value = [];
      }
    };
    const personPageChange = (pagination: any) => {
      personPagination.current = pagination.current;
      personPagination.pageSize = pagination.pageSize;
      loadPersons();
    }; const showPersonModal = () => {
      Object.assign(personForm, {
        id: '', personName: '', employeeNo: '', phone: '', personType: 1, photoBase64: '',
      });
      personModalVisible.value = true;
    };
    const onPhotoChange = (event: Event) => {
      const file = (event.target as HTMLInputElement).files?.[0];
      if (!file) return;
      const reader = new FileReader();
      reader.onload = () => {
        const image = new Image();
        image.onload = () => {
          const maxSize = 720;
          const scale = Math.min(1, maxSize / Math.max(image.width, image.height));
          const canvas = document.createElement('canvas');
          canvas.width = Math.round(image.width * scale);
          canvas.height = Math.round(image.height * scale);
          const context = canvas.getContext('2d');
          if (!context) {
            personForm.photoBase64 = reader.result as string;
            return;
          }
          context.drawImage(image, 0, 0, canvas.width, canvas.height);
          personForm.photoBase64 = canvas.toDataURL('image/jpeg', 0.85);
        };
        image.onerror = () => { personForm.photoBase64 = reader.result as string; };
        image.src = reader.result as string;
      };
      reader.readAsDataURL(file);
    };
    const savePerson = async () => {
      if (!personForm.personName) { message.error('请输入姓名'); return; }
      if (!personForm.id && !personForm.photoBase64) { message.error('请上传人脸照片'); return; }
      try {
        await request({ url: `${PREFIX}/person/save`, method: 'POST', data: personForm });
        message.success('保存成功');
        personModalVisible.value = false;
        loadPersons();
      } catch (error: any) {
        message.error(error?.response?.data?.message || error?.message || '保存失败，请检查人脸照片');
      }
    };
    const removePerson = async (id: string) => {
      try {
        await request({ url: `${PREFIX}/person/remove/${id}`, method: 'DELETE' });
        message.success('删除成功');
        loadPersons();
      } catch {
        message.error('删除失败');
      }
    };
    const editPerson = (record: Record<string, unknown>) => {
      Object.assign(personForm, {
        id: record.id as string,
        personName: record.personName as string || '',
        employeeNo: record.employeeNo as string || '',
        phone: record.phone as string || '',
        personType: record.personType as number || 1,
        photoBase64: '',
      });
      personModalVisible.value = true;
    };
    const exportCsv = async () => {
      if (!meetingReserveId.value) return;
      try {
        const { data } = await request({ url: `${PREFIX}/attendance/export`, method: 'GET', params: { meetingReserveId: meetingReserveId.value } });
        const csv = data || '';
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `签到记录_${meetingReserveId.value}.csv`;
        link.click();
        URL.revokeObjectURL(url);
        message.success('导出成功');
      } catch {
        message.error('导出失败');
      }
    };
    const auditPerson = async (id: string, auditStatus: number) => {
      try {
        await request({ url: `${PREFIX}/person/audit/${id}`, method: 'POST', params: { auditStatus } });
        message.success(auditStatus === 1 ? '审核通过' : '审核拒绝');
        loadPersons();
      } catch {
        message.error('操作失败');
      }
    };
    const openDetail = (record: Record<string, unknown>) => {
      router.push({ name: 'faceCheckinMeetingDetail', params: { id: record.id as string } });
    };
    const showMeetingModal = () => {
      Object.assign(meetingForm, {
        id: '', meetingSubject: '', meetingTimeStart: '', durationMinutes: 60, attendanceTimeStart: '', attendanceTimeEnd: '', personIds: [], remark: '',
      });
      meetingModalVisible.value = true;
    };
    const saveMeeting = async () => {
      if (!meetingForm.meetingSubject) { message.error('请输入会议主题'); return; }
      if (meetingForm.meetingSubject.length > 30) { message.error('会议主题最多30字'); return; }
      if (meetingForm.remark && meetingForm.remark.length > 255) { message.error('备注最多255字'); return; }
      if (!meetingForm.meetingTimeStart) { message.error('请选择开始时间'); return; }
      if (!meetingForm.durationMinutes) { message.error('请选择持续时间'); return; }
      if (!meetingForm.attendanceTimeStart || !meetingForm.attendanceTimeEnd) { message.error('请设置考勤时间'); return; }
      if (!meetingForm.personIds.length) { message.error('请选择与会人员'); return; }
      const formatDateTime = (date: Date) => `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:00`;
      const startTime = new Date(meetingForm.meetingTimeStart.replace(' ', 'T'));
      const meetingTimeStart = formatDateTime(startTime);
      const endTime = new Date(startTime.getTime() + meetingForm.durationMinutes * 60 * 1000);
      const meetingTimeEnd = formatDateTime(endTime);
      const attendanceTimeStart = `${meetingForm.attendanceTimeStart}:00`;
      const attendanceTimeEnd = `${meetingForm.attendanceTimeEnd}:00`;
      if (meetingForm.attendanceTimeEnd < meetingForm.attendanceTimeStart) { message.error('考勤截止时间不能早于考勤开始时间'); return; }
      if (meetingForm.attendanceTimeStart < meetingForm.meetingTimeStart || meetingForm.attendanceTimeEnd > meetingTimeEnd.slice(0, 16)) { message.error('考勤时间需在会议持续时间内'); return; }
      try {
        await request({
          url: `${PREFIX}/meeting/${meetingForm.id ? 'update' : 'save'}`,
          method: 'POST',
          data: {
            id: meetingForm.id,
            meetingSubject: meetingForm.meetingSubject,
            meetingTimeStart,
            meetingTimeEnd,
            attendanceStartTime: attendanceTimeStart,
            attendanceEndTime: attendanceTimeEnd,
            personIds: meetingForm.personIds,
            remark: meetingForm.remark,
          },
        });
        message.success('保存成功');
        meetingModalVisible.value = false;
        loadMeetings();
      } catch {
        message.error('保存失败');
      }
    };
    const removeMeeting = async (id: string) => {
      try {
        await request({ url: `${PREFIX}/meeting/remove/${id}`, method: 'DELETE' });
        message.success('删除成功');
        loadMeetings();
      } catch {
        message.error('删除失败');
      }
    };
    const controlDevice = async (action: 'start' | 'stop') => {
      if (action === 'start' && !deviceMeetingId.value) { message.error('请选择要识别的会议'); return; }
      deviceControlLoading.value = true;
      try {
        const response = await fetch(`/face-checkin-device/${action}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json', 'X-Device-Control-Token': 'local-device-control-token' },
          body: JSON.stringify(action === 'start' ? { meetingReserveId: deviceMeetingId.value } : {}),
        });
        const result = await response.json();
        if (!response.ok || !result.success) { message.error(result.message || '操作失败'); return; }
        message.success(result.data?.message || '操作成功');
        loadDeviceStatus();
      } catch {
        message.error('无法连接本机设备控制服务');
      } finally {
        deviceControlLoading.value = false;
      }
    };
    const startDevice = () => controlDevice('start');
    const stopDevice = () => controlDevice('stop');
    const loadDeviceStatus = async () => {
      try {
        const response = await fetch('/face-checkin-device/status', {
          headers: { 'X-Device-Control-Token': 'local-device-control-token' },
        });
        const result = await response.json();
        const data = result.data || {};
        deviceStatus.running = !!data.running;
        deviceStatus.pid = data.pid || null;
        deviceStatus.exitCode = data.exitCode ?? null;
      } catch {
        deviceStatus.running = false;
      }
    };
    const latestRequest = computed<Record<string, any> | null>(() => requestList.value[0] as any || null);
    const latestRequestStatusText = computed(() => {
      const record = latestRequest.value as any;
      if (!record) return '';
      if (record.result === 'SUCCESS') return '签到成功';
      if (record.result === 'ALREADY_CHECKED_IN') return '重复签到';
      if (record.result === 'NO_PERMISSION') return '无权限';
      if (record.result === 'NOT_RECOGNIZED') return '未识别';
      if (record.result === 'OUT_OF_TIME_WINDOW') return '时间窗口外';
      return record.status === 1 ? '已处理' : '未处理';
    });
    const meetingLabel = (id: string) => {
      const meeting = meetingOptions.value.find((item) => item.value === id);
      return meeting?.label || id || '未指定';
    };
    let monitorTimer = 0;
    const startMonitor = () => {
      monitorTimer = window.setInterval(() => {
        loadDeviceStatus();
        loadRequests();
        if (meetingReserveId.value) loadAttendance();
      }, 10000);
    };
    onMounted(() => { loadMeetings(); loadPersons(); loadPersonOptions(); loadRequests(); loadDeviceStatus(); startMonitor(); });
    onUnmounted(() => { if (monitorTimer) window.clearInterval(monitorTimer); });
    return {
      currentMode,
      activeTab,
      pageTitle,
      meetingReserveId,
      meetingOptions,
      attendanceList,
      attendanceLoading,
      requestList,
      requestLoading,
      personList,
      personLoading,
      personKeyword,
      personPagination,
      personModalVisible,
      personForm,
      attendanceColumns,
      requestColumns,
      personColumns,
      statusText,
      statusColor,
      loadAttendance,
      loadRequests,
      loadPersons,
      personPageChange,
      participantCategoryText,
      participantCategoryColor,
      showPersonModal,
      onPhotoChange,
      savePerson,
      removePerson,
      editPerson,
      exportCsv,
      auditPerson,
      meetingList,
      meetingLoading,
      meetingModalVisible,
      meetingForm,
      meetingColumns,
      showMeetingModal,
      saveMeeting,
      removeMeeting,
      openDetail,
      meetingKeyword,
      meetingStatusFilter,
      meetingStatusOptions,
      filteredMeetingList,
      meetingStatusText,
      meetingStatusColor,
      durationOptions,
      personOptions,
      deviceMeetingId,
      deviceControlLoading,
      deviceStatus,
      startDevice,
      stopDevice,
      loadDeviceStatus,
      latestRequest,
      latestRequestStatusText,
      meetingLabel,
    };
  },
});
</script>
<style scoped>
.toolbar { margin-bottom: 16px; }
.device-control-card { margin-bottom: 16px; }

.device-tip {
    font-size: 12px;
    color: #666;
}

.latest-result {
    margin-bottom: 12px;
}
.single-mode-tabs :deep(.ant-tabs-nav) { display: none !important; }
</style>
