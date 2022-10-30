<template>
  <div>
<!--    <div class="background">
      <img :src="imgSrc" width="100%" height="100%" alt=""/>
    </div>-->
    <div class="upload">
      <el-col :span=8>
        <el-form>
          <el-form-item label="">
            <el-upload
                class="upload-demo"
                ref="upload"
                action="/filesController/uploadFiles"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :before-upload="beforeUpload"
                :file-list="fileList"
                :auto-upload="false"
                :multiple="true">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传文件</el-button>
              <div slot="tip" class="el-upload__tip">支持多文件一次上传，单个文件最大100MB，总共不超过512MB</div>
            </el-upload>
          </el-form-item>
        </el-form>
      </el-col>
    </div>
  </div>
</template>

<script>
import {uploadFileRequest} from "../utils/api";

export default {
  data() {
    return {
      fileList: [{
        name: '示例文件.jpeg',
        url: ''
      }],
      imgSrc: require('../assets/back.jpg'),
    };
  },
  methods: {
    submitUpload() {
      this.$refs.upload.submit();
    },

    upload(item) {

      // this.$refs.upload.submit();
      var fileObj = item.file;
      // 第一步.将图片上传到服务器.
      var formData = new FormData();
      formData.append('file', fileObj);
      uploadFileRequest("/filesController/uploadFiles", formData).then(resp => {
        var json = resp.data;
        console.log(json);
      });
    },

    beforeUpload(file) {

      console.log("beforeUpload");
/*      var xls = file.name.split(".");
      if (xls[1] == "jpg") {
        this.$message.info("你上传的是图片！");
      } else {
        this.$message.info("你上传的不是图片！");
      }*/
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    }
  }
}
</script>

<style type="text/css">
.upload {
  margin-top: 20px;
}

.background {
  width: 100%;
  height: 100%; /**宽高100%是为了图片铺满屏幕 */
  z-index: -1;
  position: absolute;
}
</style>