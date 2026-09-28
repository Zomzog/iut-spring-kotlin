<script setup lang="ts">
const props = defineProps({
  columnClass: {
    type: String,
    default: '',
  },
});
</script>

<template>
  <div class="slidev-layout two-columns w-full h-full grid grid-rows-[auto_1fr] p-4">
    <div class="slide-title mb-8">
      <slot name="title" />
    </div>

    <div class="grid grid-cols-2 gap-2 h-full">
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
/* Basic styling for the title slot if needed */
.slidev-layout.two-columns .slide-title {
  /* You can add text alignment or font size adjustments here if your theme doesn't handle H1/H2 well in this slot */
  text-align: left; /* Example: center the title */
}

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
