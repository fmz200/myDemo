<template>
  <el-card style="width: 500px" v-loading="loading">
    <div>
      <div style="text-align: left">
        <el-form :model="passwordForm" label-position="top" ref="passwordForm"
                 :rules="[{type: 'password', message: '密码格式不对哦!'}]"
                 style="color:#20a0ff;font-size: 14px;">
          <el-form-item label="旧密码:" prop="oldPassword">
            <el-input type="password" v-model="passwordForm.oldPassword" auto-complete="off" size="mini"
                      style="width: 300px"
                      show-password
                      placeholder="请输入旧密码">
            </el-input>
          </el-form-item>
          <el-form-item label="新密码:" prop="newPassword">
            <el-input type="password" v-model="passwordForm.newPassword" auto-complete="off" size="mini"
                      style="width: 300px"
                      show-password
                      placeholder="请输入新密码">
            </el-input>
          </el-form-item>
          <el-form-item label="确认新密码:" prop="conPassword">
            <el-input type="password" v-model="passwordForm.conPassword" auto-complete="off" size="mini"
                      style="width: 300px"
                      show-password
                      placeholder="请再次输入新密码">
            </el-input>
          </el-form-item>
        </el-form>
        <div>
          <el-button type="primary" @click="submitForm('passwordForm')" size="mini">确定</el-button>
        </div>
      </div>
    </div>
  </el-card>
</template>
<script>
import {putRequest} from '../../utils/api'
import {isEmpty} from "../../utils/utils";

export default {
  data() {
    return {
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        conPassword: '',
      },
      loading: false
    }
  },

  methods: {
    submitForm(formName) {
      var _this = this;
      var oldPassword = _this.passwordForm.oldPassword;
      if (isEmpty(oldPassword)){
        _this.$message({type: 'info', message: "请输入旧密码！"});
        return;
      }
      var newPassword = _this.passwordForm.newPassword;
      if (isEmpty(newPassword)){
        _this.$message({type: 'info', message: "请输入新密码！"});
        return;
      }
      var conPassword = _this.passwordForm.conPassword;
      if (isEmpty(conPassword)){
        _this.$message({type: 'info', message: "请输入再次输入新密码！"});
        return;
      }
      var param = {
        oldPassword: oldPassword,
        newPassword: newPassword,
        conPassword: conPassword,
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          _this.loading = true;
          putRequest("/updateUserPassword", param).then(resp => {
            _this.loading = false;
            if (resp.status == 200) {
              _this.$message({type: resp.data.status, message: resp.data.msg});
            } else {
              _this.$message({type: 'error', message: '修改失败【400】!'});
            }
          }, resp => {
            _this.loading = false;
            _this.$message({type: 'error', message: '修改失败【401】!'});
          });
        } else {
          _this.$message({type: 'error', message: '修改失败【402】!'})
          return false;
        }
      });
    }
  }
}
</script>
