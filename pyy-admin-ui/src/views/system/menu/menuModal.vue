<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    @close="resetForm"
    :wrapperClosable="false" ref="menuDrawer" size="30%">
    <div class="model-content">
      <el-form ref="menuForm" :model="menuForm" :rules="menuFormRules" size="small" label-width="80px">
        <el-form-item label="菜单类型">
          <el-radio-group v-model="menuForm.type" size="mini" style="width: 178px">
            <el-radio-button label="1">目录</el-radio-button>
            <el-radio-button label="2">菜单</el-radio-button>
            <el-radio-button label="3">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="menuForm.type != 3" label="菜单名称" prop="name">
          <el-input v-model="menuForm.name"  placeholder="请输入菜单名称"/>
        </el-form-item>
        <el-form-item v-else-if="menuForm.type == 3" label="按钮名称" prop="name">
          <el-input v-model="menuForm.name" placeholder="请输入按钮名称" />
        </el-form-item>
        <el-form-item v-show="menuForm.type != 3" label="菜单图标">
          <el-popover
            placement="bottom-start"
            width="450"
            trigger="click"
            @show="$refs['iconSelect'].reset()">
            <IconSelect ref="iconSelect" @selected="selected" />
            <el-input slot="reference" v-model="menuForm.icon" placeholder="点击选择图标" clearable>
              <svg-icon v-if="menuForm.icon" slot="prefix" :icon-class="menuForm.icon" class="el-input__icon" style="height: 32px;width: 16px;" />
              <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item v-show="menuForm.type != 3" label="外链菜单">
          <el-radio-group v-model="menuForm.iframe" size="mini">
            <el-radio-button label="1">是</el-radio-button>
            <el-radio-button label="0">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="menuForm.type == 2" label="菜单缓存">
          <el-radio-group v-model="menuForm.cache" size="mini">
            <el-radio-button label="1">是</el-radio-button>
            <el-radio-button label="0">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="menuForm.type != 3" label="菜单隐藏">
          <el-radio-group v-model="menuForm.hidden" size="mini">
            <el-radio-button label="1">是</el-radio-button>
            <el-radio-button label="0">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="menuForm.type != 1" label="权限标识">
          <el-input :disabled="menuForm.iframe === 'true'" v-model="menuForm.permission" placeholder="权限标识，如：system:user:list"/>
        </el-form-item>
        <el-form-item v-if="menuForm.type != 3" label="路由地址" prop="path">
          <el-input v-model="menuForm.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="菜单排序">
          <el-input-number v-model.number="menuForm.sort" :min="0" :max="999" controls-position="right" />
        </el-form-item>
        <el-form-item v-show="menuForm.iframe == 0 && menuForm.type == 2" label="组件名称">
          <el-input v-model="menuForm.componentName"  placeholder="匹配组件内Name字段"/>
        </el-form-item>
        <el-form-item v-show="menuForm.iframe === 0 && menuForm.type == 2" label="组件路径">
          <el-input v-model="menuForm.componentPath"  placeholder="请输入组件路径"/>
        </el-form-item>
        <el-form-item label="上级类目" :rules="[{ required: menuForm.type != 1, message: '请选择上级类目', trigger: 'blur' }]">
          <treeselect v-model="menuForm.parentId" :options="menuTreeList" placeholder="选择上级类目" />
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
  import IconSelect from '@/components/IconSelect'
  import * as menuAPI from '../../../api/system/menu/index'

  export default {
    name: 'addModal',
    data () {
      return {
        title: '',
        visible: false,
        confirmLoading: false,
        // 菜单树列表
        menuTreeList: [],
        // 新增界面数据
        menuForm: {
          id: '',
          name: '',
          sort: 1,
          path: '',
          componentPath: '',
          componentName: '',
          iframe: 0,
          roles: [],
          parentId: null,
          icon: '',
          cache: 0,
          hidden: 0,
          type: 1,
          permission: ''
        },
        // 验证规则
        menuFormRules: {
          name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
          path: [{ required: true, message: '请输入地址', trigger: 'blur' }]
        }
      }
    },
    computed: {
    },
    watch: {
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
      openAdd (id) {
        // 初始化菜单树列表
        this.handleQueryMenuTreeList()
        this.menuForm.parentId = id
        this.visible = true
      },
      // 打开编辑对话框
      openEdit (id) {
        // 初始化菜单树列表
        this.handleQueryMenuTreeList()
        this.visible = true
        this.$nextTick(() => {
          // 异步查询
          menuAPI.getMenuById(id).then(res => {
            console.log(res)
              const menu = res.data
              this.menuForm.id = menu.id
              this.menuForm.name = menu.name
              this.menuForm.sort = menu.sort
              this.menuForm.path = menu.path
              this.menuForm.componentPath = menu.componentPath
              this.menuForm.componentName = menu.componentName
              this.menuForm.iframe = menu.iframe
              this.menuForm.parentId = menu.parentId
              this.menuForm.icon = menu.icon
              this.menuForm.cache = menu.cache
              this.menuForm.hidden = menu.hidden
              this.menuForm.type = menu.type
              this.menuForm.permission = menu.permission
          })
        })
      },
      // 重置form
      resetForm () {
        this.$refs.menuForm.resetFields();
        this.menuForm = {
          id: '',
          name: '',
          sort: 1,
          path: '',
          componentPath: '',
          componentName: '',
          iframe: 0,
          roles: [],
          parentId: null,
          icon: '',
          cache: 0,
          hidden: 0,
          type: 1,
          permission: ''
        }
      },
      // 关闭Drewer对话框
      handleCancel () {
        this.resetForm()
        this.$refs.menuDrawer.closeDrawer()
      },
      // 提交
      handleSubmit () {
        // 表单验证
        this.$refs.menuForm.validate(async (valid) => {
          if (valid) {
            if (!this.menuForm.id) {
              // 异步添加
              await menuAPI.addMenu(this.menuForm)
            } else {
              // 异步修改
              await menuAPI.updateMenu(this.menuForm.id, this.menuForm)
            }
            this.$notify({ title: '成功',  message: '提交',  type: 'success'});
            // 关闭对话框
            this.handleCancel()
            // 刷新查询
            this.$parent.handleQuery()
          }
        });
      },
      selected(name) {
        this.menuForm.icon = name
      },
    },
    components: {
      Treeselect,
      IconSelect
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
