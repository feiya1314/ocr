const HellowWord = () => import( /* webpackChunkName: "group-foo" */ './components/HelloWorld.vue');

const routes = [{
    path: '/',
    component: HellowWord,
    meta: {
        keep: true
    }
}]

export default routes;