<template>
  <div class="course">
    <el-row>
      <el-col :span="24" class="search-form">
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
          <el-form-item label="课程名称">
            <el-input v-model="formInline.user" placeholder="请输入课程名称"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="4" v-for="(o, index) in 11" :key="index">
        <el-card :body-style="{ padding: '3px', }" shadow="always">
          <img src="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=228896427,2980351437&fm=26&gp=0.jpg" class="image">
          <div style="padding: 14px;">
            <span>Docker入门到精通</span>
            <div class="bottom clearfix">
              <time class="time">{{ currentDate | dateFormat}}</time>
              <el-button type="text" class="button">课程管理</el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="pager-toolbar">
        <!-- 分页工具栏 -->
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="currentPage"
                       :page-sizes="[12, 24, 26, 48]"
                       :page-size="size"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="total" style="margin-top: 15px">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
      data() {
        return {
          currentDate: new Date(),
          total: 0,
          currentPage: 1,
          page: 1,//页码
          size: 5,//每页显示个数
          formInline: {
            user: '',
            region: ''
          }
        }
      },
      methods: {
        onSubmit() {
          console.log('submit!');
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
      }
    }
</script>

<style scoped>
  .el-col-4{
    padding: 20px;
    cursor: pointer;
  }
  .el-card.is-always-shadow:hover {
    -webkit-box-shadow: 0 5px 12px 0 rgba(0,0,0,.1);
    box-shadow: 0 5px 12px 0 rgba(0,0,0,.1);
  }
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    height: 120px;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
