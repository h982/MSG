<template>
  <div class="flex flex-col h-screen w-auto">
    <Header />
    <div
      class="
        flex flex-col
        justify-center
        items-center
        flex-grow
        bg-blue-100
        w-auto
        pt-10
        pb-10
      "
    >
      <div
        class="
          flex flex-col
          justify-center
          w-2/5
          h-3/5
          bg-blue-50
          border-2 border-blue-500 border-opacity-50
          rounded-lg
          mb-6
        "
      >
        <div class="flex justify-center mt-10">
          <div class="w-1/5 mr-2 text-center">맛집 이름</div>
          <input class="w-3/5 border-2" v-model="name" />
        </div>
        <div class="flex justify-center mt-6">
          <div class="w-1/5 mr-2 text-center">지역</div>
          <input
            class="w-3/5 border-2"
            v-model="area"
            placeholder="ㅇㅇ동, ㅇㅇ점"
          />
        </div>

        <div class="flex justify-center mt-6">
          <button
            class="
              w-1/6
              bg-blue-500
              hover:bg-blue-700
              text-white
              font-bold
              py-2
              px-4
              rounded
            "
            @click="add"
          >
            추가
          </button>
        </div>

        <div class="mt-14 text-center text-gray-400">
          구글지도에 등록되지 않은 맛집은 추가되지 않습니다.
        </div>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import { addNewStore } from "@/api/newstore.js";

export default {
  name: "AddStore",
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      name: "",
      area: "",
    };
  },
  methods: {
    add() {
      // console.log(this.name + ", " + this.area);

      // if (
      //   this.name[this.name.length - 1] == "동" ||
      //   this.area[this.area.length - 1] == "점"
      // ) {
      //   addNewStore(
      //     {
      //       name: this.name,
      //       area: this.area,
      //     },
      //     () => {
      //       // console.log(res);
      //       alert("추가 요청이 완료되었습니다.");
      //     },
      //     () => {
      //       alert("오류가 발생했습니다.");
      //     }
      //   );
      //   this.name = "";
      //   this.area = "";
        
      // } else {
      //   alert("'지역'에 동 / 점을 정확히 기입해주세요.");
      //   return;
      // }

      addNewStore(
          {
            name: this.name,
            area: this.area,
          },
          () => {
            // console.log(res);
            alert("추가 요청이 완료되었습니다.");
          },
          () => {
            alert("오류가 발생했습니다.");
          }
        );
        this.name = "";
        this.area = "";
    },
  },
};
</script>

<style></style>
