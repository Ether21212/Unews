<template>
  <div class="hot">
    <div style="min-height: 350px">
      <div style="display: flex; align-items: center; margin-bottom: 20px">
        <img src="@/assets/imgs/hot.png" alt="" style="width: 70px">
        <div style="flex: 1; text-align: right; ">
          <span @click="loadHot" style="font-size: 18px; cursor: pointer"><i class="el-icon-refresh"></i> 换一换</span>
        </div>
      </div>
      <div>
        <div  @click="$router.push('/front/newsDetail?id=' + item.id)" v-for="(item, index) in hotNewsList" :key="item.id"
              style="font-size: 16px; margin-bottom: 10px; cursor: pointer; color: #666" class="line1">
          <strong style="color: red; margin-right: 10px" v-if="index < 3">{{ index + 1 }}</strong>
          <strong style="color: #333; margin-right: 10px" v-else>{{ index + 1 }}</strong>
          <span>{{ item.title }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Hot",
  data() {
    return {
      hotNewsList: []
    }
  },
  created() {
    this.loadHot()
  },
  methods: {
    loadHot() {
      this.$request.get('/news/selectHot').then(res => [
        this.hotNewsList = res.data || []
      ])
    },
  }
}
</script>

<style scoped>
.hot{
  border: 1px solid #ccc; /* 给视频和新闻卡片添加浅灰色边框 */
  border-radius: 10px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  padding: 10px; /* 内边距 */
  background-color: #fff; /* 背景色为白色 */
  transition: box-shadow 0.3s ease-in-out; /* 添加阴影动画效果 */
}
</style>