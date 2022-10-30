<template>
  <div>
    <div style="display: flex;justify-content: flex-start; margin-top: 10px;">
      <el-col :span=8>
        <el-col :span=8>
          标题：
        </el-col>
        <el-input
            :maxlength=50
            v-model="title"
            placeholder="支持模糊查询"
            clearable
            style="width: 150px"
            size="mini">
        </el-input>
      </el-col>

      <el-col :span=8>
        <el-col :span=8>
          作者：
        </el-col>
        <el-input
            :maxlength=50
            v-model="nickname"
            placeholder="请输入作者"
            clearable
            style="width: 150px"
            size="mini">
        </el-input>
      </el-col>

      <el-col :span=8>
        <el-col :span=8>
          归属分类：
        </el-col>
        <el-select
            v-model="cid"
            clearable
            style="width: 150px"
            size="mini">
          <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.cateName"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>

      <el-col :span=8></el-col>
    </div>

    <div style="display: flex;justify-content: flex-start; margin-top: 10px;">
      <el-col :span=10>
        <el-col :span=8>
          发表时间范围：
        </el-col>
        <el-date-picker
            format="yyyy-MM-dd"
            value-format="yyyyMMdd"
            v-model="publishDateStart"
            clearable
            placeholder="开始时间"
            style="width: 150px"
            size="mini">
        </el-date-picker>
        <span>-</span>
        <el-date-picker
            format="yyyy-MM-dd"
            value-format="yyyyMMdd"
            v-model="publishDateEnd"
            clearable
            placeholder="结束时间"
            style="width: 150px"
            size="mini">
        </el-date-picker>
      </el-col>

      <el-col :span=10>
        <el-col :span=8>
          编辑时间范围：
        </el-col>
        <el-date-picker
            format="yyyy-MM-dd"
            value-format="yyyyMMdd"
            v-model="editTimeStart"
            clearable
            placeholder="开始时间"
            style="width: 150px"
            size="mini">
        </el-date-picker>
        <span>-</span>
        <el-date-picker
            format="yyyy-MM-dd"
            value-format="yyyyMMdd"
            v-model="editTimeEnd"
            clearable
            placeholder="结束时间"
            style="width: 150px"
            size="mini">
        </el-date-picker>
      </el-col>

      <el-col :span=4 align="right">
        <el-button
            type="primary"
            size="small"
            style="margin-left: 10px"
            @click="searchClick">
          搜索
        </el-button>
        <el-button
            type="info"
            size="small"
            style="margin-left: 10px"
            @click="resetParam">
          重置
        </el-button>
      </el-col>
    </div>
    <div style="width: 100%;height: 1px;background-color: #20a0ff;margin-top: 8px;margin-bottom: 0px"></div>
    <el-table
        stripe
        ref="multipleTable"
        :data="articles"
        tooltip-effect="dark"
        style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
        @selection-change="handleSelectionChange"
        v-loading="loading">
      <el-table-column
          width="35"
          align="left"
          v-if="!showEdit && !showDelete">
      </el-table-column>
      <el-table-column
          type="selection"
          width="35"
          align="left"
          v-if="showEdit || showDelete">
      </el-table-column>
      <el-table-column
          label="标题"
          width="350"
          align="left">
        <template slot-scope="scope">
          <span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">
            {{ scope.row.title }}
          </span>
          <!-- <p>{{ scope.row.summary + "..."}}</p>-->
        </template>
      </el-table-column>
      <el-table-column
          label="发表时间"
          width="140"
          align="left">
        <template slot-scope="scope">
          {{ scope.row.publishDate | formatDateTime }}
        </template>
      </el-table-column>
      <el-table-column
          label="编辑时间"
          width="140"
          align="left">
        <template slot-scope="scope">
          {{ scope.row.editTime | formatDateTime }}
        </template>
      </el-table-column>
      <el-table-column
          prop="nickname"
          label="作者"
          width="120"
          align="left">
      </el-table-column>
      <el-table-column
          prop="cateName"
          label="所属分类"
          width="120" align="left">
      </el-table-column>
      <el-table-column
          prop="pageView"
          label="浏览次数"
          width="80" align="left">
      </el-table-column>
      <el-table-column
          label="操作"
          align="left"
          v-if="showEdit || showDelete">
        <template slot-scope="scope">
          <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)" v-if="showEdit">编辑
          </el-button>
          <el-button
              size="mini"
              @click="handleRestore(scope.$index, scope.row)" v-if="showRestore">还原
          </el-button>
          <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)" v-if="showDelete">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="blog_table_footer">
      <el-button
          type="danger"
          size="mini"
          style="margin: 0px;"
          v-show="this.articles.length>0 && showDelete"
          :disabled="this.selItems.length==0"
          @click="deleteMany">
        批量删除
      </el-button>
      <span></span>
      <el-pagination
          background
          :page-size="pageSize"
          layout="total, prev, pager, next, sizes"
          :total="totalCount"
          @current-change="currentChange"
          v-show="this.articles.length>0">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {postRequest, putRequest} from '../utils/api'
