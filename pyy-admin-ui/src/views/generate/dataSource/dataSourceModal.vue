<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="dataSourceDrawer" size="35%">

    <div class="model-content">
      <el-form ref="dataSourceForm" :rules="dataSourceFormRules"  :model="dataSourceForm" label-width="100px" size="small">
        <el-form-item label="数据源名称" prop="name">
          <el-input v-model="dataSourceForm.name" type="text" placeholder="请输入数据源名称"></el-input>
        </el-form-item>
        <el-form-item label="数据库类型" prop="dbType">
          <el-select v-model="dataSourceForm.dbType" placeholder="请选择数据库类型" style="width: 100%;">
            <el-option label="MySQL" value="MySQL"></el-option>
            <el-option label="Oracle" value="Oracle"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主机IP" prop="host">
          <el-input v-model="dataSourceForm.host" placeholder="请输入主机IP"></el-input>
        </el-form-item>
        <el-form-item label="端口号" prop="port">
          <el-input v-model="dataSourceForm.port" type="number" placeholder="请输入端口号"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="dataSourceForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="dataSourceForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="数据库名" prop="dbName">
          <el-input v-model="dataSourceForm.dbName" placeholder="请输入数据库名"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="plain" size="small" @click="handleCancel">关闭</el-button>
        <el-button type="primary" size="small":loading="testLoading" @click="handleTestConnection">测试连接</el-button>
        <el-button type="primary" size="small" @click="handleSubmit">提交</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script type="text/ecmascript-6">
  import * as generateDataSourceAPI from '../../../api/generate/dataSource/index'
  import * as generateAPI from '../../../api/generate/index'

  export default {
    name: 'addModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 数据源列表
        siteList: [],
        // 模版列表
        templateList: [],
        // 新增界面数据
        dataSourceForm: {
          id: '',
          name: '',
          dbType: '',
          host: '',
          port: '',
          username: '',
          password: '',
          dbName: ''
        },
        // 验证规则
        dataSourceFormRules: {
          name: [{required: true, message: '请选择数据源名称', trigger: 'blur'}],
          dbType: [{required: true, message: '请选择数据源类型', trigger: 'blur'}],
          host: [{required: true, message: '请输入主机IP地址', trigger: 'blur'}],
          port: [{required: true, message: '请输入端口', trigger: 'blur'}],
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}],
          dbName: [{required: true, message: '请输入数据库名称', trigger: 'blur'}],
        },
        testLoading: false, // 测试连接

      }
    },
    methods: {
      // 打开添加对话框
      openAdd () {
        this.visible = true
      },
      // 打开编辑对话框
      openEdit (id) {
        console.log(id)
        this.visible = true
        this.$nextTick(() => {
          // 异步查询
          generateDataSourceAPI.getDataSourceById(id).then(res => {
              const dataSource = res.data
              this.dataSourceForm.id = dataSource.id
              this.dataSourceForm.name = dataSource.name
              this.dataSourceForm.dbType = dataSource.dbType
              this.dataSourceForm.host = dataSource.host
              this.dataSourceForm.port = dataSource.port
              this.dataSourceForm.username = dataSource.username
              this.dataSourceForm.password = dataSource.password
              this.dataSourceForm.dbName = dataSource.dbName
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.dataSourceForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        this.$refs.dataSourceDrawer.closeDrawer()
      },
      // 测试连接
      async handleTestConnection () {
       console.log('测试连接')
        this.testLoading = true
        await generateAPI.testConnection(this.dataSourceForm);
        this.$notify({ title: '成功',  message: '测试数据源连接',  type: 'success'});
        this.testLoading = false
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.dataSourceForm.validate(async (valid) => {
          debugger
          if (valid) {
            if (!this.dataSourceForm.id) {
              // 异步添加
              await generateDataSourceAPI.addDataSource(this.dataSourceForm)
            } else {
              // 异步修改
              await generateDataSourceAPI.updateDataSource(this.dataSourceForm.id, this.dataSourceForm)
            }
            this.$notify({ title: '成功',  message: '提交数据源',  type: 'success'});
            // 关闭对话框
            this.handleCancel()
            // 刷新查询
            this.$parent.handleQuery()
          }
        });
      }
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
