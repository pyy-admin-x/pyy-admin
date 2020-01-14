<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="sysUserDrawer" size="30%">
    <div class="model-content">
      <el-form :model="sysUserForm" :rules="sysUserFormRules" ref="sysUserForm" label-width="80px" size="small">
        <el-form-item label="用户名" prop="username">
           <el-input v-model="sysUserForm.username" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="用户昵称" prop="name">
          <el-input v-model="sysUserForm.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <treeselect v-model="sysUserForm.deptId"
                      :options="deptTreeList"
                      :default-expand-level="1"
                      :show-count="true"
                      placeholder="请选择部门"/>
        </el-form-item>
        <el-form-item label="岗位" prop="jobId">
          <el-select v-model="sysUserForm.jobId" clearable placeholder="请选择岗位">
            <el-option
              v-for="(job, index) in jobList" :key="index"
              :label="job.name"
              :value="job.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
           <el-input v-model="sysUserForm.email" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
           <el-input v-model="sysUserForm.phone" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="sysUserForm.status" label="0">正常</el-radio>
          <el-radio v-model="sysUserForm.status" label="1">锁定</el-radio>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="sysUserForm.roleIds" clearable multiple collapse-tags placeholder="请选择角色">
            <el-option
              v-for="(role, index) in roleList" :key="index"
              :label="role.name"
              :value="role.id">
            </el-option>
          </el-select>
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
  import * as sysUserAPI from '../../../api/system/user/index'
  import * as jobAPI from '../../../api/system/job/index'
  import * as deptAPI from '../../../api/system/dept/index'
  import * as roleAPI from '../../../api/system/role/index'

  export default {
    name: 'sysUserModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 部门树列表
        deptTreeList: [],
        // 岗位列表
        jobList: [],
        // 角色列表
        roleList: [],
        // 新增界面数据
        sysUserForm: {
          id: '',
          name: '',
          username: '',
          deptId: null,
          jobId: null,
          roleIds:[],
          email: '',
          phone: '',
          avatar: '',
          status: '0',
        },
        // 验证规则
        sysUserFormRules: {
          name: [{required: true, message: '请输入名称', trigger: 'blur'}],
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}],
          email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
          deptId: [{required: true, message: '请选择部门', trigger: 'blur'}],
          jobId: [{required: true, message: '请选择岗位', trigger: 'blur'}],
          roleIds: [{required: true, message: '请选择角色', trigger: 'blur'}],
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
      // 查询岗位列表
      async handleQueryJobList () {
        const result = await jobAPI.getJobList()
        this.jobList = result.data
      },
      // 查询角色列表
      async handleQueryRoleList() {
        const result = await roleAPI.getRoleList()
        this.roleList = result.data
      },
      // 打开添加对话框
      openAdd () {
        // 初始化岗位树列表
        this.handleQueryDeptTreeList()
        // 初始化岗位列表
        this.handleQueryJobList()
        // 初始化角色列表
        this.handleQueryRoleList()
        this.visible = true
      },
      // 打开编辑对话框
      openEdit (id) {
        // 初始化岗位树列表
        this.handleQueryDeptTreeList()
        // 初始化岗位列表
        this.handleQueryJobList()
        // 初始化角色列表
        this.handleQueryRoleList()
        this.visible = true
        this.$nextTick(() => {
          // 异步查询
          sysUserAPI.getSysUserById(id).then(res => {
            console.log(res)
            const sysUser = res.data
            this.sysUserForm.id = sysUser.id
            this.sysUserForm.name = sysUser.name
            this.sysUserForm.username = sysUser.username
            this.sysUserForm.deptId = sysUser.dept.id
            this.sysUserForm.jobId = sysUser.job.id
            this.sysUserForm.email = sysUser.email
            this.sysUserForm.phone = sysUser.phone
            this.sysUserForm.avatar = sysUser.avatar
            this.sysUserForm.status = sysUser.status
            let roleIds = []
            sysUser.roleList.map(role => {
              roleIds.push(role.id)
            })
            this.sysUserForm.roleIds = roleIds
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.sysUserForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        console.log(this.$refs.sysUserDrawer)
        this.$refs.sysUserDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.sysUserForm.validate(async (valid) => {
          if (valid) {
            if (!this.sysUserForm.id) {
              // 异步添加
              await sysUserAPI.addSysUser(this.sysUserForm)
            } else {
              // 异步修改
              await sysUserAPI.updateSysUser(this.sysUserForm.id, this.sysUserForm)
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
