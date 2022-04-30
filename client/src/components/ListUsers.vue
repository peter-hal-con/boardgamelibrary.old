<template>
  <table id="user_list" class="table">
    <thead>
      <tr>
        <th scope="col">Username</th>
        <th scope="col" v-for="(authority) in authorities" :key="authority">{{authority}}</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(user) in users" :key="user.id" :id="'tr-'+user.username">
        <td id="username">{{user.username}}</td>
        <td v-for="(authority) in authorities" :key="authority" :id="authority">{{(require('jsonpath').query(user.authorities, '$..authority').includes(authority)) ? "&#10004;" : " "}}</td>
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
      users: [],
      authorities: ['ROLE_ADMIN', 'ROLE_COMMITTEE']
    }
  },
  mounted() {
    fetchGraphQL('query { userList { id, username, authorities { authority } } }')
    .then(response => this.users = response.data.userList)
  }
}
</script>
