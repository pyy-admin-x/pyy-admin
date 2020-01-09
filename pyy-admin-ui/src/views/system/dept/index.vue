<template>
  <div class="page-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="dept-form-inline" size="small">
<!--      <el-form-item label="">-->
<!--        <el-input v-model="params.name" placeholder="请输入部门名称"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>-->
<!--      </el-form-item>-->
      <!-- 新增部门按钮 -->
      <el-button @click.prevent="handleAdd" type="primary" size="small" icon="el-icon-plus">新增部门</el-button>
    </el-form>
    <!-- 部门Tree列表 -->
    <el-table :data="deptTreeList" style="width: 100%;margin-bottom: 20px;" row-key="id"
              default-expand-all
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="name" label="部门名称"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="createTime" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | dateFormat}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="320">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit"   type="primary"  @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger"   @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增部门 -->
    <dept-modal ref="deptModel"></dept-modal>
  </div>
</template>

<script>
  import * as deptAPI from '../../../api/system/dept/index'
  export default {
    data() {
      return {
        deptTreeList: [],
        params: {
          name: '',// 部门名称
        },
        isShowAddModal: false, // 新增部门Drawer显示状态
      }
    },
    mounted () {
      // 默认查询部门
      this.handleQuery()
    },
    methods: {
      // 部门查询
      async handleQuery () {
        const result = await deptAPI.getDeptTreeList()
        this.deptTreeList = result.data
      },
      // 部门新增
      handleAdd () {
        this.$refs.deptModel.title = '新增'
        this.$refs.deptModel.openAdd()
      },
      // 部门编辑
      handleEdit (id) {
        this.$refs.deptModel.title = '编辑'
        this.$refs.deptModel.openEdit(id)
      },
      // 部门删除
      handleDelete (id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await deptAPI.deleteDept(id)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          this.$message({ype: 'info', message: '已取消删除'});
        });
      }
    },
    components: {
      deptModal: () => import('./deptModal.vue'),
    }
  }
</script>
