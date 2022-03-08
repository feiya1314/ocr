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
            <div class="upload" @click="uploadImg">
              <CircleButton class="upload-img common-btn" :btnImgPath="uploadImg" titleStr="上传图片" />
            </div>
            <div class="translate" @click="startOcr">
              <CircleButton class="right-arrow common-btn" :btnImgPath="ocrImg" titleStr="开始识别" />
            </div>
          </div>
          <div class="input-box">
            <div class="input">
            </div>
            <div v-show="!show" class="pasteInputDiv" @paste="handlePaste">
              <input type="text" class="pasteInput" autosize placeholder="请粘贴图片到此处" maxlength="0" />
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
            <div class="output-content">
              {{orcResult}}
            </div>
            <div v-if="showCopyBtn" class="output-content" @click="deleteImg">
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

export default {
  name: "ScreenShot",
  components: {
    //导入组件，这样在当前组件中，使用导入的组件
    HeaderBanner,
    FooterBanner,
    CircleButton,
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
      show: false,
      showDelBtn: false,
      showPreview: false,
      showCopyBtn: false,
      url: null,
      srcList: [],
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
      if (!file) {
        this.$message.error("粘贴内容非图片");
        return;
      }
      let fileSize = file.size;
      let fileSizeLimit = 2101440;

      if (fileSize > fileSizeLimit) {
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
      //this.httpRequest(file);
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
  left: 5px;
}
.content-container {
  width: 1100px;
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