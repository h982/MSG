<template>
  <div class="w-11/12">
    <div class="flex w-full mt-2 mb-2 justify-start">
      <div
        id="googleMap"
        class="
          w-3/4
          h-80
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          mr-2
          flex
          justify-center
        "
        ref="inbox"
      >
      
      <GMapMap
        style="width: 100%; height: 100%;"
        :center="{
          lat : store.latitude,
          lng : store.longitude
        }"
        :zoom="18"
      >

      <GMapMarker
        :position="{
          lat : store.latitude,
          lng : store.longitude
        }"
        :icon = "{ url : require('@/images/marker.png')}"
        ref="deal"/> 

      </GMapMap>
      </div>
      <div
        class="
          w-2/5
          h-80
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          pt-2
          flex
          justify-center
        "
      >
      <div flex items-stretch>
      <div 
        class="
          py-4
          mt-2
          items-center
          rounded-lg
          text-center
          text-3xl
          font-bold
          text-blue-400"
      >{{store.name}}
      </div>
      <div class="flex w-auto">
      <star-rating
            :rating="rate"
            :read-only="true"
            :increment="0.1"
            :show-rating="false"
      ></star-rating>
      </div>
      <div class="mt-4 text-center">{{store.address}}</div>
      <div class="mt-2 text-center">{{store.area}}</div>
      <div class="py-4 text-center">
      <button class="bg-white text-center bg-blue-200 hover:bg-blue-500 font-bold rounded py-2 px-4 mr-2" @click="goReview">
          리뷰 등록
        </button>
      </div>
      </div>  
      </div>
    </div>
    <!-- 차트 -->
    <div
      class="
        w-full
        h-64
        bg-white
        border-2 border-blue-500 border-opacity-50
        rounded-lg
        flex
        justify-center
        inline-block
        align-middle
      "
    >
      
      <div class="flex flex-col w-full h-full rounded-lg overflow-auto mb-2">
        <div class="flex flex-row h-14 w-full border-2 rounded-t-lg">
          <div
            class="
              hidden
              md:flex
              justify-center
              items-center
              h-full
              w-8/12
              text-center
              border-r-2
              mr-2
            "
          >
            내용
          </div>
          <div class="flex justify-center items-center h-full w-2/12 text-center border-r-2 mr-2">
            별점
          </div>
          <div class="flex justify-center items-center h-full w-2/12 text-center mr-2">등록일</div>
        </div>


        <div v-if="reviewList">
          <div
            class="
              flex
              h-14
              w-full
              border-b-2 border-r-2 border-l-2
              cursor-pointer
              hover:bg-indigo-200
            "
            v-for="(review, index) in reviewList"
            :key="index"
            @click="showModal(review)"
          >
            <div class="hidden md:flex justify-center items-center text-center h-full w-8/12 border-r-2 mr-2">
              <p v-if="review.content.length >= 40">{{ review.content.substring(0, 40) }}...</p>
              <p v-else>{{ review.content }}</p>
            </div>
            <div class="flex justify-center items-center h-full w-2/12 border-r-2 mr-2">
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
            <div class="flex justify-center items-center h-full w-2/12 text-center mr-2">
              {{ review.regDate.split('T')[0] }}<br />{{ review.regDate.split('T')[1] }}
            </div>
          </div>
        </div>
        <div
          v-if="reviewList.length == 0"
          class="
            flex
            justify-center
            items-center
            text-center
            h-14
            border-b-2 border-r-2 border-l-2
            cursor-pointer
            hover:bg-blue-50
          "
        >
          작성된 리뷰가 없습니다.
        </div>
      <Modal v-if="isShow" @closeModal="closeModal" @deleteReview="deleteReview" :review="review" />
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters , mapActions} from "vuex";
import StarRating from 'vue-star-rating';
import { getReviewStore , getGoogleReviewScore, delReview} from '@/api/review.js';
import Modal from '@/components/Modal.vue';

export default {
  name: "info",
  components:{
    StarRating,
    Modal
  },
  data(){
    return {
      rate: 0,
      reviewList: [],
      isShow: false,
      center:{lat:0,lng:0},
    }
  },
  created() {
    //this.store = this.$store.state.store;
    this.getScore();
    this.getReview();
    this.isShow = false;
  },
  computed : {
    ...mapGetters(['store']),
  },
  methods: {
    ...mapActions(['set_totalReviewCnt','set_reviewValidation', 'toggle_isDel']),
    goReview(){
      this.set_reviewValidation(true);
      this.$router.push("Review");
    },
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
    async getScore(){
      await getGoogleReviewScore(
        this.store.area + this.store.name,
        (res) => {
          if(res.object == null){
            this.rate = res.object;
          } else {
            this.rate = res.object;
          }
        },
        (err) => {
          console.log(err);
        }
      );
    },

    async getReview() {

      let param = {
        dong : this.store.area,
        storeName : this.store.name
      };

      await getReviewStore(
        param,
        (res) => {
          this.reviewList = res.object;
          // console.log(this.reviewList)
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
    }
  },
};
</script>
<style>
  
</style>