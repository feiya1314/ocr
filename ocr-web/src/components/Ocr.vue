<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner>
    </HeaderBanner>
    <main class="row-fluid">
      <div class="content-container">
        <div class="left-content-box">
          <div class="top-banner-container">
            <!-- <div class="upload-btn-div">
              <button class="upload-btn">上传图片</button>
            </div> -->
            <!-- 点击 div 触发 input选择文件，使用div触发，代替input，input隐藏掉，只作为选择文件-->
            <div class="upload" @click="onPickFile">
              <CircleButton class="upload-img common-btn" :btnImgPath="uploadImg" titleStr="上传图片" />
              <!-- ref ： 将该 input 注册到 当前 vue 组件的 $ref 对象上，实例名为 fileInput ，其他组件如果使用到的化，可以直接通过 $ref 获取
              本例中，是使用div的点击事件传递到 input 上，用div模拟input选择文件-->
              <input type="file" ref="fileInput" accept="image/*" @change="getUploadFile" style="display: none" />
            </div>
            <div class="translate" @click="startOcr">
              <CircleButton class="right-arrow common-btn" :btnImgPath="ocrImg" titleStr="开始识别" />
            </div>
            <div class="ocr-lang-div-desc" style="left:60px"><span class="ocr-lang-desc">识别语言</span></div>
            <div class="ocr-lang-div-location">
              <div class="ocr-lang-div-container">
                <div class="ocr-lang-div ocr-lang-hover" v-for="(titleLang, langIndex) in titleLangQueue" :key="langIndex" @click="switchLang(langIndex)">
                  <WordButton :class="{'ocr-lang':titleLang.select}" :wordDisplay="titleLang.display" style="min-width: 50px;" />
                </div>
                <div class="ocr-lang-div more-lang" @click='moreLang(1212)' style="display:none">
                  <CircleButton class="common-btn" :btnImgPath="downArrowImg" style="width: 48px;" titleStr="更多语言" />
                </div>
              </div>
            </div>
          </div>
          <div class="input-box">
            <div class="input">
            </div>
            <div v-show="!show" id="pasteInputDivId" class="pasteInputDiv" @paste="handlePaste">
              <input type="text" class="pasteInput" autosize placeholder="请粘贴或者拖拽图片到此处" maxlength="0" />
            </div>
            <div v-if="show" class="pasteImgDiv">
              <div @click="deleteImg">
                <!-- 向组件 CircleButton 的 btnImgName 参数传值 btnImgPath 是传一个静态的值，就是一个字符串，不会从属性中 delBtnImg 找对应的值
                 :btnImgPath 或者 v-bind:btnImgPath 是动态赋值-->
                <CircleButton class="common-btn" :btnImgPath="delBtnImg" titleStr="清除图片" />
                <!-- <CircleButton class="del-preview-btn" :btnImgName="del.svg"/> -->
              </div>
              <div class="pasteImgContainer">
                <img class="pasteImg" v-bind:src="url" @click="zoomInImg" />
              </div>
              <!-- <div v-if="showDelBtn" class="deleteImgContainer" @click="deleteImg">
                <img class="deleteBtn" srcset="@/assets/images/deleteBtn.svg" />
              </div> -->
            </div>
            <div class="preview" v-if="showPreview" @click="zoomOutImg">
              <div class="previewImgDiv">
                <img :src="url" class="previewImg" />
              </div>
            </div>
          </div>
        </div>

        <div class="right-content-box">
          <div class="top-banner-container">
            <div class="result-title">
              <span>
                <p>识别结果</p>
              </span>
            </div>
          </div>
          <div class="output-box">
            <div id="ocr-result-text" class="output-content">
              {{orcResult}}
            </div>
            <div v-if="showCopyBtn" id="copy-text" class="output-content" v-clipboard:error="copyError" v-clipboard:copy="orcResult" v-clipboard:success="copyResult">
              <CircleButton class="copy-btn common-botton-btn" :btnImgPath="copyImg" titleStr="复制内容" />
            </div>
          </div>
        </div>
      </div>
    </main>
    <FooterBanner>

    </FooterBanner>
  </div>
</template>
<script>
import HeaderBanner from "./HeaderBanner.vue";
import FooterBanner from "./FooterBanner.vue";
import CircleButton from "./CircleButton.vue";
import WordButton from "./WordButton.vue";

