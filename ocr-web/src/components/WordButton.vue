<template>
  <div>
    <div class="circle-btn-container">
      <!-- <button class="circle-btn">

    </button> -->
      <!-- <svg t="1646568210137" class="circle-btn" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6313">
      <path d="M617.92 516.096l272 272-101.824 101.824-272-272-272 272-101.856-101.824 272-272-275.008-275.04L241.056 139.2l275.04 275.04 275.04-275.04 101.824 101.824-275.04 275.04z" p-id="6314"></path>
    </svg> -->
      <div class="circle-btn-bg">
        <!--  -->
        <!-- <img class="circle-btn"/> -->
        <div class="circle-btn">{{innerWordDisplay}}</div>
      </div>
    </div>

    <!-- 下面两个都能取到值 -->
    <!-- <span>{{innerbtnImgName}}</span>
    <span>{{btnImgName}}</span> -->
  </div>
</template>

<script>
export default {
  name: "WordButton",
  // 定义 组件对象的属性，并指定类型,可以通过props向组件传递数据 用于从父组件接收数据的数组或对象
  props: {
    wordDisplay: String,
    value: String,
  },
  data() {
    return {
      innerWordDisplay: this.wordDisplay, // 把传过来的值赋值给新的变量
      innerValue: this.value,
    };
  },
  watch: {
    // 监听 wordDisplay 字段，父组件值变动后，会触发该
    wordDisplay(newVal) {
      this.innerWordDisplay = newVal; //对父组件传过来的值进行监听，如果改变也对子组件内部的值进行改变
    },
  },
  //   methods: {
  //     changeName() {
  //       this.name = "Lily"; // 这里修改的只是自己内部的值，就不会报错了
  //     },
  //   },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--  "scoped" 表示该组件只适合当前组件 -->
<style scoped>
.circle-btn-container {
  height: 48px;
  cursor: pointer;
  /* width: 48px; */
}

.circle-btn-bg {
  line-height: 48px;
}
.circle-btn {
  padding: 0 10px 0 10px;
}
</style>