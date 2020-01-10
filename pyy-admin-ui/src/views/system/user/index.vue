<template>
  <div class="container">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="params">
        <el-form-item>
          <el-input v-model="params.name" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleQuery()">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-plus" @click.prevent="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <el-table :data="list" @selection-change="handleSelectionChange">style="width: 100%">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="id" label="用户ID"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="deptId" label="部门ID"/>
      <el-table-column prop="jobId" label="岗位ID"/>
      <el-table-column prop="email" label="邮箱"/>
      <el-table-column prop="phone" label="手机号"/>
      <el-table-column prop="avatar" label="头像"/>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          {{scope.row.status == 0 ? '正常' : '锁定'}}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
        <template slot-scope="scope">
          {{scope.row.createTime | dateFormat}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
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

    <!-- 新增或修改用户 -->
    <sysUser-modal ref="sysUserModal"></sysUser-modal>
  </div>
</template>

<script>
  import * as sysUserAPI from '../../../api/system/user/index'
  import sysUserModal from './userModal.vue'

  export default {
    data() {
      return {
        list: [], // 用户列表
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          name: '',// 用户名称
        },
        selections: [], // 选择项
        isShowAddModal: false, // 新增用户Drawer显示状态
      }
    },
    mounted() {
      // 默认查询用户
      this.handleQuery()
    },
    methods: {
      // 查询
      async handleQuery() {
        const result = await sysUserAPI.getSysUserPageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.records
      },
      handleSelectionChange(selections) {
        this.selections = selections
      },
      handleAdd() {
        this.$refs.sysUserModal.title = '新增'
        this.$refs.sysUserModal.openAdd()
      },
      handleEdit(id) {
        this.$refs.sysUserModal.title = '编辑'
        this.$refs.sysUserModal.openEdit(id)
      },
      handleDelete(id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await sysUserAPI.deleteSysUser(id)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          this.$message({type: 'info', message: '已取消删除'});
        });
      },
      handleBatchDelete() {
        this.$confirm('此操作将永久删除选中数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步批量删除
          const ids = this.selections.map(item => item.id)
          await sysUserAPI.deleteBatchSysUser(ids)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          this.$message({type: 'info', message: '已取消删除'});
        });
      },
      handleSizeChange(size) {
        this._changePage(this.page, size)
      },
      handleCurrentChange(page) {
        this._changePage(page, this.size)
      },
      // 分页查询
      _changePage(page, size) {
        this.page = page
        this.size = size
        this.handleQuery()
      },
    },
    components: {
      sysUserModal
    }
  }
</script>

<style>

</style>