export default {
  name: "ScreenShot",
  components: {
    //导入组件，这样在当前组件中，使用导入的组件
    HeaderBanner,
    FooterBanner,
    CircleButton,
    WordButton,
  },
  props: {
    // url: {
    //   type: String,
    //   default: "",
    // },

    httpRequest: {
      type: Function,
      default: function () {
        return "null";
      },
    },
  },
  data() {
    return {
      delBtnImg: require("@/assets/images/del.svg"),
      rightArrowImg: require("@/assets/images/rightArrow.svg"),
      uploadImg: require("@/assets/images/upload.svg"),
      ocrImg: require("@/assets/images/ocr.svg"),
      copyImg: require("@/assets/images/copy.svg"),
      downArrowImg: require("@/assets/images/downArrow.svg"),
      upArrowImg: require("@/assets/images/upArrow.svg"),
      show: false,
      showDelBtn: false,
      showPreview: false,
      showCopyBtn: false,
      fileSizeLimit: 2101440,
      titleLangQueue: [
        { display: "中文", code: "CN", select: true },
        { display: "英文", code: "EN", select: false },
        { display: "日语", code: "JP", select: false },
      ],
      url: null,
      srcList: [],
      // 标记当前选中的语言
      selectLangIndex: 0,
      file: null,
      orcResult: "",
      fileType: 0,
    };
  },
  methods: {
    sendImgRequest() {
      var formData = new FormData();

      formData.append("fileType", this.fileType);
      formData.append("file", this.file, this.file.name);
      formData.append("ocrLang", this.titleLangQueue[this.selectLangIndex].code);

      this.$axios({
        headers: {
          "Content-Type": "multipart/form-data",
          // Origin: "http://127.0.0.1:8000",
        },
        method: "post",
        url: "http://127.0.0.1:8000/ocr",
        data: formData,
      }).then((response) => {
        this.orcResult = response.data;
        if (this.orcResult != null && this.orcResult != "") {
          this.showCopyBtn = true;
        }
      });
    },
    startOcr() {
      if (this.file == null || this.url == "") {
        alert("请粘贴或者上传图片");
        return;
      }
      if (this.orcResult != null && this.orcResult != "") {
        console.log("当前图片已识别");
        return;
      }
      this.sendImgRequest();
    },
    switchLang(nextLang) {
      this.titleLangQueue[this.selectLangIndex].select = false;
      this.titleLangQueue[nextLang].select = true;
      this.selectLangIndex = nextLang;
      console.log("curl lang " + this.titleLangQueue[nextLang].code);
    },
    moreLang(morelang) {
      console.log("more lange" + morelang);
    },
    setFile(file) {
      if (!file) {
        this.$message.error("粘贴内容非图片");
        return;
      }
      let fileSize = file.size;

      if (fileSize > this.fileSizeLimit) {
        this.$message.error("图片大小超过 2 M");
        return;
      }
      // 此时file就是我们的剪切板中的图片对象
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = (event) => {
        this.show = true;
        let fileResult = event.target.result;
        this.url = fileResult;
        this.srcList.push(fileResult);
        this.$emit("imgBase64", event.target.result);
      };
      // this.$emit("imgFile", file);
      this.file = file;
    },
    // 获取 fileInput 的节点，然后触发click方法
    onPickFile() {
      this.$refs.fileInput.click();
    },
    // 弹出文件选择框
    getUploadFile(event) {
      let files = event.target.files;
      let filename = files[0].name;
      console.log("upload file name ", filename);
      this.setFile(files[0]);
    },
    handlePaste(event) {
      const items = (event.clipboardData || window.clipboardData).items;
      let file = null;
      if (!items || items.length === 0) {
        this.$message.error("当前浏览器不支持本地");
        return;
      }
      // 搜索剪切板items
      for (let i = 0; i < items.length; i++) {
        if (items[i].type.indexOf("image") !== -1) {
          file = items[i].getAsFile();
          break;
        }
      }
      this.setFile(file);
    },
    deleteImg() {
      this.show = false;
      this.url = "";
      this.file = null;
      this.showDelBtn = false;
      this.showCopyBtn = false;
      this.orcResult = "";
    },
    zoomOutImg() {
      this.showPreview = false;
    },
    zoomInImg() {
      this.showPreview = true;
    },
    copyResult: (e) => {
      console.log(e.action);
      alert("复制成功");
    },
    copyError: (e) => {
      console.log(e.action);
      alert("复制失败");
    },
    onDrag: function (e) {
      e.stopPropagation();
      e.preventDefault();
      console.log("进入");
      //this.$refs.dropbox.style = "border:0.25rem dashed #007bff;";
    },
    onDragLeave: function (e) {
      e.stopPropagation();
      e.preventDefault();
      console.log("离开");
      // this.$refs.dropbox.style = "border:0.25rem dashed #ddd;";
    },
    onDrop: function (e) {
      e.stopPropagation();
      e.preventDefault();
      console.log("松手");

      let dt = e.dataTransfer;
      this.setFile(dt.files[0]);
      // 多个文件时
      // for (var i = 0; i !== dt.files.length; i++) {
      //   this.uploadFile(dt.files[i], url);
      // }
    },
  },
  mounted: function () {
    let dropbox = document.getElementById("pasteInputDivId");
    dropbox.addEventListener("dragenter", this.onDrag, false);
    dropbox.addEventListener("dragover", this.onDrag, false);
    dropbox.addEventListener("dragleave", this.onDragLeave, false);
    dropbox.addEventListener("drop", this.onDrop, false);
  },
};
</script>
<style scoped>
.upload-btn-div {
  width: 100%;
  height: 100%;
  padding-top: 7px;
  padding-left: 10px;
  display: flex;
}
.ocr-lang {
  color: #3c70eb;
  border-bottom: 2px solid #3c70eb;
}
.ocr-lang-div-desc {
  height: 48px;
  position: absolute;
  line-height: 48px;
  min-width: 50px;
}
.ocr-lang-div-container {
  display: inline-flex;
}
.ocr-lang-div-location {
  left: 130px;
  position: absolute;
  height: 48px;
}
.ocr-lang-div {
  display: inline-block;
  min-width: 50px;
  /* height: 100%; */
  text-align: center;
  height: 50px;
}
.ocr-lang-hover:hover {
  background-color: #f5f5f5;
}
.result-title {
  font-size: 16px;
  font-weight: bold;
  color: #333333;
}
.upload-btn {
  width: 80px;
  height: 36px;
  background-color: #4caf50; /* Green */
  border: none;
  color: white;
  font-size: 16px;
  border-radius: 3px;
  -webkit-box-shadow: 2px 2px 3px rgb(93 94 94 / 10%);
  box-shadow: 2px 2px 3px rgb(93 94 94 / 10%);
}
.common-btn {
  position: absolute;
  z-index: 100;
  top: 0;
  right: 0;
}
.common-botton-btn {
  position: absolute;
  z-index: 100;
  bottom: 0;
  right: 10px;
}
.right-arrow {
  right: -24px;
}
.upload-img {
  width: 48px;
  left: 5px;
}
.content-container {
  width: 1280px;
  height: 300px;
  margin: 80px auto 20px auto;
  background: rgb(180, 126, 126);
  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-shadow: 0 2px 5px rgb(0 0 0 / 35%);
  box-shadow: 0 2px 5px rgb(0 0 0 / 35%);
  border-radius: 5px;
  /* box-shadow: 0 1px 3px rgb(18 18 18 / 10%); */
}
.top-banner-container {
  width: 100%;
  height: 50px;
  border-bottom-style: solid;
  border-bottom-width: 1px;
  border-color: #dddddd;
  font-size: 14px;
}
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
.left-content-box {
  padding: 0px;
  height: 100%;
  position: relative;
  background: #ffffff;
  z-index: 10;
  width: 50%;
  float: left;
  /* box-shadow: 0 1px 3px rgb(18 18 18 / 10%); */
  border-radius: 5px 0 0 5px;
}
.input-box {
  position: relative;
  display: block;
  border-right-style: solid;
  border-right-width: 1px;
  border-color: #dddddd;
  height: calc(100% - 52px);
}
.right-content-box {
  padding: 0px;
  position: relative;
  height: 100%;
  display: flex;
  flex-direction: column;
  width: 50%;
  float: left;
  background: #ffffff;
  border-radius: 0 5px 5px 0;
}
#dragEle {
  height: 100%;
  position: absolute;
  width: 10px;
  background: transparent;
  left: 41.66666667%;
  margin-left: -5px;
  z-index: 999;
  top: 0 !important;
  cursor: col-resize !important;
  background: #c0ccda;
}
.deleteImgContainer {
  position: absolute;
  background: #c7312c;
  opacity: 0.8;
  border-end-end-radius: 5px;
  border-end-start-radius: 5px;
  bottom: -1px;
  width: 201px;
  left: 101px;
  transform: translateX(-50%);
}
.pasteInputDiv {
  box-sizing: border-box;
  margin: auto;
  width: 100%;
  height: 100%;
}

