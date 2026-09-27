<script setup lang="ts">
const props = defineProps({
  columnClass: {
    type: String,
    default: '',
  },
});
</script>

<template>
  <div class="slidev-layout three-columns w-full h-full grid grid-rows-[auto_1fr] p-16">
    <div class="slide-title mb-8">
      <slot name="title" />
    </div>

    <div class="grid grid-cols-3 gap-8 h-full">
      <div class="col-left" :class="props.columnClass">
        <slot name="left"/>
      </div>
      <div class="col-middle" :class="props.columnClass">
        <slot name="middle" />
      </div>
      <div class="col-right" :class="props.columnClass">
        <slot name="right" />
      </div>
    </div>
  </div>
</template>

<style>
/* Basic styling for the title slot if needed */
.slidev-layout.three-columns .slide-title {
  /* You can add text alignment or font size adjustments here if your theme doesn't handle H1/H2 well in this slot */
  text-align: center; /* Example: center the title */
}

/* Ensure content within columns starts at the same top level */
/* This is still good practice to prevent internal element margins from misaligning content within their columns */
.slidev-layout.three-columns .col-left > *:first-child,
.slidev-layout.three-columns .col-middle > *:first-child,
.slidev-layout.three-columns .col-right > *:first-child {
  margin-top: 0 !important;
}

/* Ensure all columns align content to the top */
.slidev-layout.three-columns .col-left,
.slidev-layout.three-columns .col-middle,
.slidev-layout.three-columns .col-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>
