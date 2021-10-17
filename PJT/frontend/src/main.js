import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import './assets/tailwind.css';
import VueClickAway from 'vue3-click-away';
import  VueGoogleMaps from '@fawmi/vue-google-maps'

createApp(App).use(store).use(router).use(VueClickAway).use(VueGoogleMaps, {
  load: {
    key: process.env.VUE_APP_GOOGLE_MAP_API_KEY,
    libraries: ["places"]
  }
}).mount('#app');
