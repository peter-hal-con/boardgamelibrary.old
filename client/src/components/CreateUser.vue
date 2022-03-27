<template>
  <form @submit.prevent="create_user">
    <h3>Create User</h3>
    <div class="form-group">
      <label for="username">Email address</label>
      <input id="username" v-model="userDetails.username" type="email" class="form-control form-control-lg" />
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input id="password" v-model="userDetails.password" type="password" class="form-control form-control-lg" />
    </div>
    <button id="submit_create_user" type="submit" class="btn btn-dark btn-lg btn-block">Create User</button>
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
        password: ""
      }
    }
  },
  methods: {
    create_user: function() {
      fetchGraphQL('mutation{userCreate(user:{username:"'+this.userDetails.username+'" password:"'+this.userDetails.password+'" accountLocked:false accountExpired:false passwordExpired:false enabled:true}){id}}')
      .catch(error => {
        console.error(error); // eslint-disable-line no-console
      });
    }
  }
};
</script>
