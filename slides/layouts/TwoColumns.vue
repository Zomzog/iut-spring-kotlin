<script setup lang="ts">
const props = defineProps({
  columnClass: {
    type: String,
    default: '',
  },
});
</script>

<template>
  <div class="slidev-layout two-columns full w-full h-full grid grid-rows-[auto_1fr] p-16">
    <div class="grid grid-cols-2 gap-8 h-full">
      <div class="w-full" :class="props.columnClass">
        <slot name="left"/>
      </div>
      <div class="w-full" :class="props.columnClass">
        <slot name="right" />
      </div>
    </div>
  </div>
</template>

<style>

/* Ensure content within columns starts at the same top level */
/* This is still good practice to prevent internal element margins from misaligning content within their columns */
.slidev-layout.two-columns .col-left > *:first-child,
.slidev-layout.two-columns .col-right > *:first-child {
  margin-top: 0 !important;
}

/* Ensure all columns align content to the top */
.slidev-layout.two-columns .col-left,
.slidev-layout.two-columns .col-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>
