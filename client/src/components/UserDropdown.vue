<template>
  <div>
    <a id="user_dropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-expanded="false">
      {{username}}
    </a>
    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
      <a id="create_user" class="dropdown-item" href="/#/createUser">Create User</a>
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
      username: JSON.parse(localStorage.auth).username
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
