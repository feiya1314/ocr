<template>

</template>


<script>
export default {
    // todo 普通提示框、loading框，支持显式 隐藏点击确定按钮
  name: "Tips",
  // 定义 组件对象的属性，并指定类型,可以通过props向组件传递数据 用于从父组件接收数据的数组或对象
  props: {
    // HelloWorld 组件有一个 msg 属性，值类型是String
    // 使用该组件时，{{ msg }} 来取该属性的值
    msg: String,
  },
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
};
</script>

<style>
</style>