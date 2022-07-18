<script>
import { RouterLink, RouterView } from "vue-router";
export default {
  name: 'App',
  components: {
  }
}
</script>

<template>
  <header>
    <nav>
      <RouterLink to="/">Home</RouterLink>
    </nav>
  </header>

  <RouterView />
</template>

<style>

</style>
