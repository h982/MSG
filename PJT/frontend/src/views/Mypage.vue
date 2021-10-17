<template>
  <div class="flex flex-col h-screen w-auto">
    <Header />
    <div class="flex flex-col justify-center items-center flex-grow bg-blue-100 w-auto pt-10 pb-10">
      <div class="mb-10 text-4xl font-black">마이페이지</div>

      <div class="flex flex-col bg-blue-50 rounded-lg">
        <div class="flex flex-row h-14 border-2 rounded-t-lg">
          <div class="flex justify-center items-center h-full w-44 md:w-56 border-r-2 mr-2">
            맛집
          </div>
          <div
            class="
              hidden
              md:flex
              justify-center
              items-center
              h-full
              w-72
              text-center
              border-r-2
              mr-2
            "
          >
            내용
          </div>
          <div class="flex justify-center items-center h-full w-28 text-center border-r-2 mr-2">
            별점
          </div>
          <div class="flex justify-center items-center h-full w-32 text-center mr-2">등록일</div>
        </div>
        <div v-if="reviewList">
          <div
            class="
              flex flex-row
              h-14
              border-b-2 border-r-2 border-l-2
              cursor-pointer
              hover:bg-indigo-200
            "
            v-for="(review, index) in reviewList"
            :key="index"
            @click="showModal(review)"
          >
            <div
              class="
                flex
                md:hidden
                justify-center
                items-center
                text-center
                h-full
                w-44
                border-r-2
                mr-2
              "
            >
              {{ review.dong }}<br />{{ review.store }}
            </div>
            <div
              class="
                hidden
                md:flex
                justify-center
                items-center
                text-center
                h-full
                w-56
                border-r-2
                mr-2
              "
            >
              {{ review.dong }} {{ review.store }}
            </div>
            <div class="hidden md:flex justify-center items-center p-2 h-full w-72 border-r-2 mr-2">
              <p v-if="review.content.length >= 40">{{ review.content.substring(0, 40) }}...</p>
              <p v-else>{{ review.content }}</p>
            </div>
            <div class="flex justify-center items-center h-full w-28 border-r-2 mr-2">
              <star-rating
                class="flex justify-center items-center h-full"
                :increment="0.5"
                @update:rating="rating = $event"
                :show-rating="false"
                :rating="review.starScore"
                :starSize="20"
                :read-only="true"
              ></star-rating>
            </div>
            <div class="flex justify-center items-center h-full w-32 text-center">
              {{ review.regDate.split('T')[0] }}<br />{{ review.regDate.split('T')[1] }}
            </div>
          </div>
        </div>
        <div
          v-if="!reviewList & (curPage === 1)"
          class="
            flex
            justify-center
            items-center
            text-center
            h-14
            border-b-2 border-r-2 border-l-2
            cursor-pointer
            hover:bg-indigo-200
          "
        >
          작성된 리뷰가 없습니다.
        </div>
      </div>
      <Pagination class="mt-3" :pageCnt="pageCnt" :pageSize="pageSize" @paging="pagingMethod" />
    </div>
    <Footer />
  </div>
  <Modal v-if="isShow" @closeModal="closeModal" @deleteReview="deleteReview" :review="review" />
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import Pagination from '@/components/Pagination.vue';
import Modal from '@/components/Modal.vue';
import StarRating from 'vue-star-rating';
import { mapGetters } from 'vuex';
import { getReviewInMypage, getReviewCnt, delReview } from '@/api/review.js';
import { mapActions } from 'vuex';

export default {
  name: 'MYPAGE',
  components: { Header, Footer, Pagination, Modal, StarRating },
  data() {
    return {
      reviewList: [],
      pageCnt: 5,
      pageSize: 6,
      isShow: false,
    };
  },
  created() {
    this.isShow = false;
    this.pageCnt = 5;
    this.pageSize = 6;
    this.getTotalReviewCnt();
    this.getReview();
  },
  mounted() {},
  methods: {
    ...mapActions(['set_totalReviewCnt', 'set_curPage', 'set_offset', 'toggle_isDel']),
    deleteReview(rid) {
      delReview(
        rid,
        () => {
          this.toggle_isDel(true);
          this.set_totalReviewCnt(this.totalReviewCnt - 1);
          this.getReview();
          alert('리뷰 삭제가 완료되었습니다.');
          this.closeModal();
        },
        () => {
          alert('오류가 발생했습니다.');
        }
      );
    },
    async getReview() {
      let userData = {
        mid: this.id,
        page: this.curPage - 1,
        size: 6,
        sort: 'regDate,desc',
      };

      await getReviewInMypage(
        userData,
        (res) => {
          this.reviewList = res.object.content;

          if (!this.reviewList && this.isDel) {
            this.set_curPage(this.curPage - 1);
            this.getReview();
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    getTotalReviewCnt() {
      getReviewCnt(
        this.id,
        (res) => {
          this.set_totalReviewCnt(res.object);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    showModal(review) {
      this.review = review;
      this.isShow = true;
    },
    closeModal() {
      this.isShow = false;
    },
    pagingMethod(page) {
      this.getReview(page);
    },
  },
  computed: {
    ...mapGetters(['id', 'curPage', 'isDel', 'totalReviewCnt']),
  },

  // 해당 페이지에서 나갈때 값을 초기화하도록 세팅
  // 만약 새로고침 시에는 url이 같으므로 변경 X
  beforeRouteLeave(to, from, next) {
    // just use `this` this.name = to.params.name next()
    if (to.fullPath !== from.fullPath) {
      this.set_curPage(1);
      this.set_offset(0);
    }

    next();
  },
};
</script>
