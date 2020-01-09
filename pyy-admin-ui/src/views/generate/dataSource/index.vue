<template>
  <div class="site-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="demo-form-inline" size="small">
      <el-form-item label="数据源名称">
        <el-input v-model="params.name" placeholder="请输入数据源名称"/>
      </el-form-item>
      <el-form-item label="数据库名称">
        <el-input v-model="params.dbName" placeholder="请输入数据库名称"/>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="params.username" placeholder="请输入用户名"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
      <!-- 新增数据源按钮 -->
      <el-button @click.prevent="handleAdd" type="primary"  size="small" icon="el-icon-plus">新增数据源</el-button>
    </el-form>

    <!-- 数据源列表 -->
    <el-table :data="list" style="width: 100%">
      <el-table-column prop="name" label="数据源名称"/>
      <el-table-column prop="dbType" label="类型" />
      <el-table-column prop="host" label="主机地址" />
      <el-table-column prop="port" label="端口" />
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="dbName" label="数据库名称"/>
      <el-table-column prop="createTime" label="创建时间" width="180px">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200px">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit"   type="primary"  @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger"   @click="handleDelete(scope.row.id)">删除</el-button>
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

    <!-- 新增数据源 -->
    <add-modal ref="addModel" ></add-modal>
  </div>
</template>

<script>
  import * as generateDataSourceAPI from '../../../api/generate/dataSource/index'
  import addModal from './dataSourceModal.vue'
  export default {
    data() {
      return {
        list: [], // 数据源列表
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          name: '',// 数据源名称
          username: '',// 数据库用户名
          dbName: '',// 数据源名称
        },
        isShowAddModal: false, // 新增数据源Drawer显示状态
      }
    },
    mounted () {
      // 默认查询数据源
      this.handleQuery()
    },
    methods: {
      // 查询
      async handleQuery () {
        const result = await generateDataSourceAPI.getDataSourcePageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.records
      },
      handleAdd () {
        this.$refs.addModel.title = '新增'
        this.$refs.addModel.openAdd()
      },
      handleEdit (id) {
        this.$refs.addModel.title = '编辑'
        this.$refs.addModel.openEdit(id)
      },
      handleDelete (id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await generateDataSourceAPI.deleteDataSource(id)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          this.$message({type: 'info', message: '已取消删除'});
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
