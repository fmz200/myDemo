<template>
  <div class="files_list">
    <div class="query_condition">
      <el-col :span=6>
        <span>文件名称：</span>
        <el-input
            :maxlength=50
            v-model="fileName"
            placeholder="支持模糊查询"
            clearable
            style="width: 200px"
            size="mini">
        </el-input>
      </el-col>
      <el-col :span=6>
        <span>文件类型：</span>
        <el-select
            v-model="fileTypes"
            clearable
            multiple
            style="width: 200px;"
            size="mini">
          <el-option
              v-for="item in categories"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span=6>
        <span>上传时间范围：</span>
        <el-date-picker
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            v-model="uploadTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 200px"
            :editable="false"
            size="mini">
        </el-date-picker>
      </el-col>
      <el-col :span=6>
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

    <div style="width: 100%;height: 1px;background-color: #20a0ff;margin-top: 8px;margin-bottom: 0"></div>
    <el-table
        stripe
        ref="multipleTable"
        :data="filesInfoList"
        tooltip-effect="dark"
        style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
        :row-style="{height: '0'}"
        :cell-style="{padding: '3px'}"
        @selection-change="handleSelectionChange"
        v-loading="loading">
      <el-table-column
          type="selection"
          min-width="5%"
          align="left"
      >
      </el-table-column>
      <el-table-column
          label="文件名"
          min-width="35%"
          show-overflow-tooltip
          align="left">
        <template slot-scope="scope">
          <span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">
            {{ scope.row.fileName }}
          </span>
          <!-- <p>{{ scope.row.summary + "..."}}</p>-->
        </template>
      </el-table-column>
      <el-table-column
          prop="fileType"
          label="文件类型"
          min-width="10%"
          show-overflow-tooltip
          align="left">
      </el-table-column>
      <el-table-column
          prop="fileSize"
          label="文件大小"
          min-width="10%"
          show-overflow-tooltip
          align="left">
      </el-table-column>
      <el-table-column
          label="上传时间"
          min-width="20%"
          show-overflow-tooltip
          align="left">
        <template slot-scope="scope">
          {{ scope.row.uploadTime | formatDateTime }}
        </template>
      </el-table-column>
      <el-table-column
          prop="downloadTimes"
          label="下载次数"
          min-width="15%"
          show-overflow-tooltip
          align="left">
      </el-table-column>
      <el-table-column
          label="操作"
          align="left"
          show-overflow-tooltip
          min-width="20%">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="primary"
              @click="handleDownload(scope.$index, scope.row)">
            下载
          </el-button>
          <el-button
              size="mini"
              @click="handleDelete(scope.$index, scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="file_table_footer">
      <el-button
          type="primary"
          size="mini"
          v-show="this.filesInfoList.length>0"
          :disabled="this.selItems.length==0"
          @click="handleBatchDownload">
        批量下载
      </el-button>
      <el-button
          size="mini"
          v-show="this.filesInfoList.length>0"
          :disabled="this.selItems.length==0"
          @click="deleteMany">
        批量删除
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
          v-show="this.filesInfoList.length>0">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {postRequest, putRequest} from '../utils/api'
import {getRequest} from '../utils/api'
import {isEmpty, nalValue} from "../utils/utils";

