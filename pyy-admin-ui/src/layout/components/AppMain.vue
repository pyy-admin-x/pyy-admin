<template>
  <section class="app-main">
    <div class="dashboard-header">
      <!--<el-alert-->
        <!--title="成功提示的文案"-->
        <!--type="success"-->
        <!--show-icon>-->
      <!--</el-alert>-->
    </div>
    <div class="dashboard-editor-container">
      <transition name="fade-transform" mode="out-in">
        <router-view :key="key" />
      </transition>
    </div>

  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
  padding-top: 50px;
}
.dashboard-editor-container {
  padding: 18px 22px 22px;
}
.fixed-header+.app-main {
  padding-top: 50px;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
