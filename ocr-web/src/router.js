import VueRouter from 'vue-router';
import Vue from 'vue';

// const HellowWord = () =>
//     import ( /* webpackChunkName: "group-foo" */ './components/HelloWorld.vue');
const Ocr = () =>
    import ( /* webpackChunkName: "group-foo" */ './components/Ocr.vue');

const routes = [{
        path: '/',
        component: Ocr,
        meta: {
            keep: true
        }
    },
    {
        path: '/ocr',
        component: Ocr,
        meta: {
            keep: true
        }
    }
]

Vue.use(VueRouter);
// 创建路由对象并导出，在main.js文件中导入并使用即可
const router = new VueRouter({
    mode: 'history',
    // base: process.env.BASE_URL,
    routes,
})

export default router;
// export default routes;