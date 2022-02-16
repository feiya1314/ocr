<template>
  <div class="root" style="dispaly:flex;">
    <HeaderBanner>

    </HeaderBanner>
    <div class="input">
      <div v-show="!show" class="pasteInputDiv" @paste="handlePaste">
        <input type="text" class="pasteInput" autosize placeholder="请粘贴图片到此处" maxlength="0" />
      </div>
      <div v-if="show" class="pasteImgDiv" @mouseenter="showDeleteBtn" @mouseleave="hidenDelteBtn">
        <div class="pasteImgContainer">
          <img class="pasteImg" v-bind:src="url" @click="zoomInImg" />
        </div>
        <div v-if="showDelBtn" class="deleteImgContainer" @click="deleteImg">
          <img class="deleteBtn" srcset="@/assets/images/deleteBtn.svg" />
        </div>
      </div>
      <div class="preview" v-if="showPreview" @click="zoomOutImg">
        <div class="previewImgDiv">
          <img :src="url" class="previewImg" />
        </div>
      </div>
    </div>
    <div class="output">
      {{orcResult}}
    </div>
  </div>
</template>
<script>
import HeaderBanner from "./HeaderBanner.vue";
export default {
  name: "ScreenShot",
  components: {
    //导入组件，这样在当前组件中，使用导入的组件
    HeaderBanner
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
      show: false,
      showDelBtn: false,
      showPreview: false,
      url: null,
      srcList: [],
      file: null,
      orcResult: "解析结果",
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
          Origin: "http://127.0.0.1:8000",
        },
        method: "post",
        url: "http://127.0.0.1:8000/ocr",
        data: formData,
      }).then((response) => {
        this.orcResult = response.data;
      });
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
      this.sendImgRequest();
    },
    deleteImg() {
      this.show = false;
      this.url = "";
      this.showDelBtn = false;
    },
    showDeleteBtn() {
      if (!this.show || this.url == "") {
        return;
      }
      this.showDelBtn = true;
    },
    hidenDelteBtn() {
      this.showDelBtn = false;
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
.root {
  /* margin-top: 10px; */
  width: 100%;
  height: 100%;
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
  width: 200px;
  height: 200px;
}
.pasteImg {
  width: 200px;
  height: 200px;
  cursor: zoom-in;
  cursor: -webkit-zoom-in;
}
.previewImg {
  text-align: center;
  text-align: -webkit-center;
  margin-top: 20px;
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
  background-color: #fbfdff;
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  height: 200px;
  text-align: center;
  line-height: 156px;
  vertical-align: top;
  width: 200px;
  font-size: 19px;
}
.pasteInput:hover {
  cursor: pointer;
  border: 1px dashed #409eff;
}
.pasteImgDiv {
  position: relative;
  margin: auto;
  width: 200px;
  height: 200px;
}
.pasteImg {
  position: relative;
  width: 200px;
  height: 200px;
  border: 1px solid #c0ccda;
  border-radius: 5px;
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