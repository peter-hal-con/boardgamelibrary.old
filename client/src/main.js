import Vue from 'vue'
import { ApolloClient } from 'apollo-client'
import { createHttpLink } from 'apollo-link-http'
import { InMemoryCache } from 'apollo-cache-inmemory'
import VueApollo from 'vue-apollo'

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

// HTTP connection to the API
const httpLink = createHttpLink({
  // You should use an absolute URL here
  uri: 'http://localhost:8080/graphql',
})

// Cache implementation
const cache = new InMemoryCache()

// Create the apollo client
const apolloClient = new ApolloClient({
  link: httpLink,
  cache,
})

Vue.use(VueApollo);

const apolloProvider = new VueApollo({
  defaultClient: apolloClient,
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  // inject apolloProvider here like vue-router or vuex
  apolloProvider,
})