.previewImg {
  text-align: center;
  text-align: -webkit-center;
  margin-top: 20px;
}
.previewImgDiv {
  justify-content: center;
  align-items: center;
  display: -webkit-flex;
  height: 100%;
  overflow-y: auto;
}
.deleteBtn {
  width: 25px;
  height: 25px;
  vertical-align: -webkit-baseline-middle;
}
.preview {
  /* overflow: auto; */
  /* background-color: rgba(18,18,18,.65); */
  cursor: zoom-out;
  cursor: -webkit-zoom-out;
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 101;
  overflow: hidden;
  -webkit-transition: background-color 0.2s ease-in-out;
  transition: background-color 0.2s ease-in-out;
  background-color: rgba(18, 18, 18, 0.65);
}
.pasteInput {
  border: 0px dashed #ffffff;
  height: 97%;
  text-align: center;
  line-height: 156px;
  vertical-align: top;
  width: 98%;
  font-size: 19px;
}
.pasteInput:hover {
  cursor: pointer;
  border: 1px dashed #409eff;
}
.pasteImgContainer {
  width: 100%;
  height: 100%;
}
.pasteImgDiv {
  position: relative;
  margin: auto;
  width: 100%;
  height: 100%;
}
.pasteImg {
  position: relative;
  width: auto;
  height: auto;
  max-width: 99%;
  max-height: 99%;
  cursor: zoom-in;
  cursor: -webkit-zoom-in;
  /* border: 1px solid #c0ccda;
  border-radius: 5px; */
}
.close-position {
  position: absolute;
  right: -10px;
  top: -10px;
  font-size: 22px;
  background: #fff;
  border-radius: 50%;
  color: red;
  font-weight: bold;
  z-index: 9999;
}
.close-position:hover {
  cursor: pointer;
}
</style>