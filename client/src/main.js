import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './assets/css/bootstrap.css'

import LoginButton from "./components/LoginButton"

Vue.config.productionTip = false

Vue.component("LoginButton", LoginButton)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
