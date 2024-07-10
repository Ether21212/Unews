<template>
  <div class="container">
    <div class="left">
      <div class="loginWord">
        Unews 新闻网
      </div>
      <img src="@/assets/imgs/logo.png" style=" width: 200px;  height: 200px;  object-fit: contain; margin-top: 40px;margin-left: 55px" >
    </div>
    <div class="right">
      <div style="text-align: center; font-size: 20px; margin-bottom: 60px;margin-top:50px; color: #333">欢迎您，管理员</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="medium" style="width: 100%; background-color: #2a60c9; border-color: #2a60c9; color: white" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right">
        <a href="/login">返回用户登录</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { role: 'ADMIN' },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/')  // 跳转主页
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  background-color: #f8f8f8;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
.left{
  width: 380px;
  padding: 40px;
  background-color: #417dff;
  border-radius: 5px;
  height: 500px;
  box-shadow: 0 14px 28px rgba(0,0,0,0.25),0 10px 10px rgba(0,0,0,0.22);
}
.right{
  width: 380px;
  padding: 40px;
  background-color: rgba(255, 255, 255, .8);
  height: 500px;
  border-radius: 5px;
  box-shadow: 0 14px 28px rgba(0,0,0,0.25),0 10px 10px rgba(0,0,0,0.22);
}
.loginWord{
  text-align: center;
  font-size: 35px;
  color: #f8f8f8;
  margin-top: 60px;
}
</style>