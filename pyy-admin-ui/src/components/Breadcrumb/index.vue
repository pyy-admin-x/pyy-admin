<template>
  <div class="app-breadcrumb-container">
    <span class="refresh" @click="refresh">
      <svg-icon icon-class="refresh" />
    </span>
    <el-breadcrumb class="app-breadcrumb" separator="/">
      <transition-group name="breadcrumb">
        <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
          <span v-if="item.redirect==='noRedirect'||index==levelList.length-1"
                class="no-redirect">{{ item.meta.title }}</span>
          <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
        </el-breadcrumb-item>
      </transition-group>
    </el-breadcrumb>
  </div>

</template>

<script>
  import pathToRegexp from 'path-to-regexp'

  export default {
    data() {
      return {
        levelList: null
      }
    },
    watch: {
      $route() {
        this.getBreadcrumb()
      }
    },
    created() {
      this.getBreadcrumb()
    },
    methods: {
      getBreadcrumb() {
        let matched = this.$route.matched.filter(item => {
          if (item.name) {
            return true
          }
        })
        const first = matched[0]
        if (first && first.name !== '首页') {
          matched = [{path: '/dashboard', meta: {title: '首页'}}].concat(matched)
        }
        if (matched.length >= 4) {
          matched.splice(1, 1)
        }
        this.levelList = matched
      },
      isDashboard(route) {
        const name = route && route.name
        if (!name) {
          return false
        }
        return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
      },
      pathCompile(path) {
        // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
        const {params} = this.$route
        var toPath = pathToRegexp.compile(path)
        return toPath(params)
      },
      refresh() {
        this.$router.go(0)
      },
      handleLink(item) {
        const {redirect, path} = item
        if (redirect) {
          this.$router.push(redirect)
          return
        }
        this.$router.push(this.pathCompile(path))
      }
    }
  }
</script>

<style lang="scss" scoped>
  .refresh {
    position: absolute;
    display: inline-block;
    width: 30px;
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
  }
  .app-breadcrumb.el-breadcrumb[data-v-b50ef614] {
    display: inline-block;
    font-size: 14px;
    line-height: 50px;
    margin-left: 36px;
  }
  .app-breadcrumb {
    margin-left: 80px;
  }
  .app-breadcrumb.el-breadcrumb {
    display: inline-block;
    font-size: 14px;
    line-height: 50px;
    margin-left: 8px;

    .no-redirect {
      color: #97a8be;
      cursor: text;
    }
  }
</style>
