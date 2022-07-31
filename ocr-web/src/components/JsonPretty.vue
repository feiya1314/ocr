<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner />
    <main class="row-fluid">
      <div>
        <vue-json-pretty :path="'res'" :data="{ key: 'value' }" @click="handleClick"> </vue-json-pretty>
      </div>
      <FooterBanner />
    </main>
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
};
</script>
<style scoped>
.root {
  /* margin-top: 10px; */
  width: 100%;
  height: 100%;
}
.row-fluid {
  position: relative;
  display: block;
  height: calc(100% - 230px);
  margin: 0 auto;
  background: rgb(247, 247, 247);
}
</style>