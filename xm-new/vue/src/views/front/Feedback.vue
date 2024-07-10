<template>
  <div class="main-content" style="width: 50%">
    <div class="card">
      <div style="margin: 30px;text-align: center;font-size:24px" >期待您的声音</div>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input  size="medium" v-model="form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="问题" prop="problem">
          <el-input  type="textarea" size="medium" v-model="form.problem" placeholder="请输入问题"></el-input>
        </el-form-item>
        <el-form-item label="想法" prop="idea">
          <el-input  type="textarea" size="medium" v-model="form.idea" placeholder="请问您有什么想法"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button  size="medium" type="primary" @click="save">提交反馈</el-button>
        </div>
      </el-form>
    </div>

  </div>
</template>



<script>
export default {
  name: "Feedback",
  data(){
    return{
      form: {},
      rules: {
        title:[
          {required:true,message:'请输入标题',trigger:'blur'}
        ],
        problem:[
          {required:true,message:'请输入问题',trigger:'blur'}
        ],


      }
    }
  },
  created() {
  },
  methods:{
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/feedback/update' : '/feedback/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('提交成功')
              this.form={}
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },

  }
}
</script>



<style scoped>
.card {
  background-color: #fff;  /* 设置背景颜色为白色 */
  border-radius: 8px;  /* 设置边角为圆角 */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);  /* 增加阴影深度和覆盖范围 */
  margin: 40px auto;  /* 增加上下边距以更好地展示阴影 */
  padding: 20px;  /* 提供内边距 */
}

/* 可以添加更多的全局样式来美化表单元素 */
el-input, el-form-item {
  margin-bottom: 20px;  /* 每个表单元素下方增加空间 */
}
</style>