<template>
  <!-- <div class="flex w-full h-32 mt-2 justify-center"> -->
  <div class="w-full h-full bg-white rounded-lg pt-2 mr-2" ref="chart">
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
      this.genChart();
    },
    vsWords: function () {
      this.genChart();
    },
  },
  computed: {
    ...mapGetters(["words", "vsWords", "store", "vsStore"]),
  },
  methods: {
    genChart() {
      var list = this.wordsToChart();
      this.data = bb.generate({
        data: {
          x: "x",
          columns: [["x", "매우 긍정", "긍정", "중립", "부정", "매우 부정"], list[0], list[1]],
          type: "line", // for ESM specify as: line()
        },
        axis: {
          x: {
            type: "category",
            tick: {
              multiline: false,
              tooltip: true,
            },
          },
        },
        bindto: "#columnOrientedData",
      });
    },
    wordsToChart() {
      let columns = [];
      let label = { 매우긍정: 0, 긍정: 0, 중립: 0, 부정: 0, 매우부정: 0 };
      for (let i = 0; i < this.words.length; i++) {
        label[this.words[i].sentiment]++;
      }
      let word_columns = [this.store.name];
      word_columns.push(label["매우긍정"]);
      word_columns.push(label["긍정"]);
      word_columns.push(label["중립"]);
      word_columns.push(label["부정"]);
      word_columns.push(label["매우부정"]);

      label = { 매우긍정: 0, 긍정: 0, 중립: 0, 부정: 0, 매우부정: 0 };
      for (let i = 0; i < this.vsWords.length; i++) {
        label[this.vsWords[i].sentiment]++;
      }
      let vsWords_columns = [];
      if (this.vsStore) {
        vsWords_columns.push(this.vsStore.name);
        vsWords_columns.push(label["매우긍정"]);
        vsWords_columns.push(label["긍정"]);
        vsWords_columns.push(label["중립"]);
        vsWords_columns.push(label["부정"]);
        vsWords_columns.push(label["매우부정"]);
      }
      columns.push(word_columns);
      columns.push(vsWords_columns);

      // console.log(columns);
      return columns;
    },
  },
};
</script>

<style></style>
