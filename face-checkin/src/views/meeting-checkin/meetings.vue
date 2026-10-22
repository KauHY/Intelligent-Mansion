<template>
  <FaceCheckinIndex mode="meetings" />
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import FaceCheckinIndex from '~views/meeting-manage/face-checkin/index.vue';

export default defineComponent({
  name: 'MeetingCheckinMeetings',
  components: { FaceCheckinIndex },
});
</script>
