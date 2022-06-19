<template>
  <div>
    <a id="user_dropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-expanded="false">
      {{username}}
    </a>
    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
      <a v-if="is_admin_or_committee" id="create_copy" class="dropdown-item" href="/#/createCopy">Create Library Items</a>
      <div v-if="is_admin_or_committee" class="dropdown-divider"></div>
      <a v-if="is_admin" id="create_user" class="dropdown-item" href="/#/createUser">Create User</a>
      <a v-if="is_admin" id="list_users" class="dropdown-item" href="/#/listUsers">List Users</a>
      <div v-if="is_admin" class="dropdown-divider"></div>
      <a id="password_change" class="dropdown-item" href="/#/change-password/">Change Password</a>
      <a id="logout" v-on:click="logout()" class="dropdown-item" href="#">Logout</a>
    </div>
  </div>
</template>
<script>
import { bus } from '../main'

export default {
  name: "UserDropdown",
  data() {
    return {
      username: JSON.parse(localStorage.auth).username,
      is_admin: JSON.parse(localStorage.auth).roles.includes('ROLE_ADMIN'),
      is_admin_or_committee: JSON.parse(localStorage.auth).roles.includes('ROLE_ADMIN') || JSON.parse(localStorage.auth).roles.includes('ROLE_COMMITTEE')
    }
  },
  methods: {
    logout: function() {
      localStorage.removeItem("auth")
      bus.$emit('loginStateChange', false);
      if(this.$route.path !== '/') this.$router.push('/');
      this.$forceUpdate();
      fetch(`${process.env.VUE_APP_SERVER_URL}/logoff`, {
        method: 'POST',
        redirect: 'manual'
      }).catch(error => {
        console.error(error); // eslint-disable-line no-console
      });
    }
  }
}
</script>
