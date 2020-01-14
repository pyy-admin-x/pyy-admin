<template>
  <div class="page-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="menu-form-inline" size="small">
<!--      <el-form-item label="">-->
<!--        <el-input v-model="params.name" placeholder="请输入菜单名称"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>-->
<!--      </el-form-item>-->
      <!-- 新增菜单按钮 -->
      <el-button @click.prevent="handleAdd" type="primary" size="small" icon="el-icon-plus">新增</el-button>
    </el-form>

    <!-- 菜单Tree列表 -->
    <el-table :data="menuTreeList" style="width: 100%;margin-bottom: 20px;"
              row-key="id"
              size="medium"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="name" label="菜单名称"></el-table-column>
      <el-table-column prop="icon" label="图标" width="75">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="75"/>
      <el-table-column prop="path" label="路由地址"></el-table-column>
      <el-table-column prop="componentPath" label="组件路径"></el-table-column>
      <el-table-column prop="type" label="类型" width="75">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 1" type="primary" size="small">目录</el-tag>
          <el-tag v-else-if="scope.row.type === 2" type="success" size="small">菜单</el-tag>
          <el-tag v-else-if="scope.row.type === 3" type="info" size="small">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="iframe" label="外链" width="75">
        <template slot-scope="scope">
          <span>{{ scope.row.iframe == 0 ? '否' : '是'}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="cache" label="缓存" width="75">
        <template slot-scope="scope">
          <span>{{ scope.row.iframe == 0 ? '否' : '是'}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="hidden" label="隐藏" width="75">
        <template slot-scope="scope">
          <span>{{ scope.row.iframe == 0 ? '否' : '是'}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | dateFormat}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="420">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-plus" type="success"  @click="handleAddSonMenu(scope.row.id)">新增子菜单</el-button>
          <el-button size="mini" icon="el-icon-edit" type="primary"  @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" icon="el-icon-edit" type="danger"  @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增菜单 -->
    <menu-modal ref="menuModel"></menu-modal>
  </div>
</template>

<script>
  import * as menuAPI from '../../../api/system/menu/index'
  export default {
    data() {
      return {
        menuTreeList: [],
        parentId: null,
        params: {
          name: '',// 菜单名称
        },
        isShowAddModal: false, // 新增菜单Drawer显示状态
      }
    },
    mounted () {
      // 默认查询菜单
      this.handleQuery()
    },
    methods: {
      // 菜单查询
      async handleQuery () {
        const result = await menuAPI.getMenuTreeList()
        this.menuTreeList = result.data
      },
      // 菜单新增
      handleAdd () {
        this.$refs.menuModel.title = '新增'
        this.$refs.menuModel.openAdd()
      },
      // 子菜单新增
      handleAddSonMenu (id) {
        this.$refs.menuModel.title = '新增子菜单'
        this.$refs.menuModel.openAdd(id)
      },
      // 菜单编辑
      handleEdit (id) {
        this.$refs.menuModel.title = '编辑'
        this.$refs.menuModel.openEdit(id)
      },
      // 菜单删除
      handleDelete (id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          // 执行异步删除
          await menuAPI.deleteMenu(id)
          this.$message({type: 'success', message: '删除成功!'});
          // 刷新列表
          this.handleQuery()

        }).catch(() => {
          //this.$message({ype: 'info', message: '已取消删除'});
        });
      }
    },
    components: {
      menuModal: () => import('./menuModal.vue'),
    }
  }
</script>
