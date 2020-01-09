<template>
  <div class="site-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="params" class="demo-form-inline" size="small">
      <el-form-item label="日志描述">
        <el-input v-model="params.description" placeholder="请输入日志描述"></el-input>
      </el-form-item>
      <el-form-item label="操作人名称">
        <el-input v-model="params.username" placeholder="请输入操作人名称"></el-input>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker
          v-model="params.operateTimeRange"
          type="datetimerange"
          align="right"
          size="small"
          value-format="yyyy-MM-dd HH:mm:ss"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['12:00:00', '08:00:00']"
          style="width: 350px">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click.prevent="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 日志列表 -->
    <el-table :data="list" style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="log-table-expand" size="small">
            <el-form-item label="请求方式：">
              <span>{{ props.row.requestMethod }}</span>
            </el-form-item>
            <el-form-item label="请求方法：">
              <span>{{ props.row.classPath}}.{{ props.row.actionMethod}}()</span>
            </el-form-item>
            <el-form-item label="请求URL：">
              <span>{{ props.row.actionUrl }}</span>
            </el-form-item>
            <el-form-item label="请求参数：">
              <span>{{ props.row.params }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column prop="username" label="操作人名称" />
      <el-table-column prop="requestIp" label="操作IP"/>
      <el-table-column prop="ipLocation" label="IP归属地"/>
      <el-table-column prop="description" label="操作描述" />
      <el-table-column prop="browser" label="浏览器" />
      <el-table-column prop="operatingSystem" label="操作系统" />
      <el-table-column prop="consumingTime" label="消耗时间">
        <template slot-scope="scope">
          <el-tag :type="scope.row.consumingTime >= 1000 ? 'danger' : 'success'">{{scope.row.consumingTime}}ms</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | dateFormat}}</span>
        </template>
      </el-table-column>

    </el-table>

    <!-- 分页工具栏 -->
    <el-pagination @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page="currentPage"
                   :page-sizes="[5, 10, 15, 30, 40]"
                   :page-size="size"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="total" style="margin-top: 15px">
    </el-pagination>
  </div>
</template>

<script>
  import * as operationLogAPI from '../../../api/monitor/operationLog/index'
  export default {
    data() {
      return {
        list: [], // 日志列表
        total: 0,
        currentPage: 1,
        page: 1,//页码
        size: 5,//每页显示个数
        params: {
          type: 1, // 正常日志
          description:'', // 描述
          username: '',// 操作人名称
          operateTimeRange: '', // 操作时间范围
          startTime:'', // 开始时间
          finishTime: '',// 完成时间
        },
        isShowAddModal: false, // 新增日志Drawer显示状态
      }
    },
    mounted () {
      // 默认查询日志
      this.handleQuery()
    },
    methods: {
      // 查询
      async handleQuery () {

        if (this.params.operateTimeRange) {
          this.params.startTime = this.params.operateTimeRange[0]
          this.params.finishTime = this.params.operateTimeRange[1]
        }
        delete this.params.operateTimeRange // 时间参数不传递后台
        console.log(this.params)
        const result = await operationLogAPI.getOperationLogPageList(this.page, this.size, this.params)
        const queryResult = result.data
        this.total = queryResult.total
        this.list = queryResult.records
      },
      handleSizeChange(size) {
        console.log(`每页 ${size} 条`);
        this._changePage(this.page, size)
      },
      handleCurrentChange(page) {
        console.log(`当前页: ${page}`);
        this._changePage(page, this.size)
      },
      // 分页查询
      _changePage (page, size) {
        this.page = page
        this.size = size
        this.handleQuery()
      },
    },
    components: {

    }
  }
</script>

<style>
  .log-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 100%;
  }
  .log-table-expand label {
    width: 90px;
    color: #97a8be;
  }
</style>
