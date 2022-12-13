
<template>
  <TopNavBarVue></TopNavBarVue>
  <!--  @mousemove="onMousemove" :style="{ backgroundColor: `hsl(${x % 360}, 30%, 90%, 0.3)` }" -->
  <div class="main" v-cloak @click="changeColor"
    :style="{background: 'linear-gradient' + '(90deg,' + `hsl(${x+y}, 30%, 90%, 0.8)` + ',' + `hsl(${x+y+120}, 30%, 90%, 0.8)` +',' + `hsl(${x+y+240}, 30%, 90%, 0.8)` + ' )'}">
    <router-view v-slot="{ Component, route }">
      <transition name="slide-fade">
        <component :is="Component" :key="route.path + Math.random()" />
      </transition>
    </router-view>
  </div>

  <FooterVue></FooterVue>
  <backToTopVue />

</template>

<script>

import backToTopVue from './components/backToTop.vue';
import FooterVue from './components/Footer.vue';
import TopNavBarVue from './components/TopNavBar.vue';

export default {
  components: {
    backToTopVue,
    FooterVue,
    TopNavBarVue,
  },
  data() {
    return {
      x: 100,
      y: 100,
    }
  },
  methods: {
    changeColor(event) {
      this.x = event.clientX + Math.random() * 50
      this.y = event.clientY + Math.random() * 50
    }
  },
  mounted() {
  },
  created() {
    // 这行代码的意思是定义一个全局变量，等于该组件的实例
    window.mv = this

    //在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem("store")) {
      this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem("store"))));
      sessionStorage.removeItem('store');
      // this.$router.push({ name: this.$store.state.currentTag.path })
    }

    //在页面刷新时将vuex里的信息保存到sessionStorage里
    window.addEventListener("beforeunload", () => {
      sessionStorage.setItem("store", JSON.stringify(this.$store.state))
    })
  },
}

</script>

<style lang="less" scoped>
.main {
  min-height: 800px;
  position: relative;
  z-index: 0;
  flex: 1 0 auto;
  transition: 0.5s all ease-in-out;
  // animation: Gradient 30s ease infinite;
  // background-size: 300% 300% !important;

  @keyframes Gradient {
    0% {
      background-position: 0 50%;
    }

    50% {
      background-position: 100% 50%;
    }

    100% {
      background-position: 0 50%;
    }
  }
}

@media (max-width: 1000px) {
  .main {
    min-height: 600px;
  }

}
</style>
