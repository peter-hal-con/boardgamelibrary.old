import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './assets/css/bootstrap.css'

import LoginButton from "./components/LoginButton"
import UserDropdownLoginButtonToggle from "./components/UserDropdownLoginButtonToggle"
import UserDropdown from "./components/UserDropdown"

Vue.config.productionTip = false

Vue.component("LoginButton", LoginButton)
Vue.component("UserDropdownLoginButtonToggle", UserDropdownLoginButtonToggle)
Vue.component("UserDropdown", UserDropdown)

export const bus = new Vue();

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
