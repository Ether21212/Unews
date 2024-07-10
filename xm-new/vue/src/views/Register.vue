<template>
  <div class="container">
    <div class="left">
      <div class="loginWord">
        Unews 新闻网
      </div>
      <img src="@/assets/imgs/logo.png" style=" width: 200px;  height: 200px;  object-fit: contain; margin-top: 40px;margin-left: 55px" >
    </div>
    <div class="right">
      <div style="text-align: center; font-size: 20px; margin-bottom: 40px; color: #333">欢迎注册</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPass">
          <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请确认密码" show-password  v-model="form.confirmPass"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="medium" style="width: 100%; background-color: #2a60c9; border-color: #2a60c9; color: white" @click="register">注 册</el-button>
        </el-form-item>
        <div style="text-align: right">
          已有账号？去 <a href="/login">登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    // 验证码校验
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 5 || value.length > 16) {
        callback(new Error('密码长度必须在5到16位之间'));
      } else {
        if (this.form.confirmPass !== '') {
          this.$refs.formRef.validateField('confirmPass');
        }
        callback();
      }
    };

    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'));
      } else if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    return {
      form: { username: '', password: '', confirmPass: '', role: 'USER' },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPass: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPass, trigger: 'blur' }
        ]
      }
    }
  },
  created() {

  },
  methods: {
    register() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')
              this.$message.success('注册成功')
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
  background-color: #f8f8f8;

  display: flex;
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