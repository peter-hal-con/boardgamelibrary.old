<!-- Vue component -->
<template>
  <multiselect
    :value='value'
    :options="titles"
    @search-change="asyncFind"
    label="label"
    track-by="bgg_id"
    @input="onInput">
  </multiselect>
</template>

<script>
  import Vue from 'vue'
  import Multiselect from 'vue-multiselect'

  // register globally
  Vue.component('multiselect', Multiselect)

  function queryBGG(searchQuery) {
    const tmp = searchQuery.replace(/\s/g, '+').replace(/&/g, 'and')
    return fetch('https://boardgamegeek.com/xmlapi2/search?type=boardgame,boardgameaccessory,boardgameexpansion&query=' + encodeURIComponent(tmp) + '')
    .then(response => response.text())
    .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
    .then(data => Array.prototype.slice.call(data.documentElement.getElementsByTagName("item")).map(itemElement => {
      var title = {
        bgg_id: itemElement.getAttribute('id'),
        name: null
      }
      Array.prototype.slice.call(itemElement.getElementsByTagName("name")).forEach(nameElement => {
        if(title.name === null || nameElement.getAttribute('type') === 'primary') {
          title["name"] = nameElement.getAttribute('value')
        }
      })
      const yearpublishedElements = itemElement.getElementsByTagName("yearpublished");
      if(yearpublishedElements.length > 0) {
        title["yearpublished"] = yearpublishedElements[0].getAttribute("value");
      }
      return title;
    }));
  }

  export default {
    // OR register locally
    components: { Multiselect },
    props: ['value'],
    data () {
      return {
        titles: [],
        isLoading: false
      }
    },
    methods: {
      asyncFind (searchQuery) {
        this.isLoading = true;
        queryBGG(searchQuery)
        .then(titles => titles.map(title => {
          if('yearpublished' in title) {
            title["label"] = title["name"] + " (" + title["yearpublished"] + ")";
          } else {
            title["label"] = title["name"];
          }
          return title;
        }))
        .then(titles => {
          this.titles = titles;
          this.isLoading = false;
        });
      },
      onInput(ev) {
        this.$emit('input', ev)
      }
    }
  }
</script>

<!-- New step!
     Add Multiselect CSS. Can be added as a static asset or inside a component. -->
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
  .multiselect__input:focus {
    border: none
  }
</style>
