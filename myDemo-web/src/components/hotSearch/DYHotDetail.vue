<template>
  <el-row v-loading="loading">
    <el-col :span="24">
      <div style="text-align: left;">
        <el-button type="text" icon="el-icon-back" @click="goBack" style="padding-bottom: 0;">返回</el-button>
      </div>
    </el-col>
    <el-col :span="24">
      <div>
        <div><h3 style="margin-top: 0;margin-bottom: 0">{{ title }}</h3></div>
        <div style="width: 100%;margin-top: 5px;display: flex;justify-content: flex-end;align-items: center">
          <span
              style="color: #20a0ff;margin-right:20px;font-size: 16px;">排名: {{ rank }}
          </span>
          <span
              style="color: #20a0ff;margin-right:20px;font-size: 16px;">热度: {{ hotValve }}
          </span>
          <span
              style="color: #20a0ff;margin-right:20px;font-size: 16px;">视频数量: {{ count }}
          </span>
          <span
              style="color: #20a0ff;margin-right:20px;font-size: 16px;">时间: {{ mapList[0].value }}
          </span>
          <span style="margin:0 50px 0 0">

          </span>
        </div>
        <div>
          <el-col :span="4" v-for="(item,index) in videoList" :key="index" align="left" class="video_style">
            <video-player
                class="video-player vjs-custom-skin"
                ref="videoPlayer"
                :playsinline="true"
                :options="playerOptions[index]"
                @play="onPlayerPlay($event)"
                @pause="onPlayerPause($event)"
                @ended="onPlayerEnded($event)"
                @waiting="onPlayerWaiting($event)"
                @playing="onPlayerPlaying($event)"
                @loadeddata="onPlayerLoadeddata($event)"
                @timeupdate="onPlayerTimeupdate($event)"
                @canplay="onPlayerCanplay($event)"
                @canplaythrough="onPlayerCanplaythrough($event)"
                @statechanged="playerStateChanged($event)"
                @ready="playerReadied"
            >
            </video-player>
            <!-- 不加这个meta会提示Failed to load resource: the server responded with a status of 403 (Forbidden)-->
            <!-- VIDEOJS: ERROR: (CODE:4 MEDIA_ERR_SRC_NOT_SUPPORTED) The media could not be loaded,
            either because the server or network failed or because the format is not supported.-->
            <meta name="referrer" content="never">
            <el-row class="title_row_style">
              <el-row class="">视频热度排行：{{ index + 1 }}</el-row>
              <span class="title_style" :title=item.title @click="itemClick(item)">
                {{ item.title.length > 70 ? item.title.substring(0, 70) + "......" : item.title }}
              </span>
            </el-row>
          </el-col>
        </div>
      </div>
    </el-col>
    <el-col>
    </el-col>
  </el-row>
</template>
<script>

import {postRequest} from "../../utils/api";

