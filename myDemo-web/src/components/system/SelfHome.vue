<template xmlns="" xmlns="">
  <el-container class="self_home">
    <el-main class="main">
      <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
        <el-tab-pane label="修改密码" name="password">
          <password></password>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>
<script>
  import password from '@/components/system/Password'
  import {getRequest} from '../../utils/api'
  export default {
    mounted: function () {
      var _this = this;
      getRequest("/isAdmin").then(resp=> {
        if (resp.status == 200) {
          _this.isAdmin = resp.data;
        }
      })
    },
    data() {
      return {
        activeName: 'post',
        isAdmin: false
      };
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    },
    components: {
      'password': password
    }
  };
</script>
<style>
  .self_home > .header {
    background-color: #ececec;
    margin-top: 10px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .self_home > .main {
    /*justify-content: flex-start;*/
    display: flex;
    flex-direction: column;
    padding-left: 0px;
    background-color: #fff;
    padding-top: 0px;
    margin-top: 8px;
  }
</style>
