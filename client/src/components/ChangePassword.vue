<template>
  <form @submit.prevent="change_password">
    <h3>Change Password</h3>
    <div id="password_mismatch" v-if="!passwords_match" class="alert alert-danger" role="alert">Passwords do not match.</div>
    <div id="password_too_short" v-else-if="!password_sufficient_length" class="alert alert-danger" role="alert">Password too short.</div>
    <div class="form-group">
      <label>New password</label>
      <input id="new_password" v-model="new_password" type="password" class="form-control form-control-lg" />
    </div>
    <div class="form-group">
      <label>Confirm new password</label>
      <input id="confirm_new_password" v-model="confirm_new_password" type="password" class="form-control form-control-lg" />
    </div>
    <button id="submit_change_password" type="submit" class="btn btn-dark btn-lg btn-block" :disabled="!passwords_match || !password_sufficient_length">Change My Password</button>
  </form>
</template>
<script>
import {fetchGraphQL} from '../graphql'

export default {
  name: "ChangePassword",
  data() {
    return {
      new_password: "",
      confirm_new_password: ""
    };
  },
  methods: {
    change_password: function() {
      if(this.passwords_match && this.password_sufficient_length) {
        var auth = JSON.parse(localStorage.auth)
        var new_password = this.new_password
        fetchGraphQL('query{userByUsername(username:"' + auth.username + '"){id}}').then(response => {
          fetchGraphQL('mutation{userUpdate(id:' + response.data.userByUsername.id + ' user:{password:"' + new_password + '"}){id}}')
        }).then(this.$router.push('/')).catch(error => {
          console.error(error); // eslint-disable-line no-console
        });
      }
    }
  },
  computed: {
    passwords_match: function() {
      return this.new_password === this.confirm_new_password;
    },
    password_sufficient_length: function() {
      return this.new_password.length >= 5;
    }
  }
}
</script>