export default {
  methods: {
    goBack() {
      this.$router.go(-1);
    },

    itemClick(row) {
      window.open(row.link);
    },

    // 播放回调
    onPlayerPlay(player) {
      console.log('player play!', player)
    },

    // 暂停回调
    onPlayerPause(player) {
      console.log('player pause!', player)
    },

    // 视频播完回调
    onPlayerEnded($event) {
      console.log($event)
    },

    // DOM元素上的readyState更改导致播放停止
    onPlayerWaiting($event) {
      console.log($event)
    },

    // 已开始播放回调
    onPlayerPlaying($event) {
      console.log($event)
    },

    // 当播放器在当前播放位置下载数据时触发
    onPlayerLoadeddata($event) {
      console.log($event)
    },

    // 当前播放位置发生变化时触发。
    onPlayerTimeupdate($event) {
      console.log($event)
    },

    //媒体的readyState为HAVE_FUTURE_DATA或更高
    onPlayerCanplay(player) {
      // console.log('player Canplay!', player)
    },

    //媒体的readyState为HAVE_ENOUGH_DATA或更高。这意味着可以在不缓冲的情况下播放整个媒体文件。
    onPlayerCanplaythrough(player) {
      // console.log('player Canplaythrough!', player)
    },

    //播放状态改变回调
    playerStateChanged(playerCurrentState) {
      console.log('player current update state', playerCurrentState)
    },

    //将侦听器绑定到组件的就绪状态。与事件监听器的不同之处在于，如果ready事件已经发生，它将立即触发该函数。。
    playerReadied(player) {
      console.log('example player 1 readied', player);
    },

    getRealVideoUPath(videoList) {
      this.loading = true;
      var _this = this;
      var linkList = [];
      for (var v = 0; v < videoList.length; v++) {
        linkList.push(videoList[v].link);
      }

      postRequest("/douYin/getRealVideoUPath", {
        linkList: linkList
      }).then(resp => {
        if (resp.status == 200) {
          var urlMaps = resp.data; // 存放真实的视频链接，key为视频的id，value为视频链接
          for (var key in urlMaps) {
            console.log("load... key：" + key + "，value：" + urlMaps[key]);
          }
          this.buildVideoPlayers(videoList, urlMaps);
        } else {
          this.loading = false;
          _this.$message({type: 'error', message: '视频数据加载失败!'});
        }
      }, resp => {
        this.loading = false;
        console.log("load error1... " + resp);
      }).catch(resp => {
        this.loading = false;
        console.log("load error2... " + resp);
      })
    },

    buildVideoPlayers(videoList, urlMaps) {
      var _this = this;
      var videoPlayers = [];
      for (var i = 0; i < videoList.length; i++) {
        var video = videoList[i];
        var videoIndex = video.link.lastIndexOf("video");
        var index = video.link.lastIndexOf("?");
        var itemId = video.link.substring(videoIndex + 6, index - 1); // 视频的itemId

        var videoPlayer = {
          playbackRates: [0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.5, 3.0], // 可选的播放速度
          autoplay: false, // 如果为true,浏览器准备好时开始回放。
          muted: false, // 默认情况下将会消除任何音频。
          loop: false, // 是否视频一结束就重新开始。
          preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
          language: 'zh-CN',
          aspectRatio: '9:16', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
          fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
          sources: [{
            type: "video/mp4", // 类型
            src: urlMaps[itemId] // 动态获取url地址，eg：https://aweme.snssdk.com/aweme/v1/play/?video_id=v0d00fg10000c9ek54jc77u7pk9s2k30&ratio=720p&line=0
          }],
          poster: video.img_url, // 封面地址
          notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
          controlBar: {
            timeDivider: true, // 当前时间和持续时间的分隔符
            durationDisplay: true, // 显示持续时间
            remainingTimeDisplay: false, // 是否显示剩余时间功能
            fullscreenToggle: true // 是否显示全屏按钮
          }
        }
        videoPlayers.push(videoPlayer);
      }
      _this.playerOptions = videoPlayers;
      this.loading = false;
    }

  },
  mounted: function () {
    // this.loading = true;设置到这个方法里面不生效。。。
    var douYinDetail = this.$route.query.data;
    this.activeName = this.$route.query.activeName;
    var _this = this;
    _this.title = douYinDetail.title;
    _this.hotValve = douYinDetail.value;
    _this.mapList = douYinDetail.map_list;
    _this.rank = douYinDetail.rank;
    _this.count = douYinDetail.count;
    _this.videoList = douYinDetail.extra_list;
    this.getRealVideoUPath(douYinDetail.extra_list);
  },
  data() {
    return {
      douYinDetail: {},
      mapList: [],
      videoList: [],
      loading: false,
      activeName: '',
      title: '',
      hotValve: 0,
      rank: 0,
      count: 0,
      playerOptions: []
    }
  }
}
</script>

<style type="text/css">
.title_style {
  color: #409eff;
  cursor: pointer;

}

.title_row_style {
  height: 70px;
  margin-bottom: 10px;
  margin-top: 10px;
}

// 超出一定长度鼠标悬停时显示气泡
.title_style_backup {
  color: #409eff;
  cursor: pointer;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.video_style {
  margin-right: 20px;
  margin-bottom: 20px;
}
</style>