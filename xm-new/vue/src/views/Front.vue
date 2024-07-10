<template>
  <div class="front-head">
    <!--头部-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="" class="logo">
        <div class="title">Unews 理闻</div>
      </div>
      <div class="front-header-center">
        <div style="display: flex">
          <div class="menu" :class="{ 'menu-active' : $route.path.includes(item.path) }"
               v-for="item in menus" :key="item.path" @click="$router.push(item.path)">{{ item.text }}</div>
        </div>
      </div>
      <div style="width: 300px">
        <el-input v-model="title" prefix-icon="el-icon-search" size="" placeholder="请输入新闻关键字搜索" style="width: 220px; margin-right: 5px  ;font-size: 14px; /* 调整搜索框字体大小 */"></el-input>
        <el-button size="medium" @click="search">搜索</el-button>
      </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="" style="object-fit: cover;border-radius: 50%; ">
              <div style="margin-left: 10px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">

              <el-dropdown-item>
                <div @click="$router.push('/front/userQuestion')">我的问题</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/userAnswer')">我的回答</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/userFeedback')">我的反馈</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/person')">个人信息</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="logout">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>

    <Footer />

  </div>

</template>

<script>
import Footer from "@/components/Footer";
export default {
  name: "FrontLayout",
  components: {
    Footer
  },
  data () {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      menus: [
        { text: '理闻首页', path: '/front/home' },
        { text: '热门问答', path: '/front/question' },
        { text: '理闻视频', path: '/front/video' },
        { text: '意见反馈', path: '/front/feedback' },
        { text: '系统公告', path: '/front/notice' },
      ],
      title: this.$route.query.title
    }
  },

  mounted() {

  },
  methods: {
    search() {
      location.href = '/front/search?title=' + this.title
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }

}
</script>

<style scoped>
@import "@/assets/css/front.css";

.menu {
  padding: 0 20px; /* 调整菜单项的内边距 */
  color: #003c9d; /* 调整字体颜色 */
  cursor: pointer;
  font-size: 18px; /* 调整字体大小 */
  font-weight: bold; /* 使字体加粗 */
  transition: color 0.3s, background-color 0.3s; /* 添加过渡效果 */
  line-height: 80px; /* 确保菜单项高度一致 */
  height: 100%; /* 确保菜单项高度一致 */
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
}

.menu:hover:not(.menu-active) {
  color: #000000;
}

.menu-active {
  color: #fff;
  background-color: #0056b3; /* 活动菜单项的背景颜色 */
  border-radius: 4px; /* 添加圆角 */
  padding: 0 20px; /* 调整活动菜单项的内边距 */
  margin-top: 0; /* 确保顶部间隔一致 */
  line-height: 80px; /* 确保选中状态下行高一致 */
  height: 100%; /* 确保选中状态下高度一致 */
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
}

.front-header {
  background-image: url("@/assets/imgs/usst.png");
  display: flex;
  align-items: center; /* 确保内容垂直居中 */
  height: 80px; /* 设置导航栏高度 */
}

.logo {
  max-width: 100%; /* 让图片宽度不超过父容器的宽度 */
  max-height: 100%; /* 让图片高度不超过父容器的高度 */
  height: auto; /* 自动调整高度以保持比例 */
  display: block; /* 去掉默认的内联块元素的间隙 */
}

</style>