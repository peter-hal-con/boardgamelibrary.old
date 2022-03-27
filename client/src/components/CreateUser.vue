<template>
  <form @submit.prevent="create_user">
    <h3>Create User</h3>
    <div id="username_not_email" v-if="!username_is_email" class="alert alert-danger" role="alert">Username is not an email address.</div>
    <div id="password_mismatch" v-else-if="!passwords_match" class="alert alert-danger" role="alert">Passwords do not match.</div>
    <div id="password_too_short" v-else-if="!password_sufficient_length" class="alert alert-danger" role="alert">Password too short.</div>
    <div class="form-group">
      <label for="username">Email address</label>
      <input id="username" v-model="userDetails.username" type="email" class="form-control form-control-lg" />
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input id="password" v-model="userDetails.password" type="password" class="form-control form-control-lg" autocomplete="new-password" />
    </div>
    <div class="form-group">
      <label for="confirm_password">Confirm Password</label>
      <input id="confirm_password" v-model="userDetails.confirm_password" type="password" class="form-control form-control-lg" autocomplete="new-password" />
    </div>
    <button id="submit_create_user" type="submit" class="btn btn-dark btn-lg btn-block" :disabled="!passwords_match || !password_sufficient_length || !username_is_email">Create User</button>
  </form>
</template>
<script>
import {fetchGraphQL} from '../graphql'

export default {
  name: "CreateUser",
  data() {
    return {
      userDetails: {
        username: "",
        password: "",
        confirm_password: ""
      }
    }
  },
  methods: {
    create_user: function() {
      fetchGraphQL('mutation{userCreate(user:{username:"'+this.userDetails.username+'" password:"'+this.userDetails.password+'" accountLocked:false accountExpired:false passwordExpired:false enabled:true}){id}}')
      .then(response => {
        console.log(response); // eslint-disable-line no-console
        this.userDetails.username = "";
        this.userDetails.password = "";
        this.userDetails.confirm_password = "";
      })
      .catch(error => {
        console.error(error); // eslint-disable-line no-console
      });
    }
  },
  computed: {
    passwords_match: function() {
      return this.userDetails.password === this.userDetails.confirm_password;
    },
    password_sufficient_length: function() {
      return this.userDetails.password.length >= 5;
    },
    username_is_email: function() {
      return String(this.userDetails.username)
        .toLowerCase()
        .match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
    }
  }
};
</script>
