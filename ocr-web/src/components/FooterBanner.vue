<template>

  <footer class="main-footer">
    <div class="footer-inner">
      <div class="footer-inner-container">
        <div class="footer-ad-container">
          <div class="footer-ad-img">

          </div>
        </div>
        <div class="footer-nav-list">

          <div class="title">友情链接：</div>
          <dd>
            <a href="https://www.easyocr.cn" target="_blank">在线图片文字识别</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="https://www.easyocr.cn/beta/json" target="_blank">JSON格式化</a>
          </dd>
        </div>
        <div class="footer-gav-list">
          <div class="footer-gav-link">
            <a href="">隐私保护</a>
            <a href="">权利保护</a>
            <a href="" class="footer-gg-b-email">联系我们</a>
            <a href="" class="footer-gg-b-feedback">问题反馈</a>
          </div>
          <p class="footer-gg-b-copy">
            <a href="https://beian.miit.gov.cn" target="_blank">豫ICP备2022010979号-1</a> &nbsp;&nbsp;&nbsp;&nbsp;

            <img src="@/assets/images/beian.png" style="width:18px;height:18px;vertical-align: middle;" />
            <a target="_blank" rel="nofollow noreferrer" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030402005563">
              粤公网安备 44030402005563号 
            </a>
            &nbsp;&nbsp;&nbsp;&nbsp;©2021 - 2022 EASYOCR.CN ALL RIGHT RESERVED
          </p>
        </div>
      </div>
    </div>
  </footer>

</template>

<script>
export default {
  name: "FooterBanner",
  // 定义 组件对象的属性，并指定类型,可以通过props向组件传递数据
  // props: {
  //   // HelloWorld 组件有一个 msg 属性，值类型是String
  //   // 使用该组件时，{{ msg }} 来取该属性的值
  //   msg: String,
  // },
  data() {
    return {
      msg: "这里是Hello页面",
    };
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--  "scoped" 表示该组件只适合当前组件 -->
<style scoped>
footer {
  margin-top: 2px;
  /* margin-top: 1vh; */
  font-size: calc(2px + 1vh);
  display: block;
}
a {
  color: inherit;
  text-decoration: none;
}
.main-footer {
  -webkit-box-shadow: 0 1px 3px rgb(18 18 18 / 10%);
  box-shadow: 0 1px 3px rgb(18 18 18 / 10%);
  position: relative;
  z-index: 11;
  min-width: 1000px;
  overflow: hidden;
  background: rgb(247, 247, 247);
  background-clip: content-box;
  border-top: 1px solid rgba(235, 235, 235, 0.904);
}

.footer-inner {
  width: auto;
  max-width: 1200px;
  min-width: 1100px;
  padding-left: 16px;
  padding-right: 30px;

  /* position: relative; */
  /* display: -webkit-box;
  display: -ms-flexbox;
  display: flex; */
  width: 1100px;
  /* height: 52px; */
  padding: 0 16px;
  margin: 0 auto;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-transition: -webkit-transform 0.3s;
  transition: -webkit-transform 0.3s;
  transition: transform 0.3s;
  transition: transform 0.3s, -webkit-transform 0.3s;
}
.footer-inner-container {
  position: relative;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  padding: 0px 0 15px 0;
}
.footer-ad-container {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.footer-ad-img {
  width: 1100px;
  height: 90px;
  /* background: #999999; */
  margin-top: 5px;
}
.footer-nav-list {
  margin-top: 10px !important;
  text-align: center;
  width: 100%;
  margin: 0 auto;
}
.footer-nav-list .title {
  display: inline-block;
  vertical-align: middle;
  margin: 0 10px 0 0;
  font-size: 16px;
  font-weight: bold;
}
.footer-nav-list dd {
  display: inline-block;
  vertical-align: middle;
  margin-right: 0px;
  color: #666;
  margin-left: 0;
  line-height: 1.53846154;
  margin-inline-start: 20px;
}

.footer-nav-list dd a:hover {
  color: #0fd59d;
}
.footer-nav-list dd a {
  color: #666;
  -webkit-transition: all 0.2s ease;
  -moz-transition: all 0.2s ease;
  -ms-transition: all 0.2s ease;
  -o-transition: all 0.2s ease;
  transition: all 0.2s ease;
  display: inline-block;
  text-decoration: none;
  cursor: pointer;
  font-family: "微软雅黑", monospace, Tahoma;
  font-size: 14px;
}
.footer-gav-list {
  flex: 1;
  padding: 20px;
}
.footer-gav-link {
  text-align: center;
}
.footer-gav-link a {
  position: relative;
  font-size: 12px;
  margin: 0 15px;
  color: #999999;
  /* border-right: 1px solid #ccc; */
}
.footer-gg-b-copy {
  font-size: 12px;
  color: #999999;
  margin: 15px;
  padding: 0;
  text-align: center;
}
.footer-gg-b-copy a {
  display: inline-block;
  color: #0fd59d;
  cursor: pointer;
  font-family: "微软雅黑", monospace, Tahoma;
  font-size: 14px;
}
</style>
