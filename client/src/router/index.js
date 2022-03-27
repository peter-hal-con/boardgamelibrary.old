import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/Welcome'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Welcome',
      component: Welcome
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../components/Login.vue')
    },
    {
      path: '/createUser',
      name: 'createUser',
      component: () => import('../components/CreateUser.vue')
    }
  ]
})
