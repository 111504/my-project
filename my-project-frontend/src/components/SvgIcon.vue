<template>
    <svg :class="svgClass" v-bind="$attrs" :style="{ color: color }">
        <use :href="iconName" />
    </svg>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
    name: {
        type: String,
        required: true
    },
    color: {
        type: String,
        default: ''
    }
})

const iconName = computed(() => `#icon-${props.name}`)
const svgClass = computed(() => {
    if (props.name) {
        return `svg-icon icon-${props.name}`
    }
    return 'svg-icon'
})
</script>

<style scoped>

.svgClass {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>

<!--<template>-->
<!--  <svg aria-hidden="true">-->
<!--    <use :href="symbolId" :fill="color" />-->
<!--  </svg>-->
<!--</template>-->
<!--<script lang="ts">-->
<!--import { defineComponent, computed } from 'vue';-->
<!--export default defineComponent({-->
<!--  name: 'SvgIcon',-->
<!--  props: {-->
<!--    prefix: {-->
<!--      type: String,-->
<!--      default: 'icon',-->
<!--    },-->
<!--    name: {-->
<!--      type: String,-->
<!--      required: true,-->
<!--    },-->
<!--    color: {-->
<!--      type: String,-->
<!--    },-->
<!--  },-->
<!--  setup(props) {-->
<!--    const symbolId = computed(() => `#${props.prefix}-${props.name}`);-->
<!--    return { symbolId };-->
<!--  },-->
<!--});-->
<!--</script>-->
