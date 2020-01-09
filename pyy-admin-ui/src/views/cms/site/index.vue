<template>
  <div class="site-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="demo-form-inline" size="small">
      <el-form-item label="站点名称">
        <el-input v-model="params.siteName" placeholder="请输入站点名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
      <!-- 新增站点按钮 -->
      <el-button @click.prevent="handleAdd" type="primary"  size="small" icon="el-icon-plus">新增站点</el-button>
    </el-form>

    <!-- 站点列表 -->
    <el-table :data="list" style="width: 100%">
      <el-table-column type="index" label="序号" width="50"/>
      <el-table-column prop="siteName" label="站点名称" width="200"/>
      <el-table-column prop="siteDomain" label="站点域名" width="200"/>
      <el-table-column prop="sitePort" label="站点端口" width="100"/>
      <el-table-column prop="siteWebPath" label="站点访问地址"/>
      <el-table-column prop="sitePhysicalPath" label="站点物理地址"/>
      <el-table-column prop="siteCreateTime" label="创建时间" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.siteCreateTime | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.row.siteId)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.siteId)">删除
          </el-button>
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

    <!-- 新增站点 -->
    <add-modal ref="addModel" ></add-modal>
  </div>
</template>

<script>
  import * as cmsSiteAPI from '../../../api/cms/site/index'
  import addModal from './siteModel.vue'
  export default {
    data() {
      return {
        list: [], // 站点列表
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          siteName: '',// 站点名称
        },

        isShowAddModal: false, // 新增站点Drawer显示状态
      }
    },
    mounted () {
      // 默认查询站点
      this.handleQuery()
    },
    methods: {
      // 查询
      async handleQuery () {
        const result = await cmsSiteAPI.getSitePageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.list
      },
      handleAdd () {
        this.$refs.addModel.title = '新增'
        this.$refs.addModel.openAdd()
      },
      handleEdit (siteId) {
        this.$refs.addModel.title = '编辑'
        this.$refs.addModel.openEdit(siteId)
      },
      handleDelete (siteId) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await cmsSiteAPI.deleteSite(siteId)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          this.$message({type: 'info', message: '已取消删除'});
        });
      },
      handleSizeChange(size) {
        console.log(`每页 ${size} 条`);
        this._changePage(this.site, size)
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
