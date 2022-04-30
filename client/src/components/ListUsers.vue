<template>
  <table id="user_list" class="table">
    <thead>
      <tr>
        <th scope="col">Username</th>
        <th scope="col">ROLE_ADMIN</th>
        <th scope="col">ROLE_COMMITTEE</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(user) in users" :key="user.id" :id="'tr-'+user.username">
        <td>{{user.username}}</td>
        <td>{{(require('jsonpath').query(user.authorities, '$..authority').includes('ROLE_ADMIN')) ? "&#10004;" : " "}}</td>
        <td>{{(require('jsonpath').query(user.authorities, '$..authority').includes('ROLE_COMMITTEE')) ? "&#10004;" : " "}}</td>
      </tr>
    </tbody>
  </table>
</template>
<script>
import {fetchGraphQL} from '../graphql'

export default {
  name: "ListUsers",
  data() {
    return {
      users: []
    }
  },
  mounted() {
    fetchGraphQL('query { userList { id, username, authorities { authority } } }')
    .then(response => this.users = response.data.userList)
  }
}
</script>
