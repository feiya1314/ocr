import Vue from 'vue'
import App from './App.vue'

// 引入路由
//import VueRouter from 'vue-router';
// import routes from './router'; 

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