export default {
  data() {
    return {
      filesInfoList: [],
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
      fileTypes: [],
      fileSize: '',
      filePtah: '',
      uploadTime: '',
      editTime: '',
      attrUser: '',
      categoryId: '',
      categoryName: '',
      downloadTimes: 0,
      state: '',
      uploadTimeStart: '',
      uploadTimeEnd: '',
      editTimeStart: '',
      editTimeEnd: ''
    }
  },
  mounted: function () {
    var _this = this;
    this.loading = true;
    this.getCategories();
    this.loadFilesInfoList(1, this.pageSize);
    window.bus.$on('fileListReload', function () {
      _this.loading = true;
      _this.loadFilesInfoList(_this.currentPage, _this.pageSize);
    })
  },

  methods: {

    getCategories() {
      let _this = this;
      getRequest("/filesController/getFileTypes").then(resp => {
        _this.categories = resp.data.fileTypes;
      });
    },

    searchClick() {
      this.loadFilesInfoList(1, this.pageSize);
    },

    resetParam() {
      this.fileName = '';
      this.main_code = '';
      this.uploadTimeStart = '';
      this.uploadTimeEnd = '';
      this.editTimeStart = '';
      this.editTimeEnd = '';
      this.fileTypes = [];
      this.attrUser = '';
    },

    itemClick(row) {
      this.$message({type: 'info', message: '文件预览暂不支持!'});
      //this.$router.push({path: '/blogDetail', query: {aid: row.id}})
    },

    //翻页
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.loadFilesInfoList(currentPage, this.pageSize);
    },

    // x条/页
    pageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadFilesInfoList(1, this.pageSize);
    },

    loadFilesInfoList(pageNum, pageSize) {
      var _this = this;
      _this.loading = true;

      if (!isEmpty(this.uploadTime)) {
        this.uploadTimeStart = this.uploadTime[0];
        this.uploadTimeEnd = this.uploadTime[1];
      } else {
        this.uploadTimeStart = "";
        this.uploadTimeEnd = "";
      }
      if (!isEmpty(this.editTime)) {
        this.editTimeStart = this.editTime[0];
        this.editTimeEnd = this.editTime[1];
      } else {
        this.editTimeStart = "";
        this.editTimeEnd = "";
      }
      var param = {
        fileName: this.fileName,
        fileTypes: this.fileTypes,
        attrUser: this.attrUser,
        categoryId: this.categoryId,
        uploadTimeStart: this.uploadTimeStart,
        uploadTimeEnd: this.uploadTimeEnd,
        editTimeStart: this.editTimeStart,
        editTimeEnd: this.editTimeEnd,
        state: nalValue(this.state),
        pageNum: pageNum,
        pageSize: pageSize
      };
      var url = '';
      if (this.state == -2) {
        url = "/admin/filesController/all"
      } else {
        url = "/filesController/queryFilesInfo"
      }

      postRequest(url, param).then(resp => {
        _this.loading = false;
        if (resp.status == 200) {
          _this.filesInfoList = resp.data.filesInfoList;
          _this.totalCount = resp.data.totalCount;
        } else {
          _this.$message({type: 'error', message: '数据加载失败[100]!'});
        }
      }, resp => {
        _this.loading = false;
        if (resp.response.status == 403) {
          _this.$message({type: 'error', message: resp.response.data});
        } else {
          _this.$message({type: 'error', message: '数据加载失败[101]!'});
        }
      }).catch(resp => {
        console.log("load error... " + resp);
        //压根没见到服务器
        _this.loading = false;
        _this.$message({type: 'error', message: '数据加载失败[102]!'});
      })
    },

    handleSelectionChange(val) {
      this.selItems = val;
    },

    handleEdit(index, row) {
      this.$router.push({path: '/editBlog', query: {from: this.activeName, id: row.id}});
    },

    handleDelete(index, row) {
      this.dustbinData.push(row.fileId);
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
              window.bus.$emit('fileListReload')//通过选项卡都重新加载数据
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

    deleteMany() {
      var selItems = this.selItems;
      for (var i = 0; i < selItems.length; i++) {
        this.dustbinData.push(selItems[i].fileId)
      }
      this.deleteToDustBin(selItems[0].state);
    },

    // 批量下载
    handleBatchDownload() {
      var selItems = this.selItems;
      var param = '';
      for (var i = 0; i < selItems.length; i++) {
        var fileInfo = selItems[i];
        var fileId = fileInfo.fileId + ',';
        param += fileId;
      }
      this.downloadFiles(param);
    },

    // 下载一个
    handleDownload(index, row) {
      this.downloadFiles(row.fileId);
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
        window.location.href = '/filesController/downloadFiles?paramListString=' + param;
        _this.loading = false;
        _this.$message({type: 'success', message: "请求成功！若文件未下载请稍后再试！"});
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
      this.$confirm(state == 1 ? '将该文件放入回收站，是否继续?' : '永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true;
        var url = "/filesController/deleteFiles";
        postRequest(url, {fileIds: _this.dustbinData, state: state}).then(resp => {
          if (resp.status == 200) {
            var data = resp.data;
            _this.$message({type: 'success', message: data.msg});
            if (data.status == '200') {
              window.bus.$emit('fileListReload')//通过选项卡都重新加载数据
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
.files_list {
  margin-top: 20px;
}

.files_list .query_condition {
  display: flex;
  justify-content: flex-start;
  margin-top: 10px;
  font-size: 1px;
}

.file_table_footer {
  display: flex;
  box-sizing: content-box;
  padding-top: 10px;
  padding-bottom: 0;
  margin-bottom: 0;

}
</style>