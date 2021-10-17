<template>
  <div>
    <ul class="flex bg-indigo-50 rounded-md border-2">
      <li
        class="px-3 py-2 border-r-2 h-full w-full"
        :class="{
          'opacity-50': curPage === 1 || totalPageCnt === 0 || totalOffset === 0,
          'hover:bg-indigo-200': curPage !== 1 && totalPageCnt !== 0 && totalOffset !== 0,
          ' cursor-pointer': curPage !== 1 && totalPageCnt !== 0 && totalOffset !== 0,
        }"
        @click="goFirst"
      >
        <i class="fas fa-angle-double-left"></i>
      </li>
      <li
        class="px-3 py-2 border-r-2 h-full w-full"
        :class="{
          'opacity-50': isFirst,
          'hover:bg-indigo-200': !isFirst,
          ' cursor-pointer': !isFirst,
        }"
        @click="prevOffset"
      >
        <i class="fas fa-chevron-left"></i>
      </li>
      <li
        class="float-left px-3 py-2 h-full w-full hover:bg-indigo-200 border-r-2"
        :class="{ 'bg-indigo-200': page === curPage, 'cursor-pointer': page !== curPage }"
        v-for="page in pageList"
        :key="page"
        @click="sendPage(page)"
      >
        {{ page }}
      </li>
      <li
        class="px-3 py-2 h-full w-full border-r-2"
        :class="{
          'opacity-50': isLast,
          'hover:bg-indigo-200': !isLast,
          ' cursor-pointer': !isLast,
        }"
        @click="nextOffset"
      >
        <i class="fas fa-chevron-right"></i>
      </li>
      <li
        class="px-3 py-2 h-full w-full"
        :class="{
          'opacity-50': curPage === totalPageCnt || totalPageCnt === 0 || totalOffset === 0,
          'hover:bg-indigo-200':
            curPage !== totalPageCnt && totalPageCnt !== 0 && totalOffset !== 0,
          ' cursor-pointer': curPage !== totalPageCnt && totalPageCnt !== 0 && totalOffset !== 0,
        }"
        @click="goLast"
      >
        <i class="fas fa-angle-double-right"></i>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
export default {
  name: 'PAGINATION',
  props: ['pageCnt', 'pageSize'],
  components: {},
  data() {
    return {
      isFirst: false,
      isLast: false,
      totalPageCnt: 0,
      totalOffset: 0,
      pageList: [],
    };
  },
  created() {
    this.setPage();
  },
  mounted() {},
  methods: {
    ...mapActions(['set_curPage', 'toggle_isDel', 'set_offset']),
    setPage() {
      this.isFirst = false;
      this.isLast = false;

      this.totalPageCnt = Math.ceil(this.totalReviewCnt / this.pageSize);
      this.totalOffset = Math.ceil(this.totalPageCnt / this.pageCnt);

      if (
        this.offset >= this.totalOffset - 1 ||
        this.totalPageCnt === 0 ||
        this.totalOffset === 0
      ) {
        this.isLast = true;
      }

      if (this.offset < 1 || this.totalPageCnt === 0 || this.totalOffset === 0) {
        this.isFirst = true;
      }

      this.getPageList();
    },
    sendPage(page) {
      if (this.curPage === page) {
        return;
      }
      this.set_curPage(page);
      this.$emit('paging', page - 1);
    },
    prevOffset() {
      if (this.offset < 1) {
        return;
      }

      this.set_offset(this.offset - 1);
      this.sendPage((this.offset + 1) * this.pageCnt);
      this.getPageList();
    },
    nextOffset() {
      if (this.offset >= this.totalOffset - 1) {
        return;
      }

      this.sendPage((this.offset + 1) * this.pageCnt + 1);
      this.set_offset(this.offset + 1);
      this.getPageList();
    },
    goFirst() {
      if (this.totalPageCnt === 0 || this.totalOffset === 0) {
        return;
      }
      this.sendPage(1);
      this.set_offset(0);
      this.getPageList();
    },
    goLast() {
      if (this.totalPageCnt === 0 || this.totalOffset === 0) {
        return;
      }
      this.sendPage(this.totalPageCnt);
      this.set_offset(this.totalOffset - 1);
      this.getPageList();
    },
    getPageList() {
      const curMaxPage = this.offset * 5;
      this.pageList = [];
      for (let i = 1; i <= 5; i++) {
        if (curMaxPage + i > this.totalPageCnt) {
          break;
        }
        this.pageList.push(curMaxPage + i);
      }
    },
  },
  watch: {
    curPage: function (val) {
      if (this.isDel) {
        if (val % this.pageCnt === 0) {
          this.prevOffset();
        }

        this.toggle_isDel(false);
      }

      if (this.offset < 1) {
        this.isFirst = true;
      } else {
        this.isFirst = false;
      }

      if (this.offset >= this.totalOffset - 1) {
        this.isLast = true;
      } else {
        this.isLast = false;
      }
    },
    totalReviewCnt: function (val) {
      this.totalPageCnt = Math.ceil(val / this.pageSize);
      this.totalOffset = Math.ceil(this.totalPageCnt / this.pageCnt);
      this.getPageList();
    },
  },
  computed: {
    ...mapGetters(['totalReviewCnt', 'curPage', 'isDel', 'offset']),
  },
};
</script>
