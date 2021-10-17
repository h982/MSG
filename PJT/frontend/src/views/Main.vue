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
      <div class="h-20 mb-12 mx-auto">
        <img class="h-full w-auto" src="@/images/banner.png" />
      </div>
      <div class="h-16 w-full mb-10">
        <Search-bar class="w-8/12 sm:w-7/12 md:6/12 mx-auto z-10" :searchType="1"/>
      </div>
      <div
        class="
          divide-y-2 divide-indigo-100 divide-solid
          w-8/12
          md:h-80
          bg-blue-50
          h-auto
          z-0
        "
      >
        <div class="flex items-center h-14 md:h-1/5 text-xl ml-7 font-semibold">
          실시간 검색어 순위
        </div>
        <div
          class="
            md:grid md:grid-cols-2 md:divide-x-2 md:divide-indigo-100
            h-4/5
          "
        >
          <div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(0)"
            >
              {{ this.show[0] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(1)"
            >
              {{ this.show[1] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(2)"
            >
              {{ this.show[2] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(3)"
            >
              {{ this.show[3] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(4)"
            >
              {{ this.show[4] }}
            </div>
          </div>
          <div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(5)"
            >
              {{ this.show[5] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(6)"
            >
              {{ this.show[6] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(7)"
            >
              {{ this.show[7] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(8)"
            >
              {{ this.show[8] }}
            </div>
            <div
              class="
                flex
                items-center
                pl-7
                h-12 h-1/12
                md:h-1/5
                font-semibold
                hover:bg-indigo-200
                cursor-pointer
              "
              @click="go(9)"
            >
              {{ this.show[9] }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import SearchBar from "@/components/SearchBar.vue";
import { getRealtime } from "@/api/realtime.js";
import { updateSearch } from "@/api/search.js";
import { mapActions } from "vuex";

export default {
  name: "MAIN",
  components: {
    SearchBar,
    Header,
    Footer,
  },
  data() {
    return {
      top10: [],
      show: [],
    };
  },
  created() {
    getRealtime(
      (res) => {
        this.top10 = [];
        this.show = [];

        var len = res.object.length;

        // 얻어온 데이터는 top10 배열에 push하고 나머지는 -로 채운다.
        for (var i = 0; i < 10; i++) {
          if (i < len) {
            this.top10.push(res.object[i]);
            this.show.push(res.object[i].name + " " + res.object[i].area);
          } else {
            this.top10.push("-");
            this.show.push("-");
          }
        }
      },
      () => {
        alert("오류가 발생했습니다!!!");
      }
    );
  },
  methods: {
    ...mapActions(["set_store"]),
    go(idx) {

      if(this.show[idx] == '-') {
        return;
      }
      
      var item = {
        name: this.top10[idx].name,
        area: this.top10[idx].area,
        address: this.top10[idx].address,
        latitude: this.top10[idx].latitude,
        longitude: this.top10[idx].longitude
      }

      updateSearch(
        item,
        () => {
          this.set_store(this.top10[idx]);
          this.$router.push("Analysis");
        },
        () => {
          alert("언급량 최신화 실패!");
          return;
        }
      );

    },
  },
};
</script>
