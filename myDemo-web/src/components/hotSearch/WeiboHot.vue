<template>
  <div class="files_list">
    <div style="width: 100%;height: 1px;background-color: #20a0ff;margin-top: 8px;margin-bottom: 0"></div>
    <el-table
        stripe
        ref="multipleTable"
        :data="weiboHotList"
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
            {{ scope.row.word }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
          prop="category"
          label="热搜分类"
          min-width="10%"
          align="left">
      </el-table-column>
      <el-table-column
          prop="num"
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
          v-show="this.weiboHotList.length>0">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {getRequest} from '../../utils/api'

export default {
  data() {
    return {
      weiboHotList: [],
      selItems: [],
      loading: false,
      currentPage: 1,
      totalCount: -1,
      pageSize: 10,
      keywords: '',
      dustbinData: [],
      categories: [],
      fileName: '',
      fileType: '',
      fileSize: '',
      filePtah: '',
      uploadTime: '',
      editTime: '',
      attrUser: '',
      categoryId: '',
      categoryName: '',
      downloadTimes: 0,
      uploadTimeStart: '',
      uploadTimeEnd: '',
      editTimeStart: '',
      editTimeEnd: ''
    }
  },
  mounted: function () {
    var _this = this;
    this.loading = true;
    this.loadWeiboHotList(1, this.pageSize);
/*    window.bus.$on('blogTableReload', function () {
      _this.loading = true;
      _this.loadWeiboHotList(_this.currentPage, _this.pageSize);
    })*/
  },

  methods: {
    searchClick() {
      this.loadWeiboHotList(1, this.pageSize);
    },

    itemClick(row) {

      var url = "https://s.weibo.com/weibo?q=%23" + row.word + "%23&topic_ad=";
      console.log("hotSearch.url = " + url);
      window.open(url); // 在新标签页中打开
      // javascript:window.location.href='http://baidu.com'; // 在当前标签页中打开
    },

    //翻页
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.loadWeiboHotList(currentPage, this.pageSize);
    },

    // x条/页
    pageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadWeiboHotList(1, this.pageSize);
    },

    loadWeiboHotList(pageNum, pageSize) {
      var _this = this;
      _this.loading = true;
      var start = (pageNum - 1) * pageSize;
      var end = pageNum * pageSize;

      getRequest("/weibo/hotSearch").then(resp => {
        _this.loading = false;

        if (resp.status == 200) {
          var weiboHotList = resp.data.data.realtime;
          _this.weiboHotList = weiboHotList.slice(start, end);
          _this.totalCount = resp.data.data.realtime.length;
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