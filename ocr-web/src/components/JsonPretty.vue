<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner />
    <main class="row-fluid">
      <div class="main-content-container">
        <div class="main-content">
          <div id="resize-container">
            <div id="resize-left">
              <div id="origin-json-container" class="origin-json-container-class">
                <textarea spellcheck="false" id="origin-json-text" v-model="originJsonData" placeholder="请输入json数据..." />
              </div>
            </div>
            <div id="resize-bar"></div>
            <div id="resize-right">
              <div id="pretty-json-options-container" @mouseenter='changeRightOptionState("visible")' @mouseleave='changeRightOptionState("hidden")'>
                <div id="pretty-json-options">
                  <div id="copy-text" class="output-content" v-clipboard:error="copyError" v-clipboard:copy="prettyJsonStr" v-clipboard:success="copyResult">
                    <CircleButton class="common-btn" :btnImgPath="copyImg" titleStr="复制" />
                  </div>

                  <div id="compressImg-text" class="output-content" v-clipboard:error="copyError" v-clipboard:copy="compressJsonStr" v-clipboard:success="copyResult">
                    <CircleButton class="common-btn" :btnImgPath="compressImg" titleStr="压缩后复制" />
                  </div>
                  <CircleButton class="common-btn" :btnImgPath="treeLine" titleStr="显示缩进对齐线" :selected="lineBtnSelected" @click="switchShowLine" />
                  <CircleButton class="common-btn" :btnImgPath="orderedListImg" titleStr="显示行号" :selected="showLineNumber" @click="showLineNumber=!showLineNumber" />
                </div>
              </div>
              <div class="pretty-json-container">
                <vue-json-pretty v-if="!parseError && formatJsonData!=null " :path="'res'" :showLineNumber="showLineNumber" showIcon=true :data='formatJsonData' :showLine='formatJsonConfig.showLine' :showLength='formatJsonConfig.showLength' @click="handleClick"> </vue-json-pretty>
                <div v-if="parseError" style="text-align: left;">
                  <span style="color: #f1592a;font-weight:bold;">{{parseErrorMsg}}</span>
                </div>
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
import CircleButton from "./CircleButton.vue";
// import { jsonlint } from '@/utils/jsonlint'

export default {
  components: {
    VueJsonPretty,
    HeaderBanner,
    FooterBanner,
    CircleButton,
  },
  props: {},
  data() {
    return {
      formatJsonConfig: {
        showLine: true,
        showLength: true,
      },
      delBtnImg: require("@/assets/images/del.svg"),
      compressImg: require("@/assets/images/compress.svg"),
      copyImg: require("@/assets/images/copy.svg"),
      treeLine: require("@/assets/images/treeLine.svg"),
      orderedListImg: require("@/assets/images/ordered-list.svg"),
      // jsonlintIns:require("jsonlint"),
      formatJsonData: null,
      originJsonData: null,
      parseError: false,
      parseErrorMsg: null,
      rightOptionShow: "visible",
      prettyJsonStr: null,
      compressJsonStr: null,
      lineBtnSelected: false,
      showLineNumber: false,
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
      //var resizeBarWidth = 10;
      // var windowWidth = window.outerWidth;
      // var bodyWidth = document.body.clientWidth;
      // console.log("windowWidth:" + windowWidth);
      // console.log("bodyWidth:" + bodyWidth);
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
          resize.style.left = leftWidth - 5 + "px";
          // 设置右边区域的宽度
          right.style.width = container.clientWidth - leftWidth + "px";
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
    inputJson: function (e) {
      console.log(e);
    },
    changeRightOptionState: function (state) {
      this.rightOptionShow = state;
    },
    handleClick: function (e) {
      console.log(e);
    },
    copyResult(e) {
      console.log(e.action);
      this.$message.success("已复制到粘贴板");
    },
    copyError(e) {
      console.log(e.action);
      this.$message.error("复制失败");
    },
    switchShowLine() {
      if (this.formatJsonConfig.showLine == true) {
        this.formatJsonConfig.showLine = false;
        this.lineBtnSelected = false;
        return;
      }
      this.formatJsonConfig.showLine = true;
      this.lineBtnSelected = true;
    },
    // replaceBlank(jsonStr) {
    //   //去掉空格
    //   let str = jsonStr.replace(/\ +/g, "");
    //   //去掉回车换行
    //   str = str.replace(/[\r\n]/g, "");

    //   return str;
    // },
  },
  mounted: function () {
    this.dragAndMove();
  },
  watch: {
    // 监听 originJsonData 属性的变化，此方法名要和属性名一致
    originJsonData(newVal) {
      var jsonData = null;
      // console.log(newVal);
      if (newVal == null || newVal == "") {
        this.parseError = true;
        this.formatJsonData = jsonData;
        this.parseErrorMsg = "";
        return;
      }

      try {
        // jsonlint.parse(newVal);
        jsonData = JSON.parse(newVal);
        // todo 当对象中有NaN、Infinity和-Infinity这三种值的时候 --- 会变成null
        this.prettyJsonStr = JSON.stringify(jsonData, null, 2);
        this.compressJsonStr = JSON.stringify(jsonData);
        // console.log(this.prettyJsonStr);
        // console.log(this.compressJsonStr);
        //this.originJsonData = this.prettyJsonStr;
      } catch (e) {
        console.log(e);
        this.parseError = true;
        this.parseErrorMsg = e.message;
        return;
      }
      this.parseError = false;
      this.formatJsonData = jsonData;
    },
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
.common-btn {
  width: 40px;
  height: 40px;
}

/* -------------- 右栏选项栏 ---------- */
#pretty-json-options-container {
  border-bottom-color: rgb(231, 231, 231);
  border-bottom-style: solid;
  border-bottom-width: 1px;
}

#pretty-json-options {
  display: flex;
  height: 40px;
}

#copy-text {
  width: 40px;
  height: 40px;
}
/*-------------- 右栏选项栏 ---------- */

