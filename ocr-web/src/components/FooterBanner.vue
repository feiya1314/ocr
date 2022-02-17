<template>
  <div class="main-footer-box">
    <footer class="main-footer">
      <div class="footer-inner">
        <div class="footer-gg-b-link">
          <a href="http://www.json.cn/privacy.html">隐私保护</a>
          <a href="http://www.json.cn/right.html">权利保护</a>
          <a href="http://www.json.cn/contact.html" class="footer-gg-b-email">联系我们</a>
          <a href="http://www.json.cn/feedback.html" class="footer-gg-b-feedback">问题反馈</a>
        </div>
      </div>
    </footer>
  </div>
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
  margin-top: 1vh;
  font-size: calc(2px + 1vh);
}
a {
  color: inherit;
  text-decoration: none;
}
.main-footer {
  -webkit-box-shadow: 0 1px 3px rgb(18 18 18 / 10%);
  box-shadow: 0 1px 3px rgb(18 18 18 / 10%);
  position: relative;
  z-index: 100;
  min-width: 1000px;
  overflow: hidden;
  background: #fff;
  background-clip: content-box;
}

.footer-inner {
  width: auto;
  max-width: 1156px;
  min-width: 1000px;
  padding-left: 16px;
  padding-right: 30px;

  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  width: 1000px;
  height: 52px;
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
</style>
