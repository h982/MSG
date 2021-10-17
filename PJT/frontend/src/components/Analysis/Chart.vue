<template>
  <!-- <div class="flex w-full h-32 mt-2 justify-center"> -->
  <div class="w-full h-64 bg-white rounded-lg pt-2 mr-2 align-middle" ref="chart">
    <div id="columnOrientedData" class="flex flex-wrap content-center w-11/12 h-full"></div>
  </div>
  <!-- </div> -->
</template>

<script>
import bb, { line, pie } from "billboard.js";
import "billboard.js/dist/theme/insight.css";
import { mapGetters } from "vuex";

export default {
  name: "Chart",
  data() {
    return {
      data: undefined,
      types: { line: line(), pie: pie() },
    };
  },
  mounted() {
    this.genChart();
  },
  watch: {
    words: function () {
      // console.log(this.words);
      this.genChart();
    },
  },
  computed: {
    ...mapGetters(["words"]),
  },
  methods: {
    genChart() {
      this.data = bb.generate({
        data: {
          columns: this.wordsToChart(),
          type: "pie", // for ESM specify as: line()
        },
        bindto: "#columnOrientedData",
      });
    },
    wordsToChart() {
      let label = { 매우긍정: 0, 긍정: 0, 중립: 0, 부정: 0, 매우부정: 0 };

      for (let i = 0; i < this.words.length; i++) {
        // console.log(this.words[i].sentiment);
        label[this.words[i].sentiment]++;
      }

      var columns = [];
      columns.push(["매우긍정", label["매우긍정"]]);
      columns.push(["긍정", label["긍정"]]);
      columns.push(["중립", label["중립"]]);
      columns.push(["부정", label["부정"]]);
      columns.push(["매우부정", label["매우부정"]]);

      // console.log(columns);
      return columns;
    },
  },
};
</script>

<style></style>
