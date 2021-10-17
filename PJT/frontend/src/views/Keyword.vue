<template>
  <div class="w-11/12">
    <div class="flex w-full justify-start">
      <div
        class="
          w-1/3
          h-12
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          flex
          mr-2
          justify-center
        "
      >
        <div v-if="waitForWords == false">
          <div v-if="isPos">
            <span style="color: dodgerblue; font-weight: 1000"
              >긍정적인 평가 {{ wordPercent }}%</span
            >
          </div>
          <div v-else>
            <span style="color: red; font-weight: 1000">부정적인 평가 {{ wordPercent }}%</span>
          </div>
          <div style="font-size: small; opacity: 0.8; color: black">
            {{ store.name }} 의 대표 긍,부정
          </div>
        </div>
      </div>
    </div>
    <div class="flex w-full h-80 mt-2 mb-2 justify-start">
      <wordcloud />
      <div
        v-if="this.Greview.length > 0"
        class="
          w-2/5
          h-80
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          pt-2
          flex flex-col
          items-center
          justify-center
        "
      >
        <div
          class="flex justify-center items-center w-36 p-2 font-bold ml-10 mb-2 bg-gray-100 rounded"
        >
          구글 리뷰
        </div>
        <div class="w-full overflow-auto flex justify-center mb-2" v-if="words.length != 0">
          <div class="w-11/12 text-center">
            <div class="flex w-full bg-gray-200">
              <div class="w-2/3 px-6 py-2 font-bold text-xs text-gray-500">구글 리뷰</div>
              <div class="w-1/3 px-6 py-2 font-bold text-xs text-gray-500">긍, 부정</div>
            </div>
            <div v-for="(review, index) in Greview" v-bind:key="index">
              <div class="flex" v-if="review.google_review_txt.length > 0">
                <div class="w-2/3 text-sm text-gray-500 border-t-2 border-gray-200">
                  {{ review.google_review_txt }}
                </div>
                <div
                  v-if="review.google_emotion == 2"
                  class="
                    w-1/3
                    h-full
                    text-sm text-blue-500
                    border-t-2 border-gray-200
                    flex
                    justify-center
                    items-center
                    text-center
                  "
                >
                  긍정
                </div>
                <div
                  v-else-if="review.google_emotion == 1"
                  class="
                    w-1/3
                    text-sm text-gray-500
                    border-t-2 border-gray-200
                    h-full
                    flex
                    justify-center
                    items-center
                    text-center
                  "
                >
                  중립
                </div>
                <div
                  v-else
                  class="
                    w-1/3
                    text-sm text-red-500
                    border-t-2 border-gray-200
                    h-full
                    flex
                    justify-center
                    items-center
                    text-center
                  "
                >
                  부정
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        class="
          w-2/5
          h-80
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          pt-2
          flex flex-col
          items-center
          justify-center
          text-gray-500
        "
        v-else
      >
        리뷰가 없습니다.
      </div>
    </div>
    <div class="flex w-full h-80 mt-2 mb-2 justify-start">
      <div
        class="
          w-3/5
          h-80
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          pt-2
          mr-2
          flex flex-col
          justify-center
        "
      >
        <div class="flex justify-center items-center w-36 p-2 font-bold ml-10 bg-gray-100 rounded">
          키워드 감성 통계
        </div>
        <chart v-bind:words="words" />
      </div>
      <div
        class="
          w-2/5
          h-80
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          pt-2
          flex flex-col
          justify-center
          items-center
        "
      >
        <div
          v-if="words.length != 0"
          class="flex justify-center items-center w-36 p-2 font-bold mb-2 bg-gray-100 rounded"
        >
          키워드 순위
        </div>
        <div class="w-full overflow-auto flex justify-center mb-2" v-if="words.length != 0">
          <div class="w-11/12 table-fixed text-center">
            <div class="flex w-full bg-gray-200">
              <div class="w-2/12 px-6 py-2 font-bold text-xs text-gray-500">순위</div>
              <div class="w-5/12 px-6 py-2 font-bold text-xs text-gray-500">키워드</div>
              <div class="w-2/12 px-6 py-2 font-bold text-xs text-gray-500">언급 수</div>
              <div class="w-3/12 px-6 py-2 font-bold text-xs text-gray-500">긍, 부정</div>
            </div>
            <div class="flex w-full" v-for="(word, index) in words" v-bind:key="index">
              <div class="w-2/12 text-sm text-gray-500 border-t-2 border-gray-200">
                {{ index + 1 }}
              </div>
              <div class="w-5/12 text-sm text-gray-500 border-t-2 border-gray-200">
                {{ word.keyword }}
              </div>
              <div class="w-2/12 text-sm text-gray-500 border-t-2 border-gray-200">
                {{ word.count }}
              </div>
              <div
                v-if="word.sentiment == '매우긍정'"
                class="w-3/12 text-sm text-blue-500 border-t-2 border-gray-200"
              >
                {{ word.sentiment }}
              </div>
              <div
                v-if="word.sentiment == '긍정'"
                class="w-3/12 text-sm text-blue-300 border-t-2 border-gray-200"
              >
                {{ word.sentiment }}
              </div>
              <div
                v-if="word.sentiment == '중립'"
                class="w-3/12 text-sm text-gray-500 border-t-2 border-gray-200"
              >
                {{ word.sentiment }}
              </div>
              <div
                v-if="word.sentiment == '부정'"
                class="w-3/12 text-sm text-red-300 border-t-2 border-gray-200"
              >
                {{ word.sentiment }}
              </div>

              <div
                v-if="word.sentiment == '매우부정'"
                class="w-3/12 text-sm text-red-500 border-t-2 border-gray-200"
              >
                {{ word.sentiment }}
              </div>
            </div>
          </div>
        </div>
        <div
          class="
            w-2/5
            h-80
            bg-white
            rounded-lg
            pt-2
            flex flex-col
            items-center
            justify-center
            text-gray-500
          "
          v-else
        >
          키워드가 없습니다.
        </div>
      </div>
    </div>
    <!-- 차트 -->
    <!-- <chart /> -->
  </div>
