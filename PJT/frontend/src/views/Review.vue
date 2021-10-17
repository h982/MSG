<template>
  <div class="flex flex-col h-screen w-auto">
    <Header />
    <div class="flex flex-col justify-center items-center flex-grow bg-blue-100 w-auto pt-10 pb-10">
      <!-- 별점 -->
      <div
        class="w-11/12 h-auto bg-white border-2 border-blue-500 border-opacity-50 rounded-lg pt-2"
      >
        <div class="flex justify-center w-full">
          <star-rating
            :increment="0.5"
            @update:rating="rating = $event"
            :show-rating="false"
          ></star-rating>
        </div>
        <div class="flex justify-center w-full mt-2">{{ currentText }}</div>
      </div>
      <!-- 리뷰 작성 -->
      <div
        class="
          flex flex-col
          justify-center
          w-11/12
          h-4/6
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          p-3
          mt-4
        "
      >
        <textarea
          v-model="content"
          class="w-full h-full bg-white border-2 border-blue-800 border-opacity-30 rounded-lg"
          maxlength="500"
          @input="checkByte(this)"
        ></textarea>
        <div class="flex justify-end">
          (<span id="nowByte">{{ wordCnt }}</span
          >/500)
        </div>
      </div>
      <!-- 이미지 업로드 -->
      <div
        class="
          flex
          w-11/12
          h-3/6
          bg-white
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          mt-4
          pt-2
          pl-4
          pb-2
          pr-4
          items-center
        "
      >
        <div class="whitespace-nowrap overflow-x-auto overflow-y-hidden" v-if="uploadImgCnt > 0">
          <div class="ml-4 mr-4 relative inline-block" v-for="(file, index) in files" :key="index">
            <div class="fileDeleteButton flex justify-end" @click="fileDelete(file.number)">
              <i class="fas fa-times-circle red"></i>
            </div>
            <img v-bind:src="file.preview" style="width: 90px; height: 90px" />
          </div>
        </div>
      </div>
      <div class="mt-8">
        <button class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 mr-4 rounded">
          취소
        </button>
        <button class="bg-green-400 hover:bg-green-500 text-white font-bold mr-4 py-2 px-4 rounded">
          <label for="chooseFile">이미지 업로드</label>
          <input
            v-on:change="fileChange($event.target.files)"
            type="file"
            name="file"
            id="chooseFile"
            multiple
            style="display: none"
            ref="fileupload"
          />
        </button>
        <button
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          @click="registReview"
        >
          저장
        </button>
      </div>
    </div>
    <Footer />
  </div>
</template>
<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import StarRating from 'vue-star-rating';
import { addReview } from '@/api/review.js';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'Review',
  components: {
    StarRating,
    Header,
    Footer,
  },
  data() {
    return {
      rating: 0,
      content: '',
      wordCnt: 0,
      files: [],
      filesSize: 0,
      uploadImgCnt: 0,
    };
  },
  computed: {
    ...mapGetters(['id', 'store']),
    currentText() {
      return this.rating;
    },
  },
  methods: {
    ...mapActions(['set_reviewValidation']),
    checkByte(obj) {
      // console.log(obj)
      const maxByte = 500; //최대 500바이트
      const text_val = obj.content; //입력한 문자
      const text_len = text_val.length; //입력한 문자수

      let totalByte = 0;
      for (let i = 0; i < text_len; i++) {
        const each_char = text_val.charAt(i);
        const uni_char = escape(each_char); //유니코드 형식으로 변환
        if (uni_char.length > 4) {
          // 한글 : 2Byte
          totalByte += 2;
        } else {
          // 영문,숫자,특수문자 : 1Byte
          totalByte += 1;
        }
      }

      if (totalByte > maxByte) {
        alert('최대 500자까지만 입력가능합니다.');
        this.content = this.content.substring(0, 250);
        totalByte = 500;
      }
      this.wordCnt = totalByte;
    },
    fileChange(fileList) {
      let num = -1;

      if (fileList.length + this.uploadImgCnt > 5) {
        alert('파일은 5개까지만 업로드할 수 있습니다.');
        return false;
      }

      fileList.forEach((file, index) => {
        // 전체 이미지의 용량이 5MB를 초과한다면
        if (this.filesSize + file.size > 5242880) {
          alert('이미지는 최대 5MB 까지 업로드 가능합니다.\n다시 시도해주세요.');
          return false;
        }

        this.files = [
          ...this.files,
          {
            file: file,
            preview: URL.createObjectURL(file),
            number: index + this.uploadImgCnt,
          },
        ];

        this.filesSize += file.size;
        num = index;
      });

      if (num > -1) {
        this.uploadImgCnt = this.uploadImgCnt + num + 1;
      }

      // 파일업로더 값을 초기화하여 동일 이미지를 올려도 올라갈 수 있게함
      this.$refs.fileupload.value = null;
    },
    fileDelete(val) {
      this.files = this.files.filter((data) => data.number !== Number(val));
      this.uploadImgCnt -= 1;
    },
    registReview() {
      let formData = new FormData();

      formData.append('mid', this.id);
      formData.append('dong', this.store.area);
      formData.append('store', this.store.name);
      formData.append('star_score', this.rating);
      formData.append('content', this.content);
      formData.append('flag', true);
      this.files.forEach((element) => {
        formData.append('multipartFiles', element.file);
      });

      // for (let key of formData.entries()) {
      //   console.log(`${key}`);
      // }

      addReview(
        formData,
        () => {
          alert("저장되었습니다.");
          this.$router.go(-1);
        },
        () => {
          alert('리뷰 등록 오류가 발생했습니다.');
        }
      );
    },
  },
  watch: {},
  beforeRouteLeave(to, from, next) {
    // just use `this` this.name = to.params.name next()
    if (to.fullPath !== from.fullPath) {
      this.set_reviewValidation(false);
    }
    next();
  },
};
</script>

<style></style>
