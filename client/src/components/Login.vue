<template>
  <form @submit.prevent="login">
    <h3>Sign In</h3>
    <div id="login_failed" v-if="loginFailed" class="alert alert-danger" role="alert">
      Invalid login.
    </div>
    <div class="form-group">
      <label>Email address</label>
      <input id="username" v-model="userDetails.username" type="email" class="form-control form-control-lg" />
    </div>
    <div class="form-group">
      <label>Password</label>
      <input id="password" v-model="userDetails.password" type="password" class="form-control form-control-lg" />
    </div>
    <button id="submit_login" type="submit" class="btn btn-dark btn-lg btn-block">Sign In</button>
  </form>
</template>
<script>
import {bus} from '../main'
import {checkResponseStatus} from '../handlers'

export default {
  name: "Login",
  data() {
    return {
      userDetails: {
        username: "",
        password: ""
      },
      loginFailed: false
    };
  },
  methods: {
    login: function() {
      fetch(`${process.env.VUE_APP_SERVER_URL}/api/login`, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.userDetails)
      }).then(checkResponseStatus).then(response => {
        localStorage.auth = JSON.stringify(response);
        bus.$emit('loginStateChange', true);
        this.$router.push('/');
        this.$forceUpdate();
      }).catch(error => {
        if(error.response.status == 401) {
          this.loginFailed = true
        } else {
          console.error(error); // eslint-disable-line no-console
        }
      });
    }
  }
}
</script>
