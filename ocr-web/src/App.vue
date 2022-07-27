<template>

  <!-- <img alt="Vue logo" src="./assets/logo.png" /> -->
  <!-- 使用HelloWorld组件，并对组件msg属性添加值 -->
  <!-- <HelloWorld msg="Welcome to Your Vue.js App" /> -->
  <router-view></router-view>
</template>

<script>
// import HelloWorld from "./components/HelloWorld.vue";
// import Ocr from './components/Ocr.vue'

// export default 后面的对象 就相当于 new Vue() 构造函数中的接受的对象，它们都是定义组件所需要的数据
// export 用来导出模块，Vue 的单文件组件通常需要导出一个对象
// export default 只能导出一个默认模块，这个模块可以匿名
export default {
  name: "App",
  //components: {
  // 导入组件，这样在当前组件中，使用导入的组件
  //HelloWorld,
  //Ocr
  //},
};
</script>

<style>
html,
body {
  height: 100%;
  background: rgb(247, 247, 247);
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 10px; */
  width: 100%;
  height: 100%;
}
</style>
