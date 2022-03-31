<template>
  <form @submit.prevent="create_user">
    <h3>Create User</h3>
    <div :id="'message_'+message.type" v-if="message.type" :class="'alert alert-'+message.type" role="alert">{{message.text}}</div>
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
    <h3>User Roles:</h3>
    <table class="table">
      <tr>
        <td>ROLE_ADMIN</td>
        <td><input id="authority_ROLE_ADMIN" v-model="userDetails.authority_ROLE_ADMIN" type="checkbox" class="form-check-input" /></td>
      </tr>
      <tr>
        <td>ROLE_COMMITTEE</td>
        <td><input id="authority_ROLE_COMMITTEE" v-model="userDetails.authority_ROLE_COMMITTEE" type="checkbox" class="form-check-input" /></td>
      </tr>
    </table>
    <button id="submit_create_user" type="submit" class="btn btn-dark btn-lg btn-block" :disabled="!passwords_match || !password_sufficient_length || !username_is_email">Create User</button>
  </form>
</template>
<script>
import {fetchGraphQL} from '../graphql'

export default {
  name: "CreateUser",
  data() {
    return {
      message: {
        type: "",
        text: ""
      },
      userDetails: {
        username: "",
        password: "",
        confirm_password: "",
        authority_ROLE_ADMIN:false,
        authority_ROLE_COMMITTEE:false
      }
    }
  },
  methods: {
    create_user: function() {
      let userDetails = JSON.parse(JSON.stringify(this.userDetails))
      fetchGraphQL('mutation{userCreate(user:{username:"'+userDetails.username+'" password:"'+userDetails.password+'" accountLocked:false accountExpired:false passwordExpired:false enabled:true}){id}}')
      .then(response => {
        if(response.data.userCreate.id) {
          for(let authority_id = 0; authority_id < this.available_authorities.length; authority_id++) {
            if(userDetails["authority_"+this.available_authorities[authority_id]]) {
              fetchGraphQL('mutation{userAuthorityCreate(userAuthority:{user:{id:'+response.data.userCreate.id+'} authority:{id:'+(authority_id+1)+'}}){errors{message}}}')
              .catch(error => {
                console.error(error); // eslint-disable-line no-console
              });
            }
          }
          this.message.type = "success";
          this.message.text = "Successfully created user: '" + userDetails.username + "'";
          this.userDetails.username = "";
          this.userDetails.password = "";
          this.userDetails.confirm_password = "";
          this.userDetails.authority_ROLE_ADMIN = false;
          this.userDetails.authority_ROLE_COMMITTEE = false;
        } else {
          this.message.type = "danger";
          this.message.text = "Could not create user: '" + userDetails.username + "'. Does that user already exist?";
        }
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
    },
    available_authorities: function() {
      return ["ROLE_ADMIN", "ROLE_COMMITTEE"];
    }
  }
};
</script>