/* ------------ 左栏 ----------------- */
#origin-json-container {
  height: 100%;
  width: 100%;
}
/* #origin-json-container:focus-within {
  -webkit-transition: border linear 0.2s, -webkit-box-shadow linear 0.5s;
  border-color: rgba(141, 39, 142, 0.75);
  -webkit-box-shadow: 0 0 18px rgba(111, 1, 32, 3);
} */
#resize-left:focus-within {
  /* box-shadow: 2px 2px 5px #000;
  border-color: #246b9b; */
  /* -webkit-box-shadow: inset 0 1px 1px rgb(0 0 0 / 8%), 0 0 8px rgb(102 175 233 / 60%);
  box-shadow: inset 0 1px 1px rgb(0 0 0 / 8%), 0 0 8px rgb(102 175 233 / 60%);
  border: 1px solid #eee; */
  /* -webkit-transition: border linear 0.2s, -webkit-box-shadow linear 0.5s;
  -webkit-box-shadow: 10px 10px 10px 18px rgba(111, 1, 32, 3);
  box-shadow: 10px 10px 10px 5px #888888;
  transition: border linear 0.2s, -webkit-box-shadow linear 0.5s; */
  box-shadow: inset -3px -3px 9px 5px #62bfb8, 0 0 10px 2px #62bfb8;
  border: 1px solid #62bfb8;
  -webkit-box-shadow: inset -3px -3px 9px 5px #62bfb8, 0 0 10px 2px #62bfb8;
  /* box-shadow: inset 0 1px 1px rgb(0 0 0 / 8%), 0 0 8px rgb(102 175 233 / 60%);
  border: 1px solid #eee; */
}

#origin-json-text {
  height: 100%;
  width: 100%;
  border: 0px solid #eee;
  outline: none;
  resize: none;
  display: block;
  box-sizing: border-box;
  padding: 10px 10px 10px 30px;
}
/* ------------ 左栏 ----------------- */

/* ------------ 分栏页配置 -------------*/
#resize-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: nowrap;
  align-items: stretch;
  position: relative;
}
#resize-left {
  width: calc(50%);
  background-color: rgb(255, 255, 255);
  overflow: hidden auto;
  word-break: normal;
  border-style: solid;
  border-width: 1px;
  border-color: rgb(231, 231, 231);
  z-index: 111;
}
#resize-bar {
  width: 10px;
  height: 100%;
  left: 50%;
  position: absolute;
  z-index: 100;
}
#resize-bar:hover {
  background: transparent;
  cursor: ew-resize !important;
}
#resize-right {
  width: calc(50%);
  /* height: 100vh; */
  border-style: solid;
  border-width: 1px;
  border-color: rgb(231, 231, 231);
  background-color: rgb(255, 255, 255);
  overflow: hidden auto;
  word-break: normal;
  position: relative;
}
/* ------------ 分栏页配置 -------------*/
</style>