import Vue from 'vue';
// import ElementUI from 'element-ui'
import App from './App.vue';
import axios from 'axios';

// 引入路由
// import VueRouter from 'vue-router';
import router from './router';
import VueClipboard from 'vue-clipboard2'


Vue.config.productionTip = false
Vue.prototype.$axios = axios //全局注册，使用方法为:this.$axios
Vue.use(VueClipboard)
    // 创建Vue对象时，传入的对象是选项对象，具体选项有 data、props、el、render等，参考https://cn.vuejs.org/v2/api/#name 选项部分
new Vue({
        // Vue实例挂载的元素节点,是Vue实例的作用范围
        // el:'#app',
        // render 渲染函数 Vue 选项中的 render 函数若存在，则 Vue 构造函数不会从 template 选项或通过 el 选项指定的挂载元素中提取出的 HTML 模板编译渲染函数。
        // 用来替换模板，属于组件的属性
        router,
        render: h => h(App),
        // router,
    }).$mount('#app') // 如果 Vue 实例在实例化时没有收到 el 选项，则它处于“未挂载”状态，没有关联的 DOM 元素。可以使用 vm.$mount() 手动地挂载一个未挂载的实例。