<template>
  <div class="files_list">
    <div style="width: 100%;height: 1px;background-color: #20a0ff;margin-top: 8px;margin-bottom: 0"></div>
    <el-table
        stripe
        ref="multipleTable"
        :data="douyinHotList"
        tooltip-effect="dark"
        style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
        v-loading="loading">
      <el-table-column
          prop="rank"
          min-width="5%"
          align="left"
      >
      </el-table-column>
      <el-table-column
          label="热搜事件"
          min-width="20%"
          show-overflow-tooltip
          align="left">
        <template slot-scope="scope">
          <span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">
            {{ scope.row.title }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
          prop="count"
          label="相关热搜视频数量"
          min-width="10%"
          align="left">
      </el-table-column>
      <el-table-column
          prop="value"
          label="热度值"
          min-width="10%"
          align="left">
      </el-table-column>
    </el-table>
    <div class="file_table_footer">
      <el-button
          type="primary"
          size="mini"
          @click="searchClick">
        刷新
      </el-button>
      <span></span>
      <el-pagination
          float:right
          background
          :page-size="pageSize"
          layout="total, prev, pager, next, sizes"
          :total="totalCount"
          @current-change="currentChange"
          @size-change="pageSizeChange"
          v-show="this.douyinHotList.length>0">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {getRequest} from '../../utils/api'

export default {
  data() {
    return {
      douyinHotList: [],
      selItems: [],
      loading: false,
      currentPage: 1,
      totalCount: -1,
      pageSize: 10
    }
  },
  mounted: function () {
    this.loadDouyinHotList(1, this.pageSize);
  },

  methods: {
    searchClick() {
      this.loadDouyinHotList(1, this.pageSize);
    },

    itemClick(row) {
      this.$router.push({path: '/DYHotDetail', query: {from: this.activeName, data: row}});
    },

    //翻页
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.loadDouyinHotList(currentPage, this.pageSize);
    },

    // x条/页
    pageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadDouyinHotList(1, this.pageSize);
    },

    loadDouyinHotList(pageNum, pageSize) {
      var _this = this;
      _this.loading = true;
      var start = (pageNum - 1) * pageSize;
      var end = pageNum * pageSize;

      getRequest("/douYin/hotSearch").then(resp => {
        _this.loading = false;

        if (resp.status == 200) {
          var douyinHotList = resp.data.billboard_data;
          _this.douyinHotList = douyinHotList.slice(start, end);
          _this.totalCount = resp.data.billboard_data.length;
        } else {
          _this.$message({type: 'error', message: '数据加载失败1!'});
        }
      }, resp => {
        _this.loading = false;

        if (resp.response.status == 403) {
          _this.$message({type: 'error', message: resp.response.data});
        } else {
          _this.$message({type: 'error', message: '数据加载失败2!'});
        }
      }).catch(resp => {

        console.log("load error... " + resp);
        //压根没见到服务器
        _this.loading = false;
        _this.$message({type: 'error', message: '数据加载失败3!'});
      })
    },

    handleSelectionChange(val) {
      this.selItems = val;
    },

    handleShowLogDetail(index, row) {
      this.itemClick(row);
    },

  },
  props: ['state', 'showEdit', 'showDelete', 'activeName', 'showRestore']
}
</script>

<style type="text/css">
.files_list {
  margin-top: 20px;
}

.file_table_footer {
  display: flex;
  box-sizing: content-box;
  padding-top: 10px;
  padding-bottom: 0;
  margin-bottom: 0;
  justify-content: space-between;
}
</style>