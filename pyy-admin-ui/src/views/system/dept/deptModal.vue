<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="deptDrawer" size="30%">
    <div class="model-content">
      <el-form :model="deptForm" :rules="deptFormRules" ref="deptForm" label-width="80px" size="small">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="deptForm.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <treeselect v-model="deptForm.parentId"
                      :options="deptTreeList"
                      :default-expand-level="1"
                      :show-count="true"
                      placeholder="请选择上级部门（默认顶级部门）"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="deptForm.sort" controls-position="right"  :min="1" :max="100"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="deptForm.remark"
                    :autosize="{ minRows: 4, maxRows: 6}"
                    placeholder="请输入内容"
                    maxlength="200"
                    show-word-limit/>
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
  // import the component
  import Treeselect from '@riophae/vue-treeselect'
  // import the styles
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import * as deptAPI from '../../../api/system/dept/index'

  export default {
    name: 'addModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 部门树列表
        deptTreeList: [],
        // 新增界面数据
        deptForm: {
          id: '',
          name: '',
          parentId: null,
          sort: 1,
          remark: '',
        },
        // 验证规则
        deptFormRules: {
          name: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
        }
      }
    },
    mounted () {
    },
    methods: {
      // 查询部门树列表
      async handleQueryDeptTreeList () {
        const result = await deptAPI.getDeptTreeList()
        this.deptTreeList = result.data
      },
      // 打开添加对话框
      openAdd () {
        // 初始化部门树列表
        this.handleQueryDeptTreeList()
        this.visible = true
      },
      // 打开编辑对话框
      openEdit (id) {
        // 初始化部门树列表
        this.handleQueryDeptTreeList()
        this.visible = true
        this.$nextTick(() => {
          // 异步查询
          deptAPI.getDeptById(id).then(res => {
            console.log(res)
              const dept = res.data
              this.deptForm.id = dept.id
              this.deptForm.name = dept.name
              this.deptForm.parentId = dept.parentId
              this.deptForm.sort = dept.sort
              this.deptForm.remark = dept.remark
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.deptForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        this.$refs.deptDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.deptForm.validate(async (valid) => {
          if (valid) {
            if (!this.deptForm.id) {
              // 异步添加
              await deptAPI.addDept(this.deptForm)
            } else {
              // 异步修改
              await deptAPI.updateDept(this.deptForm.id, this.deptForm)
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
      Treeselect
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
