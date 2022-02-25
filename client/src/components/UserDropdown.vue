<template>
  <div>
    <a id="user_dropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-expanded="false">
      {{username}}
    </a>
    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
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
      this.$router.push('/');
      this.$forceUpdate();
    }
  }
}
</script>
