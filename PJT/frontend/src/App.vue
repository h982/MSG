<template>
  <div>
    <router-view />
    <Loading v-if="isLoading" />
  </div>
</template>

<script>
import Loading from '@/components/Loading.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'APP',
  components: {
    Loading,
  },
  created() {},
  computed: {
    ...mapGetters(['isLoading']),
  },
};
</script>

<style src="./assets/tailwind.css" />
