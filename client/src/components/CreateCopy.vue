<template>
  <form @submit.prevent="create_copy">
    <h3>Create Library Item</h3>
    <div :id="'message_'+message.type" v-if="message.type" :class="'alert alert-'+message.type" role="alert">{{message.text}}</div>
    <div class="form-group">
      <label for="copy_title">Game Title</label>
      <BggAutocomplete id="copy_title" v-model="selectedTitle"/>
    </div>
    <button id="submit_create_copy" type="submit" class="btn btn-dark btn-lg btn-block">Create Library Item</button>
  </form>
</template>
<script>
import BggAutocomplete from './BggAutocomplete.vue'

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
      }
    }
  },
  methods: {
    create_copy: function() {
    }
  }
};
</script>
