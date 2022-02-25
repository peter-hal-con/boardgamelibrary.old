<template>
  <div>
    <UserDropdown v-if="loginState"/>
    <LoginButton v-else />
  </div>
</template>
<script>
import { bus } from '../main'

export default {
  name: "UserDropdownLoginButtonToggle",
  data() {
    return {
      loginState: localStorage.auth != null
    }
  },
  created() {
    bus.$on('loginStateChange', () => {
      this.loginState = localStorage.auth != null
    })
  }
}
</script>
