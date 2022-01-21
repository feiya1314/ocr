<template>
  <div class="ScreenShot">
    <div v-show="!show" class="pasteInputDiv" @paste="handlePaste">
      <input type="text" class="pasteInput" autosize placeholder="请粘贴图片到此处" maxlength="0" />
    </div>
    <div v-if="show" class="pasteImgDiv" @mouseenter="showDeleteBtn" @mouseleave="hidenDelteBtn">
      <!-- <i class="el-icon-error close-position" @click="deleteImg" /> -->
      <!-- <el-image
        class="pasteImg"
        :src="url"
        fit="fill"
        :preview-src-list="srcList"
        :z-index="99999"
      >
      </el-image> -->
      <img class="pasteImg" v-bind:src="url" />
      <img v-if="showDelBtn" class="deleteBtn" srcset="@/assets/images/deleteBtn.svg" @click="deleteImg" />
    </div>
  </div>
</template>
<script>
export default {
  name: "ScreenShot",
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
      url: null,
      srcList: [],
      file: null,
    };
  },
  watch: {
    url() {
      if (this.url === "") {
        this.show = false;
        this.srcList = [];
      } else {
        this.show = true;
        this.srcList.push(this.url);
      }
    },
  },
  methods: {
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
      // 此时file就是我们的剪切板中的图片对象
      const reader = new FileReader();
      reader.onload = (event) => {
        this.show = true;
        let fileResult = event.target.result;
        this.url = fileResult;
        this.srcList.push(fileResult);
        this.$emit("imgBase64", event.target.result);
      };
      reader.readAsDataURL(file);
      this.$emit("imgFile", file);
      this.file = file;
      this.httpRequest(file);
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
      // todo set to false
      this.showDelBtn = true;
    },
  },
};
</script>
<style scoped>
.ScreenShot {
  margin-top: 10px;
  width: 100%;
  height: 100%;
}
.pasteInputDiv {
  box-sizing: border-box;
  margin: auto;
  width: 200px;
  height: 200px;
}
.pasteImg{
  width: 200px;
  height: 200px;
}
.deleteBtn{
  width: 30px;
  height: 30px;
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