import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import ArticleList from '@/components/ArticleList'
import CateMana from '@/components/CateMana'
import DataCharts from '@/components/DataCharts'
import PostArticle from '@/components/PostArticle'
import UserMana from '@/components/UserMana'
import BlogDetail from '@/components/BlogDetail'
import FilesList from '@/components/FilesList'
import FilesUpload from '@/components/FilesUpload'
import WeiboHot from '@/components/hotSearch/WeiboHot'
import DouyinHot from '@/components/hotSearch/DouyinHot'
import DYHotDetail from '@/components/hotSearch/DYHotDetail'
import SelfHome from '@/components/system/SelfHome'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '登录',
      hidden: true,
      component: Login
    }, {
      path: '/home',
      name: '',
      component: Home,
      hidden: true
    }, {
      path: '/home',
      component: Home,
      name: '博客管理',
      iconCls: 'fa fa-file-text-o',
      children: [
        {
          path: '/articleList',
          name: '博客列表',
          component: ArticleList,
          meta: {
            keepAlive: true
          }
        }, {
          path: '/postArticle',
          name: '发表博客',
          component: PostArticle,
          meta: {
            keepAlive: false
          }
        }, {
          path: '/blogDetail',
          name: '博客详情',
          component: BlogDetail,
          hidden: true,
          meta: {
            keepAlive: false
          }
        }, {
          path: '/editBlog',
          name: '编辑博客',
          component: PostArticle,
          hidden: true,
          meta: {
            keepAlive: false
          }
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '文件管理',
      iconCls: 'fa fa-file-text-o',
      children: [
        {
          path: '/filesList',
          name: '文件列表',
          component: FilesList,
          meta: {
            keepAlive: true
          }
        }, {
          path: '/uploadFiles',
          name: '文件上传',
          component: FilesUpload,
          meta: {
            keepAlive: false
          }
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '热搜榜单',
      iconCls: 'fa fa-file-text-o',
      children: [
        {
          path: '/douyinHot',
          name: '抖音热搜',
          component: DouyinHot,
          meta: {
            keepAlive: true
          }
        }, {
          path: '/weiboHot',
          name: '微博热搜',
          component: WeiboHot,
          meta: {
            keepAlive: true
          }
        }, {
          path: '/DYHotDetail',
          name: '抖音热搜详情',
          component: DYHotDetail,
          hidden: true,
          meta: {
            keepAlive: false
          }
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '用户管理',
      iconCls: 'fa fa-user-o',
      children: [
        {
          path: '/user',
          iconCls: 'fa fa-user-o',
          name: '用户管理',
          component: UserMana
        }, {
          path: '/selfHome',
          name: '个人中心',
          component: SelfHome,
          hidden: true,
          meta: {
            keepAlive: false
          }
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '栏目管理',
      children: [
        {
          path: '/cateMana',
          iconCls: 'fa fa-reorder',
          name: '栏目管理',
          component: CateMana
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '数据统计',
      iconCls: 'fa fa-bar-chart',
      children: [
        {
          path: '/charts',
          iconCls: 'fa fa-bar-chart',
          name: '数据统计',
          component: DataCharts
        }
      ]
    }
  ]
})
