<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="jobDrawer" size="30%">
    <div class="model-content">
      <el-form :model="jobForm" :rules="jobFormRules" ref="jobForm" label-width="80px" size="small">
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="jobForm.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <treeselect v-model="jobForm.deptId"
                      :options="deptTreeList"
                      :default-expand-level="1"
                      :show-count="true"
                      placeholder="请选择部门"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="jobForm.sort" controls-position="right"  :min="1" :max="100"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="jobForm.remark"
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
  import * as jobAPI from '../../../api/system/job/index'
  import * as deptAPI from '../../../api/system/dept/index'

  export default {
    name: 'jobModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 部门树列表
        deptTreeList: [],
        // 新增界面数据
        jobForm: {
          id: '',
          name: '',
          deptId: null,
          sort: 1,
          remark: '',
        },
        // 验证规则
        jobFormRules: {
          name: [{required: true, message: '请输入岗位名称', trigger: 'blur'}],
          deptId: [{required: true, message: '请选择部门', trigger: 'blur'}],
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
        // 初始化岗位树列表
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
          jobAPI.getJobById(id).then(res => {
            console.log(res)
            const job = res.data
            this.jobForm.id = job.id
            this.jobForm.name = job.name
            this.jobForm.deptId = job.dept.id
            this.jobForm.sort = job.sort
            this.jobForm.remark = job.remark
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.jobForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        console.log(this.$refs.jobDrawer)
        this.$refs.jobDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.jobForm.validate(async (valid) => {
          if (valid) {
            if (!this.jobForm.id) {
              // 异步添加
              await jobAPI.addJob(this.jobForm)
            } else {
              // 异步修改
              await jobAPI.updateJob(this.jobForm.id, this.jobForm)
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
