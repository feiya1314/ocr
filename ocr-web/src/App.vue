<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" />
    <!-- 使用HelloWorld组件，并对组件msg属性添加值 -->
    <HelloWorld msg="Welcome to Your Vue.js App" />
  </div>
</template>

<script>
import HelloWorld from "./components/HelloWorld.vue";
// import Ocr from './components/Ocr.vue'

export default {
  name: "App",
  components: {
    // 导入组件，这样在当前组件中，使用导入的组件
    HelloWorld
    //Ocr
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
