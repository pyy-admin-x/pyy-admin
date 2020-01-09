<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="pageDrawer" size="30%">
    <div class="model-content">
      <el-form :model="pageForm" :rules="pageFormRules" ref="pageForm" label-width="80px" size="small">
        <el-form-item label="所属站点" prop="siteId">
          <el-select v-model="pageForm.siteId" placeholder="请选择站点">
            <el-option v-for="item in siteList" :key="item.siteId" :label="item.siteName"
                       :value="item.siteId"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="选择模版" prop="templateId">
          <el-select v-model="pageForm.templateId" placeholder="请选择">
            <el-option v-for="item in templateList" :key="item.templateId" :label="item.templateName"
                       :value="item.templateId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="页面名称" prop="pageName">
          <el-input v-model="pageForm.pageName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="别名" prop="pageAliase">
          <el-input v-model="pageForm.pageAliase" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="访问路径" prop="pageWebPath">
          <el-input v-model="pageForm.pageWebPath" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="物理路径" prop="pagePhysicalPath">
          <el-input v-model="pageForm.pagePhysicalPath" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="数据Url" prop="dataUrl">
          <el-input v-model="pageForm.dataUrl" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="pageForm.pageType">
            <el-radio class="radio" label="0">静态</el-radio>
            <el-radio class="radio" label="1">动态</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker type="datetime" placeholder="创建时间"
                          v-model="pageForm.pageCreateTime"></el-date-picker>
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
  import * as cmsPageAPI from '../../../api/cms/page/index'
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
        // 模版列表
        templateList: [],
        siteParams: {
          siteName: '',// 站点名称
        },
        templateParams: {
          templateName: '',// 模板名称
        },
        // 新增界面数据
        pageForm: {
          pageId: '',
          siteId: '',
          templateId: '',
          pageName: '',
          pageAliase: '',
          pageWebPath: '',
          pageParameter: '',
          pagePhysicalPath: '',
          pageType: '0',
          dataUrl: '',
          pageCreateTime: new Date()
        },
        // 验证规则
        pageFormRules: {
          siteId: [{required: true, message: '请选择站点', trigger: 'blur'}],
          templateId: [{required: true, message: '请选择模版', trigger: 'blur'}],
          pageName: [{required: true, message: '请输入页面名称', trigger: 'blur'}],
          pageWebPath: [{required: true, message: '请输入访问路径', trigger: 'blur'}],
          pagePhysicalPath: [{required: true, message: '请输入物理路径', trigger: 'blur'}]
        }
      }
    },

    mounted () {
      //初始化站点列表
      this.handleQuerySiteList()
      //初始化模板列表
      this.handleQueryTemplateList()
    },
    methods: {
      // 查询站点列表
      async handleQuerySiteList () {
        const result = await cmsSiteAPI.getSiteList(this.siteParams)
        this.siteList = result.data
      },
      // 查询模板列表
      async handleQueryTemplateList () {
        const result = await cmsTemplateAPI.getTemplateList(this.templateParams)
        this.templateList = result.data
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
          cmsPageAPI.getPageById(id).then(res => {
            console.log(res)
            if (res.success) {
              const cmsPage = res.data
              this.pageForm.pageId = cmsPage.pageId
              this.pageForm.siteId = cmsPage.siteId
              this.pageForm.templateId = cmsPage.templateId
              this.pageForm.pageName = cmsPage.pageName
              this.pageForm.pageAliase = cmsPage.pageAliase
              this.pageForm.pageWebPath = cmsPage.pageWebPath
              this.pageForm.pageParameter = cmsPage.pageParameter
              this.pageForm.pagePhysicalPath = cmsPage.pagePhysicalPath
              this.pageForm.pageType = cmsPage.pageType
              this.pageForm.dataUrl = cmsPage.dataUrl
              this.pageForm.pageCreateTime = cmsPage.pageCreateTime
            }
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.pageForm.resetFields();
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        this.$refs.pageDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.pageForm.validate(async (valid) => {
          if (valid) {
            if (!this.pageForm.pageId) {
              // 异步添加
              await cmsPageAPI.addPage(this.pageForm)
            } else {
              // 异步修改
              await cmsPageAPI.updatePage(this.pageForm.pageId, this.pageForm)
            }
            this.$notify({ title: '成功',  message: '提交页面',  type: 'success'});
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
