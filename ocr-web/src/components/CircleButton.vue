<template>
  <div>
    <div class="circle-btn-container">
      <!-- <button class="circle-btn">

    </button> -->
      <!-- <svg t="1646568210137" class="circle-btn" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6313">
      <path d="M617.92 516.096l272 272-101.824 101.824-272-272-272 272-101.856-101.824 272-272-275.008-275.04L241.056 139.2l275.04 275.04 275.04-275.04 101.824 101.824-275.04 275.04z" p-id="6314"></path>
    </svg> -->
      <div class="circle-btn-bg" :title="titleStr">
        <!--  -->
        <img class="circle-btn" :srcset="innerbtnImgPath" />
      </div>
    </div>

    <!-- 下面两个都能取到值 -->
    <!-- <span>{{innerbtnImgName}}</span>
    <span>{{btnImgName}}</span> -->
  </div>
</template>

<script>
export default {
  name: "CircleButton",
  // 定义 组件对象的属性，并指定类型,可以通过props向组件传递数据 用于从父组件接收数据的数组或对象
  props: {
    // HelloWorld 组件有一个 msg 属性，值类型是String
    // 使用该组件时，{{ msg }} 来取该属性的值
    btnImgPath: String,
    titleStr: String,
  },
  // data是每个组件的内存, 可以定义各种变量，在组件内使用，这是存储数据和希望跟踪的任何其他变量的地方
  data() {
    return {
      innerbtnImgPath: this.btnImgPath, // 把传过来的值赋值给新的变量
    };
  },
  watch: {
    btnImgPath(newVal) {
      this.innerbtnImgPath = newVal; //对父组件传过来的值进行监听，如果改变也对子组件内部的值进行改变
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
  width: 48px;
}
.circle-btn {
  height: 24px;
  width: 24px;
  margin: 4px;
}
.circle-btn-bg {
  border-radius: 100%;
  height: 32px;
  width: 32px;
  margin: 8px;
}
.circle-btn-bg:hover {
  background-color: #f5f5f5;
}
</style>