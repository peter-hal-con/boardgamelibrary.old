<template>
  <table id="user_list" class="table">
    <thead>
      <tr>
        <th scope="col">Username</th>
        <th scope="col" v-for="(authority) in authorities" :key="authority">{{authority}}</th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(user) in userList" :key="user.id" :id="'tr-'+user.username">
        <td id="username">{{user.username}}</td>
        <td v-for="(authority) in authorities" :key="authority" :id="authority">{{(require('jsonpath').query(user.authorities, '$..authority').includes(authority)) ? "&#10004;" : " "}}</td>
        <td id="update">
          <a class="btn btn-primary" :href="'/#/updateUser/'+user.id" role="button" style="color:white">Update</a>
        </td>
      </tr>
    </tbody>
  </table>
</template>
<script>
import gql from 'graphql-tag'

export default {
  name: "ListUsers",
  apollo: {
    userList: gql`query { userList { id, username, authorities { authority } } }`
  },
  data() {
    return {
      userList: [],
      authorities: ['ROLE_ADMIN', 'ROLE_COMMITTEE']
    }
  }
}
</script>
