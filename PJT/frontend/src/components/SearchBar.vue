<template>
  <div class="flex flex-col">
    <div
      class="
        flex
        items-center
        bg-blue-50
        h-4/5
        border-2
        rounded
        focus-within:ring-1 focus-within:ring-indigo-600
      "
      v-click-away="onClickOutside"
    >
      <button
        class="h-12 w-12 border-r-2 rounded-tl rounded-bl border-msg-content hover:bg-gray-300"
      >
        <i class="fas fa-search text-2xl" />
      </button>
      <input
        type="text"
        :value="search"
        @input="changeSearch"
        @keyup.down="pressDown"
        @keyup.up="pressUp"
        class="
          py-2
          px-2
          bg-blue-50
          w-4/5
          md:w-5/6
          lg:w-11/12
          placeholder-gray-400
          text-gray-900
          appearance-none
          focus:outline-none
        "
        @focus="focusSearchBar = true"
        placeholder="맛집을 입력해주세요."
      />
    </div>
    <Search-List
      :list="searchList"
      :idx="idx"
      :searchStore="search"
      :searchType="searchType"
      v-if="focusSearchBar"
      ref="refSearchList"
      class="h-auto w-full sm:w-full md:6/12 mx-auto relative"
    />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { getSearch } from '@/api/search.js';
import SearchList from './SearchList.vue';

export default {
  name: 'SearchBar',
  props: ['searchType'],
  components: {
    SearchList,
  },
  data() {
    return {
      search: '',
      focusSearchBar: false,
      fullName: '',
      idx: -1,
      searchList: [],
    };
  },
  created() {
    this.search = '';

    if (this.store && this.searchType === 1) {
      this.search = this.store.name;
    }

    this.searchList = [];
  },
  watch: {
    vsStore: function (val) {
      if (val && this.searchType === 2) {
        this.search = val.name;
      }
    },
  },
  computed: {
    ...mapGetters(['store', 'vsStore']),
  },
  methods: {
    changeSearch(e) {
      this.search = e.target.value;
      if (this.search.length == 0) this.searchList = [];
      else {
        getSearch(
          this.search,
          (res) => {
            this.searchList = res.object;
            this.idx = -1;
          },
          () => {
            alert('오류가 발생했습니다.');
          }
        );
      }
    },
    onClickOutside() {
      this.focusSearchBar = false;
    },
    pressDown() {
      if (this.searchList.length == 0) {
        return;
      }

      if (this.searchList.length <= this.idx) {
        return;
      }

      if (this.idx + 1 < this.searchList.length) {
        this.idx++;
      }

      this.fullName = this.searchList[this.idx].name + ' ' + this.searchList[this.idx].area;
      this.search = this.fullName;
    },
    pressUp() {
      if (this.searchList.length == 0) {
        return;
      }

      if (this.idx <= 0) {
        return;
      }

      if (this.idx > 0) {
        this.idx--;
      }

      this.fullName = this.searchList[this.idx].name + ' ' + this.searchList[this.idx].area;
      this.search = this.fullName;
    },
  },
};
</script>
