<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="roleDrawer" size="30%">
    <div class="model-content">
      <el-form :model="roleForm" :rules="roleFormRules" ref="roleForm" label-width="80px" size="small">
        <el-form-item label="角色名称" prop="name">
           <el-input v-model="roleForm.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="角色标识" prop="code">
           <el-input v-model="roleForm.code" auto-complete="off" placeholder="超级管理员标识：ADMIN"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="roleForm.sort" controls-position="right"  :min="1" :max="100"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
           <el-input type="textarea" v-model="roleForm.remark"
                     :autosize="{ minRows: 4, maxRows: 6}"
                     placeholder="请输入内容"
                     maxlength="200"
                     show-word-limit
                     auto-complete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="plain" size="small" @click="handleCancel">关闭</el-button>
        <el-button type="primary" size="small" @click="handleSubmit">提交</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script type="text/ecmascript-6">
  import * as roleAPI from '@/api/system/role/index'
  export default {
    name: 'roleModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 菜单树列表
        menuTreeList: [],
        // 新增界面数据
        roleForm: {
          id: '',
          name: '',
          sort: 1,
          remark: '',
        },
        // 验证规则
        roleFormRules: {
          name: [{required: true, message: '请输入名称', trigger: 'blur'}],
        }
      }
    },
    mounted () {
    },
    methods: {
      // 打开添加对话框
      openAdd () {
        this.visible = true
      },
      // 打开编辑对话框
      openEdit (id) {
        this.visible = true
        this.$nextTick(() => {
          // 异步查询
          roleAPI.getRoleById(id).then(res => {
            console.log(res)
            const role = res.data
            this.roleForm.id = role.id
            this.roleForm.name = role.name
            this.roleForm.code = role.code
            this.roleForm.sort = role.sort
            this.roleForm.remark = role.remark
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.roleForm.resetFields();
        this.roleForm = {
          id: '',
          name: '',
          sort: 1,
          remark: '',
        }
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        console.log(this.$refs.roleDrawer)
        this.$refs.roleDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.roleForm.validate(async (valid) => {
          if (valid) {
            if (!this.roleForm.id) {
              // 异步添加
              await roleAPI.addRole(this.roleForm)
            } else {
              // 异步修改
              await roleAPI.updateRole(this.roleForm.id, this.roleForm)
            }
            this.$notify({ title: '成功',  message: '提交',  type: 'success'});
            // 关闭对话框
            this.handleCancel()
            // 刷新查询
            this.$parent.handleQuery()
          }
        });
      }
    },
    components: {
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss">
  .model-content {
    margin: 24px;
    padding: 15px;
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
