<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="siteDrawer" size="35%">

    <div class="model-content">
      <el-form :model="siteForm" :rules="siteFormRules" ref="siteForm" label-width="110px" size="small">
        <el-form-item label="站点名称" prop="siteName">
          <el-input v-model="siteForm.siteName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="站点域名" prop="siteDomain">
          <el-input v-model="siteForm.siteDomain" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="站点端口" prop="sitePort">
          <el-input v-model="siteForm.sitePort" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="站点访问地址" prop="siteWebPath">
          <el-input v-model="siteForm.siteWebPath" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="站点物理地址" prop="sitePhysicalPath">
          <el-input v-model="siteForm.sitePhysicalPath" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker type="datetime" placeholder="创建时间"
                          v-model="siteForm.siteCreateTime"></el-date-picker>
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
  import * as cmsSiteAPI from '../../../api/cms/site/index'

  export default {
    name: 'addModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 站点列表
        siteList: [],
        // 模版列表
        templateList: [],
        // 新增界面数据
        siteForm: {
          siteId: '',
          siteName: '',
          siteDomain: '',
          sitePort: '',
          siteWebPath: '',
          sitePhysicalPath: '',
          siteCreateTime: new Date()
        },
        // 验证规则
        siteFormRules: {
          siteName: [{required: true, message: '请输入站点名称', trigger: 'blur'}],
          siteDomain: [{required: true, message: '请输入站点域名', trigger: 'blur'}],
          sitePort: [{required: true, message: '请输入站点端口', trigger: 'blur'}],
          siteWebPath: [{required: true, message: '请输入访问路径', trigger: 'blur'}],
          sitePhysicalPath: [{required: true, message: '请输入物理路径', trigger: 'blur'}]
        }
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
          cmsSiteAPI.getSiteById(id).then(res => {
            console.log(res)
            if (res.success) {
              const cmsSite = res.data
              this.siteForm.siteId = cmsSite.siteId
              this.siteForm.templateId = cmsSite.templateId
              this.siteForm.siteName = cmsSite.siteName
              this.siteForm.siteDomain = cmsSite.siteDomain
              this.siteForm.sitePort = cmsSite.sitePort
              this.siteForm.siteWebPath = cmsSite.siteWebPath
              this.siteForm.sitePhysicalPath = cmsSite.sitePhysicalPath
              this.siteForm.siteCreateTime = cmsSite.siteCreateTime
            }
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.siteForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        this.$refs.siteDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.siteForm.validate(async (valid) => {
          if (valid) {
            if (!this.siteForm.siteId) {
              // 异步添加
              await cmsSiteAPI.addSite(this.siteForm)
            } else {
              // 异步修改
              await cmsSiteAPI.updateSite(this.siteForm.siteId, this.siteForm)
            }
            this.$notify({ title: '成功',  message: '提交站点',  type: 'success'});
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
