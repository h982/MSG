<template>
  <div
    class="
      flex
      justify-center
      items-center
      fixed
      top-0
      left-0
      right-0
      bottom-0
      w-screen
      h-screen
      z-100
      bg-gray-700 bg-opacity-75
    "
  >
    <div
      class="
        flex flex-col
        w-10/12
        h-5/6
        md:w-8/12 md:h-4/6
        mx-auto
        fixed
        translate-x-1/2 translate-y-1/2
        bg-white
        z-50
      "
      v-click-away="closeModal"
    >
      <div class="flex flex-row h-5/6">
        <div v-if="imgNameList.length > 0" class="hidden md:flex relative w-1/2">
          <div class="absolute left-1 flex justify-center items-center h-full">
            <button
              class="h-10 w-10"
              :class="{ 'cursor-default': isFirst, hidden: isFirst }"
              :disabled="isFirst"
              @click="prevImg"
            >
              <i class="fas fa-chevron-circle-left text-xl font-bold text-gray-200 opacity-70"></i>
            </button>
          </div>

          <ul class="flex justify-center items-center h-full w-full bg-gray-900">
            <li
              v-for="(img, index) in imgNameList"
              :key="index"
              class="h-full w-auto rounded-tl-lg"
              :class="{ hidden: index !== curImg }"
            >
              <img
                class="h-full w-auto"
                :src="'http://j5c101.p.ssafy.io/api/img/' + img"
                alt="이미지가 없습니다."
              />
            </li>
          </ul>
          <div class="absolute right-1 flex justify-center items-center h-full">
            <button
              class="h-10 w-10"
              :class="{ 'cursor-default': isLast, hidden: isLast }"
              :disabled="isLast"
              @click="nextImg"
            >
              <i class="fas fa-chevron-circle-right text-xl font-bold text-gray-200 opacity-70"></i>
            </button>
          </div>
        </div>
        <div class="hidden md:flex relative w-1/2 border-r-2 justify-center items-center" v-else>
          등록된 이미지가 없습니다.
        </div>
        <div class="flex md:border-l-2 flex-col w-full md:w-1/2">
          <div class="flex flex-col justify-center relative h-1/6 border-b-2 p-2 md:p-5">
            <div class="flex text-xl font-extrabold">{{ review.dong }} {{ review.store }}</div>
            <div class="flex mt-1 md:hidden">
              <star-rating
                class="font-black"
                :increment="0.5"
                @update:rating="rating = $event"
                :show-rating="true"
                :rating="review.starScore"
                :starSize="25"
                :read-only="true"
              ></star-rating>
            </div>
            <div class="absolute right-2 bottom-1 sm:bottom-2">
              <p class="text-xs">{{ getDate }}</p>
            </div>
          </div>
          <div
            class="hidden md:flex flex-col items-center justify-center px-5 py-3 h-1/6 border-b-2"
          >
            <star-rating
              :increment="0.5"
              @update:rating="rating = $event"
              :show-rating="false"
              :rating="review.starScore"
              :starSize="30"
              :read-only="true"
            ></star-rating>
            <div class="text-xl font-black">
              {{ review.starScore }}
            </div>
          </div>
          <div class="flex flex-col md:flex-none h-5/6 md:h-4/6">
            <div class="h-1/2 md:h-full p-5">{{ review.content }}</div>
            <div class="h-1/2 flex md:hidden w-full border-t-2">
              <div v-if="imgNameList.length > 0" class="flex relative w-full">
                <div class="absolute left-1 flex justify-center items-center h-full">
                  <button
                    class="h-10 w-10"
                    :class="{ 'cursor-default': isFirst, hidden: isFirst }"
                    :disabled="isFirst"
                    @click="prevImg"
                  >
                    <i
                      class="fas fa-chevron-circle-left text-xl font-bold text-gray-200 opacity-70"
                    ></i>
                  </button>
                </div>

                <ul class="flex justify-center items-center h-full w-full bg-gray-900">
                  <li
                    v-for="(img, index) in imgNameList"
                    :key="index"
                    class="flex h-full w-auto rounded-tl-lg"
                    :class="{ hidden: index !== curImg }"
                  >
                    <img
                      class="flex h-full w-auto"
                      :src="'http://j5c101.p.ssafy.io/api/img/' + img"
                      alt="이미지가 없습니다."
                    />
                  </li>
                </ul>
                <div class="absolute right-1 flex justify-center items-center h-full">
                  <button
                    class="h-10 w-10"
                    :class="{ 'cursor-default': isLast, hidden: isLast }"
                    :disabled="isLast"
                    @click="nextImg"
                  >
                    <i
                      class="fas fa-chevron-circle-right text-xl font-bold text-gray-200 opacity-70"
                    ></i>
                  </button>
                </div>
              </div>
              <div class="flex relative w-full justify-center items-center" v-else>
                등록된 이미지가 없습니다.
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="flex justify-center items-center h-1/6 border-t-2 p-5">
        <button
          v-if="review.mid == this.id"
          class="
            bg-red-600
            text-white text-base
            font-semibold
            w-20
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-red-800
            focus:outline-none
            focus:ring-2
            focus:ring-red-500
            focus:ring-offset-2
            focus:ring-offset-red-200
            mr-10
          "
          @click="deleteReview(review.rid)"
        >
          삭제
        </button>
        <button
          class="
            bg-blue-600
            text-white text-base
            font-semibold
            w-20
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-blue-800
            focus:outline-none
            focus:ring-2
            focus:ring-blue-500
            focus:ring-offset-2
            focus:ring-offset-blue-200
          "
          @click="closeModal"
        >
          닫기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import StarRating from 'vue-star-rating';
import { mapGetters } from 'vuex';
import { getReviewImg } from '@/api/review.js';

export default {
  name: 'MODAL',
  components: {
    StarRating,
  },
  props: ['review'],
  data() {
    return {
      curImg: 0,
      imgNameList: [],
      isLast: false,
      isFirst: false,
    };
  },
  created() {
    // console.log(this.review.mid, this.id);
    this.curImg = 0;
    this.getImg();
  },
  methods: {
    deleteReview(rid) {
      this.$emit('deleteReview', rid);
      this.$emit('closeModal');
    },
    closeModal() {
      this.$emit('closeModal');
    },
    async getImg() {
      await getReviewImg(
        this.review.rid,
        (res) => {
          this.imgNameList = res.object.fileNameList;

          if (this.imgNameList.length === 1) {
            this.isLast = true;
          } else {
            this.isLast = false;
          }
          this.isFirst = true;
        },
        (error) => {
          alert('문제가 발생했습니다. 다시 시도해주세요.');
          console.log(error);
        }
      );
    },
    nextImg() {
      if (this.isFirst) {
        this.isFirst = false;
      }
      this.curImg += 1;
      if (this.curImg === this.imgNameList.length - 1) {
        this.isLast = true;
      }
    },
    prevImg() {
      if (this.isLast) {
        this.isLast = false;
      }

      this.curImg -= 1;
      if (this.curImg === 0) {
        this.isFirst = true;
      }
    },
  },
  computed: {
    ...mapGetters(['id']),
    getDate: function () {
      let dateArr = this.review.regDate.split('T');
      return `${dateArr[0]}\n${dateArr[1]}`;
    },
  },
};
</script>