import {getRequest} from '../utils/api'
import {dev} from "../../config";
//  import Vue from 'vue'
//  var bus = new Vue()

export default {
  data() {
    return {
      articles: [],
      selItems: [],
      loading: false,
      currentPage: 1,
      totalCount: -1,
      pageSize: 10,
      keywords: '',
      dustbinData: [],
      categories: [],
      title: '',
      main_code: '',
      publishDateStart: '',
      publishDateEnd: '',
      editTimeStart: '',
      editTimeEnd: '',
      nickname: '',
      cid: ''
    }
  },
  mounted: function () {
    var _this = this;
    this.loading = true;
    this.getCategories();
    this.loadBlogs(1, this.pageSize);
    var _this = this;
    window.bus.$on('blogTableReload', function () {
      _this.loading = true;
      _this.loadBlogs(_this.currentPage, _this.pageSize);
    })
  },

  methods: {

    getCategories() {
      let _this = this;
      getRequest("/admin/category/all").then(resp => {
        _this.categories = resp.data;
      });
    },

    searchClick() {
      this.loadBlogs(1, this.pageSize);
    },

    resetParam() {
      this.title = '';
      this.main_code = '';
      this.publishDateStart = '';
      this.publishDateEnd = '';
      this.editTimeStart = '';
      this.editTimeEnd = '';
      this.nickname = '';
      this.cid = '';
    },

    itemClick(row) {
      this.$router.push({path: '/blogDetail', query: {aid: row.id}})
    },

    deleteMany() {
      const selItems = this.selItems;
      for (let i = 0; i < selItems.length; i++) {
        this.dustbinData.push(selItems[i].id)
      }
      this.deleteToDustBin(selItems[0].state)
    },

    //翻页
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.loading = true;
      this.loadBlogs(currentPage, this.pageSize);
    },

    loadBlogs(page, count) {
      var _this = this;
      _this.loading = true;

      // null问题
      var start = this.publishDateStart == null ? "" : this.publishDateStart;
      var end = this.publishDateEnd == null ? "" : this.publishDateEnd;
      var editTimeStart = this.editTimeStart == null ? "" : this.editTimeStart;
      var editTimeEnd = this.editTimeEnd == null ? "" : this.editTimeEnd;
      var params = "&title=" + this.title
          + "&main_code=" + this.main_code
          + "&publishDateStart=" + start
          + "&publishDateEnd=" + end
          + "&nickname=" + this.nickname
          + "&cid=" + this.cid
      ;
      var param = {
        title: this.title,
        nickname: this.nickname,
        cid: this.cid,
        publishDateStart: start,
        publishDateEnd: end,
        editTimeStart: editTimeStart,
        editTimeEnd: editTimeEnd,
        state: this.state,
        page: page,
        count: count
      };
      var url = '';
      if (this.state == -2) {
        url = "/admin/article/all" //+ "?page=" + page + "&count=" + count + params;
      } else {
        url = "/article/all"// + "?state=" + this.state + "&page=" + page + "&count=" + count + params;
      }

      postRequest(url, param).then(resp => {
        _this.loading = false;
        if (resp.status == 200) {
          _this.articles = resp.data.articles;
          _this.totalCount = resp.data.totalCount;
        } else {
          _this.$message({type: 'error', message: '数据加载失败!'});
        }
      }, resp => {
        _this.loading = false;
        if (resp.response.status == 403) {
          _this.$message({type: 'error', message: resp.response.data});
        } else {
          _this.$message({type: 'error', message: '数据加载失败!'});
        }
      }).catch(resp => {
        console.log("load error... " + resp);
        //压根没见到服务器
        _this.loading = false;
        _this.$message({type: 'error', message: '数据加载失败!'});
      })
    },

    handleSelectionChange(val) {
      this.selItems = val;
    },
    handleEdit(index, row) {
      this.$router.push({path: '/editBlog', query: {from: this.activeName, id: row.id}});
    },

    handleDelete(index, row) {
      this.dustbinData.push(row.id);
      this.deleteToDustBin(row.state);
    },

    handleRestore(index, row) {
      let _this = this;
      this.$confirm('将该文件还原到原处，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true;
        putRequest('/article/restore', {articleId: row.id}).then(resp => {
          if (resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            if (data.status == 'success') {
              window.bus.$emit('blogTableReload')//通过选项卡都重新加载数据
            }
          } else {
            _this.$message({type: 'error', message: '还原失败!'});
          }
          _this.loading = false;
        });
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消还原'
        });
      });
    },

    handleShowLogDetail(index, row) {
      this.itemClick(row);
    },

    // 批量下载
    handleBatchDownload() {
      var selItems = this.selItems;
      var param = '';
      for (var i = 0; i < selItems.length; i++) {
        var interfaceInfo = selItems[i];
        var uuid = interfaceInfo.uuid + ',';
        param += uuid;
      }
      this.downloadFiles(param);
    },

    // 下载一个
    handleDownload(index, row) {
      this.downloadFiles(row.uuid);
    },

    downloadFiles(param) {
      let _this = this;
      this.$confirm('将该文件下载到本地，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        _this.loading = true;

        console.log("........................." + param);
        // 方法一
        window.location.href = '/interfaceLog/downloadInterfaceLogs?paramListString=' + param;

        // 方法二,打包到Java不好使
        /*var elemIF = document.createElement('iframe');
        elemIF.src = '/interfaceLog/downloadInterfaceLogs?paramListString=' + str;
        elemIF.style.display = 'none';
        document.body.appendChild(elemIF);*/

        _this.loading = false;
        _this.$message({type: 'success', message: "请求成功！若文件未下载可能是后台报错！"});
        /*        getRequest('/interfaceLog/downloadInterfaceLogs', {
                  /!*interfaceInfoBeanList: paramList,*!/
                  paramListString: JSON.stringify(paramList)
                }).then(resp => {
                  _this.loading = false;
                  if (resp.status == 200) {
                    _this.$message({type: 'success', message: "操作成功！附件未下载请查看后台日志是否报错。"});
                    var data = resp.data;
                    if (data.code == 200) {
                      _this.$message({type: 'success', message: data.msg});
                    } else {
                      _this.$message({type: 'warning', message: data.msg});
                    }
                  } else {
                    _this.$message({type: 'error', message: '下载失败!'});
                  }
                });*/
      }).catch(() => {
        _this.loading = false;
        _this.$message({
          type: 'info',
          message: '已取消下载'
        });
      });
    },

    deleteToDustBin(state) {
      var _this = this;
      this.$confirm(state != 2 ? '将该文件放入回收站，是否继续?' : '永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true;
        var url = '';
        if (_this.state == -2) {
          url = "/admin/article/dustbin";
        } else {
          url = "/article/dustbin";
        }
        putRequest(url, {aids: _this.dustbinData, state: state}).then(resp => {
          if (resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            if (data.status == 'success') {
              window.bus.$emit('blogTableReload')//通过选项卡都重新加载数据
            }
          } else {
            _this.$message({type: 'error', message: '删除失败!'});
          }
          _this.loading = false;
          _this.dustbinData = []
        }, resp => {
          _this.loading = false;
          _this.$message({type: 'error', message: '删除失败!'});
          _this.dustbinData = []
        });
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        });
        _this.dustbinData = []
      });
    }
  },
  props: ['state', 'showEdit', 'showDelete', 'activeName', 'showRestore']
}
</script>

<style type="text/css">
.blog_table_footer {
  display: flex;
  box-sizing: content-box;
  padding-top: 10px;
  padding-bottom: 0px;
  margin-bottom: 0px;
  justify-content: space-between;
}

p {
  font-size: 3px;
}
</style>