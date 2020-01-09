<template>
  <div class="page-container">
    <!-- 左侧导航 -->
    <div class="left-container">
      <div class="toolbar">
        <el-card class="box-card">
          <el-form :inline="true" :model="dataSourceForm" :rules="dataSourceFormRules" ref="dataSourceForm" class="toolbar-form-inline" size="small">
            <el-form-item label="" prop="currentDataSourceId">
              <el-select v-model="dataSourceForm.currentDataSourceId" placeholder="请选择数据源">
                <el-option v-for="(item, index) in dataSources" :key="index"
                           :label="item.name"
                           :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="small" style="width:100%" type="primary" @click="getTables" :loading="tableLoading" icon="el-icon-circle-check">
                选择要生成的表
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 5px;">
          <el-input placeholder="关键字过滤" v-model="filterText" size="small" style="padding-top:5px;padding-bottom:5px;"/>
          <div class="left-tree">
            <el-tree class="tree" v-loading="treeLoading" :data="treeData" element-loading-text="拼命加载中"
                     ref="tree" size="medium" @node-click="selectTableChange" :filter-node-method="filterNode">
                <span class="custom-tree-node" slot-scope="{ node, data }">
                  <span>  <svg-icon icon-class="table"/> {{ data.name }} </span>
                </span>
            </el-tree>
          </div>
        </el-card>
      </div>
    </div>
    <!-- 右侧内容 -->
    <div class="right-container">
      <!-- 表基本信息 -->
      <div class="base-info">
        <el-form ref="tableForm" class="tableForm" :model="tableModel" :inline="true" label-width="60px"
                 size="small">
          <el-form-item label="表名">
            <el-input v-model="tableModel.name" :readonly="true" ></el-input>
          </el-form-item>
          <el-form-item label="类名">
            <el-input v-model="tableModel.className" ></el-input>
          </el-form-item>
          <el-form-item label="描述" >
            <el-input v-model="tableModel.description" ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!-- 表列信息 -->
      <div class="column-info">
        <el-table :data="tableModel.columns" class="right-table" size="mini" height="450" max-height="500" border>
          <el-table-column
            type="index"
            width="35">
          </el-table-column>
          <el-table-column prop="name" label="主键" width="45">
            <template slot-scope="scope">
              <span v-if="scope.row.primaryKey">
                <svg-icon icon-class="key" style="color:#CBA623;"/>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="字段名" />
          <el-table-column prop="fieldName" label="属性名" />
          <el-table-column prop="dataType" label="数据类型" />
          <el-table-column prop="javaType" label="Java类型" />
          <el-table-column prop="length" label="长度" width="50"/>
          <el-table-column prop="precision" label="精度" width="50"/>
          <el-table-column prop="javaType" label="非空" width="50">
            <template slot-scope="scope">
              <span v-if="!scope.row.nullable" type="success">✔</span>
              <span v-else type="danger">✘</span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
        </el-table>
      </div>
      <!-- 生成配置信息 -->
      <div class="option-info">
        <el-form ref="optionForm" class="optionForm" :inline="true" size="small" label-width="60px">
          <span>
            <el-form-item label="作者">
              <el-input v-model="generateModel.author" placeholder="如：张三" style="width: 100px !important;"/>
            </el-form-item>
            <el-form-item label="包名">
              <el-input v-model="generateModel.basePackage" placeholder="如：com.thtf">
                <el-button type="file" slot="append" @click="chooseBasePackage"><svg-icon icon-class="folder"/></el-button>
              </el-input>
            </el-form-item>
            <el-form-item label="路径">
              <el-input v-model="generateModel.outPutFolderPath" placeholder="生成代码存放目录">
                <el-button type="file" slot="append" @click="chooseOutputFolder"><svg-icon icon-class="folder"/></el-button>
              </el-input>
            </el-form-item>

            <el-button size="small" type="primary"
                       :disabled="disabledGenerateBtn"
                       :loading="generateLoading"
                       icon="el-icon-download"
                       @click="generateCode">生成代码</el-button>
          </span>
        </el-form>
      </div>
    </div>
    <!-- 数据源配置界面 -->
    <datasource-dialog ref="datasourceDialog" v-if="datasourceVisible"></datasource-dialog>
    <!-- 表格数据选择界面 -->
    <code-generate-dialog title="选择要生成的表" ref="selectTableDialog" v-if="selectTableDialogVisible"
                         :data="selectTableData" :columns="selectTableColumns" @selectionChange="tableSelectionChange"
                         :showHeader="true">
    </code-generate-dialog>
  </div>
</template>

