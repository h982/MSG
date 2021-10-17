<template>
  <div
    wire:loading
    class="
      fixed
      top-0
      left-0
      right-0
      bottom-0
      w-full
      h-screen
      z-50
      overflow-hidden
      bg-gray-700
      opacity-75
      flex flex-col
      items-center
      justify-center
    "
  >
    <div
      class="
        loader
        ease-linear
        rounded-full
        opacity-100
        border-4 border-t-4 border-gray-200
        h-12
        w-12
        mb-4
      "
    ></div>
    <h2 class="text-center text-white text-xl font-semibold opacity-100">Loading...</h2>
    <p class="w-1/3 text-center text-white opacity-100">잠시만 기다려주세요.</p>
  </div>
</template>

<script>
export default {
  name: 'LOADING',
};
</script>
