import { createRouter, createWebHistory } from 'vue-router';
import Ocr from './components/Ocr.vue';
import NotFound from './components/NotFound.vue';
// import JsonPretty from './components/JsonPretty.vue';
import betaRoutes from './config/betaRoute';

// // const HellowWord = () =>
// //     import ( /* webpackChunkName: "group-foo" */ './components/HelloWorld.vue');
// const Ocr = () =>
//     import ( /* webpackChunkName: "group-foo" */ './components/Ocr.vue');

// const JsonPretty = () =>
//     import ( /* webpackChunkName: "group-foo" */ './components/JsonPretty.vue');


const routes = [
    { path: '/', component: Ocr },
    { path: '/ocr', component: Ocr },
    {
        name: '404',
        path: '/404',
        component: NotFound
    },
    {
        path: "/:pathMatch(.*)", // 此处需特别注意至于最底部
        redirect: '/404'
    }

]

// 添加beta的页面
routes.push(...betaRoutes.routes);

// 3. Create the router instance and pass the `routes` option
// You can pass in additional options here, but let's
// keep it simple for now.
const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes, // short for `routes: routes`
})

// const routes = [{
//         path: '/',
//         component: Ocr,
//         meta: {
//             keep: true
//         }
//     },
//     {
//         path: '/ocr',
//         component: Ocr,
//         meta: {
//             keep: true
//         }
//     },
//     {
//         path: '/json',
//         component: JsonPretty,
//         meta: {
//             keep: true
//         }
//     }
// ]

// Vue.use(VueRouter);
// // 创建路由对象并导出，在main.js文件中导入并使用即可
// const router = new VueRouter({
//     mode: 'history',
//     // base: process.env.BASE_URL,
//     routes,
// })

export default router;
// export default routes;