<script>
  import * as generateDataSourceAPI from '../../../api/generate/dataSource/index'
  import * as generateAPI from '../../../api/generate/index'
  import CodeGenerateDialog from "./codeGenerateModal.vue"
  import {mapGetters, mapActions} from 'vuex'

  export default {
    data() {
      return {
        treeLoading: false,
        tableLoading: false,
        generateLoading: false,
        datasourceVisible: false,
        selectTableDialogVisible: false,
        disabledGenerateBtn: true,
        filterText: "",
        selectTableData: null,
        tableModel: {},
        generateModel: {
          author: '',
          basePackage: '',
          outPutFolderPath: '',
          connParam: null,
          tableModels: null
        },
        dataSources: [],
        dataSourceForm: {
          currentDataSourceId: '', // 当前数据源ID
        },
        // 验证规则
        dataSourceFormRules: {
          currentDataSourceId: [{required: true, message: '请选择数据源', trigger: 'blur'}],
        },
        dataSourceParams: {
          username: '',// 数据库用户名
          dbName: '',// 数据源名称
        },
        treeData: null,
        selectTableColumns: [
          {
            prop: "name",
            label: "名称"
          },
          {
            prop: "description",
            label: "描述"
          }
        ]
      }
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val)
      }
    },
    computed: {
      ...mapGetters(['currentDataSource'])
    },
    mounted() {
      this.getDataSources()
    },
    methods: {
      // 查询数据源列表
      async getDataSources() {
        const result = await generateDataSourceAPI.getDataSourceList(this.dataSourceParams)
        this.dataSources = result.data
      },
      // 过滤表显示
      filterNode(value, data) {
        if (!value) return true
        return data.name.indexOf(value) !== -1
      },
      // 获取要生成的表
      getTables() {

        // 表单验证
        this.$refs.dataSourceForm.validate((valid) => {
          if (valid) {
            this.tableLoading = true
            // 根据ID查询数据源 - 存入vuex
            this.$store.dispatch('dataSource/getDataSourceById', this.dataSourceForm.currentDataSourceId).then(async (res) => {
              // 根据数据源获取数据库表信息
              console.log(this.currentDataSource)
              const result = await generateAPI.getTables(this.currentDataSource);
              this.selectTableDialogVisible = true
              this.selectTableData = result.data
              this.$nextTick(() => {
                this.$refs.selectTableDialog.init()
              })
            }).catch(error => {
              console.log('查询数据源失败')
            })

            this.tableLoading = false
          }
        })
      },
      // 选择要生成的表
      async tableSelectionChange(selections) {
        this.treeLoading = true
        let params = {
          dataSource: this.currentDataSource,
          tableModels: selections
        };
        const result = await generateAPI.getGenerateModel(params)
        this.generateModel = result.data
        this.treeData = this.generateModel.tableModels
        this.generateModel.outPutFolderPath = 'C:\\thtf\\output'
        this.disabledGenerateBtn = false
        this.treeLoading = false
      },
      // 选择查看表信息
      selectTableChange(data) {
        this.tableModel = data
      },
      // 选择代码输出目录
      chooseBasePackage() {
        this.generateModel.basePackage = 'com.thtf'
      },
      // 选择代码输出目录
      chooseOutputFolder() {
        this.generateModel.outPutFolderPath = 'C:\\thtf\\output'
      },
      // 生成代码
      async generateCode() {
        this.generateLoading = true;
        const result = await generateAPI.generateModels(this.generateModel)
        this.$message({message: '代码生成完成', type: 'success'})
        this.generateLoading = false
      }
    },
    components: {
      CodeGenerateDialog
    }
  }
</script>

<style>
  .page-container {
    position: absolute;
    top: 60px;
    bottom: 5px;
    width: 98%;
  }

  .left-container {
    padding: 4px;
    float: left;
    width: 24%;
    height: 100%;
  }
  .left-container .el-input.el-input--small.el-input--suffix {
    width: 170px !important;
  }

  .right-container {
    padding-top: 1px;
    padding-bottom: 6px;
    left: 200px;
    float: right;
    width: 75%;
    height: 100%;
  }

  .page-container > div {
    border-color: rgba(173, 180, 180, 0.5);
    border-width: 1px;
    border-style: solid;
  }

  .base-info,
  .column-info,
  .option-info {
    padding-top: 15px;
    border-color: rgba(173, 180, 180, 0.2);
    border-width: 1px;
    border-style: solid;
  }

  .base-info,
  .column-info,
  .option-info {
    margin: 5px;
  }

  .tableForm,
  .optionForm {
    text-align: left;
  }

  .tree {
    padding-top: 10px;
  }

</style>
