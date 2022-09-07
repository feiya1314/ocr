<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner />
    <main class="row-fluid">
      <code-diff class="diff-content" :old-string="'12345'" :new-string="'3456'" syncScroll=true file-name="test.txt" output-format="side-by-side" />
      <circle-button :btnImgPath="copyImg" />
    </main>
    <FooterBanner />
  </div>

</template>

<script>
import { CodeDiff } from "v-code-diff";

import HeaderBanner from "./HeaderBanner.vue";
import FooterBanner from "./FooterBanner.vue";
import CircleButton from "./CircleButton.vue";
// import { jsonlint } from '@/utils/jsonlint'

export default {
  components: {
    CodeDiff,
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
    };
  },
  methods: {
    // dragAndMove: function () {
    //   var resize = document.getElementById("resize-bar");
    //   var left = document.getElementById("resize-left");
    //   var right = document.getElementById("resize-right");
    //   var container = document.getElementById("resize-container");
    //   var mouseDown = false;
    //   var borderLimit = 150;
    //   //var resizeBarWidth = 10;
    //   // var windowWidth = window.outerWidth;
    //   // var bodyWidth = document.body.clientWidth;
    //   // console.log("windowWidth:" + windowWidth);
    //   // console.log("bodyWidth:" + bodyWidth);
    //   resize.onmousedown = function (e) {
    //     // 记录鼠标按下时的x轴坐标
    //     var preX = e.clientX;
    //     resize.left = resize.offsetLeft;
    //     mouseDown = true;
    //     //console.log("resize mouse down on resize bar");
    //     document.onmousemove = function (e) {
    //       if (mouseDown == false) {
    //         return false;
    //       }
    //       //console.log("document mouse move on resize bar");
    //       var curX = e.clientX;
    //       var deltaX = curX - preX;
    //       var leftWidth = resize.left + deltaX;
    //       // 左边区域的最小宽度限制为100px
    //       if (leftWidth < borderLimit) leftWidth = borderLimit;
    //       // 右边区域最小宽度限制为100px
    //       if (leftWidth > container.clientWidth - borderLimit)
    //         leftWidth = container.clientWidth - borderLimit;
    //       // 设置左边区域的宽度
    //       left.style.width = leftWidth + "px";
    //       // 设备分栏竖条的left位置
    //       resize.style.left = leftWidth - 5 + "px";
    //       // 设置右边区域的宽度
    //       right.style.width = container.clientWidth - leftWidth + "px";
    //     };
    //     document.onmouseup = function (e) {
    //       console.log("document mouse move up ");
    //       document.onmousemove = null;
    //       document.onmouseup = null;
    //       e.clientX;
    //       mouseDown = false;
    //       resize.releaseCapture && resize.releaseCapture();
    //     };
    //     return false;
    //   };
    //   resize.onmouseup = function (e) {
    //     mouseDown = false;
    //     console.log("resize mouse up");
    //     e.clientX;
    //     resize.releaseCapture && resize.releaseCapture();
    //   };
    // },
    // inputJson: function (e) {
    //   console.log(e);
    // },
    // changeRightOptionState: function (state) {
    //   this.rightOptionShow = state;
    // },
    // handleClick: function (e) {
    //   console.log(e);
    // },
    // copyResult(e) {
    //   console.log(e.action);
    //   this.$message.success("已复制到粘贴板");
    // },
    // copyError(e) {
    //   console.log(e.action);
    //   this.$message.error("复制失败");
    // },
    // switchShowLine() {
    //   if (this.formatJsonConfig.showLine == true) {
    //     this.formatJsonConfig.showLine = false;
    //     this.lineBtnSelected = false;
    //     return;
    //   }
    //   this.formatJsonConfig.showLine = true;
    //   this.lineBtnSelected = true;
    // },
    // replaceBlank(jsonStr) {
    //   //去掉空格
    //   let str = jsonStr.replace(/\ +/g, "");
    //   //去掉回车换行
    //   str = str.replace(/[\r\n]/g, "");
    //   return str;
    // },
  },
  mounted: function () {
    // this.dragAndMove();
  },
  watch: {
    // 监听 originJsonData 属性的变化，此方法名要和属性名一致
    // originJsonData(newVal) {
    //   var jsonData = null;
    //   // console.log(newVal);
    //   if (newVal == null || newVal == "") {
    //     this.parseError = true;
    //     this.formatJsonData = jsonData;
    //     this.parseErrorMsg = "";
    //     return;
    //   }
    //   try {
    //     // jsonlint.parse(newVal);
    //     jsonData = JSON.parse(newVal);
    //     // todo 当对象中有NaN、Infinity和-Infinity这三种值的时候 --- 会变成null
    //     this.prettyJsonStr = JSON.stringify(jsonData, null, 2);
    //     this.compressJsonStr = JSON.stringify(jsonData);
    //     // console.log(this.prettyJsonStr);
    //     // console.log(this.compressJsonStr);
    //     //this.originJsonData = this.prettyJsonStr;
    //   } catch (e) {
    //     console.log(e);
    //     this.parseError = true;
    //     this.parseErrorMsg = e.message;
    //     return;
    //   }
    //   this.parseError = false;
    //   this.formatJsonData = jsonData;
    // },
  },
};
</script>
<style scoped>
.root {
  /* margin-top: 10px; */
  width: 100%;
  height: 100%;
}
.diff-content {
  width: 100%;
  height: 800px;
}
</style>