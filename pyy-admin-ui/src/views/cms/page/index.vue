<template>
  <div class="page-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="demo-form-inline" size="small">
      <el-form-item label="站点">
        <el-select v-model="params.siteId" placeholder="请选择站点">
          <el-option v-for="(site, index) in siteList" :key="index" :label="site.siteName" :value="site.siteId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="页面别名">
        <el-input v-model="params.pageAliase" placeholder="请输入页面别名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
      <!-- 新增页面按钮 -->
      <el-button @click.prevent="handleAdd" type="primary" size="small" icon="el-icon-plus">新增页面</el-button>
    </el-form>

    <!-- 页面列表 -->
    <el-table border :data="list" style="width: 100%">
      <el-table-column type="index" label="序号" width="50"/>
      <el-table-column prop="pageName" label="页面名称" width="180"/>
      <el-table-column prop="pageAliase" label="别名" width="120"/>
      <el-table-column prop="pageType" label="页面类型" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.pageType === '0' ? '静态' : '动态' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="pageWebPath" label="访问路径"/>
      <el-table-column prop="pagePhysicalPath" label="物理路径"/>
      <el-table-column prop="pageCreateTime" label="创建时间" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.pageCreateTime | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="320">
        <template slot-scope="scope">
          <el-button-group>
            <el-button
              size="small"
              type="info"
              @click="handlePreview(scope.row.pageId)">
              <svg-icon icon-class="preview"/>
              预览
            </el-button>
            <el-button
              size="small"
              icon="el-icon-edit"
              type="primary"
              @click="handleEdit(scope.row.pageId)">修改
            </el-button>
            <el-button
              size="small"
              icon="el-icon-delete"
              type="danger"
              @click="handleDelete(scope.row.pageId)">删除
            </el-button>

            <el-button
              size="small"
              type="success"
              @click="handlePostPage(scope.row.pageId)">
              <svg-icon icon-class="release"/>
              发布
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页工具栏 -->
    <el-pagination @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page="currentPage"
                   :page-sizes="[5, 10, 15, 30, 40]"
                   :page-size="size"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="total" style="margin-top: 15px">
    </el-pagination>

    <!-- 新增页面 -->
    <add-modal ref="addModel"></add-modal>
  </div>
</template>

<script>
  import * as cmsPageAPI from '../../../api/cms/page/index'
  import * as cmsSiteAPI from '../../../api/cms/site/index'
  import addModal from './pageModel.vue'
  export default {
    data() {
      return {
        siteList: [], // 站点列表
        list: [],
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          siteId: '', // 站点ID
          pageAliase: '',// 页面别名
        },

        isShowAddModal: false, // 新增页面Drawer显示状态
      }
    },
    mounted () {
      // 默认查询页面
      this.handleQuery()
      //初始化站点列表
      this.handlerQuerySiteList()
    },
    methods: {
      // 查询站点列表
      async handlerQuerySiteList () {
        const result = await cmsSiteAPI.getSiteList(this.params)
        this.siteList = result.data
      },
      // 页面查询
      async handleQuery () {
        const result = await cmsPageAPI.getPageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.list
      },
      // 页面新增
      handleAdd () {
        this.$refs.addModel.title = '新增'
        this.$refs.addModel.openAdd()
      },
      // 页面预览
      handlePreview (pageId) {
        window.open("http://www.xuecheng.com/cms/preview/" + pageId)
      },
      // 页面发布
      handlePostPage (pageId) {
        this.$confirm('确认发布该页面吗？', '提示', {}).then(async (res) => {
          await cmsPageAPI.postPage(pageId)
          this.$message.success('发布成功，请稍后查看结果');
        }).
        catch(() => {

        })
      },
      // 页面编辑
      handleEdit (pageId) {
        this.$refs.addModel.title = '编辑'
        this.$refs.addModel.openEdit(pageId)
      },
      // 页面删除
      handleDelete (pageId) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await cmsPageAPI.deleteSite(pageId)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          //this.$message({ype: 'info', message: '已取消删除'});
        });
      },
      handleSizeChange(size) {
        console.log(`每页 ${size} 条`);
        this._changePage(this.page, size)
      },
      handleCurrentChange(page) {
        console.log(`当前页: ${page}`);
        this._changePage(page, this.size)
      },
      // 分页查询
      _changePage (page, size) {
        this.page = page
        this.size = size
        this.handleQuery()
      },
    },
    components: {
      addModal
    }
  }
</script>
