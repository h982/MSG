<template>
  <div class="flex flex-col h-screen w-auto">
    <Header />
    <Loading v-if="isLoading" />
    <div class="flex flex-col justify-center items-center flex-grow bg-blue-100 w-auto pt-10 pb-10">
      <div class="w-11/12 bg-white border-2 border-blue-500 border-opacity-50 rounded-lg mb-6">
        <button
          class="bg-white hover:bg-blue-200 font-bold py-2 px-4 rounded mr-2"
          @click="key"
          v-bind:class="keywordClasses"
        >
          키워드 분석
        </button>
        <button
          class="bg-white hover:bg-blue-200 font-bold py-2 px-4 rounded mr-2"
          @click="comp"
          v-bind:class="compClasses"
        >
          비교 분석
        </button>
        <button
          class="bg-white hover:bg-blue-200 font-bold py-2 px-4 rounded mr-2"
          @click="mention"
          v-bind:class="mentionClasses"
        >
          언급량 분석
        </button>
        <button
          class="bg-white hover:bg-blue-200 font-bold py-2 px-4 rounded mr-2"
          @click="info"
          v-bind:class="infoClasses"
        >
          맛집 정보
        </button>
        <!-- <button>키워드 분석</button> -->
      </div>
      <div class="w-full h-full flex items-center justify-center">
        <keyword v-if="btn[0]" />
        <compare v-if="btn[1]" />
        <mention v-if="btn[2]" />
        <info v-if="btn[3]" />
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import Keyword from '@/views/Keyword.vue';
import Compare from '@/views/Compare.vue';
import Info from '@/views/Info.vue';
import Mention from '@/views/Mention.vue';
import Loading from '@/components/Loading.vue';
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'Analysis',
  components: {
    Header,
    Footer,
    Keyword,
    Compare,
    Info,
    Mention,
    Loading,
  },
  data() {
    return {
      btn: [],
      // selected: false,
      // classStyle: {
      //   "background-color": "rgba(59, 130, 246, 0.5)",
      // },
    };
  },
  computed: {
    ...mapGetters(['isLoading']),
    keywordClasses: function () {
      return {
        blue: this.btn[0],
      };
    },
    compClasses: function () {
      return {
        blue: this.btn[1],
      };
    },
    mentionClasses: function () {
      return {
        blue: this.btn[2],
      };
    },
    infoClasses: function () {
      return {
        blue: this.btn[3],
      };
    },
  },
  created() {
    this.btn = [true, false, false, false];
  },
  methods: {
    ...mapActions(['set_analysisType']),
    key() {
      if (!this.btn[0]) {
        for (var i = 0; i < 4; i++) {
          this.btn[i] = false;
        }
        this.btn[0] = true;
        this.set_analysisType(1);
      }
    },
    comp() {
      if (!this.btn[1]) {
        for (var i = 0; i < 4; i++) {
          this.btn[i] = false;
        }
        this.set_analysisType(2);
        this.btn[1] = true;
      }
    },
    mention() {
      if (!this.btn[2]) {
        for (var i = 0; i < 4; i++) {
          this.btn[i] = false;
        }
        this.btn[2] = true;
        this.set_analysisType(1);
      }
    },
    info() {
      if (!this.btn[3]) {
        for (var i = 0; i < 4; i++) {
          this.btn[i] = false;
        }
        this.btn[3] = true;
        this.set_analysisType(1);
      }
    },
  },
};
</script>

<style>
.blue {
  background-color: rgba(74, 140, 247, 0.5);
}
</style>
