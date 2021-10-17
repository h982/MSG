<template>
  <div
    id="wordcloud2"
    class="
      w-3/4
      h-80
      bg-white
      border-2 border-blue-500 border-opacity-50
      rounded-lg
      pt-2
      mr-2
      flex
      justify-center
      overflow-hidden
    "
    ref="inbox"
  >
    <div id="word-cloud2"></div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "Wordcloud2",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["vsWords"]),
  },
  mounted() {
    if (this.vsWords.length != 0) {
      this.genLayout();
    }
  },
  watch: {
    vsWords: function () {
      // console.log(this.vsWords);
      this.genLayout();
    },
  },
  methods: {
    genLayout() {
      var d3 = require("d3"),
        cloud = require("d3-cloud");
      // console.log(this.vsWords);
      var width = parseInt(this.$refs.inbox.clientWidth);
      var height = parseInt(this.$refs.inbox.clientHeight);
      cloud()
        .size([width, height])
        .words(
          this.vsWords.map(function (d) {
            return { text: d.keyword, size: d.count + 20, sentiment: d.sentiment };
          })
        )
        .padding(5)
        .rotate(function () {
          return 0;
        })
        .font("Impact")
        .fontSize(function (d) {
          return d.size;
        })
        .on("end", draw)
        .spiral("archimedean")
        .start();

      function draw(words) {
        d3.select("#word-cloud2").select("svg").remove();

        d3.select("#word-cloud2")
          .append("svg")
          .attr("width", width)
          .attr("height", height)
          .append("g")
          .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
          .selectAll("text")
          .data(words)
          .enter()
          .append("text")
          .style("font-family", "overwatch")
          .style("fill-opacity", 1)
          .style("font-size", function (d) {
            return d.size + "px";
          })
          .attr("transform", (d) => {
            return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
          })
          .style("fill", function (d) {
            if (d.sentiment == "매우긍정") {
              return "#0100FF";
            } else if (d.sentiment == "긍정") {
              return "#00D8FF";
            } else if (d.sentiment == "중립") {
              return "#FFBB00";
            } else if (d.sentiment == "부정") {
              return "#FF5E00";
            } else if (d.sentiment == "매우부정") {
              return "#FF0000";
            }
          })
          .attr("text-anchor", "middle")
          .attr("transform", function (d) {
            return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
          })
          .text(function (d) {
            return d.text;
          });
      }
    },
  },
};
</script>

<style></style>
