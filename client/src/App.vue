<template>
  <div id="app" class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <LoginButton/>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>
