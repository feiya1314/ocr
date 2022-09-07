import JsonPretty from '../components/JsonPretty.vue';
import DiffText from '../components/DiffText.vue';


let betaRoutes = [
    { path: '/beta/json', component: JsonPretty },
    { path: '/beta/diff', component: DiffText },
]

export default {
    routes: betaRoutes,
}