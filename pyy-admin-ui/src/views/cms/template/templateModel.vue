<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="templateDrawer" size="35%">

    <div class="model-content">
      <el-form :model="templateForm" :rules="templateFormRules" ref="templateForm" label-width="110px" size="small">
        <el-form-item label="所属站点" prop="templateId">
          <el-select v-model="templateForm.siteId" placeholder="请选择站点">
            <el-option v-for="(item, index) in siteList" :key="index" :label="item.siteName"
                       :value="item.siteId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模版名称" prop="templateName">
          <el-input v-model="templateForm.templateName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="模版参数" prop="templateParameter">
          <el-input v-model="templateForm.templateParameter" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="模版文件ID" prop="templateFileId">
          <el-input v-model="templateForm.templateFileId" auto-complete="off" placeholder="请选择模板文件" :disabled="true"></el-input>
          <el-upload
            class="upload-template-file"
            action="http://localhost:31001/cms/template/upload"
            :limit="1"
            :on-success="uploadSuccess"
            :file-list="fileList">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
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
  import * as cmsTemplateAPI from '../../../api/cms/template/index'

  export default {
    name: 'addModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 站点列表
        siteList: [],
        // 模板文件列表
        fileList: [],
        // 新增界面数据
        templateForm: {
          siteId: '',
          templateId: '',
          templateName: '',
          templateParameter: '',
          templateFileId: '',
        },
        // 验证规则
        templateFormRules: {
          templateName: [{required: true, message: '请输入模板名称', trigger: 'blur'}],
          templateFileId: [{required: true, message: '请选择模板文件', trigger: 'blur'}],
        }
      }
    },
    mounted () {
      // 初始化站点列表
      this.handlerQuerySiteList()
    },
    methods: {
      // 查询站点列表
      async handlerQuerySiteList () {
        const result = await cmsSiteAPI.getSiteList(this.siteParams)
        this.siteList = result.data
      },
      // 模板文件上传
      uploadSuccess (response) {
        console.log(response)
        const {data} = response
        this.templateForm.templateFileId = data
      },
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
          cmsTemplateAPI.getTemplateById(id).then(res => {
            console.log(res)
            if (res.success) {
              const cmsTemplate = res.data
              this.templateForm.siteId = cmsTemplate.siteId
              this.templateForm.templateId = cmsTemplate.templateId
              this.templateForm.templateName = cmsTemplate.templateName
              this.templateForm.templateParameter = cmsTemplate.templateParameter
              this.templateForm.templateFileId = cmsTemplate.templateFileId
            }
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.templateForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        this.$refs.templateDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.templateForm.validate(async (valid) => {
          if (valid) {
            if (!this.templateForm.templateId) {
              // 异步添加
              await cmsTemplateAPI.addTemplate(this.templateForm)
            } else {
              // 异步修改
              await cmsTemplateAPI.updateTemplate(this.templateForm.templateId, this.templateForm)
            }
            this.$notify({title: '成功', message: '提交模板', type: 'success'});
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

  .upload-template-file {
    margin-top: 15px;
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
