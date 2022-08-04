<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner />
    <main class="row-fluid">
      <div class="main-content-container">
        <div class="main-content">
          <div id="resize-container">
            <div id="resize-left">{{formatJsonText}}</div>
            <div id="resize-bar"></div>
            <div id="resize-right">
              <div class="pretty-json-container">
                <vue-json-pretty :path="'res'" :data='formatJsonText' :showLine='formatJsonConfig.showLine' :showLength='formatJsonConfig.showLength' @click="handleClick"> </vue-json-pretty>
              </div>
            </div>
          </div>

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
  methods: {
    dragAndMove: function () {
      var resize = document.getElementById("resize-bar");
      var left = document.getElementById("resize-left");
      var right = document.getElementById("resize-right");
      var container = document.getElementById("resize-container");
      var mouseDown = false;
      var borderLimit = 150;
      var windowWidth = window.outerWidth;
      var bodyWidth = document.body.clientWidth;
      console.log("windowWidth:" + windowWidth);
      console.log("bodyWidth:" + bodyWidth);
      resize.onmousedown = function (e) {
        // 记录鼠标按下时的x轴坐标
        var preX = e.clientX;
        resize.left = resize.offsetLeft;
        mouseDown = true;
        //console.log("resize mouse down on resize bar");
        document.onmousemove = function (e) {
          if (mouseDown == false) {
            return false;
          }
          //console.log("document mouse move on resize bar");
          var curX = e.clientX;
          var deltaX = curX - preX;
          var leftWidth = resize.left + deltaX;
          // 左边区域的最小宽度限制为100px
          if (leftWidth < borderLimit) leftWidth = borderLimit;
          // 右边区域最小宽度限制为100px
          if (leftWidth > container.clientWidth - borderLimit)
            leftWidth = container.clientWidth - borderLimit;
          // 设置左边区域的宽度
          left.style.width = leftWidth + "px";
          // 设备分栏竖条的left位置
          resize.style.left = leftWidth;
          // 设置右边区域的宽度
          right.style.width = container.clientWidth - leftWidth - 4 + "px";
        };
        document.onmouseup = function (e) {
          console.log("document mouse move up ");
          document.onmousemove = null;
          document.onmouseup = null;
          e.clientX;
          mouseDown = false;
          resize.releaseCapture && resize.releaseCapture();
        };
        return false;
      };
      resize.onmouseup = function (e) {
        mouseDown = false;
        console.log("resize mouse up");
        e.clientX;
        resize.releaseCapture && resize.releaseCapture();
      };
    },
  },
  mounted: function () {
    this.dragAndMove();
  },
};
</script>
<style scoped>
.root {
  /* margin-top: 10px; */
  width: 100%;
  height: 100%;
}
.main-content-container {
  width: 100%;
  height: 100%;
  margin-top: 5px;
  background: rgb(255, 255, 255);
  display: flex;
}
.main-content {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.pretty-json-container {
  /* height: calc(100% - 155px); */
  padding: 16px;
  box-sizing: border-box;
  overflow: hidden;
}
.row-fluid {
  position: relative;
  display: block;
  height: calc(100% - 155px);
  width: 100%;
  margin: 0 auto;
  background: rgb(247, 247, 247);
}

#resize-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: nowrap;
  align-items: stretch;
}
#resize-left {
  width: calc(50% - 2px);
  background-color: indianred;
  overflow: hidden auto;
  word-break: normal;
}
#resize-bar {
  width: 4px;
  height: 100%;
  cursor: ew-resize;
}
#resize-bar:hover {
  background-color: blue;
  cursor: ew-resize !important;
}
#resize-right {
  width: calc(50% - 2px);
  /* height: 100vh; */
  background-color: green;
  overflow: hidden auto;
  word-break: normal;
}
</style>