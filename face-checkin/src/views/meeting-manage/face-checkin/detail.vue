<template>
  <div class="meeting-detail-page">
    <a-card :title="`会议详情：${meeting?.meetingSubject || meetingId}`">
      <template #extra>
        <a-space>
          <a-button @click="goBack">
            返回会议管理
          </a-button>
          <a-button
            type="primary"
            @click="showEditMeeting"
          >
            编辑会议
          </a-button>
          <a-button @click="toggleDebug">
            {{ debugVisible ? '退出调试界面' : '进入调试界面' }}
          </a-button>
        </a-space>
      </template>

      <a-descriptions
        size="small"
        bordered
        :column="3"
        class="meeting-summary"
      >
        <a-descriptions-item label="会议ID">
          {{ meetingId }}
        </a-descriptions-item>
        <a-descriptions-item label="开始时间">
          {{ meeting?.meetingTimeStart || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="结束时间">
          {{ meeting?.meetingTimeEnd || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="考勤开始时间">
          {{ meeting?.attendanceStartTime || "-" }}
        </a-descriptions-item>
        <a-descriptions-item label="考勤截止时间">
          {{ meeting?.attendanceEndTime || "-" }}
        </a-descriptions-item>
        <a-descriptions-item label="与会人数">
          {{ meeting?.meetingPeopleNum || memberList.length }}
        </a-descriptions-item>
        <a-descriptions-item
          label="备注"
          :span="2"
        >
          {{ meeting?.remark || '-' }}
        </a-descriptions-item>
      </a-descriptions>

      <a-card
        size="small"
        title="识别控制"
        class="section-card"
      >
        <a-space wrap>
          <a-button
            type="primary"
            :loading="deviceControlLoading"
            @click="startDevice"
          >
            启动本会议识别
          </a-button>
          <a-button
            danger
            :loading="deviceControlLoading"
            @click="stopDevice"
          >
            停止识别
          </a-button>
          <a-button
            :loading="deviceControlLoading"
            @click="refreshDevice"
          >
            刷新状态
          </a-button>
          <a-tag :color="deviceStatus.running ? 'green' : 'default'">
            {{ deviceStatus.running ? `识别服务运行中${deviceStatus.pid ? ` PID:${deviceStatus.pid}` : ''}` : '识别服务未运行' }}
          </a-tag>
          <a-tag :color="boardSerialTagColor">
            {{ boardSerialText }}
          </a-tag>
          <a-tag
            v-if="deviceStatus.meetingReserveId"
            color="blue"
          >
            会议：{{ deviceStatus.meetingReserveId }}
          </a-tag>
        </a-space>
        <div
          v-if="latestRequest"
          class="latest-result"
        >
          <span>最近识别：</span>
          <span class="latest-name">{{ latestRequest.personName || '未识别' }}</span>
          <a-tag :color="latestRequest.result === 'SUCCESS' ? 'green' : 'orange'">
            {{ requestResultText(latestRequest.result) }}
          </a-tag>
          <span>{{ latestRequest.acceptTime }}</span>
        </div>
        <div class="device-tip">
          请先在本机启动 <code>Start-Device-Control.cmd</code>。
        </div>
      </a-card>

      <a-card
        size="small"
        title="签到名单（按签到时间倒序）"
        class="section-card"
      >
        <template #extra>
          <a-button
            size="small"
            @click="loadDetail"
          >
            刷新
          </a-button>
        </template>
        <a-table
          row-key="id"
          :columns="attendanceColumns"
          :data-source="attendanceList"
          :loading="attendanceLoading"
          :pagination="false"
          size="middle"
        >
          <template #personType="{ record }">
            <a-tag :color="record.personType === 1 ? 'blue' : 'purple'">
              {{ personTypeText(record.personType) }}
            </a-tag>
          </template>
          <template #participantCategory="{ record }">
            <a-tag :color="participantCategoryColor(record.participantCategory)">
              {{ participantCategoryText(record.participantCategory) }}
            </a-tag>
          </template>
          <template #status="{ record }">
            <a-tag :color="statusColor(record.status)">
              {{ statusText(record.status) }}
            </a-tag>
          </template>
        </a-table>
      </a-card>

      <a-card
        size="small"
        title="会议成员"
        class="section-card"
      >
        <template #extra>
          <a-button
            size="small"
            @click="showEditMeeting"
          >
            编辑成员
          </a-button>
        </template>
        <a-table
          row-key="id"
          :columns="memberColumns"
          :data-source="memberList"
          :loading="memberLoading"
          :pagination="false"
          size="middle"
        >
          <template #personType="{ record }">
            <a-tag :color="record.personType === 1 ? 'blue' : 'purple'">
              {{ personTypeText(record.personType) }}
            </a-tag>
          </template>
        </a-table>
      </a-card>

      <template v-if="debugVisible">
        <a-card
          size="small"
          title="板端调试指南"
          class="section-card"
        >
          <ul class="debug-list">
            <li>Web 启动后看下方「识别日志」，页面每 10 秒自动刷新，包含 <code>[heartbeat]</code>、<code>[connection]</code>、<code>[photo]</code>、<code>[identify]</code>。</li>
            <li>要看本机命令行实时日志，请双击 <code>code\Start-Device-Log.cmd</code>；它会跟随 <code>pc_bridge\device_control.log</code> 输出。</li>
            <li>要看桥接进程原始终端，请先在详情页「停止识别」，再运行 <code>code\Start-Ethernet-Checkin.cmd &lt;会议ID&gt;</code>。</li>
            <li>如果日志一直只有心跳且没有 <code>[connection]</code>，说明板端还没有连上 PC 的 <code>9000</code> 端口；先检查网线/IP/防火墙。</li>
            <li>如果出现 <code>[photo]</code> 但没有 <code>[identify]</code>，说明照片已到 PC，但后端识别未完成，继续看错误行。</li>
          </ul>
        </a-card>

        <a-card
          size="small"
          title="板端日志（COM5）"
          class="section-card"
        >
          <template #extra>
            <a-button
              size="small"
              @click="loadLogs"
            >
              刷新
            </a-button>
          </template>
          <pre class="log-view board-log-view">{{ boardLogLines.join('\n') || '暂无板端日志' }}</pre>
        </a-card>

        <a-card
          size="small"
          title="桥接日志"
          class="section-card"
        >
          <template #extra>
            <a-button
              size="small"
              @click="loadLogs"
            >
              刷新
            </a-button>
          </template>
          <pre class="log-view">{{ logLines.join('\n') || '暂无日志' }}</pre>
        </a-card>
      </template>
    </a-card>

    <a-modal
      v-model:visible="meetingModalVisible"
      title="编辑会议与成员"
      width="720px"
      @ok="saveMeeting"
    >
      <a-form layout="vertical">
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item
              label="会议主题"
              required
            >
              <a-input v-model:value="meetingForm.meetingSubject" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
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
          </a-col>
          <a-col :span="12">
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
          </a-col>
          <a-col :span="12">
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
          </a-col>
          <a-col :span="12">
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
          </a-col>
          <a-col :span="24">
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
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注">
              <a-textarea v-model:value="meetingForm.remark" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts">
import {
  computed, defineComponent, onMounted, onUnmounted, reactive, ref, watch,
} from 'vue';
import { useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import request from '~utils/axios';

const PREFIX = 'park-meeting/face-checkin';
const DEVICE_TOKEN = 'local-device-control-token';

const attendanceColumns = [
  {
    title: '签到时间', dataIndex: 'checkinTime', key: 'checkinTime', width: 180,
  },
  {
    title: '姓名', dataIndex: 'personName', key: 'personName', width: 130,
  },
  {
    title: '识别分类', key: 'participantCategory', slots: { customRender: 'participantCategory' }, width: 110,
  },
  {
    title: '工号', dataIndex: 'employeeNo', key: 'employeeNo', width: 120,
  },
  {
    title: '员工类型', key: 'personType', slots: { customRender: 'personType' }, width: 110,
  },
  {
    title: '状态', key: 'status', slots: { customRender: 'status' }, width: 130,
  },
  {
    title: '备注', dataIndex: 'remark', key: 'remark', width: 110,
  },
  {
    title: '设备ID', dataIndex: 'deviceId', key: 'deviceId', width: 140,
  },
  { title: '请求ID', dataIndex: 'requestId', key: 'requestId' },
];

const memberColumns = [
  { title: '姓名', dataIndex: 'personName', key: 'personName' },
  { title: '工号', dataIndex: 'employeeNo', key: 'employeeNo' },
  {
    title: '员工类型', key: 'personType', slots: { customRender: 'personType' }, width: 120,
  },
];

export default defineComponent({
  name: 'FaceCheckinMeetingDetail',
  setup() {
    const route = useRoute();
    const meetingId = computed(() => String(route.params.id || ''));
    const meeting = ref<Record<string, any> | null>(null);
    const attendanceList = ref<Array<Record<string, any>>>([]);
    const attendanceLoading = ref(false);
    const memberList = ref<Array<Record<string, any>>>([]);
    const memberLoading = ref(false);
    const debugVisible = ref(false);
    const personOptions = ref<Array<{ value: string; label: string }>>([]);
    const meetingModalVisible = ref(false);
    const meetingForm = reactive({
      id: '',
      meetingSubject: '',
      meetingTimeStart: '',
      durationMinutes: 60,
      attendanceTimeStart: '',
      attendanceTimeEnd: '',
      personIds: [] as string[],
      remark: '',
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
    const requestList = ref<Array<Record<string, any>>>([]);
    const logLines = ref<Array<string>>([]);
    const deviceControlLoading = ref(false);
    const deviceStatus = reactive({
      running: false,
      pid: null as number | null,
      meetingReserveId: '' as string | null,
      exitCode: null as number | null,
      boardSerial: {
        status: 'starting',
        detail: '',
        port: 'COM3',
        baud: 115200,
      },
    });

    const latestRequest = computed(() => requestList.value[0] || null);
    const boardLogLines = computed(() => logLines.value.filter((line: string) => line.includes('[board] ')));
    const boardSerialText = computed(() => {
      const info = deviceStatus.boardSerial || {};
      if (info.status === 'connected') return `板端串口已连接 ${info.port}:${info.baud}`;
      if (info.status === 'unavailable') return '板端串口不可用';
      if (info.status === 'failed') return '板端串口连接失败';
      return `板端串口状态：${info.status || 'starting'}`;
    });
    const boardSerialTagColor = computed(() => (deviceStatus.boardSerial?.status === 'connected' ? 'green' : 'orange'));

    const personTypeText = (value: any) => {
      const type = Number(value);
      if (type === 1) return '工作人员';
      if (type === 2) return '访客';
      return '未知';
    };
    const statusText = (status: number) => {
      const names: Record<number, string> = {
        1: '签到成功', 2: '重复签到', 3: '无权限', 4: '未识别', 5: '签到未开始或已结束', 7: '处理失败',
      };
      return names[status] || '未知状态';
    };
    const statusColor = (status: number) => {
      const colors: Record<number, string> = {
        1: 'green', 2: 'yellow', 3: 'red', 4: 'gray', 5: 'blue', 7: 'red',
      };
      return colors[status] || 'default';
    };
    const requestResultText = (result: string) => {
      const names: Record<string, string> = {
        SUCCESS: '签到成功',
        ALREADY_CHECKED_IN: '重复签到',
        NO_PERMISSION: '无权限',
        NOT_RECOGNIZED: '未识别',
        OUT_OF_TIME_WINDOW: '时间窗外',
      };
      return names[result] || (result || '未处理');
    };

    const participantCategoryText = (value: any) => ({ 1: '与会人员', 2: '非与会人员', 3: '非记录人员' }[Number(value)] || '未知');
    const participantCategoryColor = (value: any) => ({ 1: 'green', 2: 'orange', 3: 'red' }[Number(value)] || 'default');

    const loadMeeting = async () => {
      if (!meetingId.value) return;
      const { data } = await request({ url: `${PREFIX}/meeting/list`, method: 'GET' });
      meeting.value = (data || []).find((item: any) => item.id === meetingId.value) || null;
    };

    const loadAttendance = async () => {
      if (!meetingId.value) return;
      attendanceLoading.value = true;
      try {
        // hiddenLoading：轮询请求不打全屏遮罩，避免 10s 一次的 loading 闪烁
        const { data } = await request({
          url: `${PREFIX}/attendance/list`,
          method: 'GET',
          params: { meetingReserveId: meetingId.value },
          hiddenLoading: true,
        });
        attendanceList.value = data || [];
      } catch {
        attendanceList.value = [];
        message.error('签到名单加载失败');
      } finally {
        attendanceLoading.value = false;
      }
    };

    const loadMembers = async () => {
      if (!meetingId.value) return;
      memberLoading.value = true;
      try {
        const { data } = await request({ url: `${PREFIX}/member/list`, method: 'GET', params: { meetingReserveId: meetingId.value } });
        memberList.value = data || [];
      } catch {
        memberList.value = [];
        message.error('会议成员加载失败');
      } finally {
        memberLoading.value = false;
      }
    };

    const loadPersonOptions = async () => {
      try {
        const { data } = await request({ url: `${PREFIX}/person/list`, method: 'GET', params: { pageNum: 1, pageSize: 200 } });
        personOptions.value = (data?.list || [])
          .filter((item: any) => item.status === 1 && item.auditStatus === 1)
          .map((item: any) => ({
            value: item.id,
            label: `${item.personName || item.id}${item.employeeNo ? ` (${item.employeeNo})` : ''}`,
          }));
      } catch {
        personOptions.value = [];
      }
    };

    const toggleDebug = async () => {
      debugVisible.value = !debugVisible.value;
      if (debugVisible.value) await loadLogs();
    };

    const minuteValue = (value: unknown) => String(value || '').slice(0, 16);
    const formatDateTime = (date: Date) => `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:00`;

    const showEditMeeting = () => {
      if (!meeting.value) return;
      const startTime = minuteValue(meeting.value.meetingTimeStart);
      const endTime = minuteValue(meeting.value.meetingTimeEnd);
      const durationMinutes = startTime && endTime
        ? Math.max(15, Math.round((new Date(endTime).getTime() - new Date(startTime).getTime()) / 60000))
        : 60;
      const attendeeIds = String(meeting.value.attendees || '')
        .split(',').map((id: string) => id.trim()).filter(Boolean);
      Object.assign(meetingForm, {
        id: meeting.value.id,
        meetingSubject: meeting.value.meetingSubject || '',
        meetingTimeStart: startTime,
        durationMinutes,
        attendanceTimeStart: minuteValue(meeting.value.attendanceStartTime),
        attendanceTimeEnd: minuteValue(meeting.value.attendanceEndTime),
        personIds: attendeeIds,
        remark: meeting.value.remark || '',
      });
      meetingModalVisible.value = true;
    };

    const saveMeeting = async () => {
      if (!meetingForm.meetingSubject) { message.error('请输入会议主题'); return; }
      if (!meetingForm.meetingTimeStart) { message.error('请选择开始时间'); return; }
      if (!meetingForm.durationMinutes) { message.error('请选择持续时间'); return; }
      if (!meetingForm.attendanceTimeStart || !meetingForm.attendanceTimeEnd) { message.error('请设置考勤时间'); return; }
      if (!meetingForm.personIds.length) { message.error('请选择与会人员'); return; }
      const startTime = new Date(meetingForm.meetingTimeStart);
      if (Number.isNaN(startTime.getTime())) { message.error('开始时间无效'); return; }
      const endTime = new Date(startTime.getTime() + meetingForm.durationMinutes * 60 * 1000);
      const meetingTimeStart = formatDateTime(startTime);
      const meetingTimeEnd = formatDateTime(endTime);
      if (meetingForm.attendanceTimeEnd < meetingForm.attendanceTimeStart) { message.error('考勤截止时间不能早于考勤开始时间'); return; }
      if (meetingForm.attendanceTimeStart < meetingForm.meetingTimeStart || meetingForm.attendanceTimeEnd > meetingTimeEnd.slice(0, 16)) { message.error('考勤时间需在会议持续时间内'); return; }
      try {
        await request({
          url: `${PREFIX}/meeting/update`,
          method: 'POST',
          data: {
            id: meetingForm.id,
            meetingSubject: meetingForm.meetingSubject,
            meetingTimeStart,
            meetingTimeEnd,
            attendanceStartTime: `${meetingForm.attendanceTimeStart}:00`,
            attendanceEndTime: `${meetingForm.attendanceTimeEnd}:00`,
            personIds: meetingForm.personIds,
            remark: meetingForm.remark,
          },
        });
        message.success('保存成功');
        meetingModalVisible.value = false;
        await Promise.all([loadMeeting(), loadMembers()]);
      } catch {
        message.error('保存失败');
      }
    };

    const loadRequests = async () => {
      try {
        const { data } = await request({
          url: `${PREFIX}/request/list`,
          method: 'GET',
          params: { meetingReserveId: meetingId.value || undefined },
          hiddenLoading: true,
        });
        requestList.value = data || [];
      } catch {
        requestList.value = [];
      }
    };

    const controlDevice = async (action: 'start' | 'stop') => {
      deviceControlLoading.value = true;
      try {
        const response = await fetch(`/face-checkin-device/${action}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json', 'X-Device-Control-Token': DEVICE_TOKEN },
          body: JSON.stringify(action === 'start' ? { meetingReserveId: meetingId.value } : {}),
        });
        const result = await response.json();
        if (!response.ok || !result.success) {
          message.error(result.message || '操作失败');
          return;
        }
        message.success(result.data?.message || '操作成功');
      } catch {
        message.error('无法连接本机设备控制服务');
      } finally {
        deviceControlLoading.value = false;
        await refreshDevice();
        await loadLogs();
      }
    };
    const startDevice = () => controlDevice('start');
    const stopDevice = () => controlDevice('stop');

    const refreshDevice = async () => {
      try {
        const response = await fetch('/face-checkin-device/status', { headers: { 'X-Device-Control-Token': DEVICE_TOKEN } });
        const result = await response.json();
        Object.assign(deviceStatus, result.data || {});
      } catch {
        Object.assign(deviceStatus, {
          running: false,
          pid: null,
          meetingReserveId: null,
          exitCode: null,
          boardSerial: {
            status: 'failed', detail: '无法连接设备控制服务', port: 'COM3', baud: 115200,
          },
        });
      }
    };

    const loadLogs = async () => {
      try {
        const response = await fetch('/face-checkin-device/logs', { headers: { 'X-Device-Control-Token': DEVICE_TOKEN } });
        const result = await response.json();
        logLines.value = result.data || [];
      } catch {
        logLines.value = [];
      }
    };

    const loadDetail = async () => {
      await Promise.all([loadMeeting(), loadAttendance(), loadMembers(), loadRequests(), refreshDevice(), loadPersonOptions()]);
      if (debugVisible.value) await loadLogs();
    };

    const goBack = () => window.history.back();

    let timer = 0;
    const startMonitor = () => {
      timer = window.setInterval(() => {
        refreshDevice();
        loadAttendance();
        loadRequests();
        if (debugVisible.value) loadLogs();
      }, 10000);
    };

    onMounted(loadDetail);
    onMounted(startMonitor);
    onUnmounted(() => { if (timer) window.clearInterval(timer); });
    watch(meetingId, loadDetail);

    return {
      meetingId,
      meeting,
      goBack,
      loadDetail,
      attendanceList,
      attendanceLoading,
      attendanceColumns,
      loadAttendance,
      memberList,
      memberLoading,
      memberColumns,
      debugVisible,
      toggleDebug,
      meetingModalVisible,
      meetingForm,
      durationOptions,
      personOptions,
      showEditMeeting,
      saveMeeting,
      deviceControlLoading,
      deviceStatus,
      startDevice,
      stopDevice,
      refreshDevice,
      latestRequest,
      requestResultText,
      personTypeText,
      statusText,
      statusColor,
      boardLogLines,
      boardSerialText,
      boardSerialTagColor,
      participantCategoryText,
      participantCategoryColor,
      logLines,
      loadLogs,
    };
  },
});
</script>

<style scoped>
.meeting-summary { margin-bottom: 16px; }
.section-card { margin-bottom: 16px; }
.latest-result { margin-top: 12px; }

.latest-name {
    margin: 0 8px;
    font-weight: 600;
}

.debug-list {
    padding-left: 20px;
    margin: 0;
}

.debug-list li {
    margin-bottom: 6px;
}

.board-log-view {
    max-height: 220px;
}

.device-tip {
    margin-top: 12px;
    font-size: 12px;
    color: #666;
}

.log-view {
    max-height: 280px;
    padding: 12px;
    margin: 0;
    overflow: auto;
    color: #d5f5e3;
    word-break: break-all;
    white-space: pre-wrap;
    background: #111;
    border-radius: 4px;
}
</style>
