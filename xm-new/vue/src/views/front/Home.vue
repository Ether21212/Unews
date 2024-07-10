<template>
  <div class="main-content" >
    <div class="content-wrapper">
      <!-- 左侧部分开始 -->
      <div class="left-section">
        <el-carousel height="400px">
          <el-carousel-item v-for="item in bannerList" :key="item" >
            <div class="carousel-item-title">{{ item.title }}</div>
            <a :href="'/front/newsDetail?id=' + item.newsId" target="_blank">
              <img :src="item.img" alt="" class="carousel-img">
            </a>
          </el-carousel-item>
        </el-carousel>

        <div class="category-container">
          <div @click="loadCategoryNews(null)" class="category-item" :class="{ 'category-item-active' : category === null }">全部</div>
          <div @click="loadCategoryNews(item.name)" class="category-item" :class="{ 'category-item-active' : category === item.name }" v-for="item in categoryList" :key="item.id">{{ item.name }}</div>
        </div>

        <div class="news-container">
          <div @click="$router.push('/front/newsDetail?id=' + item.id)" class="news-card" v-for="item in tableData" :key="item.id">
            <img :src="item.img" alt="" class="news-img">
            <div class="news-details">
              <div class="news-title">{{ item.title }}</div>
              <div class="news-descr">{{ item.descr }}</div>
              <div class="news-meta">
                <span class="news-time"><i class="el-icon-time"></i> {{ item.time }}</span>
                <span class="news-read"><i class="el-icon-s-comment"></i> {{ item.readCount }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="pagination-container" v-if="total > pageSize">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
      <!-- 左侧部分结束 -->

      <!-- 右侧部分开始 -->
      <div class="right-section">
        <hot />
        <div class="selected-videos">
          <div class="selected-videos-title">理闻视频</div>
          <div class="video-player-container">
            <video controls class="video-player" :src="video.file"></video>
          </div>
          <div @click="loadVideo(item)" v-for="item in videoList" :key="item.id" class="video-item" :class="{'video-item-active': video.id === item.id}">
            <img src="@/assets/imgs/play.gif" alt="" class="video-icon" v-if="video.id === item.id">
            <span>{{ item.name }}</span>
          </div>
        </div>
      </div>
      <!-- 右侧部分结束 -->
    </div>
  </div>
</template>

<script>
import Hot from "@/components/Hot";
export default {
  components: { Hot },
  data() {
    return {
      bannerList: [],
      categoryList: [],
      category: null,
      tableData: [], // 所有的数据
      pageNum: 1, // 当前的页码
      pageSize: 3, // 每页显示的个数
      total: 0,
      video: {},
      videoList: []
    }
  },
  mounted() {
    this.loadBanner();
    this.loadCategory();
    this.load(1);
    this.loadVideo(null);
  },
  methods: {
    loadVideo(item) {
      this.$request.get('/video/selectPage', {
        params: {
          pageNum: 1
        }
      }).then(res => {
        this.videoList = res.data?.list || [];
        if (item) {
          this.video = item;
        } else {
          this.video = this.videoList.length ? this.videoList[0] : {};
        }
      });
    },
    loadBanner() {
      this.$request.get('/banner/selectAll').then(res => {
        this.bannerList = res.data || [];
      });
    },
    loadCategory() {
      this.$request.get('/category/selectAll').then(res => {
        this.categoryList = res.data || [];
      });
    },
    loadCategoryNews(name) {
      this.category = name;
      this.load(1);
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/news/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          category: this.category,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list;
          this.total = res.data?.total;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
  }
}
</script>
<style scoped>

.content-wrapper {
  display: flex;
  gap: 70px;
  margin:0px -50px; /* 调整外边距 */

}

.left-section {
  flex: 2;
}

.carousel-item-title {
  position: absolute;
  top: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  text-align: center;
  padding: 10px 0;
}

.carousel-img {

  width: 100%;
  height: 100%;
  object-fit: cover;
}

.category-container {
  margin: 20px 0;
  display: flex;
  gap: 20px;
}

.category-item {
  border: 1px solid #2a60c9;
  color: #2a60c9;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 20px;
}

.category-item-active {
  background-color: #2a60c9;
  color: #fff;
}

.news-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.news-card {
  display: flex;
  cursor: pointer;
  gap: 15px;
  margin-bottom: 5px;
  border: 1px solid #ccc; /* 给视频和新闻卡片添加浅灰色边框 */
  border-radius: 10px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  padding: 10px; /* 内边距 */
  background-color: #fff; /* 背景色为白色 */
  transition: box-shadow 0.3s ease-in-out; /* 添加阴影动画效果 */
}

.news-img {
  width: 150px;
  height: 100px;
  border-radius: 5px;
}

.news-details {
  flex: 1;
}

.news-title {
  font-size: 20px;
  margin-bottom: 10px;
  cursor: pointer;
}

.news-descr {
  color: #666;
  height: 40px;
  margin-bottom: 5px;
}

.news-meta {
  color: #666;
}

.news-time {
  margin-right: 20px;
}

.pagination-container {
  margin: 15px 0;
}

.right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;

}

.selected-videos {
  margin: 20px 0;
  border: 1px solid #ccc; /* 给视频和新闻卡片添加浅灰色边框 */
  border-radius: 10px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  padding: 10px; /* 内边距 */
  background-color: #fff; /* 背景色为白色 */
  transition: box-shadow 0.3s ease-in-out; /* 添加阴影动画效果 */
}

.selected-videos-title {
  font-size: 24px;
  margin-bottom: 20px;
}

.video-player-container {
  margin-bottom: 10px;

}

.video-player {
  width: 100%;
}

.video-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  font-size: 16px;
  cursor: pointer;
  border: 1px solid #ccc; /* 给视频和新闻卡片添加浅灰色边框 */
  border-radius: 10px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  padding: 10px; /* 内边距 */
  background-color: #fff; /* 背景色为白色 */
  transition: box-shadow 0.3s ease-in-out; /* 添加阴影动画效果 */
}

.video-item:hover {
  color: #409EFF;
}

.video-item-active {
  color: #409EFF;
}

.video-icon {
  width: 15px;
  margin-right: 10px;
}

.line1:hover {
  color: #409EFF !important;
}

</style>
