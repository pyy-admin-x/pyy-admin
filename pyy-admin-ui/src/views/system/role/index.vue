<template>
  <div class="container">
    <!--工具栏-->
    <div class="toolbar">
      <el-form :inline="true" :model="params" size="small">
        <el-form-item>
          <el-input v-model="params.name" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleQuery()">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" icon="el-icon-plus" @click.prevent="handleAdd">新增</el-button>
          <el-button size="small" icon="el-icon-delete" type="danger" @click="handleBatchDelete">删除</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <el-table :data="list" @selection-change="handleSelectionChange">style="width: 100%">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="name" label="角色名称"/>
      <el-table-column prop="code" label="角色标识"/>
      <el-table-column prop="sort" label="排序"/>
      <el-table-column prop="remark" label="备注"/>
      <el-table-column prop="createTime" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          <el-button size="mini" type="success" @click="handleSetPermission(scope.row.id)"><svg-icon icon-class="permission"/> 授权</el-button>
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

    <!-- 新增或修改角色 -->
    <role-modal ref="roleModal"></role-modal>
    <!-- 角色授权 -->
    <role-permission-modal ref="rolePermissionModal"></role-permission-modal>
  </div>
</template>

<script>
  import * as roleAPI from '@/api/system/role/index'
  import roleModal from './roleModal.vue'
  import rolePermissionModal from './rolePermissionModal.vue'

  export default {
    data() {
      return {
        list: [], // 角色列表
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          name: '',// 角色名称
        },
        selections: [], // 选择项
        isShowAddModal: false, // 新增角色Drawer显示状态
      }
    },
    mounted() {
      // 默认查询角色
      this.handleQuery()
    },
    methods: {
      // 查询
      async handleQuery() {
        const result = await roleAPI.getRolePageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.records
      },
      handleSelectionChange(selections) {
        this.selections = selections
      },
      handleAdd() {
        this.$refs.roleModal.title = '新增'
        this.$refs.roleModal.openAdd()
      },
      handleEdit(id) {
        this.$refs.roleModal.title = '编辑'
        this.$refs.roleModal.openEdit(id)
      },
      handleDelete(id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await roleAPI.deleteRole(id)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          //this.$message({type: 'info', message: '已取消删除'});
        });
      },
      handleBatchDelete() {
        this.$confirm('此操作将永久删除选中数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步批量删除
          const ids = this.selections.map(item => item.id)
          await roleAPI.deleteBatchRole(ids)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          //this.$message({type: 'info', message: '已取消删除'});
        });
      },
      handleSetPermission(id) {
        this.$refs.rolePermissionModal.title = '角色授权'
        this.$refs.rolePermissionModal.openEdit(id)
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
      roleModal,
      rolePermissionModal
    }
  }
</script>

<style>

</style>
