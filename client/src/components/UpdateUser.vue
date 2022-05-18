<template>
  <form v-if="ready" @submit.prevent="update_user">
    <h3>Update User {{ id }}</h3>
    <div :id="'message_'+message.type" v-if="message.type" :class="'alert alert-'+message.type" role="alert">{{message.text}}</div>
    <div id="username_not_email" v-if="!username_is_email" class="alert alert-danger" role="alert">Username is not an email address.</div>
    <div id="password_mismatch" v-else-if="!passwords_match" class="alert alert-danger" role="alert">Passwords do not match.</div>
    <div id="password_too_short" v-else-if="!password_sufficient_length" class="alert alert-danger" role="alert">Password too short.</div>
    <div class="form-group">
      <label for="username">Email address</label>
      <input id="username" v-model="newUserDetails.username" type="email" class="form-control form-control-lg" />
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input id="password" v-model="newUserDetails.password" type="password" class="form-control form-control-lg" autocomplete="new-password" />
    </div>
    <div class="form-group">
      <label for="confirm_password">Confirm Password</label>
      <input id="confirm_password" v-model="newUserDetails.confirm_password" type="password" class="form-control form-control-lg" autocomplete="new-password" />
    </div>
    <h3>User Roles:</h3>
    <table class="table">
      <tr>
        <td>ROLE_ADMIN</td>
        <td><input id="authority_ROLE_ADMIN" v-model="newUserDetails.authority_ROLE_ADMIN" type="checkbox" class="form-check-input" /></td>
      </tr>
      <tr>
        <td>ROLE_COMMITTEE</td>
        <td><input id="authority_ROLE_COMMITTEE" v-model="newUserDetails.authority_ROLE_COMMITTEE" type="checkbox" class="form-check-input" /></td>
      </tr>
    </table>
    <button id="submit_update_user" type="submit" class="btn btn-dark btn-lg btn-block" :disabled="!passwords_match || !password_sufficient_length || !username_is_email">Update User</button>
  </form>
</template>
<script>
import {fetchGraphQL} from '../graphql'

export default {
  name: "UpdateUser",
  props: ["id"],
  data() {
    return {
      ready: false,
      message: {
        type: "",
        text: ""
      },
      oldUserDetails: {
        username: "",
        password: "",
        confirm_password: "",
        authority_ROLE_ADMIN:false,
        authority_ROLE_COMMITTEE:false
      },
      newUserDetails: {
        username: "",
        password: "",
        confirm_password: "",
        authority_ROLE_ADMIN:false,
        authority_ROLE_COMMITTEE:false
      }
    }
  },
  mounted() {
    fetchGraphQL('query { user(id: ' + this.id + ') {username, authorities { authority } } }')
    .then(response => {
      this.oldUserDetails.username = response.data.user.username
      this.oldUserDetails.authority_ROLE_ADMIN = false
      this.oldUserDetails.authority_ROLE_COMMITTEE = false
      for(const authority of response.data.user.authorities) {
        if(authority.authority === "ROLE_ADMIN") this.oldUserDetails.authority_ROLE_ADMIN = true
        if(authority.authority === "ROLE_COMMITTEE") this.oldUserDetails.authority_ROLE_COMMITTEE = true
      }
      this.newUserDetails = JSON.parse(JSON.stringify(this.oldUserDetails));
      this.ready = true
    })
  },
  methods: {
    update_user: function() {
      let mutation = 'mutation {\n'
      if(this.newUserDetails.username !== this.oldUserDetails.username || this.newUserDetails.password !== "") {
        mutation += '  userUpdate(id:'+this.id+', user:{'
        if(this.newUserDetails.username !== this.oldUserDetails.username) {
          mutation += ' username:"'+this.newUserDetails.username+'"'
        }
        if(this.newUserDetails.password !== "") {
          mutation += ' password:"'+this.newUserDetails.password+'"'
        }
        mutation += ' }) { errors { message } }\n'
      }
      for(let authority_id = 0; authority_id < this.available_authorities.length; authority_id++) {
        if(this.newUserDetails["authority_"+this.available_authorities[authority_id]] !== this.oldUserDetails["authority_"+this.available_authorities[authority_id]]) {
          if(this.newUserDetails["authority_"+this.available_authorities[authority_id]]) {
            mutation += '  userAuthorityCreate(userAuthority:{ user:{ id:'+this.id+' } authority:{ id:'+(authority_id+1)+' }}) { errors { message } }\n'
          } else {
            mutation += '  userAuthorityDelete(user:'+this.id+' authority:'+(authority_id+1)+') { error }\n'
          }
        }
      }
      mutation += '}'

      fetchGraphQL(mutation).then(this.$router.push('/listUsers')).catch(error => console.error(error)); // eslint-disable-line no-console
    }
  },
  computed: {
    passwords_match: function() {
      return this.newUserDetails.password === this.newUserDetails.confirm_password;
    },
    password_sufficient_length: function() {
      return this.newUserDetails.password.length === 0 || this.newUserDetails.password.length >= 5;
    },
    username_is_email: function() {
      return String(this.newUserDetails.username)
        .toLowerCase()
        .match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
    },
    available_authorities: function() {
      return ["ROLE_ADMIN", "ROLE_COMMITTEE"];
    }
  }
};
</script>
