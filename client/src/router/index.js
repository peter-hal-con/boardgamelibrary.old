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
    },
    {
      path: '/listUsers',
      name: 'listUsers',
      component: () => import('../components/ListUsers.vue')
    },
    {
      path: '/updateUser/:id',
      name: 'updateUser',
      component: () => import('../components/UpdateUser.vue'),
      props: true
    },
    {
      path: '/change-password',
      name: 'change-password',
      component: () => import('../components/ChangePassword.vue')
    },
    {
      path: '/createCopy',
      name: 'createCopy',
      component: () => import('../components/CreateCopy.vue')
    }
  ]
})
