<template>
  <form @submit.prevent="create_copy">
    <h3>Create Library Item</h3>
    <div :id="'message_'+message.type" v-if="message.type" :class="'alert alert-'+message.type" role="alert">{{message.text}}</div>
    <div class="form-group">
      <label for="copy_owner">Owner</label>
      <select class="custom-select" id="copy_owner" v-model="copyOwnerId">
        <option v-for="owner in owners" :key="owner.id" :value="owner.id">{{owner.username}}</option>
      </select>
    </div>
    <div class="form-group">
      <label for="copy_game">Game Title</label>
      <BggAutocomplete id="copy_game" v-model="selectedTitle"/>
    </div>
    <button id="submit_create_copy" type="submit" class="btn btn-dark btn-lg btn-block">Create Library Item</button>
  </form>
</template>
<script>
import BggAutocomplete from './BggAutocomplete.vue'
import {fetchGraphQL} from '../graphql'

export default {
  name: "CreateCopy",
  components: {
    BggAutocomplete
  },
  data() {
    return {
      message: {
        type: "",
        text: ""
      },
      copyDetails: {
        copy_title: "",
      },
      selectedTitle: null,
      owners: [],
      copyOwnerId: null
    }
  },
  created: function () {
    var currentUser = JSON.parse(localStorage.auth)

    if(currentUser.roles.includes('ROLE_ADMIN')) {
      fetchGraphQL('query {userList {id, username}}')
      .then(response => {
        this.owners = response.data.userList
        for(const o of this.owners) {
          if(o.username === currentUser.username) {
            this.copyOwnerId = o.id
          }
        }
      })
    } else {
      fetchGraphQL('query {userByUsername(username:"' + currentUser.username + '") {id}}')
      .then(response => {
        this.owners = [{username:currentUser.username, id:response.data.userByUsername.id}]
        this.copyOwnerId = this.owners[0].id
      })
    }
  },
  methods: {
    create_copy: function() {
      this.message.type = "success"
      this.message.text = "Successfully created a copy of: '" + this.selectedTitle.name + "' belonging to '" + this.copyOwnerId + "'"
    }
  }
};
</script>
