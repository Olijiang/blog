import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from "./router";
import store from './store';
import '@/assets/css/hight.css'
import '@/assets/css/markdown.css'

const app = createApp(App)


app.config.globalProperties.baseUrl = (import.meta.env.MODE == 'development') ? window.developmentUrl : window.productionUrl


app.use(router)
    .use(store)
    .mount('#app')

