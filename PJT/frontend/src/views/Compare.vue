<template>
  <div class="w-11/12">
    <div class="flex w-full">
      <div class="flex flex-col w-1/2 mr-2">
        <div
          class="
            w-1/3
            h-12
            bg-white
            border-2 border-blue-500 border-opacity-50
            rounded-lg
            flex
            mr-2
            justify-start
          "
        >
          <div class="pl-5">
            <div v-if="isPos">
              <span
                class="text-base sm:text-sm"
                style="color: dodgerblue; font-weight: 1000"
                >긍정적인 평가 {{ wordPercent }}%</span
              >
              <div style="font-size: small; opacity: 0.8; color: black">
                {{ store.name }}의 대표 긍,부정
              </div>
            </div>
            <div v-else-if="isPos == false">
              <span style="color: red; font-weight: 1000"
                >부정적인 평가 {{ wordPercent }}%</span
              >
              <div style="font-size: small; opacity: 0.8; color: black">
                {{ store.name }}의 대표 긍,부정
              </div>
            </div>
            <div v-else class="text-base sm:text-sm">기다려주세요</div>
          </div>
        </div>
        <div class="flex w-full h-80 mt-2 mb-2 justify-center">
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
              class="
                flex
                justify-center
                items-center
                w-36
                p-2
                font-bold
                mb-2
                bg-gray-100
                rounded
              "
            >
              구글 리뷰
            </div>
            <div
              class="w-full overflow-auto flex justify-center mb-2"
              v-if="words.length != 0"
            >
              <div class="w-11/12 text-center">
                <div class="flex w-full bg-gray-200">
                  <div class="w-2/3 px-6 py-2 font-bold text-xs text-gray-500">
                    구글 리뷰
                  </div>
                  <div class="w-1/3 px-6 py-2 font-bold text-xs text-gray-500">
                    긍, 부정
                  </div>
                </div>
                <div v-for="(review, index) in Greview" v-bind:key="index">
                  <div class="flex" v-if="review.google_review_txt.length > 0">
                    <div
                      class="
                        w-2/3
                        text-sm text-gray-500
                        border-t-2 border-gray-200
                      "
                    >
                      {{ review.google_review_txt }}
                    </div>
                    <div
                      v-if="review.google_emotion == 2"
                      class="
                        w-1/3
                        h-full
                        text-sm text-blue-500
                        border-t-2 border-gray-200
                        inline-block
                        align-middle
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
                        inline-block
                        align-middle
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
      </div>
      <div class="flex flex-col w-1/2">
        <div
          id="test2"
          class="
            w-1/3
            h-12
            bg-white
            border-2 border-blue-500 border-opacity-50
            rounded-lg
            flex
            mr-2
            justify-start
          "
        >
          <div class="h-12 pl-5" v-if="waitForvsWords == false">
            <div v-if="vsIsPos">
              <span style="color: dodgerblue; font-weight: 1000"
                >긍정적인 평가 {{ vsWordsPercent }}%</span
              >
            </div>
            <div v-else>
              <span style="color: red; font-weight: 1000"
                >부정적인 평가 {{ vsWordsPercent }}%</span
              >
            </div>
            <div style="font-size: small; opacity: 0.8; color: black">
              {{ vsStore.name }}의 대표 긍,부정
            </div>
          </div>
          <div class="h-12 pl-3" v-else>
            <span style="color: dark; font-weight: 1000">
              가게를 선택해주세요</span
            >
          </div>
        </div>
        <div class="flex w-full h-80 mt-2 mb-2">
          <wordcloud2 />
          <div
            v-if="this.vsGreview.length > 0"
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
              class="
                flex
                justify-center
                items-center
                w-36
                p-2
                font-bold
                mb-2
                bg-gray-100
                rounded
              "
            >
              구글 리뷰
            </div>
            <div
              class="w-full overflow-auto flex justify-center mb-2"
              v-if="words.length != 0"
            >
              <div class="w-11/12 text-center">
                <div class="flex w-full bg-gray-200">
                  <div class="w-2/3 px-6 py-2 font-bold text-xs text-gray-500">
                    구글 리뷰
                  </div>
                  <div class="w-1/3 px-6 py-2 font-bold text-xs text-gray-500">
                    긍, 부정
                  </div>
                </div>
                <div v-for="(review, index) in vsGreview" v-bind:key="index">
                  <div class="flex" v-if="review.google_review_txt.length > 0">
                    <div
                      class="
                        w-2/3
                        text-sm text-gray-500
                        border-t-2 border-gray-200
                      "
                    >
                      {{ review.google_review_txt }}
                    </div>
                    <div
                      v-if="review.google_emotion == 2"
                      class="
                        w-1/3
                        h-full
                        text-sm text-blue-500
                        border-t-2 border-gray-200
                        inline-block
                        align-middle
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
                        inline-block
                        align-middle
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
      </div>
    </div>
  <div class="w-full h-80 rounded-lg border-2 border-blue-500 border-opacity-50">
    <compare-chart />
  </div>
  </div>
