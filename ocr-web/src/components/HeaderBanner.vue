<template>
  <div class="main-header-box">
    <header class="main-header">
      <div class="header-inner">
        <a :href="homeUrl">
          <img class="logo-img" srcset="@/assets/images/logo.png" />
        </a>
      </div>
    </header>
  </div>
</template>

<script>
export default {
  name: "HeaderBanner",
  // 定义 组件对象的属性，并指定类型,可以通过props向组件传递数据
  // props: {
  //   // HelloWorld 组件有一个 msg 属性，值类型是String
  //   // 使用该组件时，{{ msg }} 来取该属性的值
  //   msg: String,
  // },
  data() {
    return {
      homeUrl: this.$envConf.homeUrl,
    };
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--  "scoped" 表示该组件只适合当前组件 -->
<style scoped>
header {
  display: block;
}
a {
  color: inherit;
  text-decoration: none;
}
.main-header {
  -webkit-box-shadow: 0 1px 3px rgb(18 18 18 / 10%);
  box-shadow: 0 1px 3px rgb(18 18 18 / 10%);
  position: relative;
  /* z-index:90; */
  min-width: 1000px;
  overflow: hidden;
  background: #fff;
  background-clip: content-box;
}
.logo-img {
  max-width: 100px;
  max-height: 40px;
  padding-top: 4px;
  image-rendering: -webkit-optimize-contrast;
}
.header-inner {
  width: auto;
  max-width: 1500px;
  min-width: 1000px;
  padding-left: 16px;
  padding-right: 30px;

  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  width: 1500px;
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