</template>

<script>
// import Chart from "@/components/Analysis/Chart.vue";
import Wordcloud from '@/components/Analysis/Wordcloud.vue';
import Chart from '../components/Analysis/Chart.vue';
import { getGoogleReview } from '@/api/review.js';
import { getKeywords } from '@/api/search.js';
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'Keyword',
  components: {
    // Chart,
    Wordcloud,
    Chart,
  },
  data() {
    return {
      words: [],
      waitForWords: true,
      isPos: true,
      wordPercent: 0,
      Greview: [],
    };
  },
  computed: {
    ...mapGetters(['store']),
  },
  methods: {
    ...mapActions(['set_words']),
    countPosNeg() {
      let pos = 0;
      let neg = 0;
      let total = 0;
      for (let i = 0; i < this.words.length; i++) {
        total += this.words[i].count;
        if (this.words[i].sentiment == '긍정' || this.words[i].sentiment == '매우긍정') {
          pos += this.words[i].count;
          continue;
        } else if (this.words[i].sentiment == '부정' || this.words[i].sentiment == '매우부정') {
          neg += this.words[i].count;
          continue;
        }
      }
      if (total != 0) {
        if (pos >= neg) {
          this.wordPercent = Math.round(((pos * 100) / total) * 10 ) / 10;
        } else {
          this.isPos = false;
          this.wordPercent = Math.round(((neg * 100) / total) * 10 ) / 10;
          //this.wordPercent = (neg * 100) / total;
        }
      } else {
        this.wordPercent = 0;
      }
      this.waitForWords = false;
    },
  },
  created() {
    getKeywords(this.store, (res) => {
      this.words = res.object;
      this.countPosNeg();
      this.set_words(res.object);
    });

    getGoogleReview(
      this.store.area + this.store.name,
      (res) => {
        this.Greview = res.object;
      },
      () => {
        alert('구글 리뷰 가져오기 실패');
      }
    );
  },
};
</script>

<style></style>
