<template>
  <div class="site-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="demo-form-inline" size="small">
      <el-form-item label="岗位名称">
        <el-input v-model="params.name" placeholder="请输入岗位名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
      <!-- 新增按钮 -->
      <el-button @click.prevent="handleAdd" type="primary"  size="small" icon="el-icon-plus">新增</el-button>
      <!-- 批量删除按钮 -->
      <el-button @click.prevent="handleBatchDelete" type="danger" size="small" :disabled="selections.length < 1" ><svg-icon icon-class="delBatch"/> 批量删除</el-button>
    </el-form>

    <!-- 岗位列表 -->
    <el-table :data="list" @selection-change="handleSelectionChange">style="width: 100%">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="name" label="岗位名称" />
      <el-table-column prop="dept.name" label="所属部门"/>
      <el-table-column prop="sort" label="排序" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="createTime" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit"   type="primary"  @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger"  @click="handleDelete(scope.row.id)">删除</el-button>
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

    <!-- 新增或修改岗位 -->
    <job-modal ref="jobModal" ></job-modal>
  </div>
</template>

<script>
  import * as jobAPI from '../../../api/system/job/index'
  import jobModal from './jobModal.vue'
  export default {
    data() {
      return {
        list: [], // 岗位列表
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          name: '',// 岗位名称
        },
        selections: [], // 选择项
        isShowAddModal: false, // 新增岗位Drawer显示状态
      }
    },
    mounted () {
      // 默认查询岗位
      this.handleQuery()
    },
    methods: {
      // 查询
      async handleQuery () {
        const result = await jobAPI.getJobPageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.records
      },
      handleSelectionChange(selections) {
        this.selections = selections
      },
      handleAdd () {
        this.$refs.jobModal.title = '新增'
        this.$refs.jobModal.openAdd()
      },
      handleEdit (id) {
        this.$refs.jobModal.title = '编辑'
        this.$refs.jobModal.openEdit(id)
      },
      handleDelete (id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await jobAPI.deleteJob(id)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          //this.$message({type: 'info', message: '已取消删除'});
        });
      },
      handleBatchDelete () {
        this.$confirm('此操作将永久删除选中数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步批量删除
          const ids = this.selections.map(item => item.id)
          await jobAPI.deleteBatchJob(ids)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          //this.$message({type: 'info', message: '已取消删除'});
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
      jobModal
    }
  }
</script>
