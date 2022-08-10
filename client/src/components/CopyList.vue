<template>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">Owner</th>
        <th scope="col">Title</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(copy) in pagedCopyList.results" :key="copy.id" :id="'tr-'+copy.id">
        <td id="owner">{{copy.owner.username}}</td>
        <td id="title">{{copy.title.name}}</td>
      </tr>
    </tbody>
  </table>
</template>
<script>
import gql from 'graphql-tag'

export default {
  name: "CopyList",
  apollo: {
    pagedCopyList: {
      query () {
        var currentUser = JSON.parse(localStorage.auth)
        return  gql`query{pagedCopyList(owner:"${currentUser.username}"){results{id, owner{username}, title{name}}}}`
      },
      pollInterval: 2000
    }
  },
  data() {
    return {
      pagedCopyList: [],
      authorities: ['ROLE_ADMIN', 'ROLE_COMMITTEE']
    }
  }
}
</script>
