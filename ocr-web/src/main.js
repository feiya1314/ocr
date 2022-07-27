import { createApp } from 'vue'

// import { Message, Tooltip } from 'element-ui';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css';
import App from './App.vue';
import axios from 'axios';
import { createRouter, createWebHashHistory } from 'vue-router'

import Ocr from './components/Ocr.vue'

// 引入路由
// import VueRouter from 'vue-router';
// import router from './router';
import envConf from './config/config'
// import { apply } from 'core-js/fn/reflect';

import { VueClipboard } from '@soerenmartius/vue3-clipboard'

const routes = [
    { path: '/', component: Ocr },
    { path: '/ocr', component: Ocr },
]


// 3. Create the router instance and pass the `routes` option
// You can pass in additional options here, but let's
// keep it simple for now.
const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHashHistory(),
    routes, // short for `routes: routes`
})

// vue3 初始化方式
const app = createApp(App);

app.use(ElementPlus, { size: 'small', zIndex: 3000 });
app.use(VueClipboard);
app.use(router);

app.config.globalProperties.$axios = axios;
app.config.productionTip = false;
app.config.globalProperties.$envConf = envConf;

console.log("base url : " + process.env.OCR_BASE_URL)

app.mount('#app');


//Vue.config.productionTip = false;

// Vue.prototype.$axios = axios //全局注册，使用方法为:this.$axios
//Vue.prototype.$message = Message // 这里如果使用 Vue.use(Message)，加载的时候会自动弹出一个提示框 或者使用Vue.component(Message.name,Message) 也可以
// Vue.prototype.$envConf = envConf
// Vue.component(Tooltip.name, Tooltip)

// Vue.use(VueClipboard)



// vue2初始化方式 创建Vue对象时，传入的对象是选项对象，具体选项有 data、props、el、render等，参考https://cn.vuejs.org/v2/api/#name 选项部分
//new Vue({
// Vue实例挂载的元素节点,是Vue实例的作用范围
// el:'#app',
// render 渲染函数 Vue 选项中的 render 函数若存在，则 Vue 构造函数不会从 template 选项或通过 el 选项指定的挂载元素中提取出的 HTML 模板编译渲染函数。
// 用来替换模板，属于组件的属性
//      router,
//     render: h => h(App),
// router,
// }).$mount('#app') // 如果 Vue 实例在实例化时没有收到 el 选项，则它处于“未挂载”状态，没有关联的 DOM 元素。可以使用 vm.$mount() 手动地挂载一个未挂载的实例。