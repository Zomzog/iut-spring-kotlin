<script setup lang="ts">
import { onSlideEnter, useSlideContext } from '@slidev/client';

const { $slidev } = useSlideContext()

// This runs when the slide is first opened
onSlideEnter(() => {
  // It triggers the first v-click animation
  if ($slidev.nav.clicks < 1) {
    $slidev.nav.next();
  }
});
</script>

<template>
  <div class="slidev-layout default">
    <slot />
  </div>
</template>