</template>

<script>
import Wordcloud from "@/components/Analysis/Wordcloud.vue";
import Wordcloud2 from "@/components/Analysis/Wordcloud2.vue";
import CompareChart from "@/components/Analysis/CompareChart.vue";
import { mapGetters, mapActions } from "vuex";
import { getKeywords } from "@/api/search.js";
import { getGoogleReview } from "@/api/review.js";

export default {
  components: { Wordcloud, Wordcloud2, CompareChart },
  name: "Compare",
  data() {
    return {
      waitForWords: true,
      isPos: true,
      wordPercent: 0,
      waitForvsWords: true,
      vsIsPos: true,
      vsWordsPercent: 0,
      Greview: [],
      vsGreview: [],
    };
  },
  watch: {
    vsStore() {
      if (this.vsStore == null) return;
      getKeywords(this.vsStore, (res) => {
        this.vsWords = res.object;
        this.countPosNegVS();
        this.set_vsWords(res.object);
      });

      getGoogleReview(
        this.vsStore.area + this.vsStore.name,
        (res) => {
          // console.log("res", res.object);
          this.vsGreview = res.object;
        },
        () => {
          alert("구글 리뷰 가져오기 실패");
        }
      );
    },
  },
  created() {
    this.countPosNeg();
    this.set_vsStore(undefined);
    this.set_vsWords([]);
    getGoogleReview(
      this.store.area + this.store.name,
      (res) => {
        this.Greview = res.object;
      },
      () => {
        alert("구글 리뷰 가져오기 실패");
      }
    );
  },
  computed: {
    ...mapGetters(["words", "store", "vsStore"]),
  },
  methods: {
    ...mapActions(["set_words", "set_vsWords", "set_vsStore"]),
    countPosNeg() {
      let pos = 0;
      let neg = 0;
      let total = 0;
      for (let i = 0; i < this.words.length; i++) {
        total += this.words[i].count;
        if (
          this.words[i].sentiment == "긍정" ||
          this.words[i].sentiment == "매우긍정"
        ) {
          pos += this.words[i].count;
          continue;
        } else if (
          this.words[i].sentiment == "부정" ||
          this.words[i].sentiment == "매우부정"
        ) {
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
        }
      } else {
        this.wordPercent = 0;
      }
      this.waitForWords = false;
    },
    countPosNegVS() {
      let pos = 0;
      let neg = 0;
      let total = 0;
      for (let i = 0; i < this.vsWords.length; i++) {
        total += this.vsWords[i].count;
        if (
          this.vsWords[i].sentiment == "긍정" ||
          this.vsWords[i].sentiment == "매우긍정"
        ) {
          pos += this.vsWords[i].count;
          continue;
        } else if (
          this.vsWords[i].sentiment == "부정" ||
          this.vsWords[i].sentiment == "매우부정"
        ) {
          neg += this.vsWords[i].count;
          continue;
        }
      }
      if (total != 0) {
        if (pos >= neg) {
          this.vsIsPos = true;
          this.vsWordsPercent = Math.round(((pos * 100) / total) * 10 ) / 10;
        } else {
          this.vsIsPos = false;
          this.vsWordsPercent = Math.round(((neg * 100) / total) * 10 ) / 10;
        }
      } else {
        this.vsWordsPercent = 0;
      }
      this.waitForvsWords = false;
    },
  },
};
</script>

<style></style>
