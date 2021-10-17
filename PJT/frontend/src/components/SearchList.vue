<template>
  <div v-if="list.length > 0" class="rounded-lg z-10 relative">
    <div
      class="
        absolute
        bg-indigo-50
        w-full
        h-auto
        flex flex-col
        top-px
        rounded
        border-indigo-600 border-l-2 border-b-2 border-r-2
      "
    >
      <div
        class="flex pl-10 py-2 h-full w-full hover:bg-gray-300 cursor-pointer"
        v-for="(item, index) in list"
        :key="index"
        :ref="`${index}`"
        @click="go(item)"
      >
        {{ item.name }} {{ item.area }}
      </div>
    </div>
  </div>
  <div v-else class="rounded-lg z-10 relative">
    <div class="absolute bg-blue-50 w-full h-auto flex flex-col top-px rounded border-msg-content">
      <div
        v-if="this.searchStore.length > 0"
        class="flex pl-10 py-2 h-full w-full hover:bg-gray-300 cursor-pointer"
        @click="addStore"
      >
        맛집 추가 요청
      </div>
    </div>
  </div>
</template>

<script>
import { updateSearch } from '@/api/search.js';
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'SearchList',
  props: ['list', 'idx', 'searchStore', 'searchType'],
  methods: {
    ...mapActions(['set_store', 'set_vsStore']),
    go(item) {
      updateSearch(
        item,
        () => {
          if (this.searchType == 1) {
            this.set_store(item);
            this.$router.push('Analysis');
          } else if (this.searchType == 2) {
            this.set_vsStore(item);
          }
        },
        () => {
          alert('언급량 최신화 실패!');
          return;
        }
      );
    },
    addStore() {
      this.$router.push('AddStore');
    },
  },
  computed: {
    ...mapGetters(['store', 'vsStore']),
  },
  watch: {
    idx: function (val, oldVal) {
      if (!this.list || this.list.length <= 0) {
        return;
      }

      if (oldVal >= 0) {
        this.$refs[oldVal].classList.remove('bg-gray-300');
      }
      this.$refs[val].classList.add('bg-gray-300');
    },
  },
};
</script>
