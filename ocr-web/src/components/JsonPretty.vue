<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner />
    <main class="row-fluid">
      <div class="main-content">
        <div class="origin-json-container">

        </div>
        <div class="pretty-json-container">
          <vue-json-pretty :path="'res'" :data='formatJsonText' :showLine='formatJsonConfig.showLine' :showLength='formatJsonConfig.showLength' @click="handleClick"> </vue-json-pretty>
        </div>
      </div>
    </main>
    <FooterBanner />
  </div>

</template>

<script>
import VueJsonPretty from "vue-json-pretty";
import "vue-json-pretty/lib/styles.css";

import HeaderBanner from "./HeaderBanner.vue";
import FooterBanner from "./FooterBanner.vue";
// import CircleButton from "./CircleButton.vue";

export default {
  components: {
    VueJsonPretty,
    HeaderBanner,
    FooterBanner,
    // CircleButton,
  },
  props: {},
  data() {
    return {
      formatJsonConfig: {
        showLine: true,
        showLength: true,
      },
      formatJsonText: {
        status: 200,
        error: "",
        data: [
          {
            news_id: 51184,
            title:
              "iPhone X Review: Innovative future with real black technology",
            source: "Netease phone",
          },
          {
            news_id: 51183,
            title:
              "Traffic paradise: How to design streets for people and unmanned vehicles in the future?",
            source: "Netease smart",
            link: "http://netease.smart/traffic-paradise/1235",
            author: { names: ["Daniel", "Mike", "John"] },
          },
          {
            news_id: 51182,
            title:
              "Teslamask's American Business Relations: The government does not pay billions to build factories",
            source: "AI Finance",
            members: ["Daniel", "Mike", "John"],
          },
        ],
      },
    };
  },
  methods: {},
  mounted: function () {},
};
</script>
<style scoped>
.root {
  /* margin-top: 10px; */
  width: 100%;
  height: 100%;
}
.main-content {
  width: 100%;
  height: 100%;
  display: flex;
}
.origin-json-container {
  /* width: 100%; */
  min-width: 200px;
  height: 100%;
}
.pretty-json-container {
  height: 100%;
}
.row-fluid {
  position: relative;
  display: block;
  height: calc(100% - 150px);
  margin: 0 auto;
  background: rgb(247, 247, 247);
}
</style>