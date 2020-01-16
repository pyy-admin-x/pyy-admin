<template>

  <el-drawer
    :title="title"
    custom-class="reset-drawer"
    style="min-height: 500px;overflow: auto"
    :visible.sync="visible"
    @close="resetMenuTree"
    :wrapperClosable="false" ref="rolePermissionDrawer" size="30%">

    <div class="model-content">
      <vuescroll>
        <el-tree
          style="height: 700px"
          ref="menu"
          :data="menuTreeList"
          :default-checked-keys="roleForm.menuIds"
          check-strictly
          default-expand-all
          show-checkbox
          node-key="id"
        />
      </vuescroll>
      <div slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="plain" size="small" @click="handleCancel">关闭</el-button>
        <el-button type="primary" size="small" @click="handleSubmit">提交</el-button>
      </div>
    </div>
  </el-drawer>

</template>

<script type="text/ecmascript-6">
  import * as roleAPI from '@/api/system/role/index'
  import * as menuAPI from '@/api/system/menu/index'
  import vuescroll from 'vuescroll';

  export default {
    name: 'roleModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 菜单树列表
        menuTreeList: [],
        roleForm: {
          id: '',
          menuIds: [],
        },
        // 验证规则
        roleFormRules: {
          menuIds: [{required: true, message: '请输入选择权限', trigger: 'blur'}],
        }
      }
    },
    mounted () {
    },
    methods: {
      // 查询菜单树列表
      async handleQueryMenuTreeList () {
        const result = await menuAPI.getMenuTreeList()
        this.menuTreeList = result.data
      },
      // 打开添加对话框
      openAdd () {
        // 初始化菜单树列表
        this.handleQueryMenuTreeList()
        this.visible = true
      },
      // 打开编辑对话框
      openEdit (id) {
        // 初始化菜单树列表
        this.handleQueryMenuTreeList()
        this.visible = true
        this.$nextTick(() => {
          // 异步查询
          roleAPI.getRoleById(id).then(res => {
            console.log(res)
            const role = res.data
            this.roleForm.id = role.id
            this.roleForm.menuIds = role.menuIds
          })
        })
      },
      // 重置菜单树
      resetMenuTree () {
        this.roleForm = {
          id: '',
          menuIds: [],
        }
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetMenuTree()
        this.$refs.rolePermissionDrawer.closeDrawer()
      },
      // 提交
      async handleSubmit () {
        const menuIds = this.$refs.menu.getCheckedKeys()
        console.log(menuIds)
        await roleAPI.setPermission(this.roleForm.id, menuIds)
        this.$notify({ title: '成功',  message: '提交',  type: 'success'});
        // 关闭对话框
        this.handleCancel()
      }
    },
    components: {
      vuescroll
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss">
  .reset-drawer{
    min-height: 400px;
    overflow: auto;
  }
  .model-content {
    border: 1px solid rgb(233, 233, 233);
  }

  .el-drawer__header {
    padding: 16px 24px;
    border-radius: 4px 4px 0 0;
    background: #fff;
    color: rgba(0, 0, 0, .65);
    border-bottom: 1px solid #e8e8e8;
    margin-bottom: 0px;
  }
</style>
