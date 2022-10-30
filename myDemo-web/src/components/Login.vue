<template xmlns="" xmlns="" xmlns="" xmlns="" xmlns="">
  <div class="login-back">
    <el-form :rules="rules" class="login-container" label-position="left"
             label-width="0px" v-loading="loading">
      <h3 class="login_title">系统登录</h3>
      <el-form-item prop="account">
        <el-input type="text" id="login-input" v-model="loginForm.username" auto-complete="off"
                  placeholder="账号">
        </el-input>
      </el-form-item>
      <el-form-item prop="checkPass">
        <el-input type="password" id="login-input-password" v-model="loginForm.password" auto-complete="off"
                  placeholder="密码">
        </el-input>
      </el-form-item>
      <el-checkbox class="login_remember" v-model="checked" label-position="left">记住密码</el-checkbox>
      <el-form-item style="width: 100%">
        <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%; background: transparent;">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import {postRequest} from '../utils/api'
import {isEmpty} from "../utils/utils";

export default {
  data() {
    return {
      rules: {
        account: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        checkPass: [{required: true, message: '请输入密码', trigger: 'blur'}]
      },
      checked: true,
      loginForm: {
        username: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    submitClick: function () {
      var _this = this;
      var username = this.loginForm.username;
      var password = this.loginForm.password;
      if (isEmpty(username)) {
        this.$message({type: 'info', message: '请输入账号!'});
        return;
      }
      if (isEmpty(password)) {
        this.$message({type: 'info', message: '请输入密码!'});
        return;
      }
      this.loading = true;
      postRequest('/login', {
        username: this.loginForm.username,
        password: this.loginForm.password
      }).then(resp => {
        _this.loading = false;
        if (resp.status == 200) {
          //成功
          var json = resp.data;
          if (json.status == 'success') {
            _this.$router.replace({path: '/home'});
          } else {
            _this.$alert('登录失败!', '失败!');
          }
        } else {
          //失败
          _this.$alert('登录失败!', '失败!');
        }
      }, resp => {
        _this.loading = false;
        _this.$alert('找不到服务器⊙﹏⊙∥!', '失败!');
      });
    }
  }
}
</script>
<style>
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
}

.login-back {
  width: 100%;
  height: 100%;
  position: fixed;
  margin: -8px;
  background-size: 100% 100%;
  background-image: url("../assets/logIn/back02.jpg");
}

.login_title {
  margin: 0 auto 40px auto;
  text-align: center;
  color: white;
}

.login_remember {
  margin: 0 0 35px 0;
  text-align: left;
}

#login-input {
  background: transparent;
  color: white;
}

#login-input-password {
  background: transparent;
  color: white;
}


</style>
